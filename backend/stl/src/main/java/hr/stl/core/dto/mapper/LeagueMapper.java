package hr.stl.core.dto.mapper;

import hr.stl.core.dto.LeagueDto;
import hr.stl.core.model.League;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface LeagueMapper {

    League toLeague(LeagueDto leagueDto);

    LeagueDto toLeagueDto(League league);

}
