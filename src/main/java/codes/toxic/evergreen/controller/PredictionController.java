package codes.toxic.evergreen.controller;

import codes.toxic.evergreen.model.MeteorologicalRecord;
import codes.toxic.evergreen.model.Plot;
import codes.toxic.evergreen.model.Regression;
import codes.toxic.evergreen.repository.MeteorologicalRecordRepository;
import codes.toxic.evergreen.repository.PlotRepository;
import codes.toxic.evergreen.repository.RegressionRepository;
import codes.toxic.evergreen.dto.MakePredictionDTO;
import codes.toxic.evergreen.dto.RegressionDTO;
import codes.toxic.evergreen.dto.ResponseDTO;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prediction")
public class PredictionController {

    @Autowired
    private PlotRepository plotRepository;

    @Autowired
    private MeteorologicalRecordRepository meteorologicalRecordRepository;

    @Autowired
    private RegressionRepository regressionRepository;

    @PostMapping
    public ResponseEntity makePrediction(@RequestBody MakePredictionDTO body) {

        // Instantiate simple regression
        SimpleRegression regression = new SimpleRegression();

        // Get plot
        List<Plot> plots = plotRepository.findAllByLongitudeAndLatitude(body.getLongitude(), body.getLatitude());

        if(plots == null || plots.isEmpty())
            return ResponseEntity.ok(ResponseDTO.builder()
                    .message("NOT_FOUND")
                    .build());

        Plot plot = plots.get(0);

        // Meteorological Records
        LinkedList<MeteorologicalRecord> meteorologicalRecords = meteorologicalRecordRepository.findAllByPlotId(plot.getId());

        if(meteorologicalRecords.isEmpty())
            return ResponseEntity.ok(ResponseDTO.builder()
                    .message("EMPTY")
                    .build());

        for (MeteorologicalRecord meteorologicalRecord : meteorologicalRecords)
            regression.addData(meteorologicalRecord.getUpdateDate().getTime(), meteorologicalRecord.getTemperature());

        // Data
        double intercept = regression.getIntercept();
        double slope = regression.getSlope();

        return ResponseEntity.ok(RegressionDTO.builder()
                .plotId(plot.getId())
                .startDate(meteorologicalRecords.getFirst().getUpdateDate())
                .endDate(meteorologicalRecords.getLast().getUpdateDate())
                .intercept(intercept)
                .slope(slope)
                .build());

    }

    @GetMapping
    public ResponseEntity<List<Regression>> getAll() {
        return ResponseEntity.ok(regressionRepository.findAll());
    }

    @PutMapping
    public ResponseEntity savePrediction(@RequestBody RegressionDTO body) {

        Optional<Plot> optionalPlot = plotRepository.findById(body.getPlotId());

        if(optionalPlot.isEmpty())
            return ResponseEntity.ok(ResponseDTO.builder()
                    .message("EMPTY")
                    .build());

        Plot plot = optionalPlot.get();
        Regression regression = Regression.builder()
                .plot(plot)
                .intercept(body.getIntercept())
                .slope(body.getSlope())
                .startDate(body.getStartDate())
                .endDate(body.getEndDate())
                .build();

        regression = regressionRepository.save(regression);
        return ResponseEntity.ok(regression);

    }

}
