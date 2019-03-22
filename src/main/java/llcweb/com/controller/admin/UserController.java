package llcweb.com.controller.admin;

import llcweb.com.dao.repository.UsersRepository;
import llcweb.com.domain.models.Users;
import llcweb.com.service.UsersService;
import llcweb.com.tools.PageParam;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@Author: Ricardo
 *@Description: 此文件用于user类的数据接口
 *@Date: 15:14 2018/8/21
 **/
@Controller
@RequestMapping("/users")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UsersService usersService;

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
        Page<Users> usersPage = usersService.getPage(new PageParam(currentPage,size),users);
        List<Users> usersList = usersPage.getContent();
        //总记录数
        long total = usersPage.getTotalElements();
        map.put("pageData", usersList);
        map.put("total", total);
        map.put("draw", draw);
        map.put("result", 1);
        map.put("message", "hello");
        return map;
    }

    @RequestMapping(value = "/isCurrentUserHasRole",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> delete(@RequestParam("role")String role){
        Map<String,Object> map=new HashMap<>();

        logger.info("isCurrentUserHasRole：role="+role);
        boolean hasRole = usersService.hasRole(role);

        if (!hasRole) {
            map.put("result", 0);
            map.put("data", 0);
            logger.info("没有该权限");
            map.put("message", "没有该权限！");
        }else{
            logger.info("有该权限");
            map.put("result", 1);
            map.put("data", 1);
            map.put("message", "有该权限！");
        }
        return map;
    }
}
