package hr.stl.core.repository;

import hr.stl.core.model.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlayerRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public List<Player> getAllPlayers() {
        String sql = """
                SELECT p.userId, p.firstName, p.lastName, p.address, p.email, p.password, p.role,
                    pl.rating, pl.registeredOn, pl.dateOfBirth, pl.teamId
                FROM person p JOIN player pl ON p.userId = pl.userId
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();

        return jdbc.query(sql, params, PLAYER_ROW_MAPPER);
    }

    public Optional<Player> getPlayerById(Long id) {
        String sql = """
                    SELECT p.userId, p.firstName, p.lastName, p.address, p.email, p.password, p.role,
                        pl.rating, pl.registeredOn, pl.dateOfBirth, pl.teamId
                    FROM person p JOIN player pl ON p.userId = pl.userId
                    WHERE p.userId = :id;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        Player player = jdbc.queryForObject(sql, params, PLAYER_ROW_MAPPER);

        return Optional.ofNullable(player);
    }

    @Transactional
    public Player insertPlayer(Player player) {
        String sql = """
                INSERT INTO person
                    (firstName, lastName, address, email, password, role)
                VALUES (:firstName, :lastName, :address, :email, :password, :role)
                RETURNING userId
                """;

        MapSqlParameterSource personParams = new MapSqlParameterSource();
        personParams.addValue("firstName", player.getFirstName());
        personParams.addValue("lastName", player.getLastName());
        personParams.addValue("address", player.getAddress());
        personParams.addValue("email", player.getEmail());
        personParams.addValue("password", player.getPassword());
        personParams.addValue("role", player.getRole());

        Long userId = jdbc.queryForObject(sql, personParams, Long.class);
        player.setUserId(userId);

        sql = """
                   INSERT INTO player
                       (userId, rating, registeredOn, dateOfBirth, teamId)
                   VALUES (:userId, :rating, :registeredOn, :dateOfBirth, :teamId)
                """;

        MapSqlParameterSource playerParams = new MapSqlParameterSource();
        playerParams.addValue("userId", player.getUserId());
        playerParams.addValue("rating", player.getRating());
        playerParams.addValue("registeredOn", player.getRegisteredOn());
        playerParams.addValue("dateOfBirth", player.getDateOfBirth());
        playerParams.addValue("teamId", player.getTeamId());

        jdbc.update(sql, playerParams);

        return player;
    }

    @Transactional
    public boolean updatePlayerById(Player player) {
        String sql = """
                UPDATE person SET
                    firstName = :firstName,
                    lastName = :lastName,
                    address = :address,
                    email = :email,
                    password = :password,
                    role = :role
                WHERE userId = :id;
                """;
        MapSqlParameterSource personParams = new MapSqlParameterSource();
        personParams.addValue("id", player.getUserId());
        personParams.addValue("firstName", player.getFirstName());
        personParams.addValue("lastName", player.getLastName());
        personParams.addValue("address", player.getAddress());
        personParams.addValue("email", player.getEmail());
        personParams.addValue("password", player.getPassword());
        personParams.addValue("role", player.getRole());


        if (!(jdbc.update(sql, personParams) > 0)) return false;

        sql = """
                UPDATE player SET
                    rating = :rating,
                    registeredOn = :registeredOn,
                    dateOfBirth = :dateOfBirth,
                    teamId = :teamId
                WHERE userId = :id;
                """;

        MapSqlParameterSource playerParams = new MapSqlParameterSource();
        playerParams.addValue("id", player.getUserId());
        playerParams.addValue("rating", player.getRating());
        playerParams.addValue("registeredOn", player.getRegisteredOn());
        playerParams.addValue("dateOfBirth", player.getDateOfBirth());
        playerParams.addValue("teamId", player.getTeamId());

        return jdbc.update(sql, playerParams) > 0;
    }

    @Transactional
    public boolean deletePlayerById(Long id) {
        String sql = "DELETE FROM player WHERE userId = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        if (!(jdbc.update(sql, params) > 0)) return false;

        sql = "DELETE FROM person WHERE userId = :id";

        return jdbc.update(sql, params) > 0;
    }

    private final RowMapper<Player> PLAYER_ROW_MAPPER = (rs, rowNum) -> {
        Player p = new Player();

        p.setUserId(rs.getLong("userId"));
        p.setFirstName(rs.getString("firstName"));
        p.setLastName(rs.getString("lastName"));
        p.setAddress(rs.getString("address"));
        p.setEmail(rs.getString("email"));
        p.setPassword(rs.getString("password"));
        p.setRole(rs.getString("role"));
        p.setRating(rs.getInt("rating"));
        p.setRegisteredOn(rs.getObject("registeredOn", LocalDate.class));
        p.setDateOfBirth(rs.getObject("dateOfBirth", LocalDate.class));
        p.setTeamId(rs.getLong("teamId"));

        return p;
    };

}

