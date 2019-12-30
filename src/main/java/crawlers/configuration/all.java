package crawlers.configuration;

public class all {

	//Universal proposed waiting time before sending next request
	private static final int WAITING_TIME = 60;
	//Used for testing proposed it should be removed/changed in production
	private static final int NUMBER_OF_PAGES_TO_CRAWLER = 10;
	//Full Google's bot user-agent header
	private static final String GOOGLEBOT_HEADER = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
	//Thread per worker set to 2 for testing will be changed according to docker's limit resources that will be made later
	private static final int THREAD_PER_WORKER = 2;

}
