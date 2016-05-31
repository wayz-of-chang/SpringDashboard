package webservices.server.repository;

import org.springframework.data.repository.CrudRepository;
import webservices.server.model.Dashboard;

import java.util.List;

public interface DashboardRepository extends CrudRepository<Dashboard, Long> {
    List<Dashboard> findByName(String name);
}
