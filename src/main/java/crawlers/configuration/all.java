package crawlers.configuration;

public class all {

	//Universal proposed waiting time before sending next request
	public static final int WAITING_TIME = 60;
	//Used for testing proposed it should be removed/changed in production
	public static final int NUMBER_OF_PAGES_TO_CRAWLER = 10;
	//Full Google's bot user-agent header
	public static final String GOOGLEBOT_HEADER = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
	//Thread per worker set to 2 for testing will be changed according to docker's limit resources that will be made later
	public static final int THREAD_PER_WORKER = 2;
	//Heart beat interval for Master-Slave
	public static final int HEARTBEAT_INTERVAL = 5000;

}
