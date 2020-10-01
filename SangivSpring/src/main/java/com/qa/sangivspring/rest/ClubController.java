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

import com.qa.sangivspring.dto.ClubDTO;
import com.qa.sangivspring.persistance.domain.Club;
import com.qa.sangivspring.service.ClubService;

@RestController
@CrossOrigin
@RequestMapping("/club")
public class ClubController {

	private ClubService service;
	
	@Autowired
	public ClubController(ClubService service) {
		super();
		this.service = service;
	}

	//create
	@PostMapping("/create")
	public ResponseEntity<ClubDTO> create(@RequestBody Club club) {
		ClubDTO created = this.service.createClub(club);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	
	//readAll
	@GetMapping("/readAll")
	public ResponseEntity<List<ClubDTO>> readAllClubs() {
		return ResponseEntity.ok(this.service.readAllClubs());	
	}
	
	
	//readOne
	@GetMapping("/readOne/{ID}")
	public ResponseEntity<ClubDTO> readOne(@PathVariable Long ID) {
		return ResponseEntity.ok(this.service.readOne(ID));
	}
	
	
	//update
	@PutMapping("/update/{ID}")
	public ResponseEntity<ClubDTO> update(@PathVariable Long ID, @RequestBody ClubDTO clubDTO) {
		ClubDTO updated = this.service.update(clubDTO, ID);
		return new ResponseEntity<>(updated, HttpStatus.ACCEPTED);
	}
	
	//delete
	@DeleteMapping("/delete/{ID}")
	public ResponseEntity<ClubDTO> delete(@PathVariable Long ID) {
		return this.service.delete(ID)
				? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
