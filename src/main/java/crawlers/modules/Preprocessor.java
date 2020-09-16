package crawlers.modules;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import crawlers.models.URL;
import crawlers.modules.frontier.Frontier;
import crawlers.storage.URLService;

public class Preprocessor implements Runnable {

    private Thread thread;
    private String document;
    private String key;
    URLService urlService;

    public Preprocessor(String document, String key) {
        this.document = document;
        this.key = key;
        urlService = new URLService();
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        // Extract URLs from HTML document
        List<String> urls = UrlLexer.extractURLs(document);
        // Convert relative URLs to absolute
        urls = RelativeUrlResolver.normalize(key, urls);

        // Drop unwanted URLs
        urls = Filter.drop(urls);

        // Filter URLs allowed to crawl 
        //urls = RobotTXT.filter(key, urls);

        // Filter URLs never visited before
        //urls = Seen.filter(urls);
        // Add URLs to database and store them back to frontier

        urls.forEach(url -> { 
            Frontier.insert(url);
            urlService.add(new URL(url, new Date(Calendar.getInstance().getTimeInMillis()), key));
        });
    }
    
}
