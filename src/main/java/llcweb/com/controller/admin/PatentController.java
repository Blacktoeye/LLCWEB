package llcweb.com.controller.admin;

import llcweb.com.dao.repository.PatentRepository;
import llcweb.com.domain.entities.PageInfo;
import llcweb.com.domain.entity.UsefulPatent;
import llcweb.com.domain.models.Patent;
import llcweb.com.service.PatentService;
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
 * @Description:用于专利的数据接口
 * @Date: 20:01 2018/09/16
 *
 */

@RestController
@RequestMapping("/patent")
public class PatentController {
	private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PatentRepository patentRepository;
	@Autowired
	private PatentService patentService;
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
	 * @Description 获取最新的几条专利
	 * @Date 2018/10/9
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
			List<Patent> patents=patentRepository.getLatest(count);

			map.put("result", 1);
			map.put("message", "获取记录成功！");
			map.put("data",UsefulTools.patentToProductInfo(patents));
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
		
		UsefulPatent patent = new UsefulPatent();
		String fuzzy = request.getParameter("fuzzySearch");
		if("true".equals(fuzzy)) {
			String searchValue = request.getParameter("fuzzy");
			if(searchValue != null && !searchValue.equals("")) {
				patent.setAuthorList(searchValue);
				patent.setBelongProject(searchValue);
				patent.setAgency(searchValue);
				patent.setPublicNum(searchValue);
				patent.setTitle(searchValue);
			}
		}
		
		Page<Patent> patentPage = patentService.findAll(patent, currentPage-1, size);
		//List<UsefulPatent> usefulProjectList = patentService.patentsToUsefulpatent(patentPage.getContent());
		
		//总记录条数
		
		long total = patentPage.getTotalElements();
		map.put("pageData", patentPage.getContent());
		map.put("total", total);
		map.put("draw", draw);
		map.put("result", 1);
		 map.put("message", "成功获取分页数据！");
		 return map;
		
	}
	
	/*
	 * 根据专利id查询
	 */
    @RequestMapping(value = "/getPatentById/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getProjectById(HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "id")int id){
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	Patent patent = patentRepository.findOne(id);
    	if(patent != null) {
            map.put("result", 1);
            map.put("message", "成功获取专利！");
            logger.info("成功获取专利！");
        }else{
            map.put("result", 0);
            map.put("message", "获取专利失败！");
            logger.error("获取专利失败！");
        }
        map.put("data",patent);
        return map;
    }
    
    /*
     * 保存专利，如果id不在就新建专利
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(HttpServletRequest request, HttpServletResponse response) throws ParseException{
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	String id = request.getParameter("id");
    	String title = request.getParameter("title");
    	String aDate = request.getParameter("appliDate");
    	Date appliDate = new SimpleDateFormat("yyyy-MM-dd").parse(aDate);
    	String pDate = request.getParameter("publicDate");
    	Date publicDate = new SimpleDateFormat("yyyy-MM-dd").parse(pDate);
        String introd = request.getParameter("introduction");
        int introduction = Integer.parseInt(introd);
        String authorList = request.getParameter("authorList");
        String originalLink = request.getParameter("originalLink");
        String belongProject = request.getParameter("belongProject");
        String appliNum = request.getParameter("appliNum");
        String publicNum = request.getParameter("publicNum");
        String agency = request.getParameter("agency");
        String appliPeople = request.getParameter("appliPeople");
        
        Patent patent;
        
        boolean flag = true;
        
        //更新专利
        if(id != null && !id.equals("") && Integer.parseInt(id) > 0) {
        	patent = patentRepository.findOne(Integer.parseInt(id));
        	if(patent == null) {
        		flag = false;
        	}
        }
        //新建专利
        else {
        	patent = new Patent();
        }
        
        if(flag) {
        	//id是自增的
        	//patent.setId(Integer.parseInt(id));
        	patent.setTitle(title);
        	patent.setAppliDate(appliDate);
        	patent.setIntroduction(introd);
        	patent.setAuthorList(authorList);
        	patent.setOriginalLink(originalLink);
        	patent.setBelongProject(belongProject);
        	patent.setAppliNum(appliNum);
        	patent.setPublicNum(publicNum);
        	patent.setPublicDate(publicDate);
        	patent.setAgency(agency);
        	patent.setAppliPeople(appliPeople);
        	
        	patentRepository.save(patent);
            map.put("result", 1);
            map.put("message", "成功保存专利！");
            logger.info("成功保存专利！");
        }else{
            map.put("result", 0);
            map.put("message", "保存专利失败！");
            logger.error("保存专利失败！");
        }
        map.put("data",patent);
        return map;
    }
	
	/*
	 * 删除专利
	 */
    @RequestMapping(value = "delete", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> delete(@RequestBody Integer id){
    	Map<String, Object> map = patentService.delete(id);
		return map;
    }
    
    /*
     * 新增专利
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addProject(@RequestBody Patent patent) {
    	
    	Map<String,Object> map =new HashMap<String,Object>();
    	patentRepository.save(patent);
    	
    	Patent newPatent = patentRepository.findOne(patent.getId());

		if(newPatent == null){
			map.put("result", 1);
			map.put("message", "成功保存项目！");
			logger.info("成功保存项目！");
		}else{
			map.put("result", 0);
			map.put("message", "保存项目失败！");
			logger.error("保存项目失败！");
		}
		map.put("data",patent);
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

		Page<Patent> projectPage = patentService.getPage(pageNum-1,pageSize);
		PageInfo pageInfo = new PageInfo(0,UsefulTools.patentToProductInfo(projectPage.getContent()),projectPage.getNumberOfElements());
		pageInfo.setTotalPages(projectPage.getTotalPages());

		map.put("result", 1);
		map.put("message", "获取记录成功！");
		map.put("data",pageInfo);
		return map;
	}
}
