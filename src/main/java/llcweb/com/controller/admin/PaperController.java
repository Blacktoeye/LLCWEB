package llcweb.com.controller.admin;

import llcweb.com.dao.repository.PaperRepository;
import llcweb.com.domain.entities.PageInfo;
import llcweb.com.domain.entity.UsefulPaper;
import llcweb.com.domain.models.Paper;
import llcweb.com.domain.models.Project;
import llcweb.com.service.PaperService;
import llcweb.com.service.UsersService;
import llcweb.com.tools.UsefulTools;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
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
 * @author tong
 * @Description:用于论文的数据接口
 * @Date: 21:23 2018/09/11
 *
 */

@RestController
@RequestMapping("/paper")
public class PaperController {
	private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PaperRepository paperRepository;
	@Autowired
	private PaperService paperService;
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
	 * @Description 首页“论文”模块
	 * @Date 2018/10/7
	 * @Param [count]
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 **/
	@RequestMapping("/getLatest")
	@ResponseBody
	public Map<String,Object> getLatest(@RequestParam("count")Integer count){
		Map<String,Object> map=new HashMap<>();
		if(count==null||count.equals("")||count<=0){
			map.put("result", 0);
			map.put("message", "请正确指定读取数目！");
		}else{
			List<Paper> papers=paperRepository.getLatest(count);
			map.put("result", 1);
			map.put("message", "获取记录成功！");
			map.put("data",papers);
		}
		return map;
	}

	/*
	 * 前台首页
	 */
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> page(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		
		
		//返回DateTable前台
		String draw = request.getParameter("draw");
		//当前数据的位置
		String startIndex = request.getParameter("startIndex");
		//每页显示的条数
		String pageSize = request.getParameter("pageSize");
		
        int size = Integer.parseInt(pageSize);
        int currentPage = Integer.parseInt(startIndex)/size+1;
        
		//获取排序字段
		String orderColumn = request.getParameter("orderColunm");
		if(orderColumn == null) {
			orderColumn = "date";
		}
		
		//获取排序方式
		String orderDir = request.getParameter("orderDir");
		if(orderDir == null) {
			orderDir = "desc";
		}
		
		UsefulPaper paper = new UsefulPaper();
		String fuzzy = request.getParameter("fuzzySearch");
		if("true".equals(fuzzy)) {
			String searchValue = request.getParameter("fuzzy");
			if(searchValue != null && !searchValue.equals("")) {
				paper.setAuthorList(searchValue);
				paper.setBelongProject(searchValue);
				paper.setPeriodical(searchValue);
				paper.setTitle(searchValue);
			}
		}
		
		Page<Paper> paperPage = paperService.findAll(paper, currentPage-1, size);
		//List<UsefulPaper> usefulProjectList = paperService.papersToUsefulPaper(paperPage.getContent());
		
		//总记录条数
		
		long total = paperPage.getTotalElements();
		map.put("pageData", paperPage.getContent());
		map.put("total", total);
		map.put("draw", draw);
		map.put("result", 1);
		 map.put("message", "成功获取分页数据！");
		 return map;
		
	}
	
	/*
	 * 根据论文id查询
	 */
    @RequestMapping(value = "/getPaperById/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getProjectById(HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "id")int id){
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	Paper paper = paperRepository.findOne(id);
    	if(paper != null) {
            map.put("result", 1);
            map.put("message", "成功获取论文！");
            logger.info("成功获取论文！");
        }else{
            map.put("result", 0);
            map.put("message", "获取论文失败！");
            logger.error("获取论文失败！");
        }
        map.put("data",paper);
        return map;
    }
    
    /*
     * 保存论文，如果id不在就新建论文
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(HttpServletRequest request, HttpServletResponse response) throws ParseException{
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	String id = request.getParameter("id");
    	String title = request.getParameter("title");
        String date = request.getParameter("date");
        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        String introduction = request.getParameter("introduction");
        String authorList = request.getParameter("authorList");
        String originalLink = request.getParameter("originalLink");
        String sourceLink = request.getParameter("sourceLink");
        String belongProject = request.getParameter("belongProject");
        String periodical = request.getParameter("periodical");
        Paper paper;
        
        boolean flag = true;
        
        //更新论文
        if(id != null && !id.equals("") && Integer.parseInt(id) > 0) {
        	paper = paperRepository.findOne(Integer.parseInt(id));
        	if(paper == null) {
        		flag = false;
        	}
        }
        //新建论文
        else {
        	paper = new Paper();
        }
        
        if(flag) {
        	paper.setId(Integer.parseInt(id));
        	paper.setAuthorList(authorList);
        	paper.setTitle(title);
        	paper.setDate(date2);
        	paper.setIntroduction(introduction);
        	paper.setOriginalLink(originalLink);
        	paper.setSourceLink(sourceLink);
        	paper.setBelongProject(belongProject);
        	paper.setPeriodical(periodical);
        	paperRepository.save(paper);
            map.put("result", 1);
            map.put("message", "成功保存论文！");
            logger.info("成功保存论文！");
        }else{
            map.put("result", 0);
            map.put("message", "保存论文失败！");
            logger.error("保存论文失败！");
        }
        map.put("data",paper);
        return map;
    }
	
	/*
	 * 删除论文
	 */
    @RequestMapping(value = "delete", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> delete(@RequestBody Integer id){
    	Map<String, Object> map = paperService.delete(id);
		return map;
    	
    }
    
    /*
     * 新增论文
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addProject(@RequestBody Paper paper) {
    	
    	Map<String,Object> map =new HashMap<String,Object>();
    	paperRepository.save(paper);
    	
    	Paper newPaper = paperRepository.findOne(paper.getId());
		if(newPaper == null){
			map.put("result", 1);
			map.put("message", "成功保存项目！");
			logger.info("成功保存项目！");
		}else{
			map.put("result", 0);
			map.put("message", "保存项目失败！");
			logger.error("保存项目失败！");
		}
		map.put("data",paper);
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

		Page<Paper> projectPage = paperService.getPage(pageNum-1,pageSize);
		PageInfo pageInfo = new PageInfo(0,UsefulTools.paperToProductInfo(projectPage.getContent()),projectPage.getNumberOfElements());
		pageInfo.setTotalPages(projectPage.getTotalPages());
		map.put("result", 1);
		map.put("message", "获取记录成功！");
		map.put("data",pageInfo);
		return map;
	}
}