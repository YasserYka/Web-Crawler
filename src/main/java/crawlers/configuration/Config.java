package crawlers.configuration;

public class Config {

	//Universal proposed waiting time before sending next request
	public static final int WAITING_TIME = 60;
	//Used for testing proposed it should be removed/changed in production
	public static final int NUMBER_OF_PAGES_TO_CRAWLER = 10;
	//Thread per worker set to 2 for testing will be changed according to docker's limit resources that will be made later
	public static final int THREAD_PER_WORKER = 2;
	//Heart beat interval for Master-Slave
	public static final int HEARTBEAT_INTERVAL = 5000;
	//Tima gap before hitting a web server again
	public static final int TIME_GAP = 60;

}
