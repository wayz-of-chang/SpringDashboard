package webservices;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DashboardRepository extends CrudRepository<Dashboard, Long> {
    List<Dashboard> findByName(String name);
}
