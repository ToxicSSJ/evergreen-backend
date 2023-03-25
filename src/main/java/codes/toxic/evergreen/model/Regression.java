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
@Table(name = "regressions")
@NoArgsConstructor
@AllArgsConstructor
public class Regression {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "plot_id", referencedColumnName = "id")
    private Plot plot;

    @Column(name = "intercept")
    private double intercept;

    @Column(name = "slope")
    private double slope;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

}
