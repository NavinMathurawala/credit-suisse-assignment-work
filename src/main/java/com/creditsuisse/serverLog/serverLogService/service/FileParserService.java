package com.creditsuisse.serverLog.serverLogService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.creditsuisse.serverLog.serverLogService.entity.Event;
import com.creditsuisse.serverLog.serverLogService.model.LogFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service
public class FileParserService {
    private Map<String, Event> logEventsMap;

    Logger logger = LoggerFactory.getLogger(FileParserService.class);

    public final static int NUMBER_FOUR = 4;

    public FileParserService(){
        logEventsMap = new HashMap<>();
    }

    public Map<String, Event> getEventLogs(){
        return logEventsMap;
    }


    public void parseLogs(String filePath) throws IOException {

        logger.info("Entry Point of parseLogs method started");
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream(filePath);
            sc = new Scanner(inputStream, "UTF-8");
            // To handle large files reading one entry at a time
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                LogFile log = new ObjectMapper().readValue(line, LogFile.class);
                Event event = null;
                boolean alert = false;

                //Populating the Map
                if (!logEventsMap.containsKey(log.getId())) {
                    event = new Event(log.getId(), log.getTimestamp(), alert, log.getType(), log.getHost());
                    logEventsMap.put(log.getId(), event);

                    continue;

                }
                //Removes the entry if already existing
                Event existingEvent = logEventsMap.remove(log.getId());

                long startTime = existingEvent.getDuration();

                long duration = Math.abs(log.getTimestamp() - startTime);


                if (duration > FileParserService.NUMBER_FOUR) {
                    alert = true;
                }
                event = new Event(log.getId(), duration, alert, log.getType(), log.getHost());
                logEventsMap.put(log.getId(),event);
            }
            logger.info("Exit Point of parseLogs Method");
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } catch (FileNotFoundException e) {
            logger.error("Log file not found: {}",e.getMessage());
        } catch (IOException e) {
            logger.error("Error: {}",e.getMessage());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }
    }


    public void writeDataToFile(List<Event> data, String file){
        ObjectMapper mapper = new ObjectMapper();
        try {
            FileWriter fileWriter = new FileWriter(file);
            for(Event entry : data){
                    String json = mapper.writeValueAsString(entry);
                    fileWriter.write(json);
                    fileWriter.write("\n");
            }
            fileWriter.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
