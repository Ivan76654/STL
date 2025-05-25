package hr.stl.core.service;

import hr.stl.core.dto.PlayerDto;

import java.util.List;

public interface PlayerService {

    List<PlayerDto> getAllPlayers();

    PlayerDto getPlayerById(Long id);

    PlayerDto insertPlayer(PlayerDto playerDto);

    void updatePlayerById(Long id, PlayerDto playerDto);

    void deletePlayerById(Long id);

}
