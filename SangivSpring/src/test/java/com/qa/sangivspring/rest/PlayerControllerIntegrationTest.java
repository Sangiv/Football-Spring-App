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
import com.qa.sangivspring.dto.PlayerDTO;
import com.qa.sangivspring.persistance.domain.Player;
import com.qa.sangivspring.persistance.repository.PlayerRepository;


@SpringBootTest
@AutoConfigureMockMvc
public class PlayerControllerIntegrationTest {

    // autowiring objects for mocking different aspects of the application
    // here, a mock repo (and relevant mappers) are autowired
    // they'll 'just work', so we don't need to worry about them
    // all we're testing is how our controller integrates with the rest of the API

    @Autowired
    private MockMvc mock;

    @Autowired
    private PlayerRepository repo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Player testPlayer;
    private Player testPlayerWithID;
    private PlayerDTO playerDTO;

    private Long id;
    private String testName;
    private String testPosition;

    private PlayerDTO mapToDTO(Player player) {
        return this.modelMapper.map(player, PlayerDTO.class);
    }

    @BeforeEach
    void init() {
        this.repo.deleteAll();

        this.testPlayer = new Player("Ronaldo", "ST");
        this.testPlayerWithID = this.repo.save(this.testPlayer);
        this.playerDTO = this.mapToDTO(testPlayerWithID);

        this.id = this.testPlayerWithID.getId();
        this.testName = this.testPlayerWithID.getName();
        this.testPosition= this.testPlayerWithID.getPosition();
    }

    @Test
    void testCreate() throws Exception {
        this.mock
                .perform(request(HttpMethod.POST, "/player/create").contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(testPlayer))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(this.objectMapper.writeValueAsString(playerDTO)));
    }

    @Test
    void testRead() throws Exception {
        this.mock.perform(request(HttpMethod.GET, "/player/readOne/" + this.id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(this.objectMapper.writeValueAsString(this.playerDTO)));
    }

    @Test
    void testReadAll() throws Exception {
        List<PlayerDTO> players = new ArrayList<>();
        players.add(this.playerDTO);

        String content = this.mock
                .perform(request(HttpMethod.GET, "/player/readAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertEquals(this.objectMapper.writeValueAsString(players), content);
    }

    @Test
    void testUpdate() throws Exception {
        PlayerDTO newPlayer = new PlayerDTO(null, "Messi", "RW");
        Player updatedPlayer = new Player(newPlayer.getName(), newPlayer.getPosition());
        updatedPlayer.setId(this.id);

        String result = this.mock
                .perform(request(HttpMethod.PUT, "/player/update/" + this.id).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(newPlayer)))
                .andExpect(status().isAccepted()).andReturn().getResponse().getContentAsString();

        assertEquals(this.objectMapper.writeValueAsString(this.mapToDTO(updatedPlayer)), result);
    }

    @Test
    void testDelete() throws Exception {
        this.mock.perform(request(HttpMethod.DELETE, "/player/delete/" + this.id)).andExpect(status().isNoContent());
    }

}
