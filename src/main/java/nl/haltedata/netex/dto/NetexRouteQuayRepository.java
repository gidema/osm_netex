package nl.haltedata.netex.dto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface NetexRouteQuayRepository extends CrudRepository<NetexRouteQuay, NetexRouteQuayId> {

    public List<NetexRouteQuay> queryByRouteIdOrderByQuayIndex(String netexRouteId);
}

