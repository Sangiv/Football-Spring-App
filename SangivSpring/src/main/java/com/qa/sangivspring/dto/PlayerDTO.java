package com.qa.sangivspring.dto;

//import com.qa.sangivspring.persistance.domain.Club;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PlayerDTO {
	
	private Long id;
	private String name;
	private String position;
	
}
