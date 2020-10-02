package com.qa.sangivspring.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.sangivspring.dto.ClubDTO;
import com.qa.sangivspring.persistance.domain.Club;
import com.qa.sangivspring.persistance.repository.ClubRepository;


@SpringBootTest
class ClubServiceIntegrationTest {

    // because we're testing the service layer, we can't use a MockMvc
    // because MockMvc only models a controller (in mockito format)

    @Autowired
    private ClubService service;

    @Autowired
    private ClubRepository repo;

    @Autowired
    private ModelMapper modelMapper;

    private ClubDTO mapToDTO(Club club) {
        return this.modelMapper.map(club, ClubDTO.class);
    }

    // there's no objectMapper this time
    // because we don't need to convert any returned objects to JSON
    // that's a controller job, and we're not testing the controller! :D

    private Club testClub;
    private Club testClubWithId;
    private ClubDTO testClubDTO;

    private Long id;
    private final String name = "Arsenal";
    private final Long value = 180000000L;

    @BeforeEach
    void init() {
        this.repo.deleteAll();
        this.testClub = new Club(name, value);
        this.testClubWithId = this.repo.save(this.testClub);
        this.testClubDTO = this.mapToDTO(testClubWithId);
        this.id = this.testClubWithId.getId();
    }

    @Test
    void testCreate() {
        assertThat(this.testClubDTO)
            .isEqualTo(this.service.createClub(testClub));
    }

    @Test
    void testReadOne() {
        assertThat(this.testClubDTO)
                .isEqualTo(this.service.readOne(this.id));
    }

    @Test
    void testReadAll() {
        // check this one out with a breakpoint and running it in debug mode
        // so you can see the stream happening
        assertThat(this.service.readAllClubs())
                .isEqualTo(Stream.of(this.testClubDTO)
                        .collect(Collectors.toList()));
    }

    @Test
    void testUpdate() {
        ClubDTO newClub = new ClubDTO(null, "BVB", 250000000L, new ArrayList<>());
        ClubDTO updatedClub = new ClubDTO(this.id, newClub.getName(), newClub.getValue(), new ArrayList<>());

        assertThat(updatedClub)
            .isEqualTo(this.service.update(newClub, this.id));
    }

    @Test
    void testDelete() {
        assertThat(this.service.delete(this.id)).isTrue();
    }

}
