package hr.stl.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Team {

    private Long teamId;

    private String name;

    private LocalDate founded;

    private Long leagueId;

}
