package com.creditsuisse.serverLog.serverLogService.services;

import com.creditsuisse.serverLog.serverLogService.entity.Event;
import com.creditsuisse.serverLog.serverLogService.service.FileParserService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileParserServiceTest {

    private FileParserService mockParser = new FileParserService();

    @Test
    public void parseLogsTest() {
        try {
            mockParser.parseLogs("src/test/resources/testLogFile.txt");
            assert(mockParser.getEventLogs().size() == 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void writeDataToFileTest() {
        try {
            List<Event> data = new ArrayList<>();
            data.add(new Event());
            mockParser.writeDataToFile(data, "src/test/resources/outputFile.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
