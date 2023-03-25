package codes.toxic.evergreen.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetricDTO {

    private double temperature;
    private double humidity;
    private double pressure;

}
