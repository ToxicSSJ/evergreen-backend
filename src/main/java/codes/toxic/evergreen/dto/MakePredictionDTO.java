package codes.toxic.evergreen.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class MakePredictionDTO {

    private double longitude;
    private double latitude;

}
