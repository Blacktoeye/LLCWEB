package llcweb.com.service.impl;

import llcweb.com.dao.repository.ActivityRepository;
import llcweb.com.domain.models.Activity;
import llcweb.com.service.ActivityService;
import llcweb.com.tools.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityRepository activityRepository;

    /**
     * @Author haien
     * @Description 按照标题、上传者、参与人员、起始时间、组别和类型动态查询
     * @Date 2018/10/6
     * @Param [activity, pageNum, pageSize]
     * @return org.springframework.data.domain.Page<llcweb.com.domain.models.Activity>
     **/
    @Override
    public Page<Activity> activeSearch(Activity activity, int pageNum, int pageSize) {
        //规格定义
        Specification<Activity> specification=new Specification<Activity>() {
            @Override
            public Predicate toPredicate(Root<Activity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //所有的断言
                List<Predicate> predicates=new ArrayList<>();
                //添加断言
                if(!StringUtil.isNull(activity.getTitle())){
                    Predicate like=cb.like(root.get("title"),"%"+activity.getTitle()+"%");
                    predicates.add(like);
                }
                if(!StringUtil.isNull(activity.getModel())){
                    Predicate like=cb.like(root.get("model"),"%"+activity.getModel()+"%");
                    predicates.add(like);
                }
                if(!StringUtil.isNull(activity.getActivityType())){
                    Predicate like=cb.like(root.get("activityType"),"%"+activity.getActivityType()+"%");
                    predicates.add(like);
                }
                if(!StringUtil.isNull(activity.getAuthor())){
                    Predicate like=cb.like(root.get("author"),"%"+activity.getAuthor()+"%");
                    predicates.add(like);
                }
                if(!StringUtil.isNull(activity.getPeopleList())){
                    Predicate like=cb.like(root.get("peopleList"),"%"+activity.getPeopleList()+"%");
                    predicates.add(like);
                }
                //传进来的起时间为时间上限，查询起始时间都在其后的活动
                if(activity.getStartDate()!=null){
                    Predicate like=cb.greaterThanOrEqualTo(root.get("startDate"),activity.getStartDate());
                    predicates.add(like);
                }
                //传进来的终时间为时间下限，查询起始时间都较其早的活动
                if(activity.getEndDate()!=null){
                    Predicate like=cb.lessThanOrEqualTo(root.get("endDate"),activity.getEndDate());
                    predicates.add(like);
                }
                //将List转换为数组
                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };

        //按年份排序
        Pageable pageable=new PageRequest(pageNum,pageSize,Sort.Direction.DESC,"startDate");
        //查询
        return activityRepository.findAll(specification,pageable);
    }

    @Override
    public Page<Activity> getPage(String activityType, int pageNum, int pageSize) {
        //按时间排序
        Pageable pageable=new PageRequest(pageNum,pageSize, Sort.Direction.DESC,"startDate");
        Page<Activity> projectPage=null;

        if(StringUtil.isNull(activityType) ||"all".equals(activityType)){
            projectPage = activityRepository.findAll(pageable);
        }else{
            projectPage = activityRepository.findByActivityType(activityType,pageable);
        }
        return projectPage;
    }
}
