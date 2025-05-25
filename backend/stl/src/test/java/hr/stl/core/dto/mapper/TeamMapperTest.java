package hr.stl.core.dto.mapper;

import hr.stl.core.dto.TeamDto;
import hr.stl.core.model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class TeamMapperTest {

    private final TeamMapper teamMapper = new TeamMapperImpl();

    private Team team;

    private TeamDto teamDto;

    @BeforeEach
    public void setUp() {
        team = new Team();
        team.setTeamId(1L);
        team.setName("Team 1");
        team.setFounded(LocalDate.of(2020, 1, 1));
        team.setLeagueId(1L);

        teamDto = new TeamDto();
        teamDto.setTeamId(1L);
        teamDto.setName("Team 1");
        teamDto.setFounded(LocalDate.of(2020, 1, 1));
        teamDto.setLeagueId(1L);
    }

    @Test
    public void testTeamToTeamDto() {
        TeamDto mappedTeam = teamMapper.toTeamDto(team);

        assertThat(mappedTeam).isNotNull();
        assertThat(mappedTeam.getTeamId()).isEqualTo(team.getTeamId());
        assertThat(mappedTeam.getName()).isEqualTo(team.getName());
        assertThat(mappedTeam.getFounded()).isEqualTo(team.getFounded());
        assertThat(mappedTeam.getLeagueId()).isEqualTo(team.getLeagueId());
    }

    @Test
    public void testTeamDtoToTeam() {
        Team mappedTeam = teamMapper.toTeam(teamDto);

        assertThat(mappedTeam).isNotNull();
        assertThat(mappedTeam.getTeamId()).isEqualTo(teamDto.getTeamId());
        assertThat(mappedTeam.getName()).isEqualTo(teamDto.getName());
        assertThat(mappedTeam.getFounded()).isEqualTo(teamDto.getFounded());
        assertThat(mappedTeam.getLeagueId()).isEqualTo(teamDto.getLeagueId());
    }

}
