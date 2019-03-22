package llcweb.com.service.impl;

import llcweb.com.dao.repository.UsersRepository;
import llcweb.com.domain.models.Roles;
import llcweb.com.domain.models.Users;
import llcweb.com.service.UsersService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by:Ricardo
 * Description:
 * Date: 2018/8/24
 * Time: 14:30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UsersServiceImplTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersService usersService;


    @Test
    public void add() throws Exception {

        Users users = new Users();
        users.setUsername("admin");
        users.setPassword("admin");
        Roles roles = null;
        List<Roles> rolesList = new ArrayList<>();

        new Roles(3,"系统管理员1","ADMIN1");
        rolesList.add(roles);
//        roles = new Roles(2,"组长","GROUP");
//        rolesList.add(roles);
//        roles = new Roles(3,"成员","USER");
//        rolesList.add(roles);
        usersService.add(users,rolesList);

    }

    @Test
    public void update() throws Exception {

        Users users = usersRepository.findByUsername("admin");
        users.setUsername("admin");
        users.setPassword("admin");
        usersService.updateById(users);

//
//        String strs[] = {"数据组","网络组","机器人组","太赫兹组","高分高性能组","图像组"};
//        for (String str:strs){
//            Users users1 = usersRepository.findByUsername(str);
//            users1.setUsername(str);
//            users1.setPassword("888888");
//            usersService.updateById(users1);
//        }

    }
}