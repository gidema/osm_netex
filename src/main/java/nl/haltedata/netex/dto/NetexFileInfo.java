package nl.haltedata.netex.dto;

import java.util.Calendar;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema="netex")
@Getter
@Setter
public class NetexFileInfo {
    private String agencyId;
    private String fileGroup;
    @Id
    private String fileName;
    private Calendar lastModified;
    private Long size;   
}
