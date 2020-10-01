package com.qa.sangivspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.qa.sangivspring.dto.PlayerDTO;
import com.qa.sangivspring.exception.PlayerNotFoundException;
import com.qa.sangivspring.persistance.domain.Player;
import com.qa.sangivspring.persistance.repository.PlayerRepository;
import com.qa.sangivspring.utils.sangivspringBeanUtils;

@Service
public class PlayerService {
	
	private PlayerRepository repo;
	
	private ModelMapper mapper;
	
	@Autowired
	public PlayerService(PlayerRepository repo, ModelMapper model) {
		super();
		this.repo = repo;
		this.mapper = model;
	}
	
	private PlayerDTO mapToDTO(Player player) {
		return this.mapper.map(player, PlayerDTO.class);
	}
	
//	private Player mapFromDTO(PlayerDTO playerDTO) {
//		return this.mapper.map(playerDTO, Player.class);
//	}
//	
	
	//create
	public PlayerDTO createPlayer(Player player) {
//		Player toSave = this.mapFromDTO(playerDTO);
		Player saved = this.repo.save(player);
		return this.mapToDTO(saved);
	}
	
	//readAll
	public List<PlayerDTO> readAllPlayers() {
		return this.repo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
	}
	
	//readByID
	public PlayerDTO readOne(Long id) {
		Player found = this.repo.findById(id).orElseThrow(PlayerNotFoundException::new);
		return this.mapToDTO(found);
	}
	
	//update
	public PlayerDTO update(PlayerDTO playerDTO, Long ID) {
		Player toUpdate = this.repo.findById(ID).orElseThrow(PlayerNotFoundException::new);
		sangivspringBeanUtils.mergeObject(playerDTO, toUpdate);
		return this.mapToDTO(this.repo.save(toUpdate));
	}
	
	//delete
	public boolean delete(Long ID) {
		this.repo.deleteById(ID);
		return !this.repo.existsById(ID);
	}
	
	
}
