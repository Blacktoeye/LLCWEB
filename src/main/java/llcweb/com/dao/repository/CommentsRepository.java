package llcweb.com.dao.repository;

import llcweb.com.domain.models.Activity;
import llcweb.com.domain.models.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments,Integer> {

    Page<Activity> findAll(Specification<Activity> spec, Pageable pageable);

}
