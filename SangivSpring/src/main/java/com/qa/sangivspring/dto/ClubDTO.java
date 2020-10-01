package com.qa.sangivspring.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

//import com.qa.sangivspring.persistance.domain.Player;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ClubDTO {
	
	private Long id;
	private String name;
	private Long value;
	private List<PlayerDTO> players;
	
}
