package com.qa.sangivspring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.sangivspring.dto.PlayerDTO;
import com.qa.sangivspring.persistance.domain.Player;
import com.qa.sangivspring.persistance.repository.PlayerRepository;


@SpringBootTest
class PlayerServiceUnitTest {

    // autowiring -> using something already lying around
    @Autowired
    private PlayerService service;

    // mockbean -> get mockito to simulate responses from whatever class
    @MockBean
    private PlayerRepository repo;

    // mocked the modelMapper here, therefore need to set its values
    // for every test it is used in with the when().theReturn() method-chain
    @MockBean
    private ModelMapper modelMapper;

    // mapToDTO() function not needed if a mock modelMapper is spun up
    // because mockito inverts the control of that object - manages it
    // for us instead.

    // items span up to <expect> from our unit tests
    private List<Player> players;
    private Player testPlayer;
    private Player testPlayerWithId;
    private PlayerDTO playerDTO;

    // final values to assign to those <expected> objects
    final Long id = 1L;
    final String testName = "Ronaldo";
    final String testPosition = "ST";

    @BeforeEach
    void init() {
        this.players= new ArrayList<>();
        this.testPlayer = new Player(testName, testPosition);
        this.players.add(testPlayer);
        this.testPlayerWithId = new Player(testPlayer.getName(), testPlayer.getPosition());
        this.testPlayerWithId.setId(id);
        this.playerDTO = modelMapper.map(testPlayerWithId, PlayerDTO.class);
    }

    @Test
    void createTest() {
        // a when() to set up our mocked repo
        when(this.repo.save(this.testPlayer)).thenReturn(this.testPlayerWithId);

        // and a when() to set up our mocked modelMapper
        when(this.modelMapper.map(this.testPlayerWithId, PlayerDTO.class)).thenReturn(this.playerDTO);

        // check that the playerDTO set up as our <expected> value
        // is the same as the <actual> result when running the service.create()
        // method

        PlayerDTO expected = this.playerDTO;
        PlayerDTO actual = this.service.createPlayer(this.testPlayer);
        assertThat(expected).isEqualTo(actual);

        // we check that our mocked repository was hit - if it was, that means our
        // service works
        verify(this.repo, times(1)).save(this.testPlayer);
    }

    @Test
    void readOneTest() {
        // the repo spun up extends a Spring type of repo that uses Optionals
        // thus, when running a method in the repo (e.g. findById() ) we have to
        // return the object we want as an Optional (using Optional.of(our object) )
        when(this.repo.findById(this.id)).thenReturn(Optional.of(this.testPlayerWithId));

        when(this.modelMapper.map(this.testPlayerWithId, PlayerDTO.class)).thenReturn(this.playerDTO);

        assertThat(this.playerDTO).isEqualTo(this.service.readOne(this.id));

        verify(this.repo, times(1)).findById(this.id);
    }

    @Test
    void readAllTest() {
        // findAll() returns a list, which is handy, since we already have players spun up.
        when(this.repo.findAll()).thenReturn(this.players);

        when(this.modelMapper.map(this.testPlayerWithId, PlayerDTO.class)).thenReturn(this.playerDTO);

        assertThat(this.service.readAllPlayers().isEmpty()).isFalse();

        verify(this.repo, times(1)).findAll();
    }

    @Test
    void updateTest() {
        Player player = new Player("Messi", "RW");
        player.setId(this.id);

        PlayerDTO playerDTO = new PlayerDTO(null, "Messi", "RW");

        Player updatedPlayer = new Player(playerDTO.getName(), playerDTO.getPosition());
        updatedPlayer.setId(this.id);

        PlayerDTO updatedPlayerDTO = new PlayerDTO(this.id, updatedPlayer.getName(),
                updatedPlayer.getPosition());

        // finById() grabs a specific player out of the repo
        when(this.repo.findById(this.id)).thenReturn(Optional.of(player));

        // we then save() an updated player back to the repo
        // we'd normally save() it once we've done stuff to it, but instead we're just
        // feeding in an <expected>
        // that we set up at the top of this method ^
        when(this.repo.save(player)).thenReturn(updatedPlayer);

        when(this.modelMapper.map(updatedPlayer, PlayerDTO.class)).thenReturn(updatedPlayerDTO);

        assertThat(updatedPlayerDTO).isEqualTo(this.service.update(playerDTO, this.id));

        // since we've ran two when().thenReturn() methods, we need to run a verify() on
        // each:
        verify(this.repo, times(1)).findById(1L);
        verify(this.repo, times(1)).save(updatedPlayer);
    }

    @Test
    void deleteTest() {
        // running this.repo.existsById(id) twice, hence two returns (true &
        // false)
        // the <true> and <false> get plugged in once each to our two verify() methods
        when(this.repo.existsById(id)).thenReturn(true, false);

        assertThat(this.service.delete(id)).isTrue();


        verify(this.repo, times(1)).deleteById(id);
        
        // this plugs in the <true> from our when().thenReturn()
        // this plugs in the <false> from our when().thenReturn()
        verify(this.repo, times(2)).existsById(id);
    }

}