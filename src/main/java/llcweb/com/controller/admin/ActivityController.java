package llcweb.com.controller.admin;

import llcweb.com.dao.repository.ActivityRepository;
import llcweb.com.dao.repository.DocumentRepository;
import llcweb.com.domain.entities.PageInfo;
import llcweb.com.domain.models.Activity;
import llcweb.com.domain.models.Project;
import llcweb.com.domain.models.Users;
import llcweb.com.service.ActivityService;
import llcweb.com.service.UsersService;
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
@RequestMapping("/activity")
public class ActivityController {
    private static final Logger logger=LoggerFactory.getLogger(ActivityController.class);

    @Resource
    private ActivityRepository activityRepository;
    @Resource
    private ActivityService activityService;
    @Resource
    private UsersService usersService;
    @Resource
    private DocumentRepository documentRepository;

    /**
     * @Author haien
     * @Description 搜索活动记录
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

        Page<Activity> activityPage = null;
        Pageable pageable=new PageRequest(currentPage-1,size, Sort.Direction.DESC,"startDate");
        //模糊查找
        if("true".equals(fuzzy)){
            String searchValue=request.getParameter("fuzzy");
            //空搜
            if(StringUtil.isNull(searchValue)){
                //日志
                logger.info("无关键词搜索--默认获取全部活动记录");
                activityPage=activityRepository.findAll(pageable);
            }
            //日志
            logger.info("模糊查询---关键词："+searchValue);
            activityPage = activityRepository.fuzzySearch(searchValue,pageable);
        }
        //高级查找（空搜也使用高级搜索）
        else{
            //日志
            logger.info("---高级查询---");
            String title=request.getParameter("title");
            String author=request.getParameter("author");
            String peopleList=request.getParameter("peopleList");
            String group=request.getParameter("group");
            String activityType=request.getParameter("activityType");
            String startDate1=request.getParameter("startDate");
            String endDate1=request.getParameter("endDate");
            //字符串对象转为日期对象
            Date startDate=null;
            Date endDate=null;
            try {
                if(!StringUtil.isNull(startDate1)){
                    startDate=new SimpleDateFormat("yyyy-MM-dd").parse(startDate1);
                }
                if(!StringUtil.isNull(endDate1)) {
                    endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate1);
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
            Activity activity=new Activity( title,  author,  peopleList,  startDate,  endDate,  group,  activityType);
            activityPage = activityService.activeSearch(activity, currentPage - 1, size);
        }

        //总记录数
        long total = activityPage.getTotalElements();
        logger.info("total="+total);

        map.put("draw", draw);
        map.put("result", 1);
        if(0==total){
            map.put("total", total);
            map.put("pageData", activityPage.getContent());
            map.put("message", "未查询到记录！");
        }else {
            map.put("total", total);
            map.put("pageData", activityPage.getContent());
            map.put("message", "成功获取分页数据！");
        }
        return map;
    }

    /**
     * @Author haien
     * @Description 保存活动记录
     * @Date 2018/10/6
     * @Param [request, response]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @RequestMapping(value="/save",method=RequestMethod.POST)
    public Map<String,Object> save(HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();

        String id=request.getParameter("id");
        String title=request.getParameter("title");

        //作者、时间。直接从后台获取不用传输
        //String author=request.getParameter("author");
        String peopleList=request.getParameter("peopleList");
        String startDate1=request.getParameter("startDate");
        String endDate1=request.getParameter("endDate");
        String introduction1=request.getParameter("introduction");
        String group=request.getParameter("model");
        String activityType=request.getParameter("activityType");
        String isPublish1=request.getParameter("isPublish");
        //StringUtil.isNull(author)||
        if(StringUtil.isNull(title)||
                StringUtil.isNull(peopleList)||StringUtil.isNull(startDate1)||
                StringUtil.isNull(endDate1)||StringUtil.isNull(introduction1)||
                StringUtil.isNull(group)||StringUtil.isNull(activityType)||
                StringUtil.isNull(activityType)||StringUtil.isNull(isPublish1)){
            map.put("result", 0);
            map.put("message", "保存失败,信息不完整！");
            return map;
        }

        int introduction=Integer.parseInt(introduction1);
        int isPublish=Integer.parseInt(isPublish1);
        Date startDate=null;
        Date endDate=null;
        try {
            if(!StringUtil.isNull(startDate1)){
                startDate=new SimpleDateFormat("yyyy-MM-dd").parse(startDate1);
            }
            if(!StringUtil.isNull(endDate1)) {
                endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate1);
            }
        } catch (ParseException e) { //不以“-”格式输入日期则无法正确转换
            e.printStackTrace();
            map.put("result", 0);
            map.put("message", "日期格式错误！");
            return map;
        }

        Activity activity=null;
        //更新
        if(!StringUtil.isNull(id)&&Integer.parseInt(id)>0){
            logger.info("更新记录--id="+id+"title="+title+"author=+author");
            activity = activityRepository.findOne(Integer.parseInt(id));
            if(activity==null){
                map.put("result", 0);
                map.put("message", "查无记录！");
                return map;
            }
        }
        //添加
        else {
            logger.info("新增记录--id="+id+"title="+title);
            activity = new Activity();
        }

        activity.setAuthor(usersService.getCurrentUser().getUsername());
        activity.setPeopleList(peopleList);
        activity.setTitle(title);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setIntroduction(introduction);
        activity.setActivityType(activityType);
        activity.setModel(group);
        activity.setIsPublish(isPublish);
        activityRepository.save(activity);

        map.put("result", 1);
        map.put("message", "成功保存记录！");
        logger.info("成功保存记录！");

        return map;
    }

    @RequestMapping("/delete")
    public Map<String,Object> delete(@RequestParam("id")Integer id){
        Map<String,Object> map=new HashMap<>();

        logger.info("删除记录：id="+id);

        Activity activity=activityRepository.findOne(id);
        if (activity == null) {
            map.put("result", 0);
            logger.info("删除记录失败");
            map.put("message", "删除记录失败！");
        }else{
            activityRepository.delete(id);
            documentRepository.delete(activity.getIntroduction());
            logger.info("成功删除记录");
            map.put("result", 1);
            map.put("message", "成功删除记录！");
        }

        return map;
    }

    /**
     * @Author haien
     * @Description 首页“中心动态”模块
     * @Date 2018/10/7
     * @Param [count]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @RequestMapping("/coreDynamics")
    public Map<String,Object> getLatest(@RequestParam("count")Integer count){
        Map<String,Object> map=new HashMap<>();
        if(count==null||count.equals("")||count<=0){
            map.put("result", 0);
            map.put("message", "请正确指定读取数目！");
        }else{
            List<Activity> activities=activityRepository.getLatest(count);
            map.put("result", 1);
            map.put("message", "获取记录成功！");
            map.put("data",UsefulTools.activityToProductInfo(activities));
        }
        return map;
    }

    /**
     * @Author haien
     * @Description 获取具体的活动类
     * @Date 2018/10/9
     * @Param [activityType, count]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @RequestMapping("/getActivities")
    public Map<String,Object> getActivities(@RequestParam("activityType")String activityType,@RequestParam("count")Integer count){
        Map<String,Object> map=new HashMap<>();
        System.out.println("activityType="+activityType+"  count="+count);
        if(StringUtil.isNull(activityType)){
            map.put("result", 0);
            map.put("message", "请正确指定活动类型！");
        }
        if(count==null||count<=0){
            map.put("result", 0);
            map.put("message", "请正确指定读取数目！");
        }else{
            List<Activity> activityList =  activityRepository.getActivities(activityType,count);
            System.out.println("activity size = "+activityList.size());
            map.put("data",UsefulTools.activityToProductInfo(activityList));
            map.put("result", 1);
            map.put("message", "获取记录成功！");
        }
        return map;
    }
    /**
     * @Author ricardo
     * @Description 获取具体的活动类
     * @Date 2019/2/22
     * @Param [activityType, count]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    @RequestMapping("/getActivitiesById")
    public Map<String,Object> getActivitiesById(@RequestParam("id")Integer id){
        Map<String,Object> map=new HashMap<>();
        logger.info("获取文件信息：id="+id);

        if(null==id||id<=0){
            map.put("result",0);
            map.put("message","参数错误！");
            return map;
        }

        //获取活动
        Activity activity=activityRepository.findOne(id);
        if(activity==null){
            map.put("result",0);
            map.put("message","找不到信息！");
        }else{ //
            map.put("data",activity);
            map.put("result",1);
            map.put("message","成功获取文件！");
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
    public Map<String,Object> getPage(@RequestParam("activityType")String activityType,@RequestParam("pageNum")int pageNum,@RequestParam("pageSize")int pageSize){
        Map<String,Object> map=new HashMap<>();
        logger.info("activityType="+activityType+",pageNum="+pageNum+",pageSize="+pageSize);

        Page<Activity> activityPage = activityService.getPage(activityType,pageNum-1,pageSize);
        PageInfo pageInfo = new PageInfo(0,UsefulTools.activityToProductInfo(activityPage.getContent()),activityPage.getNumberOfElements());
        pageInfo.setTotalPages(activityPage.getTotalPages());
        map.put("result", 1);
        map.put("message", "获取记录成功！");
        map.put("data",pageInfo);
        return map;
    }
}
