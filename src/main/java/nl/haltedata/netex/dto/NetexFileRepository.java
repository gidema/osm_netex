package nl.haltedata.netex.dto;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface NetexFileRepository extends PagingAndSortingRepository<NetexFileInfo, String> {
    //
}

