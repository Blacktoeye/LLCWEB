package llcweb.com.service;

import java.util.List;
import java.util.Map;

import llcweb.com.domain.models.Paper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import llcweb.com.domain.entity.UsefulPatent;
import llcweb.com.domain.entity.UsefulProject;
import llcweb.com.domain.models.Patent;
import llcweb.com.domain.models.Project;
import llcweb.com.domain.models.Users;
@Service
public interface PatentService {
	
	public Page<Patent> findAll(UsefulPatent patent, int pageNum, int pageSize);
	
	 public Map<String, Object> add(Patent patent);
	 public Map<String, Object> update(Patent patent);
	 public Map<String, Object> delete(int id);
	
	  List<UsefulPatent> patentsToUsefulpatent(List<Patent> patentList);
	  //public List<Patent> selectAll(Users user);
	 
	 // Page<Patent> getPage(int pageNum, int pageSize, Patent patent);
 
	 //Page<Patent> selectAll(Users user, int pageNum, int pageSize);

	 Page<Patent> getPage(int pageNum, int pageSize);
}
