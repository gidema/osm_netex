package nl.haltedata.gtfs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportStatus {
    private Long rollbackCount;
    private Long quaysInDB;
    private Long writeCount;
    private Long commitCount;
    private Double progress;
    private Long readCount;
    private Long skipCount;
    private String status;
}
