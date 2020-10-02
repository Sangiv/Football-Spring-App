package com.qa.sangivspring.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.qa.sangivspring.dto.ClubDTO;
import com.qa.sangivspring.persistance.domain.Club;
import com.qa.sangivspring.service.ClubService;

@SpringBootTest
public class ClubControllerUnitTest {

	@Autowired
	private ClubController controller;
	
	@Autowired
	private ModelMapper mapper;
	
	@MockBean
    private ClubService service;

    private List<Club> clubs;
    private Club testClub;
    private Club testClubWithID;
    private ClubDTO clubDTO;
    private final Long id = 1L;

    private ClubDTO mapToDTO(Club club) {
        return this.mapper.map(club, ClubDTO.class);
    }

    @BeforeEach
    void init() {
        this.clubs = new ArrayList<>();
        this.testClub = new Club("Arsenal", 180000000L);
        this.testClubWithID = new Club(testClub.getName(), testClub.getValue());
        this.testClubWithID.setId(id);
        this.clubs.add(testClubWithID);
        this.clubDTO = this.mapToDTO(testClubWithID);
    }

    @Test
    void createTest() {
        when(this.service.createClub(testClub))
            .thenReturn(this.clubDTO);
        
        assertThat(new ResponseEntity<ClubDTO>(this.clubDTO, HttpStatus.CREATED))
                .isEqualTo(this.controller.create(testClub));
        
        verify(this.service, times(1))
            .createClub(this.testClub);
    }

    @Test
    void readOneTest() {
        when(this.service.readOne(this.id))
            .thenReturn(this.clubDTO);
        
        assertThat(new ResponseEntity<ClubDTO>(this.clubDTO, HttpStatus.OK))
                .isEqualTo(this.controller.readOne(this.id));
        
        verify(this.service, times(1))
            .readOne(this.id);
    }

    @Test
    void readAllClubsTest() {
        when(service.readAllClubs())
            .thenReturn(this.clubs
                    .stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList()));
        
        assertThat(this.controller.readAllClubs().getBody()
                .isEmpty()).isFalse();
        
        verify(this.service, times(1))
            .readAllClubs();
    }

    @Test
    void updateTest() {
        // given
        ClubDTO newClub= new ClubDTO(null, "BVB", 250000000L, null);
        ClubDTO updatedClub= new ClubDTO(this.id, newClub.getName(), newClub.getValue(), null);

        when(this.service.update(newClub, this.id))
            .thenReturn(updatedClub);
        
        assertThat(new ResponseEntity<ClubDTO>(updatedClub, HttpStatus.ACCEPTED))
                .isEqualTo(this.controller.update(this.id, newClub));
        
        verify(this.service, times(1))
            .update(newClub, this.id);
    }
    
    @Test
    void deleteTest() {
        this.controller.delete(id);

        verify(this.service, times(1))
            .delete(id);
    }
}
