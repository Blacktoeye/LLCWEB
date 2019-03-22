package llcweb.com.controller.admin;

import llcweb.com.dao.repository.SoftwareRepository;
import llcweb.com.domain.entities.PageInfo;
import llcweb.com.domain.models.Patent;
import llcweb.com.domain.models.Software;
import llcweb.com.domain.models.Users;
import llcweb.com.service.UsersService;
import llcweb.com.service.SoftwareService;
import llcweb.com.tools.StringUtil;
import llcweb.com.tools.UsefulTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/software")
public class SoftwareController {
    private static final Logger logger=LoggerFactory.getLogger(SoftwareController.class);

    @Resource
    private SoftwareRepository softwareRepository;
    @Resource
    private SoftwareService softwareService;
    @Resource
    private UsersService usersService;

    /**
     * @Author haien
     * @Description 搜索软著
     * @Date 2018/10/6
     * @Param [request]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @RequestMapping(value="/page",method = RequestMethod.GET)
    public Map<String,Object> getPage(HttpServletRequest request){
        Map<String,Object> map =new HashMap<String,Object>();

        //直接返回前台
        String draw = request.getParameter("draw");
        //当前数据的起始位置
        String startIndex = request.getParameter("startIndex");
        //数据长度
        String pageSize = request.getParameter("pageSize");
        //模糊|高级查询标志
        String fuzzy = request.getParameter("fuzzySearch");
        if(draw==null||startIndex==null||pageSize==null){
            map.put("result", 0);
            map.put("message", "参数不完整！");
            return map;
        }
        int size = Integer.parseInt(pageSize);
        //页码
        int currentPage = Integer.parseInt(startIndex)/size+1;
        Users user=usersService.getCurrentUser();

        logger.info("size = "+size+",currentPage = "+currentPage);

        Page<Software> softwarePage = null;
        Pageable pageable=new PageRequest(currentPage-1,size, Sort.Direction.DESC,"publicDate");
        //模糊查找
        if("true".equals(fuzzy)){
            String searchValue=request.getParameter("fuzzy");
            //空搜
            if(StringUtil.isNull(searchValue)){
                //日志
                logger.info("无关键词搜索--默认获取全部软著记录");
                softwarePage=softwareRepository.findAll(pageable);
            }
            //日志
            logger.info("模糊查询---关键词："+searchValue);
            softwarePage = softwareRepository.fuzzySearch(searchValue,pageable);
        }
        //高级查找（空搜也使用高级搜索）
        else{
            //日志
            logger.info("---高级查询---");
            String title=request.getParameter("title");
            String introduction=request.getParameter("introduction");
            String authorList=request.getParameter("authorList");
            String appliNum=request.getParameter("appliNum");
            String publicNum=request.getParameter("publicNum");
            String appliDate1=request.getParameter("appliDate");
            String publicDate1=request.getParameter("publicDate");
            //字符串对象转为日期对象
            Date appliDate=null;
            Date publicDate=null;
            try {
                if(!StringUtil.isNull(appliDate1)){
                    appliDate=new SimpleDateFormat("yyyy-MM-dd").parse(appliDate1);
                }
                if(!StringUtil.isNull(publicDate1)) {
                    publicDate = new SimpleDateFormat("yyyy-MM-dd").parse(publicDate1);
                }
            } catch (ParseException e) { //不以“-”格式输入日期则无法正确转换
                e.printStackTrace();
                map.put("draw", draw);
                map.put("result", 0);
                map.put("message", "日期格式错误！");
                return map;
            }
            //日志
            logger.info("---高级查询---");
            Software software=new Software(title,appliDate,introduction,authorList,appliNum,publicNum,publicDate);
            softwarePage = softwareService.activeSearch(software, currentPage - 1, size);
        }

        //总记录数
        long total = softwarePage.getTotalElements();
        logger.info("total="+total);

        map.put("draw", draw);
        map.put("result", 1);
        if(0==total){
            map.put("message", "未查询到记录！");
        }else {
            map.put("total", total);
            map.put("pageData", softwarePage);
            map.put("message", "成功获取分页数据！");
        }
        return map;
    }

    /**
     * @Author haien
     * @Description 保存软著
     * @Date 2018/10/6
     * @Param [request, response]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @RequestMapping(value="/save",method=RequestMethod.POST)
    public Map<String,Object> save(HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();

        String id=request.getParameter("id");
        String title=request.getParameter("title");
        String appliDate1=request.getParameter("appliDate");
        String introduction=request.getParameter("introduction");
        String authorList=request.getParameter("authorList");
        String originalLink=request.getParameter("originalLink");
        String sourceFile1=request.getParameter("sourceFile");
        String belongProject1=request.getParameter("belongProject");
        String appliNum=request.getParameter("appliNum");
        String publicNum=request.getParameter("publicNum");
        String agency=request.getParameter("agency");
        String appliPeople=request.getParameter("appliPeople");
        String state=request.getParameter("state");
        String publicDate1=request.getParameter("publicDate");

        if(StringUtil.isNull(title)||StringUtil.isNull(appliDate1)||
                StringUtil.isNull(introduction)||StringUtil.isNull(authorList)||
                StringUtil.isNull(originalLink)||StringUtil.isNull(sourceFile1)||
                StringUtil.isNull(belongProject1)||StringUtil.isNull(appliNum)||
                StringUtil.isNull(publicNum)||StringUtil.isNull(agency)||
                StringUtil.isNull(appliPeople)||StringUtil.isNull(state)||
                StringUtil.isNull(publicDate1)){
            map.put("result", 0);
            map.put("message", "保存失败,信息不完整！");
            return map;
        }

        int sourceFile=Integer.parseInt(sourceFile1);
        int belongProject=Integer.parseInt(belongProject1);
        Date appliDate=null;
        Date publicDate=null;
        try {
            if(!StringUtil.isNull(appliDate1)){
                appliDate=new SimpleDateFormat("yyyy-MM-dd").parse(appliDate1);
            }
            if(!StringUtil.isNull(publicDate1)) {
                publicDate = new SimpleDateFormat("yyyy-MM-dd").parse(publicDate1);
            }
        } catch (ParseException e) { //不以“-”格式输入日期则无法正确转换
            e.printStackTrace();
            map.put("result", 0);
            map.put("message", "日期格式错误！");
            return map;
        }

        Software software=null;
        //更新
        if(!StringUtil.isNull(id)&&Integer.parseInt(id)>0){
            logger.info("更新记录--id="+id+"title="+title+"appliPeople="+appliPeople);
            software = softwareRepository.findOne(Integer.parseInt(id));
            if(software==null){
                map.put("result", 0);
                map.put("message", "查无记录！");
                return map;
            }
        }
        //添加
        else {
            logger.info("新增记录--id="+id+"title="+title+"appliPeople="+appliPeople);
            software = new Software();
        }

        software.setTitle(title);
        software.setAppliDate(appliDate);
        software.setIntroduction(introduction);
        software.setAuthorList(authorList);
        software.setOriginalLink(originalLink);
        software.setSourceFile(sourceFile);
        software.setBelongProject(belongProject);
        software.setAppliNum(appliNum);
        software.setPublicNum(publicNum);
        software.setAgency(agency);
        software.setAppliPeople(appliPeople);
        software.setState(state);
        software.setPublicDate(publicDate);
        softwareRepository.save(software);

        map.put("result", 1);
        map.put("message", "成功保存记录！");
        logger.info("成功保存记录！");

        return map;
    }

    @RequestMapping("/delete")
    public Map<String,Object> delete(@RequestParam("id")Integer id){
        Map<String,Object> map=new HashMap<>();

        logger.info("删除记录：id="+id);

        Software software=softwareRepository.findOne(id);
        if (software == null) {
            map.put("result", 0);
            logger.info("删除记录失败");
            map.put("message", "删除记录失败！");
        }else{
            softwareRepository.delete(id);
            logger.info("成功删除记录");
            map.put("result", 1);
            map.put("message", "成功删除记录！");
        }

        return map;
    }

    /**
     * @Author haien
     * @Description 获取最新的软著记录
     * @Date 2018/10/9
     * @Param [activityType, count]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @RequestMapping("/getLatest")
    public Map<String,Object> getSoftwares(@RequestParam("count")Integer count){
        Map<String,Object> map=new HashMap<>();

        if(count==null||count.equals("")||count<=0){
            map.put("result", 0);
            map.put("message", "请正确指定读取数目！");
        }else{
            List<Software> softwares=softwareRepository.getLatest(count);
            map.put("result", 1);
            map.put("message", "获取记录成功！");
            map.put("data",UsefulTools.softwareToProductInfo(softwares));
        }
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
    public Map<String,Object> getPage(@RequestParam("pageNum")int pageNum,@RequestParam("pageSize")int pageSize){
        Map<String,Object> map=new HashMap<>();
        logger.info(",pageNum="+pageNum+",pageSize="+pageSize);

        Page<Software> projectPage = softwareService.getPage(pageNum-1,pageSize);
        PageInfo pageInfo = new PageInfo(0,UsefulTools.softwareToProductInfo(projectPage.getContent()),projectPage.getNumberOfElements());
        pageInfo.setTotalPages(projectPage.getTotalPages());

        map.put("result", 1);
        map.put("message", "获取记录成功！");
        map.put("data",pageInfo);
        return map;
    }
}
