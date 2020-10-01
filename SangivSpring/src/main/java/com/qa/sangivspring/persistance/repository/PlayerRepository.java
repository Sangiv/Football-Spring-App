package com.qa.sangivspring.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qa.sangivspring.persistance.domain.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

}
