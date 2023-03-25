package codes.toxic.evergreen.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class RegressionDTO {

    private long plotId;

    private Date startDate;
    private Date endDate;

    private double intercept;
    private double slope;

}
