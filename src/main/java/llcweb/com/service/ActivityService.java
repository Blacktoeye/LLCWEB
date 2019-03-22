package llcweb.com.service;

import llcweb.com.domain.models.Activity;
import org.springframework.data.domain.Page;

public interface ActivityService {
    /**
     * @Author haien
     * @Description 按照标题、上传者、参与人员、起始时间、组别和类型动态查询
     * @Date 2018/10/6
     * @Param [activity, pageNum, pageSize]
     * @return org.springframework.data.domain.Page<llcweb.com.domain.models.Activity>
     **/
    Page<Activity> activeSearch(Activity activity,int pageNum,int pageSize);

    Page<Activity> getPage(String team,int pageNum,int pageSize);
}
