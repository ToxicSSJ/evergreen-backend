package codes.toxic.evergreen.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CreatePlotDTO {

    private double longitude;
    private double latitude;

}