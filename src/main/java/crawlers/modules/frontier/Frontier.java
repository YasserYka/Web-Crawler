package crawlers.modules.frontier;

import java.util.concurrent.PriorityBlockingQueue;

import crawlers.models.FrontierElement;

public class Frontier {

    public static PriorityBlockingQueue<FrontierElement> URLS = new PriorityBlockingQueue<FrontierElement>(10, new CustomComparator());

    public static void insert(String domainaem){
        URLS.add(new FrontierElement(domainaem));
    }

    // checks if the first element in the queue is ready to crawl
    public static boolean ready(){
        if(URLS.size() > 0 && URLS.peek().getBackoffTime() < System.currentTimeMillis())
            return true;
        return false;        
    }

    public static String get(){
        return URLS.poll().getUrl();
    }
}