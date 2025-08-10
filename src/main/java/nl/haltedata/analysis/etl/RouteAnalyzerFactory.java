package nl.haltedata.analysis.etl;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;

@Component
public class RouteAnalyzerFactory {

    @Inject
    private ObjectFactory<RouteAnalyzer> objectFactory;

    public RouteAnalyzer getAnalyzer() {
        return objectFactory.getObject();
    }
}
