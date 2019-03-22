package llcweb.com.dao.repository;

import llcweb.com.domain.models.Activity;
import llcweb.com.domain.models.Platform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlatformRepository extends JpaRepository<Platform,Integer> {

    Page<Platform> findAll(Specification<Platform> spec, Pageable pageable);

    List<Platform> findByIsPublish(int isPublish);
}
