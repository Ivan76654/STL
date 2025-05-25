package hr.stl.core.service.impl;

import hr.stl.core.dto.PlayerDto;
import hr.stl.core.dto.mapper.PlayerMapper;
import hr.stl.core.model.Player;
import hr.stl.core.repository.PlayerRepository;
import hr.stl.core.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    private final PlayerMapper playerMapper;

    @Override
    public List<PlayerDto> getAllPlayers() {
        return playerRepository.getAllPlayers().stream().map(playerMapper::toPlayerDto).collect(Collectors.toList());
    }

    @Override
    public PlayerDto getPlayerById(Long id) {
        Optional<Player> player = playerRepository.getPlayerById(id);

        if (player.isEmpty()) throw new NoSuchElementException(String.format("Player with id %s not found", id));

        return playerMapper.toPlayerDto(player.get());
    }

    @Override
    public PlayerDto insertPlayer(PlayerDto playerDto) {
        playerDto.setUserId(null);

        Player player = playerMapper.toPlayer(playerDto);

        return playerMapper.toPlayerDto(playerRepository.insertPlayer(player));
    }

    @Override
    public void updatePlayerById(Long id, PlayerDto playerDto) {
        playerDto.setUserId(id);

        if (!playerRepository.updatePlayerById(playerMapper.toPlayer(playerDto)))
            throw new NoSuchElementException(String.format("Player with id %s not found", id));
    }

    @Override
    public void deletePlayerById(Long id) {
        if (!playerRepository.deletePlayerById(id))
            throw new NoSuchElementException(String.format("Player with id %s not found", id));
    }
}
