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

import com.qa.sangivspring.dto.ClubDTO;
import com.qa.sangivspring.persistance.domain.Club;
import com.qa.sangivspring.persistance.repository.ClubRepository;


@SpringBootTest
class ClubServiceUnitTest {

    // autowiring -> using something already lying around
    @Autowired
    private ClubService service;

    // mockbean -> get mockito to simulate responses from whatever class
    @MockBean
    private ClubRepository repo;

    // mocked the modelMapper here, therefore need to set its values
    // for every test it is used in with the when().theReturn() method-chain
    @MockBean
    private ModelMapper modelMapper;

    // mapToDTO() function not needed if a mock modelMapper is spun up
    // because mockito inverts the control of that object - manages it
    // for us instead.

    // items span up to <expect> from our unit tests
    private List<Club> clubs;
    private Club testClub;
    private Club testClubWithId;
    private ClubDTO clubDTO;

    // final values to assign to those <expected> objects
    final Long id = 1L;
    final String testName = "Arsenal";
    final Long testValue = 180000000L;

    @BeforeEach
    void init() {
        this.clubs= new ArrayList<>();
        this.testClub = new Club(testName, testValue);
        this.clubs.add(testClub);
        this.testClubWithId = new Club(testClub.getName(), testClub.getValue());
        this.testClubWithId.setId(id);
        this.clubDTO = modelMapper.map(testClubWithId, ClubDTO.class);
    }

    @Test
    void createTest() {
        // a when() to set up our mocked repo
        when(this.repo.save(this.testClub)).thenReturn(this.testClubWithId);

        // and a when() to set up our mocked modelMapper
        when(this.modelMapper.map(this.testClubWithId, ClubDTO.class)).thenReturn(this.clubDTO);

        // check that the clubDTO set up as our <expected> value
        // is the same as the <actual> result when running the service.create()
        // method

        ClubDTO expected = this.clubDTO;
        ClubDTO actual = this.service.createClub(this.testClub);
        assertThat(expected).isEqualTo(actual);

        // we check that our mocked repository was hit - if it was, that means our
        // service works
        verify(this.repo, times(1)).save(this.testClub);
    }

    @Test
    void readOneTest() {
        // the repo spun up extends a Spring type of repo that uses Optionals
        // thus, when running a method in the repo (e.g. findById() ) we have to
        // return the object we want as an Optional (using Optional.of(our object) )
        when(this.repo.findById(this.id)).thenReturn(Optional.of(this.testClubWithId));

        when(this.modelMapper.map(this.testClubWithId, ClubDTO.class)).thenReturn(this.clubDTO);

        assertThat(this.clubDTO).isEqualTo(this.service.readOne(this.id));

        verify(this.repo, times(1)).findById(this.id);
    }

    @Test
    void readAllTest() {
        // findAll() returns a list, which is handy, since we already have clubs spun up.
        when(this.repo.findAll()).thenReturn(this.clubs);

        when(this.modelMapper.map(this.testClubWithId, ClubDTO.class)).thenReturn(this.clubDTO);

        assertThat(this.service.readAllClubs().isEmpty()).isFalse();

        verify(this.repo, times(1)).findAll();
    }

    @Test
    void updateTest() {
        Club club = new Club("BVB", 250000000L);
        club.setId(this.id);

        ClubDTO clubDTO = new ClubDTO(null, "BVB", 250000000L, null);

        Club updatedClub= new Club(clubDTO.getName(), clubDTO.getValue());
        updatedClub.setId(this.id);

        ClubDTO updatedClubDTO = new ClubDTO(this.id, updatedClub.getName(),
                updatedClub.getValue(), null);

        // finById() grabs a specific club out of the repo
        when(this.repo.findById(this.id)).thenReturn(Optional.of(club));

        // we then save() an updated club back to the repo
        // we'd normally save() it once we've done stuff to it, but instead we're just
        // feeding in an <expected>
        // that we set up at the top of this method ^
        when(this.repo.save(club)).thenReturn(updatedClub);

        when(this.modelMapper.map(updatedClub, ClubDTO.class)).thenReturn(updatedClubDTO);

        assertThat(updatedClubDTO).isEqualTo(this.service.update(clubDTO, this.id));

        // since we've ran two when().thenReturn() methods, we need to run a verify() on
        // each:
        verify(this.repo, times(1)).findById(1L);
        verify(this.repo, times(1)).save(updatedClub);
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