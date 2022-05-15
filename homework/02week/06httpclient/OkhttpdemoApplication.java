package cwz.study.okhttpdemo;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OkhttpdemoApplication {
	final OkHttpClient okHttpClient = new OkHttpClient();

	public static void main(String[] args) throws IOException {
		OkhttpdemoApplication okhttpdemoApplication = new OkhttpdemoApplication();
		String response = okhttpdemoApplication.run("http://localhost:8801");
		System.out.println(response);
	}

	private String run(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();
		try (Response response = okHttpClient.newCall(request).execute()) {
			return response.toString();
		}

	}

}
