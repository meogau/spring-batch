package thainv.research.springbatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thainv.research.springbatch.entity.ProcessedData;

public interface ProcessedDataRepository extends JpaRepository<ProcessedData, Long> {
}