package llcweb.com.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import llcweb.com.tools.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import llcweb.com.dao.repository.PaperRepository;
import llcweb.com.domain.entity.UsefulPaper;
import llcweb.com.domain.models.Paper;
import llcweb.com.domain.models.Roles;
import llcweb.com.domain.models.Users;
import llcweb.com.service.PaperService;

@Service
public class PaperServiceImpl implements PaperService {

	@Autowired
	private PaperRepository paperRepository;
	
	@Override
	public Page<Paper> findAll(UsefulPaper paper, int pageNum, int pageSize) {
		
		 //按时间排序
        Pageable pageable=new PageRequest(pageNum,pageSize, Sort.Direction.DESC,"date");
        
       
        Page<Paper> paperList = paperRepository.findAll(new Specification<Paper>() {
        	
        	@Override
        	public Predicate toPredicate(Root<Paper> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				 Predicate predicate = cb.conjunction();
				 if (paper.getFirstDate() != null) {
	                    predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("date"), paper.getFirstDate()));
	                }
	             if (paper.getLastDate() != null) {
	                    predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("date"), paper.getLastDate()));
	                }
				 if(paper.getTitle() != null) {
					 predicate.getExpressions().add(cb.like(root.get("title"),"%" +  paper.getTitle() + "%"));
				 }
				 if(paper.getAuthorList() != null) {
					 predicate.getExpressions().add(cb.like(root.get("authorList"),"%" +  paper.getAuthorList() + "%"));
				 }
				 if(paper.getBelongProject() != null) {
					 predicate.getExpressions().add(cb.like(root.get("belongProject"),"%" +  paper.getBelongProject() + "%"));
				 }
				 if(paper.getPeriodical() != null) {
					 predicate.getExpressions().add(cb.like(root.get("periodical"),"%" +  paper.getPeriodical() + "%"));
				 }
				return predicate;
				
			}
        }, pageable);
        
		return paperList;
	}

	/**
	 * 修改论文信息
	 */
	@Override
	public Map<String, Object> update(Paper paper) {
		Map<String,Object> map=new HashMap<>();
        if(paperRepository.findOne(paper.getId())!=null){
            if(paperRepository.save(paper)!=null){
                map.put("result",1);
                map.put("msg","论文信息修改成功");
                return map;
            }
        }
        map.put("result",0);
        map.put("msg","更新失败，请确认论文是否存在！");
        return map;
	}
	/**
	 * 删除论文
	 */
	@Override
	public Map<String, Object> delete(int id) {

        Map<String, Object> map = new HashMap<>();

        if (paperRepository.findOne(id) != null) {
        	paperRepository.delete(id);
            map.put("result", 1);
            map.put("msg", "论文已删除！");
            return map;
        }
        map.put("result", 0);
        map.put("msg", "删除失败，请确认论文是否存在！");
        return map;
    }

	@Override
	public List<UsefulPaper> papersToUsefulPaper(List<Paper> paperList) {
		return null;
	}

	@Override
	public Page<Paper> getPage(int pageNum, int pageSize) {
		//按时间排序
		Pageable pageable=new PageRequest(pageNum,pageSize, Sort.Direction.DESC,"date");
		Page<Paper> projectPage= paperRepository.findAll(pageable);
		return projectPage;
	}

	/*	*//**
	 * 分页
	 *//*
	@Override
	public Page<Paper> getPage(int pageNum, int pageSize, Paper paper) {
       
      
		Specification<Paper> specification = new Specification<Paper>() {
       	
       	@Override
       	public Predicate toPredicate(Root<Paper> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
       		List<Predicate> predicates = new ArrayList<>();
       		if(paper.getTitle() != null) {
       			predicates.add(cb.like(root.get("title"),"%" +  paper.getTitle() + "%"));
				 }
       		if(paper.getAuthorList() != null) {
					 predicates.add(cb.like(root.get("authorList"),"%" +  paper.getAuthorList() + "%"));
					 }
       		if(paper.getBelongProject() != null) {
					 predicates.add(cb.like(root.get("belongProject"),"%" +  paper.getBelongProject() + "%"));
					 }
       		if(paper.getPeriodical() != null) {
					predicates.add(cb.like(root.get("periodical"),"%" +  paper.getPeriodical() + "%"));
					 }
            return cb.and(predicates.toArray(new Predicate[0]));
        }
    };
    
    //分页信息
    Pageable pageable = new PageRequest(pageNum,pageSize); //页码
    //查询
    return paperRepository.findAll(specification,pageable);
       }*/
	
	/**
	 * 添加论文信息
	 */
	@Override
	public Map<String, Object> add(Paper paper) {
        Map<String,Object> map=new HashMap<>();

        if(paperRepository.findOne(paper.getId())==null){
            if(paperRepository.save(paper)!=null){
                map.put("result",1);
                map.put("msg","论文添加成功！");
                return map;
            }
        }
        map.put("result",0);
        map.put("msg","添加失败，请确认论文是否已存在！");
        return map;
    }
}
