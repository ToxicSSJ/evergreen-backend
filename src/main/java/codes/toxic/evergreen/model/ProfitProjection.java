package codes.toxic.evergreen.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Builder
@Table(name = "profit_projections")
@NoArgsConstructor
@AllArgsConstructor
public class ProfitProjection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "corpType")
    private String corpType;

    @Column(name = "investment")
    private double investment;

    @Column(name = "tons")
    private double tons;

    @Column(name = "investment_date")
    private Date investmentDate;

    @Column(name = "harvesting_date")
    private Date harvestingDate;

    @Column(name = "estimated_profit")
    private double estimatedProfit;

    @Column(name = "interest")
    private double interest;

    @Column(name = "percentage")
    private double percentage;

    @Column(name = "projectedProfit")
    private double projectedProfit;

    @Column(name = "creationDate")
    private Date creationDate;

}
