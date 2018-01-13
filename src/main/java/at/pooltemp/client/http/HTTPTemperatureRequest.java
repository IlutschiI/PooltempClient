package at.pooltemp.client.http;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.pooltemp.client.properties.PropertyFinder;
import at.pooltemp.client.service.temperature.converter.TemperatureConverter;
import at.pooltemp.client.service.temperature.db.TemperatureDBFacade;
import at.pooltemp.client.service.temperature.model.Temperature;
import at.pooltemp.client.service.temperature.model.TemperatureDTO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HTTPTemperatureRequest {

	//private static final String BASE_URL = "http://mypooltemp.ddns.net:8000/temperature";
	private static final String BASE_URL = PropertyFinder.getInstance().findProperty("base_url");
	private TemperatureDBFacade facade = new TemperatureDBFacade();

	public void postTemperature(Temperature t) {

		TemperatureDTO temperatureDTO = TemperatureConverter.convert(t);
		
		try {
			String url = BASE_URL+"/new";
			OkHttpClient client = new OkHttpClient();
			Request request;
			request = new Request.Builder().url(url).post(RequestBody.create(MediaType.parse("application/json"),
					new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(temperatureDTO))).build();

			Response response = client.newCall(request).execute();
			if (response.code() != 200) {
				System.out.println("something went wrong!!!");
				handleError(t);
			} else {
				handleSuccess(t);
				System.out.println(new Date()+": Temperature was succesfully transmitted");
			}
			response.close();
		} catch (JsonProcessingException e1) {
			handleError(t);
			e1.printStackTrace();

		} catch (IOException e) {
			handleError(t);
			e.printStackTrace();
		}

	}

	public void postAllUnTransferedTemperatures() {
		facade.findNotTransfered().stream().forEach(this::postTemperature);
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

}
