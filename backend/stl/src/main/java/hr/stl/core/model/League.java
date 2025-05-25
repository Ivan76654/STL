package hr.stl.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class League {

    private Long leagueId;

    private String description;

    private Integer rank;

    private Long seasonId;

}
