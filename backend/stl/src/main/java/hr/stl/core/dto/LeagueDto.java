package hr.stl.core.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LeagueDto {

    private Long leagueId;

    private String description;

    private Integer rank;

    private Long seasonId;

}
