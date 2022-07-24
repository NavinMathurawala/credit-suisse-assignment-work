package com.creditsuisse.serverLog.serverLogService.service;

import com.creditsuisse.serverLog.serverLogService.entity.Event;
import com.creditsuisse.serverLog.serverLogService.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EventService {
    Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    EventRepository eventRepository;

    // Save all the events
    public void saveAllEvents(Map<String, Event> events){
        try{
            eventRepository.saveAll(events.values());
            logger.info("All events saved successfully");
        }catch (Exception e){
            logger.error("Error on Save: {}",e.getMessage());
        }
    }

    // Retrieves all events whose alert is set true
    public List<Event> getAlertEventLogs(){

        return eventRepository.findByAlert(true);
    }

    // Retrieves all normal events
    public List<Event> getNormalEventLogs(){
        return (ArrayList<Event>) eventRepository.findAll();
    }
}
