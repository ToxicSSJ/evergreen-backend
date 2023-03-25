package codes.toxic.evergreen;

import codes.toxic.evergreen.controller.PlotController;
import codes.toxic.evergreen.model.Plot;
import codes.toxic.evergreen.repository.PlotRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@SpringBootApplication
public class SpringBootDataJpaApplication {

	@Autowired
	private PlotController plotController;

	@Autowired
	private PlotRepository plotRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

	@Scheduled(cron = "0 */15 * ? * *")
	public void execute() {

		for(Plot plot : plotRepository.findAll())
			plotController.updatePlot(plot);

	}

}
