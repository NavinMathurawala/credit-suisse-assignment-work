package com.creditsuisse.serverLog.serverLogService;

import com.creditsuisse.serverLog.serverLogService.service.EventService;
import com.creditsuisse.serverLog.serverLogService.service.FileParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerLogServiceApplication implements CommandLineRunner {

	Logger logger = LoggerFactory.getLogger(ServerLogServiceApplication.class);

	@Autowired
	FileParserService fileParserService;

	@Autowired
	EventService eventService;

	public static void main(String[] args) {
		SpringApplication.run(ServerLogServiceApplication.class, args);

	}

	@Override
	public void run(String[] args) throws Exception {
		if(args.length == 0) {
			logger.error("Log file not provided");
			return;
		}

		// Method to read the log file
		fileParserService.parseLogs(args[0]);

		// Method to save events
		eventService.saveAllEvents(fileParserService.getEventLogs());

		// Method to write data for alert events
		fileParserService.writeDataToFile(eventService.getAlertEventLogs(), "AlertEventLogs.txt");

		// Method to write data to normal events
		fileParserService.writeDataToFile(eventService.getNormalEventLogs(), "NormalEventLogs.txt");
	}
}
