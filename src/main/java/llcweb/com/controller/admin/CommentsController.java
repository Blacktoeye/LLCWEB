package llcweb.com.controller.admin;

import llcweb.com.dao.repository.CommentsRepository;
import llcweb.com.dao.repository.UsersRepository;
import llcweb.com.domain.models.Comments;
import llcweb.com.domain.models.People;
import llcweb.com.domain.models.Users;
import llcweb.com.service.UsersService;
import llcweb.com.tools.IpAddressUtil;
import llcweb.com.tools.PageParam;
import llcweb.com.tools.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@Author: Ricardo
 *@Description: 此文件用于user类的数据接口
 *@Date: 15:14 2018/8/21
 **/
@Controller
@RequestMapping("/comments")
public class CommentsController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommentsRepository commentsRepository;

    @RequestMapping(value = "/page",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> page(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map =new HashMap<String,Object>();

        //直接返回前台
        String draw = request.getParameter("draw");
        //当前数据的起始位置 ，如第10条
        String startIndex = request.getParameter("startIndex");
        //数据长度
        String pageSize = request.getParameter("pageSize");
        int size = Integer.parseInt(pageSize);
        int currentPage = Integer.parseInt(startIndex)/size+1;

        Users users = new Users();
        String fuzzy = request.getParameter("fuzzySearch");
        if("true".equals(fuzzy)){//模糊查找
            String searchValue = request.getParameter("fuzzy");
            if (searchValue!=null&&!searchValue.equals("")) {
                users.setUsername(searchValue);
            }
        }
        //高级查找
        else{
            String username = request.getParameter("username");
            if (username!=null&&!username.equals("")) {
                users.setUsername(username);
            }
        }
        map.put("message", "hello");
        return map;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        String content = request.getParameter("content");
        String name = request.getParameter("name");
        String contact = request.getParameter("contact");

        System.out.println("content = "+content);
        Date date = new Date();
        String ipAddress = IpAddressUtil.getIpAddr(request);
        Comments comments = new Comments(name,contact,content,date,ipAddress,0);
        Comments other = commentsRepository.save(comments);

       if(other!=null && other.getId()>0){
           map.put("result", 1);
           map.put("message", "成功保存留言！");
           logger.info("成功保存人员！");
       }else{
           map.put("result", 0);
           map.put("message", "保存留言失败！");
           logger.info("保存留言失败！");
       }

        return map;
    }
}
