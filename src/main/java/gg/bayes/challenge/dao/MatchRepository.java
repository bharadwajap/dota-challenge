package gg.bayes.challenge.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gg.bayes.challenge.dao.entity.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>{

}
