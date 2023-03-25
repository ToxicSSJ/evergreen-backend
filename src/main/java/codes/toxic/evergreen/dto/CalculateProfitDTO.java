package codes.toxic.evergreen.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalculateProfitDTO {

    private String type;

    private double investment;
    private double tons;
    private double estimated;

    private String startDate;
    private String endDate;

}
