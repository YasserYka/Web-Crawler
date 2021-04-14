package crawlers.indexing;

public class tokenizer {
    
    // removes style and script tags since they won't contain informative information
    public String removeUndesiredTags(String html){

        return html.replaceAll("<(script|style).*?</(script|style)>", "");
    }

    // remove html tags
    public String removeTags(String html){
        
        return html.replaceAll("<.*?>", "");
    }

}
