package crawlers.modules.exclusion;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import crawlers.modules.DNSResolution;
import crawlers.util.Lexer;

public class RobotTXT {
	
	private static final String ROBOT_TXT_PATH = "/robots.txt";
    private static  CloseableHttpClient httpClient;
	
	public static void getRobotsTxt(String domainName){
		String address = DNSResolution.resolveHostnameToIP(domainName).getHostName();
		System.out.println(address);
		String file = makeGetRequest(address);
	}
	
	public static String makeGetRequest(String domainName){
		httpClient = HttpClients.createDefault();
		HttpGet request = new HttpGet(domainName+ROBOT_TXT_PATH);
        HttpEntity entity = null;
		String body = "";
		
		request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");
		try (CloseableHttpResponse response = httpClient.execute(request)) {
        	if(response.getStatusLine().getStatusCode() != 200)
        		entity = response.getEntity();
        	if(entity != null)
        		body = EntityUtils.toString(entity);
		}
        catch(ClientProtocolException cpe) {/*TODO: LOG IT*/}
        catch (IOException ie) {/*TODO:LOG IT*/}
        
        return body;
	}
	
	public static List<String> extractDisallowedPaths(String robotTxtFile){
		List<String> paths = Lexer.extractExcludedPaths(robotTxtFile);
		return paths;
	}
}
