package llcweb.com.service.impl;

import llcweb.com.dao.repository.SoftwareRepository;
import llcweb.com.domain.models.Software;
import llcweb.com.service.SoftwareService;
import llcweb.com.tools.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class SoftwareServiceImpl implements SoftwareService {
    @Resource
    private SoftwareRepository softwareRepository;

    /**
     * @Author haien
     * @Description 根据标题、申请日期、简介、作者列表、申请号、公开号、公开日期动态查询
     * @Date 2018/10/10
     * @Param [spec, pageNum, pageSize]
     * @return org.springframework.data.domain.Page<llcweb.com.domain.models.Software>
     **/
    @Override
    public Page<Software> activeSearch(Software software, int pageNum, int pageSize) {
        //规格定义
        Specification<Software> specification=new Specification<Software>() {
            @Override
            public Predicate toPredicate(Root<Software> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //所有的断言
                List<Predicate> predicates=new ArrayList<>();
                //添加断言
                if(!StringUtil.isNull(software.getTitle())){
                    Predicate like=cb.like(root.get("title"),"%"+software.getTitle()+"%");
                    predicates.add(like);
                }
                if(!StringUtil.isNull(software.getIntroduction())){
                    Predicate like=cb.like(root.get("introduction"),"%"+software.getIntroduction()+"%");
                    predicates.add(like);
                }
                if(!StringUtil.isNull(software.getAuthorList())){
                    Predicate like=cb.like(root.get("authorList"),"%"+software.getAuthorList()+"%");
                    predicates.add(like);
                }
                if(!StringUtil.isNull(software.getAppliNum())){
                    Predicate like=cb.like(root.get("appliNum"),"%"+software.getAppliNum()+"%");
                    predicates.add(like);
                }
                if(!StringUtil.isNull(software.getPublicNum())){
                    Predicate like=cb.like(root.get("publicNum"),"%"+software.getPublicNum()+"%");
                    predicates.add(like);
                }
                if(software.getAppliDate()!=null){
                    Predicate greaterThanOrEqualTo=cb.greaterThanOrEqualTo(root.get("appliDate"),software.getAppliDate());
                    predicates.add(greaterThanOrEqualTo);
                }
                if(software.getPublicDate()!=null){
                    Predicate lessThanOrEqualTo=cb.lessThanOrEqualTo(root.get("publicDate"),software.getPublicDate());
                    predicates.add(lessThanOrEqualTo);
                }
                //将List转换为数组
                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };

        //按年份排序
        Pageable pageable=new PageRequest(pageNum,pageSize,Sort.Direction.DESC,"publicDate");
        //查询
        return softwareRepository.findAll(specification,pageable);
    }

    @Override
    public Page<Software> getPage(int pageNum, int pageSize) {
        Pageable pageable=new PageRequest(pageNum,pageSize, Sort.Direction.DESC,"appliDate");
        Page<Software> projectPage= softwareRepository.findAll(pageable);
        return projectPage;
    }
}
