package at.pooltemp.client.http;

import java.io.IOException;

import at.pooltemp.client.service.temperature.db.TemperatureDBFacade;
import at.pooltemp.client.service.temperature.model.Temperature;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HTTPTemperatureRequest {

	private static final String BASE_URL = "http://192.168.0.179:8080/temperature";
	private TemperatureDBFacade facade = new TemperatureDBFacade();

	public void postTemperature(Temperature t) {

		String url = buildUrl(t);
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(url)
				.post(RequestBody.create(MediaType.parse("application/text"), "test")).build();

		try {
			Response response = client.newCall(request).execute();
			if (response.code() != 200) {
				System.out.println("something went wrong!!!");
				handleError(t);
			} else {
				handleSuccess(t);
				System.out.println("Temperature was succesfully transmitted");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void handleError(Temperature t) {
		facade.persist(t);
	}

	private void handleSuccess(Temperature t) {
		try {
			facade.updateTransfered(t);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	private String buildUrl(Temperature t) {
		return BASE_URL + "?temp=" + t.getTemperature();
	}

}
