package codes.toxic.evergreen.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PlotDTO {

    private long id;

    private double longitude;
    private double latitude;

    private double lastHumidity;
    private double lastTemperature;
    private double lastPressure;

    private long lastRecordId;
    private Date updateDate;

}
