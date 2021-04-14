package crawlers.indexing;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tokenizer {

    // match words in any langauge
    private final Pattern WORDS_PATTERN = Pattern.compile("([(a-zA-Z0-9)|(\u0080-\u9fff)]+)");
    
    // removes style and script tags since they won't contain informative information
    public String removeUndesiredTags(String html){

        return html.replaceAll("(?s)<(script|style).*?</(script|style)>", "");
    }

    // remove html tags
    public String removeTags(String html){
        
        return html.replaceAll("(?s)<.*?>", "");
    }

    public Set<String> tokenize(String html){

        html = removeUndesiredTags(html);

        html = removeTags(html);

        Set<String> tokens = new HashSet<String>();
        Matcher matcher = WORDS_PATTERN.matcher(html);

        while(matcher.find())
            tokens.add(matcher.group().toLowerCase());

        return tokens;
    }

}
