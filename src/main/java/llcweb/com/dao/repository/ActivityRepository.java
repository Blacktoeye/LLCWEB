package llcweb.com.dao.repository;

import llcweb.com.domain.models.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity,Integer> {
    /**
     * @Author haien
     * @Description 按照标题、上传者、参与人员、起始时间、组别和类型动态查询
     * @Date 2018/10/6
     * @Param [spec, pageable]
     * @return org.springframework.data.domain.Page<llcweb.com.domain.models.Activity>
     **/
    Page<Activity> findAll(Specification<Activity> spec, Pageable pageable);

    /**
     * @Author haien
     * @Description 模糊查询
     * @Date 2018/10/7
     * @Param [key, pageable]
     * @return org.springframework.data.domain.Page<llcweb.com.domain.models.Activity>
     **/
    @Query("from Activity a where a.author like %?1%" +
            " or a.title like %?1%" +
            " or a.peopleList like %?1%" +
            " or a.model like %?1%" +
            " or a.activityType like %?1%")
    Page<Activity> fuzzySearch(String key,Pageable pageable);

    /**
     * @Author haien
     * @Description 获取最新的会议、通知和招聘记录
     * @Date 2018/10/7
     * @Param [count] 超过记录总数自动获取全部记录
     * @return java.util.List<llcweb.com.domain.models.Activity>
     **/
    @Query(value="select * from activity where activity_type='会议' or activity_type='通知' or activity_type='招聘'" +
            " and is_publish=1 order by start_date desc limit ?1",nativeQuery=true)
    List<Activity> getLatest(int count);

    /**
     * @Author haien
     * @Description 根据传入的类型，获取具体的活动类
     * @Date 2018/10/9
     * @Param [count]
     * @return java.util.List<llcweb.com.domain.models.Activity>
     **/
    @Query(value="select * from activity where activity_type=?1 " +
            "and is_publish=1 order by start_date desc limit ?2",nativeQuery = true)
    List<Activity> getActivities(String activityType,int count);

    Page<Activity> findByActivityType(String activityType,Pageable pageable);
}
