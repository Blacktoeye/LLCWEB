package llcweb.com.service.impl;


import llcweb.com.dao.repository.UsersRepository;
import llcweb.com.domain.entity.UsefulUsers;
import llcweb.com.domain.models.Roles;
import llcweb.com.domain.models.Users;
import llcweb.com.domain.models.UsersRoles;
import llcweb.com.service.UsersService;
import llcweb.com.tools.PageParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by:Ricardo
 * Description:
 * Date: 2018/2/1
 * Time: 15:59
 */
@Service
public class UsersServiceImpl implements UsersService {

    private  final  Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UsersRepository usersRepository;
//    @Autowired
//    private UsersRolesRepository usersRolesRepository;


    @Override
    public void add(Users user,List<Roles> roles) {

        String username = user.getUsername();
        encryptPassword(user);

        usersRepository.save(user);
        UsersRoles usersRoles = new UsersRoles();
        usersRoles.setUrUserId(user.getId());
//
        user.setRoles(roles);
    }

    /**
     * 加密密码
     */
    private void encryptPassword(Users userEntity){
        String password = userEntity.getPassword();
        password = new BCryptPasswordEncoder().encode(password);
        userEntity.setPassword(password);
    }


    @Override
    public void updateById(Users user) {

        logger.info("service updateById id="+user.getId());
        encryptPassword(user);

        usersRepository.save(user);

    }

    @Override
    public void findById(int id) {
        logger.info("service findById id="+id);
    }
    @Transactional
    @Override
    public void deleteById(int id) {
        logger.info("service add id="+id);
    }

    @Override
    public Page<Users> getPage(PageParam pageParam, Users users) {
        //规格定义
        Specification<Users> specification = new Specification<Users>() {

            @Override
            public Predicate toPredicate(Root<Users> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>(); //所有的断言
                if(StringUtils.isNotBlank(users.getUsername())){ //添加断言
                    Predicate likeUserName = cb.like(root.get("username").as(String.class),"%"+users.getUsername()+"%");
                    predicates.add(likeUserName);
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };
        //分页信息
        Pageable pageable = new PageRequest(pageParam.getCurrentPage()-1,pageParam.getNumPerPage()); //页码：前端从1开始，jpa从0开始，做个转换
        //查询
        return this.usersRepository.findAll(specification,pageable);
    }

    @Override
    public Users getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        logger.info("username = "+userDetails.getUsername());
        //logger.info("username = "+userDetails.getPassword());
        Users users = usersRepository.findByUsernameAndPassword(userDetails.getUsername(),userDetails.getPassword());
        if(users!=null){
            logger.info("users id = "+users.getId()+",worker's id="+users.getPeopleId());
        }
        return users;
    }

    @Override
    public Page<Users> findAll(UsefulUsers document, int pageNum, int pageSize) {
        return null;
    }

    @Override
    public boolean hasRole(String strRole) {
        Users users = getCurrentUser();
        System.out.println("role = "+strRole);
        for (Roles role : users.getRoles()){
            System.out.println(role.getrFlag()+",");
            if(role.getrFlag().equals(strRole)){
                return true;
            }
        }
        return  false;
    }
}
