package com.qa.sangivspring.persistance.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;




@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Player {

		@Id //Primary Key
		@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremented
		private Long id;
		
		@Column(name= "player_name")
		@NotNull
		@Size(min= 1, max= 120)
		private String name;
		
		@Column(name= "player_position")
		@Size(min= 1, max= 120)
		private String position;
		
		@ManyToOne()
		private Club club;

		public Player(@NotNull @Size(min = 1, max = 120) String name, @Size(min = 1, max = 120) String position,
				Club club) {
			super();
			this.name = name;
			this.position = position;
			this.club = club;
		}
		
		public Player(@NotNull @Size(min = 1, max = 120) String name, @Size(min = 1, max = 120) String position) {
			super();
			this.name = name;
			this.position = position;
		}
		
}
