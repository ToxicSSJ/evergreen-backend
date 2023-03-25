package codes.toxic.evergreen.repository;

import codes.toxic.evergreen.model.Plot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlotRepository extends JpaRepository<Plot, Long> {

    List<Plot> findAllByLongitudeAndLatitude(double longitude, double latitude);

}
