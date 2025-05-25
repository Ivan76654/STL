package hr.stl.core.repository;

import hr.stl.core.model.Team;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TeamRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public List<Team> getAllTeams() {
        String sql = "SELECT team_id, name, founded, league_id FROM team";
        MapSqlParameterSource params = new MapSqlParameterSource();

        return jdbc.query(sql, params, TEAM_ROW_MAPPER);
    }

    public Optional<Team> getTeamById(Long id) {
        String sql = "SELECT team_id, name, founded, league_id FROM team WHERE team_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        Team team = jdbc.queryForObject(sql, params, TEAM_ROW_MAPPER);

        return Optional.ofNullable(team);
    }

    public Team insertTeam(Team team) {
        String sql = "INSERT INTO team(name, founded, league_id) VALUES(:name, :founded, :leagueId) RETURNING team_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", team.getName());
        params.addValue("founded", team.getFounded());
        params.addValue("leagueId", team.getLeagueId());

        Long teamId = jdbc.queryForObject(sql, params, Long.class);
        team.setTeamId(teamId);

        return team;
    }

    public boolean updateTeamById(Team team) {
        String sql = "UPDATE team SET name = :name, founded = :founded, league_id = :leagueId WHERE team_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("name", team.getName());
        params.addValue("founded", team.getFounded());
        params.addValue("leagueId", team.getLeagueId());
        params.addValue("id", team.getTeamId());

        return jdbc.update(sql, params) > 0;
    }

    public boolean deleteTeamById(Long id) {
        String sql = "DELETE FROM team WHERE team_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return jdbc.update(sql, params) > 0;
    }


    private final RowMapper<Team> TEAM_ROW_MAPPER = (rs, rowNum) -> {
        Team team = new Team();

        team.setTeamId(rs.getLong("team_id"));
        team.setName(rs.getString("name"));
        team.setFounded(rs.getObject("founded", LocalDate.class));
        team.setLeagueId(rs.getLong("league_id"));

        return team;
    };

}

