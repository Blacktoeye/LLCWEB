package llcweb.com.controller.admin;

import llcweb.com.dao.repository.DocumentRepository;
import llcweb.com.dao.repository.ImageRepository;
import llcweb.com.dao.repository.PeopleRepository;
import llcweb.com.domain.models.People;
import llcweb.com.service.PeopleService;
import llcweb.com.service.UsersService;
import llcweb.com.tools.StringUtil;
import llcweb.com.tools.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author haien
 * @Description 人员控制器
 * @Date 2018/10/2
 **/
@RestController
@RequestMapping("/people")
public class PeopleController {
    private static final Logger logger = LoggerFactory.getLogger(PeopleController.class);

    @Resource
    private UsersService usersService;
    @Resource
    private PeopleService peopleService;
    @Resource
    private PeopleRepository peopleRepository;
    @Resource
    private DocumentRepository documentRepository;
    @Resource
    private ImageRepository imageRepository;

    /**
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @Author haien
     * @Description 搜索People
     * @Date 2018/10/3
     * @Param [request]
     **/
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Map<String, Object> page(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        //直接返回前台
        String draw = request.getParameter("draw");
        //当前数据的起始位置
        String startIndex = request.getParameter("startIndex");
        //数据长度
        String pageSize = request.getParameter("pageSize");
        if (draw == null || startIndex == null || pageSize == null) {
            map.put("result", 0);
            map.put("message", "参数不完整！");
            return map;
        }
        int size = Integer.parseInt(pageSize);
        //页码
        int currentPage = Integer.parseInt(startIndex) / size + 1;

        String name = request.getParameter("name");
        String grade1 = request.getParameter("grade");
        String position = request.getParameter("position");
        int grade = 0;
        if (grade1 != null) {
            grade = Integer.parseInt(grade1);
        }
        People people = new People(name, position, grade);

        logger.info("高级查询：size = " + size + ",currentPage = " + currentPage);

        //高级查询
        Page<People> peoplePage = peopleService.activeSearch(people, currentPage - 1, size);
        //总记录数
        long total = peoplePage.getTotalElements();
        logger.info("total=" + total);

        map.put("draw", draw);
        map.put("result", 1);
        map.put("total", total);
        map.put("pageData", peoplePage.getContent());
        map.put("message", "成功获取分页数据！");
        return map;
    }

    /**
     * @return java.util.Map<java.lang.String   ,   java.lang.Object>
     * @Author haien
     * @Description 保存人员资料
     * @Date 2018/10/4
     * @Param [request, response]
     **/
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Map<String, Object> save(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();

        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String portrait1 = request.getParameter("portrait");
        String position = request.getParameter("position");
        String introduction1 = request.getParameter("introduction");
        String grade1 = request.getParameter("grade");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String sex = request.getParameter("sex");
//        String adminPosition=request.getParameter("adminPosition");
//        String highestDegree=request.getParameter("highestDegree");
//        String academicTitle=request.getParameter("academicTitle");
        String researchField = request.getParameter("researchField");

//        if(StringUtil.isNull(name)||StringUtil.isNull(portrait1)||
//                StringUtil.isNull(position)||StringUtil.isNull(introduction1)||
//                StringUtil.isNull(grade1)||StringUtil.isNull(phone)||
//                StringUtil.isNull(email)||StringUtil.isNull(sex)||
//                StringUtil.isNull(adminPosition)||StringUtil.isNull(highestDegree)||
//                StringUtil.isNull(researchField)||StringUtil.isNull(academicTitle)){
//            map.put("result", 0);
//            map.put("message", "人员保存失败,信息不完整！");
//            return map;
//        }

//        if(!ValidatorUtil.isMobile(phone)){
//            map.put("result", 0);
//            map.put("message", "手机号格式不正确！");
//            return map;
//        }
//        if(!ValidatorUtil.isEmail(email)){
//            map.put("result", 0);
//            map.put("message", "邮箱格式不正确！");
//            return map;
//        }
//        if(!sex.equals("男")||!sex.equals("女")){
//            map.put("result", 0);
//            map.put("message", "性别请选择男或女！");
//            return map;
//        }

        int portrait = Integer.parseInt(portrait1);
        int introduction = Integer.parseInt(introduction1);
        int grade = Integer.parseInt(grade1);

        People people = null;
        //更新
        if (!StringUtil.isNull(id) && Integer.parseInt(id) > 0) {
            logger.info("更新人员--id=" + id + "name=" + name + "introduction=" + introduction1);
            people = peopleRepository.findOne(Integer.parseInt(id));
            if (people == null) {
                map.put("result", 0);
                map.put("message", "查无此人！");
                return map;
            }
        }
        //添加
        else {
            logger.info("新增人员--id=" + id + "name=" + name + "introduction=" + introduction1);
            people = new People();
        }

        people.setName(name);
        people.setPortrait(portrait);
        people.setPosition(position);
        people.setGrade(grade);
        people.setIntroduction(introduction);
        people.setPhone(phone);
        people.setEmail(email);
        people.setSex(sex);
        people.setResearchField(researchField);
//        people.setAdminPosition(adminPosition);
//        people.setHighestDegree(highestDegree);
//        people.setAcademicTitle(academicTitle);
        peopleRepository.save(people);
        map.put("result", 1);
        map.put("message", "成功保存人员！");
        logger.info("成功保存人员！");

        return map;
    }

    @RequestMapping("/delete")
    public Map<String, Object> delete(@RequestParam("id") Integer id) {
        Map<String, Object> map = new HashMap<>();

        logger.info("删除人员：id=" + id);

        People people = peopleRepository.findOne(id);
        if (people == null) {
            map.put("result", 0);
            logger.info("删除人员失败");
            map.put("message", "删除人员失败！");
        } else {
            peopleRepository.delete(id);
            documentRepository.delete(people.getIntroduction());
            imageRepository.delete(people.getPortrait());
            logger.info("成功删除人员");
            map.put("result", 1);
            map.put("message", "成功删除人员！");
        }

        return map;
    }

    /**
     * */
    @RequestMapping("/getByPosition")
    public Map<String, Object> getByPosition(@RequestParam("position") String position) {
        Map<String, Object> map = new HashMap<>();
        logger.info("getByPosition：position=" + position);
        List<People> peopleList = peopleRepository.findByPosition(position);
        map.put("data", peopleList);
        map.put("result", 1);
        map.put("message", "获取成功！");
        return map;
    }

    /**
     * */
    @RequestMapping("/getByPositionAndGrade")
    public Map<String, Object> getByPosition(@RequestParam("position") String position, @RequestParam("grade") int grade) {
        Map<String, Object> map = new HashMap<>();
        logger.info("getByPosition：position=" + position+"  grade="+grade);
        List<People> peopleList;
        if(grade>0) {
            peopleList = peopleRepository.findByPositionAndGrade(position,grade);
        }else {
            peopleList = peopleRepository.findByPosition(position);
        }
        map.put("data", peopleList);
        map.put("result", 1);
        map.put("message", "获取成功！");
        return map;
    }

    /**
     * */
    @RequestMapping("/getById")
    public Map<String, Object> getById(@RequestParam("id") Integer id) {
        Map<String, Object> map = new HashMap<>();
        logger.info("getById：id=" + id);
        People peopleList = peopleRepository.findById(id);
        map.put("data", peopleList);
        map.put("result", 1);
        map.put("message", "获取成功！");
        return map;
    }
}
