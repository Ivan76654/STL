package hr.stl.core.dto.mapper;

import hr.stl.core.dto.PlayerDto;
import hr.stl.core.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class PlayerMapperTest {

    private final PlayerMapper playerMapper = new PlayerMapperImpl();

    private Player player;

    private PlayerDto playerDto;

    @BeforeEach
    public void setUp() {
        player = new Player();
        player.setUserId(1L);
        player.setFirstName("Jan");
        player.setLastName("Kowalski");
        player.setAddress("Street 1");
        player.setEmail("example@email.com");
        player.setPassword("password");
        player.setRole("PLAYER");
        player.setRating(1000);
        player.setRegisteredOn(LocalDate.of(2020, 1, 1));
        player.setDateOfBirth(LocalDate.of(2000, 1, 1));
        player.setTeamId(1L);

        playerDto = new PlayerDto();
        playerDto.setUserId(1L);
        playerDto.setFirstName("Jan");
        playerDto.setLastName("Kowalski");
        playerDto.setAddress("Street 1");
        playerDto.setEmail("example@email.com");
        playerDto.setPassword("password");
        playerDto.setRole("PLAYER");
        playerDto.setRating(1000);
        playerDto.setRegisteredOn(LocalDate.of(2020, 1, 1));
        playerDto.setDateOfBirth(LocalDate.of(2000, 1, 1));
        playerDto.setTeamId(1L);
    }

    @Test
    public void testPlayerToPlayerDto() {
        PlayerDto mappedPlayer = playerMapper.toPlayerDto(player);

        assertThat(mappedPlayer).isNotNull();
        assertThat(mappedPlayer.getUserId()).isEqualTo(player.getUserId());
        assertThat(mappedPlayer.getFirstName()).isEqualTo(player.getFirstName());
        assertThat(mappedPlayer.getLastName()).isEqualTo(player.getLastName());
        assertThat(mappedPlayer.getAddress()).isEqualTo(player.getAddress());
        assertThat(mappedPlayer.getEmail()).isEqualTo(player.getEmail());
        assertThat(mappedPlayer.getPassword()).isEqualTo(player.getPassword());
        assertThat(mappedPlayer.getRole()).isEqualTo(player.getRole());
        assertThat(mappedPlayer.getRating()).isEqualTo(player.getRating());
        assertThat(mappedPlayer.getRegisteredOn()).isEqualTo(player.getRegisteredOn());
        assertThat(mappedPlayer.getDateOfBirth()).isEqualTo(player.getDateOfBirth());
        assertThat(mappedPlayer.getTeamId()).isEqualTo(player.getTeamId());
    }

    @Test
    public void testPlayerDtoToPlayer() {
        Player mappedPlayer = playerMapper.toPlayer(playerDto);

        assertThat(mappedPlayer).isNotNull();
        assertThat(mappedPlayer.getUserId()).isEqualTo(player.getUserId());
        assertThat(mappedPlayer.getFirstName()).isEqualTo(player.getFirstName());
        assertThat(mappedPlayer.getLastName()).isEqualTo(player.getLastName());
        assertThat(mappedPlayer.getAddress()).isEqualTo(player.getAddress());
        assertThat(mappedPlayer.getEmail()).isEqualTo(player.getEmail());
        assertThat(mappedPlayer.getPassword()).isEqualTo(player.getPassword());
        assertThat(mappedPlayer.getRole()).isEqualTo(player.getRole());
        assertThat(mappedPlayer.getRating()).isEqualTo(player.getRating());
        assertThat(mappedPlayer.getRegisteredOn()).isEqualTo(player.getRegisteredOn());
        assertThat(mappedPlayer.getDateOfBirth()).isEqualTo(player.getDateOfBirth());
        assertThat(mappedPlayer.getTeamId()).isEqualTo(player.getTeamId());
    }

}
