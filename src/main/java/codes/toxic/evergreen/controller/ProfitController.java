package codes.toxic.evergreen.controller;

import codes.toxic.evergreen.dto.CalculateProfitDTO;
import codes.toxic.evergreen.model.ProfitProjection;
import codes.toxic.evergreen.repository.MeteorologicalRecordRepository;
import codes.toxic.evergreen.repository.PlotRepository;
import codes.toxic.evergreen.repository.ProfitProjectionRepository;
import codes.toxic.evergreen.util.DateUtility;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/profit")
public class ProfitController {

    @Autowired
    private PlotRepository plotRepository;

    @Autowired
    private ProfitProjectionRepository profitProjectionRepository;

    @Autowired
    private MeteorologicalRecordRepository meteorologicalRecordRepository;

    @PostMapping
    public ResponseEntity calculateProfit(@RequestBody CalculateProfitDTO body) {

        Date startDate = DateUtility.stringToDate(body.getStartDate());
        Date endDate = DateUtility.stringToDate(body.getEndDate());

        double value = getCommodityValue(body.getType());
        double real = value * body.getTons();

        Pair<Double, Double> result = calculateInterest(startDate, endDate, body.getInvestment(), body.getEstimated(), real);

        ProfitProjection profitProjection = ProfitProjection.builder()

                .corpType(body.getType())
                .tons(body.getTons())
                .creationDate(new Date())

                .interest(result.getFirst())
                .percentage(result.getSecond())
                .investment(body.getInvestment())

                .estimatedProfit(body.getEstimated())
                .projectedProfit(real)

                .harvestingDate(endDate)
                .investmentDate(startDate)

                .build();

        return ResponseEntity.ok(profitProjection);

    }

    @PutMapping
    public ResponseEntity saveProfit(@RequestBody ProfitProjection body) {
        body = profitProjectionRepository.save(body);
        return ResponseEntity.ok(body);
    }

    @GetMapping
    public ResponseEntity<List<ProfitProjection>> getAll() {
        return ResponseEntity.ok(profitProjectionRepository.findAll());
    }

    public static double getCommodityValue(String commodity) {

        String url = "https://commodities-api.com/api/latest";
        String apiKey = "g6o3s248g04gq3981sadk1zcuyt5smfxmxu43lan78rom086w28sqml8uiu5";

        HttpResponse<JsonNode> response = Unirest.get(url)
                .header("accept", "application/json")
                .queryString("access_key", apiKey)
                .queryString("symbols", commodity)
                .asJson();

        JSONObject object = response.getBody().getObject();
        System.out.println(object.toString());

        double value = response.getBody().getObject()
                .getJSONObject("data")
                .getJSONObject("rates")
                .getDouble(commodity);

        return value;

    }

    public static Pair<Double, Double> calculateInterest(Date startDate, Date endDate, double investment, double estimatedProfit, double realProfit) {

        long diffInMillies = endDate.getTime() - startDate.getTime();
        long days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        double interest = Math.pow((1 + realProfit / investment), (365.0 / days)) - 1;
        double percentage = ((realProfit - estimatedProfit) / estimatedProfit) * 100;

        return Pair.create(interest, percentage);

    }

}
