package com.qa.sangivspring.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qa.sangivspring.dto.PlayerDTO;
import com.qa.sangivspring.persistance.domain.Player;
import com.qa.sangivspring.service.PlayerService;

@RestController
@CrossOrigin
@RequestMapping("/player")
public class PlayerController {

	private PlayerService service;
	
	@Autowired
	public PlayerController(PlayerService service) {
		super();
		this.service = service;
	}

	//create
	@PostMapping("/create")
	public ResponseEntity<PlayerDTO> create(@RequestBody Player player) {
		PlayerDTO created = this.service.createPlayer(player);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	
	//readAll
	@GetMapping("/readAll")
	public ResponseEntity<List<PlayerDTO>> readAllPlayers() {
		return ResponseEntity.ok(this.service.readAllPlayers());	
	}
	
	
	//readOne
	@GetMapping("/readOne/{ID}")
	public ResponseEntity<PlayerDTO> readOne(@PathVariable Long ID) {
		return ResponseEntity.ok(this.service.readOne(ID));
	}
	
	
	//update
	@PutMapping("/update/{ID}")
	public ResponseEntity<PlayerDTO> update(@PathVariable Long ID, @RequestBody PlayerDTO playerDTO) {
		PlayerDTO updated = this.service.update(playerDTO, ID);
		return new ResponseEntity<>(updated, HttpStatus.ACCEPTED);
	}
	
	//delete
	@DeleteMapping("/delete/{ID}")
	public ResponseEntity<PlayerDTO> delete(@PathVariable Long ID) {
		return this.service.delete(ID)
				? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
