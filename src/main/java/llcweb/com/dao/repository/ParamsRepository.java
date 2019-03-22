package llcweb.com.dao.repository;

import llcweb.com.domain.models.Params;
import llcweb.com.domain.models.Platform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParamsRepository extends JpaRepository<Params,Integer> {

    Page<Params> findAll(Specification<Params> spec, Pageable pageable);

    Params findByName(String name);
}
