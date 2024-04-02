package nl.haltedata.netex.dto;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface NetexFileRepository extends CrudRepository<NetexFileInfo, String> {
    //
    
    @Query(nativeQuery = true, value = """
SELECT agency_id AS agencyId, file_group AS transportArea, MAX(file_name) AS fileName
FROM netex.netex_file_info
WHERE file_group IN (
  SELECT transport_area
  FROM netex.netex_transport_area)
AND file_group NOT IN ('ARR_BO', 'ARR_BW', 'ARR_ZH')
GROUP BY agencyId, transportArea""")
          List<NetexLatestFile> getLatestFiles();
}

