package llcweb.com.service;

import llcweb.com.domain.models.Paper;
import llcweb.com.domain.models.Software;
import org.springframework.data.domain.Page;

public interface SoftwareService {
    /**
     * @Author haien
     * @Description 根据标题、申请日期、简介、作者列表、申请号、公开号、公开日期和申请人动态查询
     * @Date 2018/10/10
     * @Param [spec, pageNum, pageSize]
     * @return org.springframework.data.domain.Page<llcweb.com.domain.models.Software>
     **/
    public Page<Software> activeSearch(Software spec,int pageNum,int pageSize);

    Page<Software> getPage(int pageNum, int pageSize);
}
