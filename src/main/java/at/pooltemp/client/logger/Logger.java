package at.pooltemp.client.logger;

import org.slf4j.LoggerFactory;

public class Logger {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger("PooltempClient");
	
	
	public static void info(String message) {
		logger.info(message);
	}
	
	public static void warn(String message) {
		logger.warn(message);
	}
	
	public static void error(String message) {
		logger.error(message);
	}
}
