package codes.toxic.evergreen.repository;

import codes.toxic.evergreen.model.MeteorologicalRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.LinkedList;
import java.util.List;

public interface MeteorologicalRecordRepository extends JpaRepository<MeteorologicalRecord, Long> {

    LinkedList<MeteorologicalRecord> findAllByPlotId(@Param("plot_id") Long plotId);

    @Query("SELECT c FROM MeteorologicalRecord c WHERE c.plot.id = :plot_id ORDER BY c.updateDate DESC")
    List<MeteorologicalRecord> findLatestByPlotId(@Param("plot_id") Long plotId);

}
