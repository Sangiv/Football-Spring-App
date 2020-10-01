package com.qa.sangivspring.persistance.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;




@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Club {

		@Id //Primary Key
		@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto incremented
		private Long id;
		
		@Column(name= "club_name")
		@NotNull
		@Size(min= 1, max= 120)
		private String name;
		
		@Column(name= "club_value")
		@Min(1)
		@Max(1000000000)
		private Long value;
		
		@OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
		private List<Player> players = new ArrayList<>();

		public Club(@NotNull @Size(min = 1, max = 120) String name, @Min(1) @Max(1000000000) Long value) {
			super();
			this.name = name;
			this.value = value;
		}
		
}
