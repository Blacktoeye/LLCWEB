package llcweb.com.service;


import llcweb.com.domain.entity.UsefulUsers;
import llcweb.com.domain.models.Roles;
import llcweb.com.domain.models.Users;
import llcweb.com.tools.PageParam;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by:Ricardo
 * Description:
 * Date: 2018/2/1
 * Time: 15:59
 */

public interface UsersService {

    /*
     *添加信息
     */
    void add(Users user,List<Roles> roles);
    /*
     *更新信息
     */
    void updateById(Users user);
    /*
     *根据id查找
     */
    void findById(int id);
    /*
     *删除
    * */
    void deleteById(int id);

    /*
  * 根据分页参数以及各字段示例查找信息
  * example 为字段可能包含的值
  * */
    Page<Users> getPage(PageParam pageParam, Users example);

    Users getCurrentUser();

    Page<Users> findAll(UsefulUsers document, int pageNum, int pageSize);

    //判断用户是否包含某个权限(大写英文 roles的flag)
    boolean hasRole(String strRole);
}
