package gg.bayes.challenge.dao;

import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gg.bayes.challenge.dao.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>{

	@Query(value = "SELECT hero as hero,  count(1) as kills FROM EVENT\r\n" + 
			"where match_id = :matchId\r\n" + 
			"and event_type = 'killed'\r\n" + 
			"and hero like '%hero%'\r\n" +
			"group by spell", 
			nativeQuery = true)
	List<Tuple> fetchKills(@Param("matchId") Long matchId);
	
	@Query(value = "SELECT event_target as item,  timestamp as timestamp FROM EVENT\r\n" + 
			"where match_id = :matchId\r\n" + 
			"and event_type = 'buys'\r\n" + 
			"and hero = :heroName", 
			nativeQuery = true)
	List<Tuple> fetchItems(@Param("matchId") Long matchId, @Param("heroName") String heroName);
	
	@Query(value = "SELECT spell as spell,  count(1) as casts FROM EVENT\r\n" + 
			"where match_id = :matchId\r\n" + 
			"and event_type = 'casts'\r\n" + 
			"and hero = :heroName\r\n" + 
			"group by spell",
			nativeQuery = true)
	List<Tuple> fetchSpells(@Param("matchId") Long matchId, @Param("heroName") String heroName);
	
	@Query(value = "SELECT event_target as target,  count(1) as damageInstances, sum(damage) as totalDamage FROM EVENT\r\n" + 
			"where match_id = :matchId\r\n" + 
			"and event_type = 'hits'\r\n" + 
			"and hero = :heroName\r\n" + 
			"group by event_target",
			nativeQuery = true)
	List<Tuple> fetchDamages(@Param("matchId") Long matchId, @Param("heroName") String heroName);
	
	
}
