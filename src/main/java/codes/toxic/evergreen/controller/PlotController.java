package codes.toxic.evergreen.controller;


import codes.toxic.evergreen.dto.MetricDTO;
import codes.toxic.evergreen.dto.PlotDTO;
import codes.toxic.evergreen.model.MeteorologicalRecord;
import codes.toxic.evergreen.model.Plot;
import codes.toxic.evergreen.repository.PlotRepository;
import codes.toxic.evergreen.dto.CreatePlotDTO;
import codes.toxic.evergreen.repository.MeteorologicalRecordRepository;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/plots")
public class PlotController {

	@Autowired
	private PlotRepository plotRepository;

	@Autowired
	private MeteorologicalRecordRepository meteorologicalRecordRepository;

	@PostMapping
	public ResponseEntity createPlot(@RequestBody CreatePlotDTO body) {

		// Create plot
		Plot plot = Plot.builder()
				.longitude(body.getLongitude())
				.latitude(body.getLatitude())
				.build();

		plot = plotRepository.save(plot);

		// Update meteorological record
		updatePlot(plot);

		return ResponseEntity.ok(plot);

	}

	@GetMapping
	public ResponseEntity<List<PlotDTO>> getAllPlots() {

		List<Plot> plots = plotRepository.findAll();
		List<PlotDTO> dtos = new ArrayList<>();

		for(Plot plot : plots) {

			List<MeteorologicalRecord> meteorologicalRecords = meteorologicalRecordRepository.findLatestByPlotId(plot.getId());
			Optional<MeteorologicalRecord> optionalMeteorologicalRecord = meteorologicalRecords.isEmpty() ? Optional.empty() : Optional.of(meteorologicalRecords.get(0));

			if(optionalMeteorologicalRecord.isEmpty()) {

				dtos.add(PlotDTO.builder()
						.id(plot.getId())
						.lastRecordId(-1)
						.lastHumidity(plot.getLatitude())
						.lastTemperature(0)
						.lastPressure(0)
						.lastHumidity(0)
						.longitude(plot.getLongitude())
						.latitude(plot.getLatitude())
						.updateDate(new Date())
						.build());

				continue;

			}

			MeteorologicalRecord meteorologicalRecord = optionalMeteorologicalRecord.get();

			dtos.add(PlotDTO.builder()
							.id(plot.getId())
							.lastRecordId(meteorologicalRecord.getId())

							.latitude(plot.getLatitude())
							.longitude(plot.getLongitude())

							.lastHumidity(meteorologicalRecord.getHumidity())
							.lastTemperature(meteorologicalRecord.getTemperature())
							.lastPressure(meteorologicalRecord.getPressure())

							.updateDate(meteorologicalRecord.getUpdateDate())

					.build());

			continue;

		}

		return new ResponseEntity<>(dtos, HttpStatus.OK);

	}

	public void updatePlot(Plot plot) {

		MetricDTO metricDTO = getMetrics(plot.getLatitude(), plot.getLongitude());

		MeteorologicalRecord meteorologicalRecord = MeteorologicalRecord.builder()
				.plot(plot)
				.temperature(metricDTO.getTemperature())
				.humidity(metricDTO.getHumidity())
				.pressure(metricDTO.getPressure())
				.updateDate(new Date())
				.build();

		meteorologicalRecordRepository.save(meteorologicalRecord);

	}

	public static MetricDTO getMetrics(double latitude, double longitude) {

		String url = "https://api.openweathermap.org/data/2.5/weather";
		String apiKey = "fbd93ff213b51954cdc432acfad620ae";

		HttpResponse<JsonNode> response = Unirest.get(url)
				.header("accept", "application/json")
				.queryString("lat", latitude)
				.queryString("lon", longitude)
				.queryString("APPID", apiKey)
				.asJson();

		JSONObject object = response.getBody().getObject().getJSONObject("main");

		double temperature = object.getDouble("temp");
		double pressure = object.getDouble("pressure");
		double humidity = object.getDouble("humidity");

		return MetricDTO.builder()
				.temperature(temperature)
				.pressure(pressure)
				.humidity(humidity)
				.build();

	}



}
