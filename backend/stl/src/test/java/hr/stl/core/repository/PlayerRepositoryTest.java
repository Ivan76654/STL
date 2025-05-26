package hr.stl.core.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hr.stl.core.model.Player;

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

@ContextConfiguration(classes = {PlayerRepository.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
public class PlayerRepositoryTest {

    @MockitoBean
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * Test {@link PlayerRepository#getAllPlayers()}.
     * <p>
     * Method under test: {@link PlayerRepository#getAllPlayers()}
     */
    @Test
    @DisplayName("Test getAllPlayers()")
    public void testGetAllPlayers() throws DataAccessException {
        when(namedParameterJdbcTemplate.query(Mockito.any(), Mockito.<SqlParameterSource>any(),
                Mockito.<RowMapper<Object>>any())).thenReturn(new ArrayList<>());

        List<Player> actualAllPlayers = playerRepository.getAllPlayers();

        verify(namedParameterJdbcTemplate).query(eq(
                        "SELECT p.userId, p.firstName, p.lastName, p.address, p.email, p.password, p.role,\n    pl.rating, pl.registeredOn, pl.dateOfBirth, pl.teamId\nFROM person p JOIN player pl ON p.userId = pl.userId\n"),
                isA(SqlParameterSource.class), isA(RowMapper.class));
        assertTrue(actualAllPlayers.isEmpty());
    }

    /**
     * Test {@link PlayerRepository#getPlayerById(Long)}.
     * <p>
     * Method under test: {@link PlayerRepository#getPlayerById(Long)}
     */
    @Test
    @DisplayName("Test getPlayerById(Long)")
    public void testGetPlayerById() throws DataAccessException {
        Player player = new Player();
        player.setAddress("42 Main St");
        player.setDateOfBirth(LocalDate.of(1970, 1, 1));
        player.setEmail("jane.doe@example.org");
        player.setFirstName("Jane");
        player.setLastName("Doe");
        player.setPassword("iloveyou");
        player.setRating(1);
        player.setRegisteredOn(LocalDate.of(1970, 1, 1));
        player.setRole("PLAYER");
        player.setTeamId(1L);
        player.setUserId(1L);
        when(namedParameterJdbcTemplate.queryForObject(Mockito.any(), Mockito.<SqlParameterSource>any(),
                Mockito.<RowMapper<Object>>any())).thenReturn(player);

        Optional<Player> actualPlayerById = playerRepository.getPlayerById(1L);

        verify(namedParameterJdbcTemplate).queryForObject(eq(
                        "    SELECT p.userId, p.firstName, p.lastName, p.address, p.email, p.password, p.role,\n        pl.rating, pl.registeredOn, pl.dateOfBirth, pl.teamId\n    FROM person p JOIN player pl ON p.userId = pl.userId\n    WHERE p.userId = :id;\n"),
                isA(SqlParameterSource.class), isA(RowMapper.class));
        assertTrue(actualPlayerById.isPresent());
        assertSame(player, actualPlayerById.get());
    }

    /**
     * Test {@link PlayerRepository#insertPlayer(Player)}.
     * <p>
     * Method under test: {@link PlayerRepository#insertPlayer(Player)}
     */
    @Test
    @DisplayName("Test insertPlayer(Player)")
    public void testInsertPlayer() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.any(), Mockito.<SqlParameterSource>any())).thenReturn(1);
        when(namedParameterJdbcTemplate.queryForObject(Mockito.any(), Mockito.<SqlParameterSource>any(),
                Mockito.<Class<Long>>any())).thenReturn(1L);

        Player player = new Player();
        player.setAddress("42 Main St");
        player.setDateOfBirth(LocalDate.of(1970, 1, 1));
        player.setEmail("jane.doe@example.org");
        player.setFirstName("Jane");
        player.setLastName("Doe");
        player.setPassword("iloveyou");
        player.setRating(1);
        player.setRegisteredOn(LocalDate.of(1970, 1, 1));
        player.setRole("PLAYER");
        player.setTeamId(1L);
        player.setUserId(1L);

        Player actualInsertPlayerResult = playerRepository.insertPlayer(player);

        verify(namedParameterJdbcTemplate).queryForObject(eq(
                        "INSERT INTO person\n    (firstName, lastName, address, email, password, role)\nVALUES (:firstName, :lastName, :address, :email, :password, :role)\nRETURNING userId\n"),
                isA(SqlParameterSource.class), isA(Class.class));
        verify(namedParameterJdbcTemplate).update(eq(
                        "   INSERT INTO player\n       (userId, rating, registeredOn, dateOfBirth, teamId)\n   VALUES (:userId, :rating, :registeredOn, :dateOfBirth, :teamId)\n"),
                isA(SqlParameterSource.class));
        assertSame(player, actualInsertPlayerResult);
    }

    /**
     * Test {@link PlayerRepository#updatePlayerById(Player)}.
     * <ul>
     *   <li>Then return {@code false}.</li>
     * </ul>
     * <p>
     * Method under test: {@link PlayerRepository#updatePlayerById(Player)}
     */
    @Test
    @DisplayName("Test updatePlayerById(Player); then return 'false'")
    public void testUpdatePlayerById_thenReturnFalse() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.any(), Mockito.<SqlParameterSource>any())).thenReturn(0);

        Player player = new Player();
        player.setAddress("42 Main St");
        player.setDateOfBirth(LocalDate.of(1970, 1, 1));
        player.setEmail("jane.doe@example.org");
        player.setFirstName("Jane");
        player.setLastName("Doe");
        player.setPassword("iloveyou");
        player.setRating(1);
        player.setRegisteredOn(LocalDate.of(1970, 1, 1));
        player.setRole("PLAYER");
        player.setTeamId(1L);
        player.setUserId(1L);

        boolean actualUpdatePlayerByIdResult = playerRepository.updatePlayerById(player);

        verify(namedParameterJdbcTemplate).update(eq(
                        "UPDATE person SET\n    firstName = :firstName,\n    lastName = :lastName,\n    address = :address,\n    email = :email,\n    password = :password,\n    role = :role\nWHERE userId = :id;\n"),
                isA(SqlParameterSource.class));
        assertFalse(actualUpdatePlayerByIdResult);
    }

    /**
     * Test {@link PlayerRepository#updatePlayerById(Player)}.
     * <ul>
     *   <li>Then return {@code true}.</li>
     * </ul>
     * <p>
     * Method under test: {@link PlayerRepository#updatePlayerById(Player)}
     */
    @Test
    @DisplayName("Test updatePlayerById(Player); then return 'true'")
    public void testUpdatePlayerById_thenReturnTrue() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.any(), Mockito.<SqlParameterSource>any())).thenReturn(1);

        Player player = new Player();
        player.setAddress("42 Main St");
        player.setDateOfBirth(LocalDate.of(1970, 1, 1));
        player.setEmail("jane.doe@example.org");
        player.setFirstName("Jane");
        player.setLastName("Doe");
        player.setPassword("iloveyou");
        player.setRating(1);
        player.setRegisteredOn(LocalDate.of(1970, 1, 1));
        player.setRole("PLAYER");
        player.setTeamId(1L);
        player.setUserId(1L);

        boolean actualUpdatePlayerByIdResult = playerRepository.updatePlayerById(player);

        verify(namedParameterJdbcTemplate, atLeast(1)).update(Mockito.any(), Mockito.<SqlParameterSource>any());
        assertTrue(actualUpdatePlayerByIdResult);
    }

    /**
     * Test {@link PlayerRepository#deletePlayerById(Long)}.
     * <ul>
     *   <li>Then return {@code false}.</li>
     * </ul>
     * <p>
     * Method under test: {@link PlayerRepository#deletePlayerById(Long)}
     */
    @Test
    @DisplayName("Test deletePlayerById(Long); then return 'false'")
    public void testDeletePlayerById_thenReturnFalse() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.any(), Mockito.<SqlParameterSource>any())).thenReturn(0);

        boolean actualDeletePlayerByIdResult = playerRepository.deletePlayerById(1L);

        verify(namedParameterJdbcTemplate).update(eq("DELETE FROM person WHERE userId = :id"),
                isA(SqlParameterSource.class));
        assertFalse(actualDeletePlayerByIdResult);
    }

    /**
     * Test {@link PlayerRepository#deletePlayerById(Long)}.
     * <ul>
     *   <li>Then return {@code true}.</li>
     * </ul>
     * <p>
     * Method under test: {@link PlayerRepository#deletePlayerById(Long)}
     */
    @Test
    @DisplayName("Test deletePlayerById(Long); then return 'true'")
    public void testDeletePlayerById_thenReturnTrue() throws DataAccessException {
        when(namedParameterJdbcTemplate.update(Mockito.any(), Mockito.<SqlParameterSource>any())).thenReturn(1);

        boolean actualDeletePlayerByIdResult = playerRepository.deletePlayerById(1L);

        verify(namedParameterJdbcTemplate, atLeast(1)).update(Mockito.any(), isA(SqlParameterSource.class));
        assertTrue(actualDeletePlayerByIdResult);
    }
}
