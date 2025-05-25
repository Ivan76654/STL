package hr.stl.core.dto.mapper;

import hr.stl.core.dto.PlayerDto;
import hr.stl.core.model.Player;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PlayerMapper {

    Player toPlayer(PlayerDto playerDto);

    PlayerDto toPlayerDto(Player player);

}
