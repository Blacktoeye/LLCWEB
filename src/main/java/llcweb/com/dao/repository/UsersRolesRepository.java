package llcweb.com.dao.repository;


import llcweb.com.domain.models.Users;
import llcweb.com.domain.models.UsersRoles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by:Ricardo
 * Description: 用户类的repository类
 * Date: 2018/8/21
 * Time: 13:36
 */
 //Integer 是id 的类型
//public interface UsersRolesRepository extends JpaRepository<UsersRoles,Integer>{
//
//    /**
//     *分页+动态查询
//     **/
//    Page<Users> findAll(Specification<UsersRoles> spec, Pageable pageable);
//
//}
