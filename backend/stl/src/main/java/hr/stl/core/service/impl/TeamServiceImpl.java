package hr.stl.core.service.impl;

import hr.stl.core.dto.TeamDto;
import hr.stl.core.dto.mapper.TeamMapper;
import hr.stl.core.model.Team;
import hr.stl.core.repository.TeamRepository;
import hr.stl.core.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    private final TeamMapper teamMapper;

    @Override
    public List<TeamDto> getAllTeams() {
        return teamRepository.getAllTeams().stream().map(teamMapper::toTeamDto).collect(Collectors.toList());
    }

    @Override
    public TeamDto getTeamById(Long id) {
        Optional<Team> team = teamRepository.getTeamById(id);

        if (team.isEmpty()) throw new NoSuchElementException(String.format("Team with id %s not found", id));

        return teamMapper.toTeamDto(team.get());
    }

    @Override
    public TeamDto insertTeam(TeamDto teamDto) {
        teamDto.setTeamId(null);

        Team team = teamMapper.toTeam(teamDto);

        return teamMapper.toTeamDto(teamRepository.insertTeam(team));
    }

    @Override
    public void updateTeamById(Long id, TeamDto teamDto) {
        teamDto.setTeamId(id);

        if (!teamRepository.updateTeamById(teamMapper.toTeam(teamDto)))
            throw new NoSuchElementException(String.format("Team with id %s not found", id));
    }

    @Override
    public void deleteTeamById(Long id) {
        if (!teamRepository.deleteTeamById(id))
            throw new NoSuchElementException(String.format("Team with id %s not found", id));
    }

}
