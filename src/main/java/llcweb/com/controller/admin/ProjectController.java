package llcweb.com.controller.admin;

import llcweb.com.dao.repository.ProjectRepository;
import llcweb.com.domain.entities.PageInfo;
import llcweb.com.domain.entities.ProductInfo;
import llcweb.com.domain.entity.UsefulProject;
import llcweb.com.domain.models.Project;
import llcweb.com.service.ProjectService;
import llcweb.com.service.UsersService;
import llcweb.com.tools.UsefulTools;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param
 * @author tong
 * @Description:用于项目类的数据接口
 * @Date:15:03 2018/9/9
 */
@Controller
@RequestMapping("/project")
public class ProjectController {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UsersService usersService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                dateFormat, false));
    }

    /**
     * @Author haien
     * @Description 获取最新的项目记录
     * @Date 2018/10/10
     * @Param [count]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @RequestMapping("/getLatest")
    @ResponseBody
    public Map<String,Object> getProjects(@RequestParam("count")Integer count){
        Map<String,Object> map=new HashMap<>();

        if(count==null||count.equals("")||count<=0){
            map.put("result", 0);
            map.put("message", "请正确指定读取数目！");
        }else{
            List<Project> projects=projectRepository.getProjects(count);
            map.put("result", 1);
            map.put("message", "获取记录成功！");
            map.put("data",UsefulTools.projectToProductInfo(projects));
        }
        return map;
    }

    /*
     * 前台首页
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> page(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<String, Object>();


        //返回DateTable前台
        String draw = request.getParameter("draw");
        //当前数据的位置
        String startIndex = request.getParameter("startIndex");
        //每页显示的条数
        String pageSize = request.getParameter("pageSize");

        int size = Integer.parseInt(pageSize);
        int currentPage = Integer.parseInt(startIndex) / size + 1;

        //获取排序字段
        String orderColumn = request.getParameter("oderColunm");
        if (orderColumn == null) {
            orderColumn = "startDate";
        }

        //获取排序方式
        String orderDir = request.getParameter("orderDir");
        if (orderDir == null) {
            orderDir = "desc";
        }

        UsefulProject project = new UsefulProject();
        String fuzzy = request.getParameter("fuzzySearch");
        if ("ture".equals(fuzzy)) {
            String searchValue = request.getParameter("fuzzy");
            if (searchValue != null && !searchValue.equals("")) {
                project.setResponsiblePerson(searchValue);
                project.setRequireNum(searchValue);
                project.setProjectType(searchValue);
                project.setProjectName(searchValue);
                project.setTeam(searchValue);
            }
        }

        Page<Project> projectPage = projectService.findAll(project, currentPage - 1, size);
        //List<UsefulProject> usefulProjectList = projectService.projectsToUsefulProject(projectPage.getContent());

        //总记录条数
        long total = projectPage.getTotalElements();
        map.put("pageData", projectPage.getContent());
        map.put("pages", projectPage.getTotalPages());
        map.put("total", total);
        map.put("draw", draw);
        map.put("result", 1);
        map.put("message", "成功获取分页数据！");
        return map;

    }


    /*
     * 根据项目id查询
     */
    @RequestMapping(value = "/getProjectById/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getProjectById(HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "id") int id) {
        Map<String, Object> map = new HashMap<String, Object>();

        Project project = projectRepository.findOne(id);
        if (project != null) {
            map.put("result", 1);
            map.put("message", "成功获取项目！");
            logger.info("成功获取项目！");
        } else {
            map.put("result", 0);
            map.put("message", "获取项目失败！");
            logger.error("获取项目失败！");
        }
        map.put("data", project);
        return map;
    }


    /*
     * 保存项目，如果id不在就新建项目
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        Map<String, Object> map = new HashMap<String, Object>();

        String id = request.getParameter("id");
        String status = request.getParameter("status");
        String money = request.getParameter("money");
        String sDate = request.getParameter("startDate");
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
        String eDate = request.getParameter("enddate");
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(eDate);
        String title = request.getParameter("title");
        String responsiblePerson = request.getParameter("responsiblePerson");
        String requireNum = request.getParameter("requireNum");
        String projectType = request.getParameter("projectType");
        String projectName = request.getParameter("projectName");
        String members = request.getParameter("members");
        String team = request.getParameter("team");
        String projectDes = request.getParameter("projectDes");
        String projectAim = request.getParameter("projectAim");
        String hostUnit = request.getParameter("hostUnit");
        String coUnit = request.getParameter("coUnit");
        String undertakeUnit = request.getParameter("undertakeUnit");
        Project project;
        boolean flag = true;
        //更新项目
        if (id != null && !id.equals("") && Integer.parseInt(id) > 0) {
            project = projectRepository.findOne(Integer.parseInt(id));
            if (project == null) {
                flag = false;
            }
        }
        //新建项目
        else {
            project = new Project();
        }

        if (flag) {
            //id是自增的。。
            //project.setId(Integer.parseInt(id));
            project.setStatus(status);
            project.setMoney(money);
            project.setStartDate(startDate);
            project.setEndDate(endDate);
            project.setTitle(title);
            project.setResponsiblePerson(responsiblePerson);
            project.setRequireNum(requireNum);
            project.setProjectType(projectType);
            project.setProjectName(projectName);
            project.setMembers(members);
            project.setTeam(team);
            project.setProjectAim(projectAim);
            project.setProjectDes(projectDes);
            project.setHostUnit(hostUnit);
            project.setCoUnit(coUnit);
            project.setUndertakeUnit(undertakeUnit);

            projectRepository.save(project);

            map.put("result", 1);
            map.put("message", "成功保存项目！");
            logger.info("成功保存项目！");
        } else {
            map.put("result", 0);
            map.put("message", "保存项目失败！");
            logger.error("保存项目失败！");
        }
        map.put("data", project);
        return map;
    }


    /*
     * 删除项目
     */
    @RequestMapping(value = "delete", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> delete(@RequestBody Integer id) {
        Map<String, Object> map = projectService.delete(id);
        return map;

    }

    /*
     * 新增项目
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> addProject(@RequestBody Project project) {

        Map<String, Object> map = new HashMap<String, Object>();
        projectRepository.save(project);

        Project newProject = projectRepository.findOne(project.getId());

        if (newProject == null) {
            map.put("result", 1);
            map.put("message", "成功保存项目！");
            logger.info("成功保存项目！");
        } else {
            map.put("result", 0);
            map.put("message", "保存项目失败！");
            logger.error("保存项目失败！");
        }
        map.put("data", project);
        return map;
    }

    /**
     * @Author ricardo
     * @Description 分组获取项目
     * @Date 2018/10/10
     * @Param [count]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @RequestMapping("/getPage")
    @ResponseBody
    public Map<String,Object> getPage(@RequestParam("team")String team,@RequestParam("pageNum")int pageNum,@RequestParam("pageSize")int pageSize){
        Map<String,Object> map=new HashMap<>();
        logger.info("team="+team+",pageNum="+pageNum+",pageSize="+pageSize);

        Page<Project> projectPage = projectService.getPage(team,pageNum-1,pageSize);
        PageInfo pageInfo = new PageInfo(0,UsefulTools.projectToProductInfo(projectPage.getContent()),projectPage.getNumberOfElements());
        pageInfo.setTotalPages(projectPage.getTotalPages());
        map.put("result", 1);
        map.put("message", "获取记录成功！");
        map.put("data",pageInfo);
        return map;
    }
}
