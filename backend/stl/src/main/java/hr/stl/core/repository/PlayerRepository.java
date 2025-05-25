package hr.stl.core.repository;

import hr.stl.core.model.Player;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PlayerRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public List<Player> getAllPlayers() {
        String sql = """
                SELECT p.user_id, p.first_name, p.last_name, p.address, p.email, p.password, p.role,
                    pl.rating, pl.registered_on, pl.date_of_birth, pl.team_id
                FROM person p JOIN player pl ON p.user_id = pl.user_id
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();

        return jdbc.query(sql, params, PLAYER_ROW_MAPPER);
    }

    public Optional<Player> getPlayerById(Long id) {
        String sql = """
                    SELECT p.user_id, p.first_name, p.last_name, p.address, p.email, p.password, p.role,
                        pl.rating, pl.registered_on, pl.date_of_birth, pl.team_id
                    FROM person p JOIN player pl ON p.user_id = pl.user_id
                    WHERE p.user_id = :id;
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
                    (first_name, last_name, address, email, password, role)
                VALUES (:firstName, :lastName, :address, :email, :password, :role)
                RETURNING user_id
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
                       (user_id, rating, registered_on, date_of_birth, team_id)
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
                    first_name = :firstName,
                    last_name = :lastName,
                    address = :address,
                    email = :email,
                    password = :password,
                    role = :role
                WHERE user_id = :id;
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
                    registered_on = :registeredOn,
                    date_of_birth = :dateOfBirth,
                    team_id = :teamId
                WHERE user_id = :id;
                """;

        MapSqlParameterSource playerParams = new MapSqlParameterSource();
        playerParams.addValue("userId", player.getUserId());
        playerParams.addValue("rating", player.getRating());
        playerParams.addValue("registeredOn", player.getRegisteredOn());
        playerParams.addValue("dateOfBirth", player.getDateOfBirth());
        playerParams.addValue("teamId", player.getTeamId());

        return jdbc.update(sql, playerParams) > 0;
    }

    @Transactional
    public boolean deletePlayerById(Long id) {
        String sql = "DELETE FROM player WHERE user_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        if (!(jdbc.update(sql, params) > 0)) return false;

        sql = "DELETE FROM person WHERE user_id = :id";

        return jdbc.update(sql, params) > 0;
    }

    private final RowMapper<Player> PLAYER_ROW_MAPPER = (rs, rowNum) -> {
        Player p = new Player();

        p.setUserId(rs.getLong("user_id"));
        p.setFirstName(rs.getString("first_name"));
        p.setLastName(rs.getString("last_name"));
        p.setAddress(rs.getString("address"));
        p.setEmail(rs.getString("email"));
        p.setPassword(rs.getString("password"));
        p.setRole(rs.getString("role"));
        p.setRating(rs.getInt("rating"));
        p.setRegisteredOn(rs.getObject("registered_on", LocalDate.class));
        p.setDateOfBirth(rs.getObject("date_of_birth", LocalDate.class));
        p.setTeamId(rs.getLong("team_id"));

        return p;
    };

}

