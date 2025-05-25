package hr.stl.core.repository;

import hr.stl.core.model.League;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LeagueRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public List<League> getAllLeagues() {
        String sql = "SELECT leagueId, description, rank, seasonId FROM league";
        MapSqlParameterSource params = new MapSqlParameterSource();

        return jdbc.query(sql, params, LEAGUE_ROW_MAPPER);
    }

    public Optional<League> getLeagueById(Long leagueId) {
        String sql = "SELECT leagueId, description, rank, seasonId FROM league WHERE leagueId = :leagueId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("leagueId", leagueId);

        League league = jdbc.queryForObject(sql, params, LEAGUE_ROW_MAPPER);

        return Optional.ofNullable(league);
    }

    public League insertLeague(League league) {
        String sql = "INSERT INTO league(description, rank, seasonId) VALUES (:description, :rank, :seasonId) RETURNING leagueId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("description", league.getDescription());
        params.addValue("rank", league.getRank());
        params.addValue("seasonId", league.getSeasonId());

        Long leagueId = jdbc.queryForObject(sql, params, Long.class);
        league.setLeagueId(leagueId);

        return league;
    }

    public boolean updateLeagueById(League league) {
        String sql = "UPDATE league SET description = :description, rank = :rank, seasonId = :seasonId WHERE leagueId = :leagueId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("description", league.getDescription());
        params.addValue("rank", league.getRank());
        params.addValue("seasonId", league.getSeasonId());

        return jdbc.update(sql, params) > 0;
    }

    public boolean deleteLeagueById(Long leagueId) {
        String sql = "DELETE FROM league WHERE leagueId = :leagueId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("leagueId", leagueId);

        return jdbc.update(sql, params) > 0;
    }

    private final RowMapper<League> LEAGUE_ROW_MAPPER = (rs, rowNum) -> {
        League league = new League();

        league.setLeagueId(rs.getLong("leagueId"));
        league.setDescription(rs.getString("description"));
        league.setRank(rs.getInt("rank"));
        league.setSeasonId(rs.getLong("seasonId"));

        return league;
    };

}
