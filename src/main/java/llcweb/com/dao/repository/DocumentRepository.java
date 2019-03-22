package llcweb.com.dao.repository;


import llcweb.com.dao.repository.myInterface.ResourceRepository;
import llcweb.com.domain.models.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by:Haien
 * Description: 文档类的repository类
 * Date: 2018/8/22
 */
public interface DocumentRepository extends ResourceRepository<Document,Integer> {

    /**
     * 模糊查询
     **/
    @Query("from Document d where d.model like %?1%" +
            " or d.title like %?1%" +
            " or d.infor like %?1%" +
            " and d.author like %?2%")
    Page<Document> fuzzySearch(String key,String userName, Pageable pageable);

}
