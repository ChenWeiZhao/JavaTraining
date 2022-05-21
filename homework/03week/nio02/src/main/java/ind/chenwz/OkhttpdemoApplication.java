package ind.chenwz;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OkhttpdemoApplication {
	//缓存客户端实例
	final OkHttpClient okHttpClient = new OkHttpClient();
	//final OkHttpClient okHttpClient = new OkHttpClient.Builder()
	//		.callTimeout(1, TimeUnit.SECONDS)
	//		.readTimeout(1, TimeUnit.SECONDS).build();

	public static void main(String[] args) throws IOException {
		OkhttpdemoApplication okhttpdemoApplication = new OkhttpdemoApplication();
		String response = okhttpdemoApplication.run("http://localhost:8088");
		System.out.println(response);
	}

	private String run(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();
		try (Response response = okHttpClient.newCall(request).execute()) {
			return response.toString();
		}

	}

}
