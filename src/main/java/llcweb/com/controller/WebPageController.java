package llcweb.com.controller;

import llcweb.com.dao.repository.*;
import llcweb.com.domain.models.*;
import llcweb.com.service.UsersService;
import llcweb.com.tools.RandomValidateCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *@Author: Ricardo
 *@Description: 此文件用于前端所有的页面的请求接口，接口名与html文件名对应
 *@Date: 15:14 2018/8/21
 **/
@Controller
@RequestMapping
public class WebPageController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UsersService usersService;
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PaperRepository paperRepository;
    @Autowired
    private PatentRepository patentRepository;
    @Autowired
    private SoftwareRepository softwareRepository;
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PeopleRepository peopleRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private PlatformRepository platformRepository;
    @Autowired
    private ParamsRepository paramsRepository;

    /**
     * 生成验证码
     */
    @RequestMapping(value = "/getVerify")
    @ResponseBody
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            randomValidateCode.getRandcode(request, response);//输出验证码图片方法
        } catch (Exception e) {
            logger.error("获取验证码失败>>>>   ", e);
        }
    }

    /**
     * 忘记密码页面校验验证码
     */
    @RequestMapping(value = "/checkVerify", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public boolean checkVerify(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try{
            //从session中获取随机数
            //String inputStr = requestMap.get("inputStr").toString();
            String inputStr = request.getParameter("inputStr");
            String random = (String) session.getAttribute("RANDOMVALIDATECODEKEY");
            if (random == null) {
                return false;
            }
            if (random.equals(inputStr)) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            logger.error("验证码校验失败", e);
            return false;
        }
    }

    //测试页面
    @RequestMapping("/test.html")
    public ModelAndView test(){

        List<String> learnList =new ArrayList<>();
        learnList.add("hello1");
        learnList.add("hello2");
        ModelAndView modelAndView = new ModelAndView("test");
        //添加当前登录用户
        modelAndView.addObject("learnList", learnList);
        return modelAndView;
    }
    @RequestMapping({"/","/index.html","/index","/main","/main.html"})
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("home/index");

        Params params = paramsRepository.findByName("index-float-window");
        if(params!=null){
            modelAndView.addObject("isFloat",params.getValue());
        }
        return modelAndView;
    }
    @ResponseBody
    @RequestMapping("/hello")
    public Users hello(){
       return usersRepository.findOne(1);
    }

    //页面映射函数
    @RequestMapping({"/Academic_communication.html","/Academic_communication"})
    public ModelAndView Academic_communication(){
        ModelAndView modelAndView = new ModelAndView("home/Academic_communication");
        return modelAndView;
    }
    @RequestMapping({"/achievement/activity.html","/achievement/activity"})
    public ModelAndView activity(@RequestParam("id")int id){
        ModelAndView modelAndView = new ModelAndView("home/achievement/activity");

        modelAndView.addObject("activity", activityRepository.findOne(id));
        return modelAndView;
    }

    @RequestMapping({"/Academic_conference.html","/Academic_conference"})
    public ModelAndView Academic_conference(){
        ModelAndView modelAndView = new ModelAndView("home/Academic_conference");
        return modelAndView;
    }
    @RequestMapping({"/Academic_cooperation.html","/Academic_cooperation"})
    public ModelAndView Academic_cooperation(){
        ModelAndView modelAndView = new ModelAndView("home/Academic_cooperation");
        return modelAndView;
    }
    @RequestMapping({"/Associate_professor.html","/Associate_professor"})
    public ModelAndView Associate_professor(){
        ModelAndView modelAndView = new ModelAndView("home/Associate_professor");
        return modelAndView;
    }
    @RequestMapping({"/contact_us.html","/contact_us"})
    public ModelAndView contact_us(){
        ModelAndView modelAndView = new ModelAndView("home/contact_us");
        return modelAndView;
    }
    @RequestMapping({"/Cultural_activity.html","/Cultural_activity"})
    public ModelAndView Cultural_activity(){
        ModelAndView modelAndView = new ModelAndView("home/Cultural_activity");
        return modelAndView;
    }
    @RequestMapping({"/doctor.html","/doctor"})
    public ModelAndView doctor(){
        ModelAndView modelAndView = new ModelAndView("home/doctor");
        return modelAndView;
    }


    @RequestMapping({"/graduate.html","/graduate"})
    public ModelAndView graduate(){
        ModelAndView modelAndView = new ModelAndView("home/graduate");
        return modelAndView;
    }
    @RequestMapping({"/Institutional_profile.html","/Institutional_profile"})
    public ModelAndView Institutional_profile(){
        ModelAndView modelAndView = new ModelAndView("home/Institutional_profile");
        return modelAndView;
    }
    @RequestMapping({"/Lecturer.html","/Lecturer"})
    public ModelAndView Lecturer(){
        ModelAndView modelAndView = new ModelAndView("home/Lecturer");
        return modelAndView;
    }
    @RequestMapping({"/postdoctor.html","/postdoctor"})
    public ModelAndView postdoctor(){
        ModelAndView modelAndView = new ModelAndView("home/postdoctor");
        return modelAndView;
    }
    @RequestMapping({"/Professor.html","/Professor"})
    public ModelAndView Professor(){
        ModelAndView modelAndView = new ModelAndView("home/Professor");
        return modelAndView;
    }


    @RequestMapping({"/project_brief","/project_brief.html"})
    public ModelAndView project_brief(@RequestParam("id")int projectId){
        ModelAndView modelAndView = new ModelAndView("home/project_brief");
        Project project = projectRepository.findOne(projectId);
        modelAndView.addObject("project", project);
        return modelAndView;
    }
    @RequestMapping({"/Research_Project.html","/Research_Project"})
    public ModelAndView Research_Project(){
        ModelAndView modelAndView = new ModelAndView("home/Research_Project");
        return modelAndView;
    }
    @RequestMapping({"/Research_Team.html","/Research_Team"})
    public ModelAndView Research_Team(){
        ModelAndView modelAndView = new ModelAndView("home/Research_Team");
        return modelAndView;
    }
    @RequestMapping({"/scientific_achievements.html","/scientific_achievements"})
    public ModelAndView scientific_achievements(){
        ModelAndView modelAndView = new ModelAndView("home/scientific_achievements");
        return modelAndView;
    }

    //招聘
    @RequestMapping({"/Talent_recruitment.html","/Talent_recruitment"})
    public ModelAndView Talent_recruitment(){
        ModelAndView modelAndView = new ModelAndView("home/Talent_recruitment");
        return modelAndView;
    }
    @RequestMapping({"/Talent_recruitment1.html","/Talent_recruitment1"})
    public ModelAndView Talent_recruitment1(){
        ModelAndView modelAndView = new ModelAndView("home/Talent_recruitment1");
        return modelAndView;
    }
    @RequestMapping({"/Talent_recruitment2.html","/Talent_recruitment2"})
    public ModelAndView Talent_recruitment2(){
        ModelAndView modelAndView = new ModelAndView("home/Talent_recruitment2");
        return modelAndView;
    }
    //人才培养
    @RequestMapping({"/Talent_training.html","/Talent_training"})
    public ModelAndView Talent_training(@RequestParam("position") String position, @RequestParam("grade") int grade){
        ModelAndView modelAndView = new ModelAndView("home/Talent_training");
        logger.info("getByPosition：position=" + position+"  grade="+grade);
        List<People> peopleList;
        if(grade>0) {
            peopleList = peopleRepository.findByPositionAndGrade(position,grade);
        }else {
            peopleList = peopleRepository.findByPosition(position);
        }
        modelAndView.addObject("peopleList",peopleList);
        modelAndView.addObject("position",position);
        modelAndView.addObject("grade",grade);
        return modelAndView;
    }
    @RequestMapping({"/Team_introduction.html","/Team_introduction"})
    public ModelAndView Team_introduction(){
        ModelAndView modelAndView = new ModelAndView("home/Team_introduction");
        return modelAndView;
    }


    //以下为子文件夹的页面映射函数
    /**
     * achievement 论文专利软著等展示页面，根据id展示
     * **/
    @RequestMapping({"/achievement/scientific_achievements1.html","/achievement/scientific_achievements1"})
    public ModelAndView scientific_achievements1(@RequestParam("id")int id){
        ModelAndView modelAndView = new ModelAndView("home/achievement/scientific_achievements1");

        modelAndView.addObject("paper", paperRepository.findOne(id));
        return modelAndView;
    }
    @RequestMapping({"/achievement/scientific_achievements2.html","/achievement/scientific_achievements2"})
    public ModelAndView scientific_achievements2(@RequestParam("id")int id){
        ModelAndView modelAndView = new ModelAndView("home/achievement/scientific_achievements2");

        Patent patent = patentRepository.findOne(id);
        modelAndView.addObject("image",imageRepository.findOne(patent.getSourceFile()));
        modelAndView.addObject("patent", patent);
        return modelAndView;
    }
    @RequestMapping({"/achievement/scientific_achievements3.html","/achievement/scientific_achievements3"})
    public ModelAndView scientific_achievements3(@RequestParam("id")int id){
        ModelAndView modelAndView = new ModelAndView("home/achievement/scientific_achievements3");
        Software software = softwareRepository.findOne(id);
        modelAndView.addObject("image",imageRepository.findOne(software.getSourceFile()));
        modelAndView.addObject("software", software);
        return modelAndView;
    }
    /**
     * communication
     * **/
    @RequestMapping({"/communication/communicationIndexPage.html","/communication/communicationIndexPage"})
    public ModelAndView communicationIndexPage(){
        ModelAndView modelAndView = new ModelAndView("home/communication/communicationIndexPage");
        return modelAndView;
    }
    @RequestMapping({"/communication/conferenceIndexPage.html","/communication/conferenceIndexPage"})
    public ModelAndView conferenceIndexPage(){
        ModelAndView modelAndView = new ModelAndView("home/communication/conferenceIndexPage");
        return modelAndView;
    }
    @RequestMapping({"/communication/cooperationIndexPage.html","/communication/cooperationIndexPage"})
    public ModelAndView cooperationIndexPage(){
        ModelAndView modelAndView = new ModelAndView("home/communication/cooperationIndexPage");
        return modelAndView;
    }
    /**
     * cultural_activity
     * **/
//    @RequestMapping({"/Cultural_activity.html","/Cultural_activity"})
//    public ModelAndView Cultural_activity(){
//        ModelAndView modelAndView = new ModelAndView("/home/Cultural_activity.html");
//        return modelAndView;
//    }
    @RequestMapping({"/Cultural_activity/revisit_demo.html","/Cultural_activity/revisit_demo"})
    public ModelAndView revisit_demo(){
        ModelAndView modelAndView = new ModelAndView("home/Cultural_activity/revisit_demo.html");
        return modelAndView;
    }
    @RequestMapping({"/Cultural_activity/play_demo.html","/Cultural_activity/play_demo"})
    public ModelAndView play_demo(){
        ModelAndView modelAndView = new ModelAndView("home/Cultural_activity/play_demo.html");
        return modelAndView;
    }
    /**
     * research
     * **/
    @RequestMapping({"/Research/Research_Project1.html","/Research/Research_Project1"})
    public ModelAndView Research_Project1(){
        ModelAndView modelAndView = new ModelAndView("home/Research/Research_Project1");
        return modelAndView;
    }
    @RequestMapping({"/Research/Research_Project2.html","/Research/Research_Project2"})
    public ModelAndView Research_Project2(){
        ModelAndView modelAndView = new ModelAndView("home/Research/Research_Project2");
        return modelAndView;
    }
    @RequestMapping({"/Research/Research_Project3.html","/Research/Research_Project3"})
    public ModelAndView Research_Project3(){
        ModelAndView modelAndView = new ModelAndView("home/Research/Research_Project3");
        return modelAndView;
    }
    @RequestMapping({"/Research/Research_Project4.html","/Research/Research_Project4"})
    public ModelAndView Research_Project4(){
        ModelAndView modelAndView = new ModelAndView("home/Research/Research_Project4");
        return modelAndView;
    }
    @RequestMapping({"/Research/Research_Project5.html","/Research/Research_Project5"})
    public ModelAndView Research_Project5(){
        ModelAndView modelAndView = new ModelAndView("home/Research/Research_Project5");
        return modelAndView;
    }
    /**
     * research project
     * **/
    @RequestMapping({"/ResearchProject/associate_demo.html","/ResearchProject/associate_demo"})
    public ModelAndView associate_demo(){
        ModelAndView modelAndView = new ModelAndView("home/ResearchProject/associate_demo");
        return modelAndView;
    }
    @RequestMapping({"/ResearchProject/lecturer_demo.html","/ResearchProject/lecturer_demo"})
    public ModelAndView lecturer_demo(){
        ModelAndView modelAndView = new ModelAndView("home/ResearchProject/lecturer_demo");
        return modelAndView;
    }
    @RequestMapping({"/ResearchProject/professor_demo.html","/ResearchProject/professor_demo"})
    public ModelAndView professor_demo(@RequestParam("id")int id){
        ModelAndView modelAndView = new ModelAndView("home/ResearchProject/professor_demo");

        People people = peopleRepository.findOne(id);
        Image image = imageRepository.findOne(people.getPortrait());
        modelAndView.addObject("people",people);
        modelAndView.addObject("image",image);

        return modelAndView;
    }

    /**
     * talent
     * **/
    @RequestMapping({"/Talent/doctor_demo.html","/Talent/doctor_demo"})
    public ModelAndView doctor_demo(){
        ModelAndView modelAndView = new ModelAndView("home/Talent/doctor_demo");
        return modelAndView;
    }

    //研究生个人介绍页面
    @RequestMapping({"/Talent/graduate_demo.html","/Talent/graduate_demo"})
    public ModelAndView graduate_demo(@RequestParam("id")int id){
        ModelAndView modelAndView = new ModelAndView("home/Talent/graduate_demo");
        People people = peopleRepository.findOne(id);
        Image image = imageRepository.findOne(people.getPortrait());
        modelAndView.addObject("people",people);
        modelAndView.addObject("image",image);
        return modelAndView;
    }
    @RequestMapping({"/Talent/post_demo.html","/Talent/post_demo"})
    public ModelAndView post_demo(){
        ModelAndView modelAndView = new ModelAndView("home/Talent/post_demo");
        return modelAndView;
    }
    //平台展示页面
    @RequestMapping({"/platform.html","/platform"})
    public ModelAndView platform(){
        ModelAndView modelAndView = new ModelAndView("home/platform");
        List<Platform> platformList = platformRepository.findByIsPublish(1);
        modelAndView.addObject("platformList",platformList);
        return modelAndView;
    }

    //搜索页面
    @RequestMapping({"/Globalsearch.html","/Globalsearch"})
    public ModelAndView Globalsearch(@RequestParam(value="keyWord",required = false)String keyWord){
        ModelAndView modelAndView = new ModelAndView("home/Globalsearch");
        logger.info("keyWord = "+keyWord);
        return modelAndView;
    }
}
