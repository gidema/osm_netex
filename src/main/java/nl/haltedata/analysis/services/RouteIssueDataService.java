package nl.haltedata.analysis.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.inject.Inject;
import nl.haltedata.analysis.dto.RouteIssueData;
import nl.haltedata.analysis.dto.RouteIssueDataDto;
import nl.haltedata.analysis.dto.RouteIssueDataRepository;

@Service
public class RouteIssueDataService implements InitializingBean {
    @Inject
    private RouteIssueDataRepository routeIssueDataRepository;

    @Inject
    private ModelMapper modelMapper;

    @Override
    public void afterPropertiesSet() {
        modelMapper.createTypeMap(RouteIssueData.class, RouteIssueDataDto.class);
        modelMapper.createTypeMap(RouteIssueDataDto.class, RouteIssueData.class);
    }
    
    @SuppressWarnings("exports")
    @Transactional(readOnly = true) // Important: perform within a transaction
    public Optional<RouteIssueDataDto> findById(Long routeIssueDataId) {
        return routeIssueDataRepository.findById(routeIssueDataId).map(data -> {
            return modelMapper.map(data, RouteIssueDataDto.class);
        });
    }

    public void saveAll(@SuppressWarnings("exports") List<RouteIssueDataDto> issues) {
        var allIssues = issues.stream().map(data -> {
            var issue = modelMapper.map(data, RouteIssueData.class);
            return issue;
        }).collect(Collectors.toList());
        routeIssueDataRepository.saveAll(allIssues);
    }
}
