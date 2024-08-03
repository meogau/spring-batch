package thainv.research.springbatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thainv.research.springbatch.entity.Data;

public interface DataRepository extends JpaRepository<Data, Long> {
}