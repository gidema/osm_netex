package nl.haltedata.analysis.dto;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RouteIssueDataMapper {
    RouteIssueDataDto sourceToDestination(RouteIssueData source);
    RouteIssueData destinationToSource(RouteIssueDataDto destination);
}
