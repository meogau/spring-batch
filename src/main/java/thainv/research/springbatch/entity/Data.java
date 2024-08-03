package thainv.research.springbatch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "data")
@lombok.Data
public class Data {
    @Id
    private String id;
    private String data;

}
