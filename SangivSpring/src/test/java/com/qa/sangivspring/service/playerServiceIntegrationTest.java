package com.qa.sangivspring.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.sangivspring.dto.PlayerDTO;
import com.qa.sangivspring.persistance.domain.Player;
import com.qa.sangivspring.persistance.repository.PlayerRepository;


@SpringBootTest
class playerServiceIntegrationTest {

    // because we're testing the service layer, we can't use a MockMvc
    // because MockMvc only models a controller (in mockito format)

    @Autowired
    private PlayerService service;

    @Autowired
    private PlayerRepository repo;

    @Autowired
    private ModelMapper modelMapper;

    private PlayerDTO mapToDTO(Player player) {
        return this.modelMapper.map(player, PlayerDTO.class);
    }

    // there's no objectMapper this time
    // because we don't need to convert any returned objects to JSON
    // that's a controller job, and we're not testing the controller! :D

    private Player testPlayer;
    private Player testPlayerWithId;
    private PlayerDTO testPlayerDTO;

    private Long id;
    private final String name = "Ronaldo";
    private final String position = "RW";

    @BeforeEach
    void init() {
        this.repo.deleteAll();
        this.testPlayer= new Player(name, position);
        this.testPlayerWithId = this.repo.save(this.testPlayer);
        this.testPlayerDTO = this.mapToDTO(testPlayerWithId);
        this.id = this.testPlayerWithId.getId();
    }

    @Test
    void testCreate() {
        assertThat(this.testPlayerDTO)
            .isEqualTo(this.service.createPlayer(testPlayer));
    }

    @Test
    void testReadOne() {
        assertThat(this.testPlayerDTO)
                .isEqualTo(this.service.readOne(this.id));
    }

    @Test
    void testReadAll() {
        // check this one out with a breakpoint and running it in debug mode
        // so you can see the stream happening
        assertThat(this.service.readAllPlayers())
                .isEqualTo(Stream.of(this.testPlayerDTO)
                        .collect(Collectors.toList()));
    }

    @Test
    void testUpdate() {
        PlayerDTO newPlayer = new PlayerDTO(null, "Messi", "RW");
        PlayerDTO updatedPlayer = new PlayerDTO(this.id, newPlayer.getName(), newPlayer.getPosition());

        assertThat(updatedPlayer)
            .isEqualTo(this.service.update(newPlayer, this.id));
    }

    @Test
    void testDelete() {
        assertThat(this.service.delete(this.id)).isTrue();
    }

}
