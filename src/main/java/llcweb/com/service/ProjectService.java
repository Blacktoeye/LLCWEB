package llcweb.com.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import llcweb.com.domain.entity.UsefulProject;
import llcweb.com.domain.models.Project;
import llcweb.com.domain.models.Project;
import llcweb.com.domain.models.Users;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author tong
 * @登录以后的项目信息服务接口
 *
 */
public interface ProjectService {
	
	/**
	 * 查询项目并按照时间分页排序
	 * @param project
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */

	public Page<Project> findAll(UsefulProject project, int pageNum, int pageSize);
	
	public Page<Project> selectAll(Users user, int pageNum, int pageSize);
	
	 public Map<String, Object> add(Project project);
	 public Map<String, Object> update(Project project);
	 public Map<String, Object> delete(int id);
	 
	List<UsefulProject> projectsToUsefulProject(List<Project> projectList);

	Page<Project> getPage(String team,int pageNum,int pageSize);
 
	

}
