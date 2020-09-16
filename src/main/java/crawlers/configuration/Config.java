package crawlers.configuration;

public class Config {

	//Universal proposed waiting time before sending next request
	public static final int WAITING_TIME = 30000;
	//Used for testing proposed it should be removed/changed in production
	public static final int NUMBER_OF_PAGES_TO_CRAWLER = 10;
	//Tima gap before hitting a web server again
	public static final int TIME_GAP = 60;

}
