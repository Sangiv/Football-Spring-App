package com.qa.sangivspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.qa.sangivspring.dto.ClubDTO;
import com.qa.sangivspring.exception.ClubNotFoundException;
import com.qa.sangivspring.persistance.domain.Club;
import com.qa.sangivspring.persistance.repository.ClubRepository;
import com.qa.sangivspring.utils.sangivspringBeanUtils;

@Service
public class ClubService {
	
	private ClubRepository repo;
	
	private ModelMapper mapper;
	
	@Autowired
	public ClubService(ClubRepository repo, ModelMapper model) {
		super();
		this.repo = repo;
		this.mapper = model;
	}
	
	private ClubDTO mapToDTO(Club club) {
		return this.mapper.map(club, ClubDTO.class);
	}
	
//	private Club mapFromDTO(ClubDTO clubDTO) {
//		return this.mapper.map(clubDTO, Club.class);
//	}
	
	
	//create
	public ClubDTO createClub(Club club) {
//		Club toSave = this.mapFromDTO(clubDTO);
		Club saved = this.repo.save(club);
		return this.mapToDTO(saved);
	}
	
	//readAll
	public List<ClubDTO> readAllClubs() {
		return this.repo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
	}
	
	//readByID
	public ClubDTO readOne(Long ID) {
		Club found = this.repo.findById(ID).orElseThrow(ClubNotFoundException::new);
		return this.mapToDTO(found);
	}
	
	//update
	public ClubDTO update(ClubDTO clubDTO, Long ID) {
		Club toUpdate = this.repo.findById(ID).orElseThrow(ClubNotFoundException::new);
		sangivspringBeanUtils.mergeObject(clubDTO, toUpdate);
		return this.mapToDTO(this.repo.save(toUpdate));
	}
	
	//delete
	public boolean delete(Long ID) {
        if (!this.repo.existsById(ID)) {
            throw new ClubNotFoundException();
        }
        this.repo.deleteById(ID);
        return !this.repo.existsById(ID);
    }
	
	
}
