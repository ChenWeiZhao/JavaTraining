package ind.chenwz;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OkhttpdemoApplication {
    //缓存客户端实例
    final static OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();
    //final OkHttpClient okHttpClient = new OkHttpClient.Builder()
    //		.callTimeout(1, TimeUnit.SECONDS)
    //		.readTimeout(1, TimeUnit.SECONDS).build();

    public static void main(String[] args) throws IOException {
        String url = "http://localhost:8088";
        String response = OkhttpdemoApplication.run(url);
        System.out.println(response);
    }

    public static String run(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = OK_HTTP_CLIENT.newCall(request).execute()) {
            return response + "\n" + response.body().string();
        }
    }

}
