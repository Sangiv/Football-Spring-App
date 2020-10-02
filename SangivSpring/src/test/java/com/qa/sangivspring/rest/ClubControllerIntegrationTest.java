package com.qa.sangivspring.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.sangivspring.dto.ClubDTO;
import com.qa.sangivspring.persistance.domain.Club;
import com.qa.sangivspring.persistance.repository.ClubRepository;


@SpringBootTest
@AutoConfigureMockMvc
public class ClubControllerIntegrationTest {

    // autowiring objects for mocking different aspects of the application
    // here, a mock repo (and relevant mappers) are autowired
    // they'll 'just work', so we don't need to worry about them
    // all we're testing is how our controller integrates with the rest of the API

    @Autowired
    private MockMvc mock;

    @Autowired
    private ClubRepository repo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Club testClub;
    private Club testClubWithID;
    private ClubDTO clubDTO;

    private Long id;
    private String testName;
    private Long testValue;

    private ClubDTO mapToDTO(Club club) {
        return this.modelMapper.map(club, ClubDTO.class);
    }

    @BeforeEach
    void init() {
        this.repo.deleteAll();

        this.testClub = new Club("Arsenal", 180000000L);
        this.testClubWithID = this.repo.save(this.testClub);
        this.clubDTO = this.mapToDTO(testClubWithID);

        this.id = this.testClubWithID.getId();
        this.testName = this.testClubWithID.getName();
        this.testValue= this.testClubWithID.getValue();
    }

    @Test
    void testCreate() throws Exception {
        this.mock
                .perform(request(HttpMethod.POST, "/club/create").contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(testClub))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(this.objectMapper.writeValueAsString(clubDTO)));
    }

    @Test
    void testRead() throws Exception {
        this.mock.perform(request(HttpMethod.GET, "/club/readOne/" + this.id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(this.objectMapper.writeValueAsString(this.clubDTO)));
    }

    @Test
    void testReadAll() throws Exception {
        List<ClubDTO> clubs = new ArrayList<>();
        clubs.add(this.clubDTO);

        String content = this.mock
                .perform(request(HttpMethod.GET, "/club/readAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertEquals(this.objectMapper.writeValueAsString(clubs), content);
    }

    @Test
    void testUpdate() throws Exception {
        ClubDTO newClub = new ClubDTO(null, "BVB", 250000000L, null);
        Club updatedClub = new Club(newClub.getName(), newClub.getValue());
        updatedClub.setId(this.id);

        String result = this.mock
                .perform(request(HttpMethod.PUT, "/club/update/" + this.id).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(newClub)))
                .andExpect(status().isAccepted()).andReturn().getResponse().getContentAsString();

        assertEquals(this.objectMapper.writeValueAsString(this.mapToDTO(updatedClub)), result);
    }

    @Test
    void testDelete() throws Exception {
        this.mock.perform(request(HttpMethod.DELETE, "/club/delete/" + this.id)).andExpect(status().isNoContent());
    }

}
