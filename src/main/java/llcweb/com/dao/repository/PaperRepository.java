package llcweb.com.dao.repository;

import llcweb.com.domain.models.Paper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by:Tong
 * Description: 论文类的repository类
 * Date: 2018/8/24
 */

public interface PaperRepository extends JpaRepository<Paper,Integer>{
	
	//分页查询
	Page<Paper> findAll(Specification<Paper> spec, Pageable pageable);
	
/*	@Query("from Paper p where p.title like %?1%"
			+ "or p.authorList like %?1%"
			+ "or p.belongProject like %?1%"
			+ "or p.periodical like %?1%"
			)
	Page<Paper> findByOneKey(String key, Pageable pageable);*/

	Page<Paper> findByAuthorList(String userName, Pageable pageable);
	
	/*
	 * 模糊查询
	 */
		@Query(value = "SELECT p from Paper p where p.title like %?1% "
				+ "or p.authorList like %?1% "
				+ "or p.belongProject like %?1% "
				+ "or p.periodical like %?1% ")
		Page<Paper> findByOneKey(String key, Pageable pageable);


	//Page<Paper> findByTeam(String string, Pageable pageable);

	/**
	 * @Author haien
	 * @Description 获取最新的论文
	 * @Date 2018/10/7
	 * @Param [count]
	 * @return java.util.List<llcweb.com.domain.models.Patent>
	 **/
	@Query(value="select * from paper where state='发表' order by date desc limit ?1",nativeQuery=true)
	List<Paper> getLatest(int count);
	
}
