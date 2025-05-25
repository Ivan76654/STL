package hr.stl.core.dto.mapper;

import hr.stl.core.dto.TeamDto;
import hr.stl.core.model.Team;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TeamMapper {

    Team toTeam(TeamDto teamDto);

    TeamDto toTeamDto(Team team);

}
