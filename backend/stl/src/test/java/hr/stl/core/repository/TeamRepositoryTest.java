package hr.stl.core.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hr.stl.core.model.Team;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TeamRepository.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
public class TeamRepositoryTest {

    @MockitoBean
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private TeamRepository teamRepository;

    /**
     * Test {@link TeamRepository#getAllTeams()}.
     * <p>
     * Method under test: {@link TeamRepository#getAllTeams()}
     */
    @Test
    @DisplayName("Test getAllTeams()")
    public void testGetAllTeams() throws DataAccessException {
        when(namedParameterJdbcTemplate.query(Mockito.any(), Mockito.<SqlParameterSource>any(),
                Mockito.<RowMapper<Object>>any())).thenReturn(new ArrayList<>());

        List<Team> actualAllTeams = teamRepository.getAllTeams();

        verify(namedParameterJdbcTemplate).query(eq("SELECT teamId, name, founded, leagueId FROM team"),
                isA(SqlParameterSource.class), isA(RowMapper.class));
        assertTrue(actualAllTeams.isEmpty());
    }

    /**
     * Test {@link TeamRepository#getTeamById(Long)}.
     * <p>
     * Method under test: {@link TeamRepository#getTeamById(Long)}
     */
    @Test
    @DisplayName("Test getTeamById(Long)")
    public void testGetTeamById() throws DataAccessException {
        Team team = new Team();
        team.setFounded(LocalDate.of(1970, 1, 1));
        team.setLeagueId(1L);
        team.setName("SELECT teamId, name, founded, leagueId FROM team WHERE teamId = :id");
        team.setTeamId(1L);
        when(namedParameterJdbcTemplate.queryForObject(Mockito.any(), Mockito.<SqlParameterSource>any(),
                Mockito.<RowMapper<Object>>any())).thenReturn(team);

        Optional<Team> actualTeamById = teamRepository.getTeamById(1L);

        verify(namedParameterJdbcTemplate).queryForObject(
                eq("SELECT teamId, name, founded, leagueId FROM team WHERE teamId = :id"), isA(SqlParameterSource.class),
                isA(RowMapper.class));
        assertTrue(actualTeamById.isPresent());
        assertSame(team, actualTeamById.get());
    }

    /**
     * Test {@link TeamRepository#insertTeam(Team)}.
     * <p>
     * Method under test: {@link TeamRepository#insertTeam(Team)}
     */
    @Test
    @DisplayName("Test insertTeam(Team)")
    public void testInsertTeam() throws DataAccessException {
        when(namedParameterJdbcTemplate.queryForObject(Mockito.any(), Mockito.<SqlParameterSource>any(),
                Mockito.<Class<Long>>any())).thenReturn(1L);

        Team team = new Team();
        team.setFounded(LocalDate.of(1970, 1, 1));
        team.setLeagueId(1L);
        team.setName("Name");
        team.setTeamId(1L);

        Team actualInsertTeamResult = teamRepository.insertTeam(team);

        verify(namedParameterJdbcTemplate).queryForObject(
                eq("INSERT INTO team(name, founded, leagueId) VALUES(:name, :founded, :leagueId) RETURNING teamId"),
                isA(SqlParameterSource.class), isA(Class.class));
        assertSame(team, actualInsertTeamResult);
    }

    /**
     * Test {@link TeamRepository#updateTeamById(Team)}.
     * <ul>
     *   <li>Given {@link NamedParameterJdbcTemplate} {@link NamedParameterJdbcTemplate#update(String, SqlParameterSource)} return one.</li>
     *   <li>Then return {@code true}.</li>
     * </ul>
     * <p>
     * Method under test: {@link TeamRepository#updateTeamById(Team)}
     */
    @Test
    @DisplayName("Test updateTeamById(Team); given NamedParameterJdbcTemplate update(String, SqlParameterSource) return one; then return 'true'")
    public void testUpdateTeamById_givenNamedParameterJdbcTemplateUpdateReturnOne_thenReturnTrue() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.any(), Mockito.<SqlParameterSource>any())).thenReturn(1);

        Team team = new Team();
        team.setFounded(LocalDate.of(1970, 1, 1));
        team.setLeagueId(1L);
        team.setName("Name");
        team.setTeamId(1L);

        boolean actualUpdateTeamByIdResult = teamRepository.updateTeamById(team);

        verify(namedParameterJdbcTemplate).update(
                eq("UPDATE team SET name = :name, founded = :founded, leagueId = :leagueId WHERE teamId = :id"),
                isA(SqlParameterSource.class));
        assertTrue(actualUpdateTeamByIdResult);
    }

    /**
     * Test {@link TeamRepository#updateTeamById(Team)}.
     * <ul>
     *   <li>Then return {@code false}.</li>
     * </ul>
     * <p>
     * Method under test: {@link TeamRepository#updateTeamById(Team)}
     */
    @Test
    @DisplayName("Test updateTeamById(Team); then return 'false'")
    public void testUpdateTeamById_thenReturnFalse() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.any(), Mockito.<SqlParameterSource>any())).thenReturn(0);

        Team team = new Team();
        team.setFounded(LocalDate.of(1970, 1, 1));
        team.setLeagueId(1L);
        team.setName("Name");
        team.setTeamId(1L);

        boolean actualUpdateTeamByIdResult = teamRepository.updateTeamById(team);

        verify(namedParameterJdbcTemplate).update(
                eq("UPDATE team SET name = :name, founded = :founded, leagueId = :leagueId WHERE teamId = :id"),
                isA(SqlParameterSource.class));
        assertFalse(actualUpdateTeamByIdResult);
    }

    /**
     * Test {@link TeamRepository#deleteTeamById(Long)}.
     * <ul>
     *   <li>Given {@link NamedParameterJdbcTemplate} {@link NamedParameterJdbcTemplate#update(String, SqlParameterSource)} return one.</li>
     *   <li>Then return {@code true}.</li>
     * </ul>
     * <p>
     * Method under test: {@link TeamRepository#deleteTeamById(Long)}
     */
    @Test
    @DisplayName("Test deleteTeamById(Long); given NamedParameterJdbcTemplate update(String, SqlParameterSource) return one; then return 'true'")
    public void testDeleteTeamById_givenNamedParameterJdbcTemplateUpdateReturnOne_thenReturnTrue() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.any(), Mockito.<SqlParameterSource>any())).thenReturn(1);

        boolean actualDeleteTeamByIdResult = teamRepository.deleteTeamById(1L);

        verify(namedParameterJdbcTemplate).update(eq("DELETE FROM team WHERE teamId = :id"),
                isA(SqlParameterSource.class));
        assertTrue(actualDeleteTeamByIdResult);
    }

    /**
     * Test {@link TeamRepository#deleteTeamById(Long)}.
     * <ul>
     *   <li>Then return {@code false}.</li>
     * </ul>
     * <p>
     * Method under test: {@link TeamRepository#deleteTeamById(Long)}
     */
    @Test
    @DisplayName("Test deleteTeamById(Long); then return 'false'")
    public void testDeleteTeamById_thenReturnFalse() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.any(), Mockito.<SqlParameterSource>any())).thenReturn(0);

        boolean actualDeleteTeamByIdResult = teamRepository.deleteTeamById(1L);

        verify(namedParameterJdbcTemplate).update(eq("DELETE FROM team WHERE teamId = :id"),
                isA(SqlParameterSource.class));
        assertFalse(actualDeleteTeamByIdResult);
    }
}
