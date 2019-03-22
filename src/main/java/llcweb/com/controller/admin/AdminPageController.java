package llcweb.com.controller.admin;

import llcweb.com.dao.repository.DocumentRepository;
import llcweb.com.dao.repository.ImageRepository;
import llcweb.com.dao.repository.PeopleRepository;
import llcweb.com.domain.models.Document;
import llcweb.com.domain.models.Image;
import llcweb.com.domain.models.People;
import llcweb.com.domain.models.Users;
import llcweb.com.service.DocumentService;
import llcweb.com.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *@Author: Ricardo
 *@Description: 此文件用于admin管理系统的所有的页面的请求接口，接口名与html文件名对应
 *@Date: 15:14 2018/8/21
 **/
@Controller
@RequestMapping("/admin")
public class AdminPageController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UsersService usersService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private PeopleRepository peopleRepository;
    @Autowired
    private ImageRepository imageRepository;

    @RequestMapping("/test.html")
    public ModelAndView test(){

        List<String> learnList =new ArrayList<>();
        learnList.add("hello1");
        learnList.add("hello2");
        ModelAndView modelAndView = new ModelAndView("admin/test");
        //添加当前登录用户
        Users users = usersService.getCurrentUser();
        modelAndView.addObject("user", users);
        modelAndView.addObject("learnList", learnList);
        return modelAndView;
    }

    @RequestMapping({"/","/index.html","/index","/main","/main.html"})
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("admin/index");
        Users users = usersService.getCurrentUser();
        modelAndView.addObject("user", users);
        return modelAndView;
    }

    //活动管理
    @RequestMapping("/activity_records.html")
    public ModelAndView activity_records(){
        ModelAndView modelAndView = new ModelAndView("admin/activity_records");
        Users users = usersService.getCurrentUser();
        modelAndView.addObject("user", users);
        return modelAndView;
    }

    //系统管理
    @RequestMapping("/system_users.html")
    public ModelAndView system_users(){

        ModelAndView modelAndView = new ModelAndView("admin/system_users");
        Users users = usersService.getCurrentUser();
        modelAndView.addObject("user", users);
        return modelAndView;
    }
    @RequestMapping("/system_people.html")
    public ModelAndView system_people(){

        ModelAndView modelAndView = new ModelAndView("admin/system_people");
        Users users = usersService.getCurrentUser();
        modelAndView.addObject("user", users);
        return modelAndView;
    }
    @RequestMapping("/gain_projects.html")
    public ModelAndView gain_projects(){

        ModelAndView modelAndView = new ModelAndView("admin/gain_projects");
        Users users = usersService.getCurrentUser();
        modelAndView.addObject("user", users);
        return modelAndView;
    }
    @RequestMapping("/gain_papers.html")
    public ModelAndView gain_papers(){

        ModelAndView modelAndView = new ModelAndView("admin/gain_papers");
        Users users = usersService.getCurrentUser();
        modelAndView.addObject("user", users);
        return modelAndView;
    }
    @RequestMapping("/gain_patents.html")
    public ModelAndView gain_patents(){

        ModelAndView modelAndView = new ModelAndView("admin/gain_patents");
        Users users = usersService.getCurrentUser();
        modelAndView.addObject("user", users);
        return modelAndView;
    }
    @RequestMapping("/gain_softwares.html")
    public ModelAndView gain_softwares(){

        ModelAndView modelAndView = new ModelAndView("admin/gain_softwares");
        Users users = usersService.getCurrentUser();
        modelAndView.addObject("user", users);
        return modelAndView;
    }

    /**
     * 文档管理的控制器
     * 未测试
     */
    @RequestMapping("/resource_files.html")
    public ModelAndView resource_files(){
        ModelAndView modelAndView = new ModelAndView("admin/resource_files");
        Users users = usersService.getCurrentUser();
        //根据用户权限查找文档,不需要在此查找数据
        //Page<Document> documentList= documentService.selectByRole(users,0,10);
        modelAndView.addObject("user", users);
        return modelAndView;
    }
    @RequestMapping("/resource_images.html")
    public ModelAndView resource_images(){
        ModelAndView modelAndView = new ModelAndView("admin/resource_images");
        Users users = usersService.getCurrentUser();
        //根据用户权限查找文档,不需要在此查找数据
        //Page<Document> documentList= documentService.selectByRole(users,0,10);
        modelAndView.addObject("user", users);
        return modelAndView;
    }
    @RequestMapping("/resource_document.html")
    public ModelAndView resource_document(){
        ModelAndView modelAndView = new ModelAndView("admin/resource_document");
        Users users = usersService.getCurrentUser();
        //根据用户权限查找文档,不需要在此查找数据
        //Page<Document> documentList= documentService.selectByRole(users,0,10);
        modelAndView.addObject("user", users);
        return modelAndView;
    }

    /**
     * 个人中心
     * */
    @RequestMapping("/user_home.html")
    public ModelAndView user_home(){
        ModelAndView modelAndView = new ModelAndView("admin/user_home");
        Users users = usersService.getCurrentUser();
        modelAndView.addObject("user", users);
        People people = peopleRepository.findOne(users.getPeopleId());
        if(people==null){
            people =new People();
            people.setName(users.getUsername());
        }
        modelAndView.addObject("person",people);
        String imgPath = null;
        if(people!=null){
            Image image = imageRepository.findOne(people.getPortrait());
            if(image!=null){
                imgPath = "/"+image.getPath();
                logger.info("find people's portrait="+imgPath);
            }
        }
        modelAndView.addObject("imgPath",imgPath);
        return modelAndView;
    }

    /**
     * 跳转更新、新建文档页面
     */
    @RequestMapping("/edit.html")
    public ModelAndView edit(@RequestParam(value="id",required=false)Integer id){ //required:不是必须传入的参数，未传入用null填充，故用integer类型不易出错

        ModelAndView modelAndView = new ModelAndView("admin/document_edit");
        Document document;
		//新建
        if(id==null||id<=0){
            //用于前端读取数据
            document = new Document();
            document.setTitle("新建文档");
            document.setModel("");
            document.setInfor("");
            document.setContent("新建文档");
        }
        //更新
        else{
            document= documentRepository.findOne(id);
        }
        Users users = usersService.getCurrentUser();
        modelAndView.addObject("user", users);
        modelAndView.addObject("document", document);
        return modelAndView;
    }


}
