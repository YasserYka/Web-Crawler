package crawlers.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class MakeRequest {
	
    private static  CloseableHttpClient httpClient;

	public static String get(String url){
		httpClient = HttpClients.createDefault();
		HttpGet request = new HttpGet();
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
}
