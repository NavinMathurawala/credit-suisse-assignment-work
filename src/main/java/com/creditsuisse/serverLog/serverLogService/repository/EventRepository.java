package com.creditsuisse.serverLog.serverLogService.repository;

import com.creditsuisse.serverLog.serverLogService.entity.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, String> {
    @Query(value = "SELECT * FROM Event e where e.alert = ?1", nativeQuery = true)
    List<Event> findByAlert(boolean b);
}
