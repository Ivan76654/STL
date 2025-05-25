package hr.stl.core.service;

import hr.stl.core.dto.LeagueDto;

import java.util.List;

public interface LeagueService {

    List<LeagueDto> getAllLeagues();

    LeagueDto getLeagueById(Long id);

    LeagueDto insertLeague(LeagueDto leagueDto);

    void updateLeagueById(Long id, LeagueDto leagueDto);

    void deleteLeagueById(Long id);

}
