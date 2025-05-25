package hr.stl.core.service;

import hr.stl.core.dto.TeamDto;

import java.util.List;

public interface TeamService {

    List<TeamDto> getAllTeams();

    TeamDto getTeamById(Long id);

    TeamDto insertTeam(TeamDto teamDto);

    void updateTeamById(Long id, TeamDto teamDto);

    void deleteTeamById(Long id);

}
