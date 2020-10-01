package com.qa.sangivspring.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qa.sangivspring.persistance.domain.Club;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

}
