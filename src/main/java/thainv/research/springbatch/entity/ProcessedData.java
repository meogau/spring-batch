package thainv.research.springbatch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "processed_data")
@Data
public class ProcessedData {
    @Id
    private String id;
    private String data;
    private int status;

}
