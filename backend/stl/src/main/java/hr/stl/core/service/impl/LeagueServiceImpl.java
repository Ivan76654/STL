package hr.stl.core.service.impl;

import hr.stl.core.dto.LeagueDto;
import hr.stl.core.dto.mapper.LeagueMapper;
import hr.stl.core.model.League;
import hr.stl.core.repository.LeagueRepository;
import hr.stl.core.service.LeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeagueServiceImpl implements LeagueService {

    private final LeagueRepository leagueRepository;

    private final LeagueMapper leagueMapper;

    @Override
    public List<LeagueDto> getAllLeagues() {
        return leagueRepository.getAllLeagues().stream().map(leagueMapper::toLeagueDto).collect(Collectors.toList());
    }

    @Override
    public LeagueDto getLeagueById(Long id) {
        Optional<League> league = leagueRepository.getLeagueById(id);

        if (league.isEmpty()) throw new NoSuchElementException(String.format("League with id %d not found", id));


        return leagueMapper.toLeagueDto(league.get());
    }

    @Override
    public LeagueDto insertLeague(LeagueDto leagueDto) {
        leagueDto.setLeagueId(null);

        League league = leagueMapper.toLeague(leagueDto);

        return leagueMapper.toLeagueDto(leagueRepository.insertLeague(league));
    }

    @Override
    public void updateLeagueById(Long id, LeagueDto leagueDto) {
        leagueDto.setLeagueId(id);

        if (!leagueRepository.updateLeagueById(leagueMapper.toLeague(leagueDto)))
            throw new NoSuchElementException(String.format("League with id %d not found", id));
    }

    @Override
    public void deleteLeagueById(Long id) {
        if (!leagueRepository.deleteLeagueById(id))
            throw new NoSuchElementException(String.format("League with id %d not found", id));
    }
}
