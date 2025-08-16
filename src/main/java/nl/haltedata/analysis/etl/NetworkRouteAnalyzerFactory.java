package nl.haltedata.analysis.etl;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.inject.Inject;

@Component
@Scope("prototype")
public class NetworkRouteAnalyzerFactory {

    @Inject
    private ObjectFactory<NetworkRouteAnalyzer> objectFactory;

    public NetworkRouteAnalyzer getAnalyzer() {
        return objectFactory.getObject();
    }
}
