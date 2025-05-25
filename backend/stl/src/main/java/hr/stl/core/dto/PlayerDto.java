package hr.stl.core.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PlayerDto {

    private Long userId;

    private String firstName;

    private String lastName;

    private String address;

    private String email;

    private String password;

    private String role;

    private Integer rating;

    private LocalDate registeredOn;

    private LocalDate dateOfBirth;

    private Long teamId;

}
