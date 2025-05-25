package hr.stl.core.service;

import hr.stl.core.dto.TeamDto;
import hr.stl.core.dto.mapper.TeamMapper;
import hr.stl.core.model.Team;
import hr.stl.core.repository.TeamRepository;
import hr.stl.core.service.impl.TeamServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMapper teamMapper;

    @InjectMocks
    private TeamServiceImpl teamService;

    private Team team1;

    private Team team2;

    private TeamDto teamDto1;

    private TeamDto teamDto2;

    @BeforeEach
    void setUp() {
        team1 = new Team();
        team1.setTeamId(1L);
        team1.setName("Team One");
        team1.setFounded(LocalDate.of(1990, 5, 20));
        team1.setLeagueId(100L);

        team2 = new Team();
        team2.setTeamId(2L);
        team2.setName("Team Two");
        team2.setFounded(LocalDate.of(2000, 6, 15));
        team2.setLeagueId(200L);

        teamDto1 = new TeamDto();
        teamDto1.setTeamId(1L);
        teamDto1.setName("Team One");
        teamDto1.setFounded(LocalDate.of(1990, 5, 20));
        teamDto1.setLeagueId(100L);

        teamDto2 = new TeamDto();
        teamDto2.setTeamId(2L);
        teamDto2.setName("Team Two");
        teamDto2.setFounded(LocalDate.of(2000, 6, 15));
        teamDto2.setLeagueId(200L);
    }

    @Test
    void getAllTeams_ShouldReturnAllMappedDtos() {
        when(teamRepository.getAllTeams()).thenReturn(List.of(team1, team2));
        when(teamMapper.toTeamDto(team1)).thenReturn(teamDto1);
        when(teamMapper.toTeamDto(team2)).thenReturn(teamDto2);

        List<TeamDto> result = teamService.getAllTeams();

        assertEquals(2, result.size());
        assertIterableEquals(List.of(teamDto1, teamDto2), result);

        verify(teamRepository).getAllTeams();
        verify(teamMapper).toTeamDto(team1);
        verify(teamMapper).toTeamDto(team2);
    }

    @Test
    void getTeamById_WhenExists_ShouldReturnDto() {
        when(teamRepository.getTeamById(1L)).thenReturn(Optional.of(team1));
        when(teamMapper.toTeamDto(team1)).thenReturn(teamDto1);

        TeamDto result = teamService.getTeamById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getTeamId());
        assertEquals("Team One", result.getName());
        assertEquals(team1.getFounded(), result.getFounded());
        assertEquals(100L, result.getLeagueId());

        verify(teamRepository).getTeamById(1L);
        verify(teamMapper).toTeamDto(team1);
    }

    @Test
    void getTeamById_WhenMissing_ShouldThrow() {
        when(teamRepository.getTeamById(42L)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> teamService.getTeamById(42L));
        assertTrue(ex.getMessage().contains("Team with id 42 not found"));

        verify(teamRepository).getTeamById(42L);
        verifyNoInteractions(teamMapper);
    }

    @Test
    void insertTeam_ShouldClearId_AndReturnSavedDto() {
        when(teamMapper.toTeam(teamDto1)).thenReturn(team1);
        when(teamRepository.insertTeam(team1)).thenReturn(team1);
        when(teamMapper.toTeamDto(team1)).thenReturn(teamDto1);

        teamService.insertTeam(teamDto1);

        verify(teamMapper).toTeam(teamDto1);
        verify(teamRepository).insertTeam(team1);
        verify(teamMapper).toTeamDto(team1);
    }

    @Test
    void updateTeamById_WhenExists_ShouldUpdateWithoutError() {
        long id = 7L;

        teamDto1.setTeamId(id);
        team1.setTeamId(id);

        when(teamMapper.toTeam(teamDto1)).thenReturn(team1);
        when(teamRepository.updateTeamById(team1)).thenReturn(true);

        assertDoesNotThrow(() -> teamService.updateTeamById(id, teamDto1));

        verify(teamMapper).toTeam(teamDto1);
        verify(teamRepository).updateTeamById(team1);
    }

    @Test
    void deleteTeamById_WhenExists_ShouldSucceedSilently() {
        when(teamRepository.deleteTeamById(13L)).thenReturn(true);
        assertDoesNotThrow(() -> teamService.deleteTeamById(13L));
        verify(teamRepository).deleteTeamById(13L);
    }

    @Test
    void deleteTeamById_WhenMissing_ShouldThrow() {
        when(teamRepository.deleteTeamById(14L)).thenReturn(false);
        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> teamService.deleteTeamById(14L));
        assertTrue(ex.getMessage().contains("Team with id 14 not found"));
        verify(teamRepository).deleteTeamById(14L);
    }

}