package hr.stl.core.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TeamDto {

    private Long teamId;

    private String name;

    private LocalDate founded;

    private Long leagueId;

}
