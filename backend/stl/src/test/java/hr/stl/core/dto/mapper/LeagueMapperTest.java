package hr.stl.core.dto.mapper;

import hr.stl.core.dto.LeagueDto;
import hr.stl.core.model.League;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class LeagueMapperTest {

    private final LeagueMapper leagueMapper = new LeagueMapperImpl();

    private League league;

    private LeagueDto leagueDto;

    @BeforeEach
    public void setUp() {
        league = new League();
        league.setLeagueId(1L);
        league.setDescription("League");
        league.setRank(1);

        leagueDto = new LeagueDto();
        leagueDto.setLeagueId(1L);
        leagueDto.setDescription("League");
        leagueDto.setRank(1);
    }

    @Test
    public void testLeagueToLeagueDto() {
        LeagueDto mappedLeague = leagueMapper.toLeagueDto(league);

        assertThat(mappedLeague).isNotNull();
        assertThat(mappedLeague.getLeagueId()).isEqualTo(league.getLeagueId());
        assertThat(mappedLeague.getDescription()).isEqualTo(league.getDescription());
        assertThat(mappedLeague.getRank()).isEqualTo(league.getRank());
    }

    @Test
    public void testLeagueDtoToLeague() {
        League mappedLeague = leagueMapper.toLeague(leagueDto);

        assertThat(mappedLeague).isNotNull();
        assertThat(mappedLeague.getLeagueId()).isEqualTo(leagueDto.getLeagueId());
        assertThat(mappedLeague.getDescription()).isEqualTo(leagueDto.getDescription());
        assertThat(mappedLeague.getRank()).isEqualTo(leagueDto.getRank());
    }

}
