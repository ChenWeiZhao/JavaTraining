package ind.chenwz;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class HttpClientDemo {

    public static CloseableHttpClient httpclient = HttpClients.createDefault();

    // GET 调用
    public static void main(String[] args) throws Exception {

        String url = "http://localhost:8088";
        HttpHeaders headers = new DefaultHttpHeaders();
        headers.add("proxy-tag", "test");
        String text = HttpClientDemo.getAsString(url, headers);
        System.out.println("url: " + url + " ; \nresponse: " + text);

    }

    public static String getAsString(String url, HttpHeaders headers) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        for (Map.Entry<String, String> entry : headers.entries()) {
            httpGet.addHeader(entry.getKey(), entry.getValue());
        }
        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
            System.out.println(response.getStatusLine());
            System.out.println(Arrays.toString(response.getAllHeaders()));
            HttpEntity entity1 = response.getEntity();
            return EntityUtils.toString(entity1, "UTF-8");
        }
    }
}
