package nl.haltedata.osm.controllers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.inject.Inject;
import nl.haltedata.osm.dto.OsmRouteIssueRepository;

@Service
public class OsmRouteIssueService {
    @Inject
    private OsmRouteIssueRepository osmRouteIssueRepository;

    @SuppressWarnings("exports")
    @Transactional(readOnly = false) // Important: perform within a transaction
    public void updateIssues() {
        osmRouteIssueRepository.updateRouteInMultipleLinesIssue();
        osmRouteIssueRepository.updateRouteNotInLineIssue();
    }
}
