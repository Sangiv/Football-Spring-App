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

import com.qa.sangivspring.dto.PlayerDTO;
import com.qa.sangivspring.persistance.domain.Player;
import com.qa.sangivspring.service.PlayerService;

@SpringBootTest
public class PlayerControllerUnitTest {

	@Autowired
	private PlayerController controller;
	
	@Autowired
	private ModelMapper mapper;
	
	@MockBean
    private PlayerService service;

    private List<Player> players;
    private Player testPlayer;
    private Player testPlayerWithID;
    private PlayerDTO playerDTO;
    private final Long id = 1L;

    private PlayerDTO mapToDTO(Player player) {
        return this.mapper.map(player, PlayerDTO.class);
    }

    @BeforeEach
    void init() {
        this.players = new ArrayList<>();
        this.testPlayer = new Player("Ronaldo", "ST");
        this.testPlayerWithID = new Player(testPlayer.getName(), testPlayer.getPosition());
        this.testPlayerWithID.setId(id);
        this.players.add(testPlayerWithID);
        this.playerDTO = this.mapToDTO(testPlayerWithID);
    }

    @Test
    void createTest() {
        when(this.service.createPlayer(testPlayer))
            .thenReturn(this.playerDTO);
        
        assertThat(new ResponseEntity<PlayerDTO>(this.playerDTO, HttpStatus.CREATED))
                .isEqualTo(this.controller.create(testPlayer));
        
        verify(this.service, times(1))
            .createPlayer(this.testPlayer);
    }

    @Test
    void readOneTest() {
        when(this.service.readOne(this.id))
            .thenReturn(this.playerDTO);
        
        assertThat(new ResponseEntity<PlayerDTO>(this.playerDTO, HttpStatus.OK))
                .isEqualTo(this.controller.readOne(this.id));
        
        verify(this.service, times(1))
            .readOne(this.id);
    }

    @Test
    void readAllPlayersTest() {
        when(service.readAllPlayers())
            .thenReturn(this.players
                    .stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList()));
        
        assertThat(this.controller.readAllPlayers().getBody()
                .isEmpty()).isFalse();
        
        verify(this.service, times(1))
            .readAllPlayers();
    }

    @Test
    void updateTest() {
        // given
        PlayerDTO newPlayer= new PlayerDTO(null, "Messi", "RW");
        PlayerDTO updatedPlayer= new PlayerDTO(this.id, newPlayer.getName(), newPlayer.getPosition());

        when(this.service.update(newPlayer, this.id))
            .thenReturn(updatedPlayer);
        
        assertThat(new ResponseEntity<PlayerDTO>(updatedPlayer, HttpStatus.ACCEPTED))
                .isEqualTo(this.controller.update(this.id, newPlayer));
        
        verify(this.service, times(1))
            .update(newPlayer, this.id);
    }
    
    @Test
    void deleteTest() {
        this.controller.delete(id);

        verify(this.service, times(1))
            .delete(id);
    }
}
