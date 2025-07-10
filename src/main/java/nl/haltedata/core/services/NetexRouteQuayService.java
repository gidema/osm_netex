package nl.haltedata.core.services;

import java.util.List;

import nl.haltedata.netex.dto.NetexRouteQuay;

public interface NetexRouteQuayService {

    List<NetexRouteQuay> getByRouteId(String routeId);

}
