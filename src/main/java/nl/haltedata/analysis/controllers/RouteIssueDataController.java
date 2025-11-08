package nl.haltedata.analysis.controllers;

import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import nl.haltedata.analysis.dto.RouteIssueDataDto;
import nl.haltedata.analysis.services.RouteIssueDataService;

@RestController
public class RouteIssueDataController {
    
    @Inject
    private RouteIssueDataService service;

    @SuppressWarnings("exports")
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("issue/{id}")
    public Optional<RouteIssueDataDto> findById(@PathVariable("id") Long id) throws Exception {
        return service.findById(id);
    }
}