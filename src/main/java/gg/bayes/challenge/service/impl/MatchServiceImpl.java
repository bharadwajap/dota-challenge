package gg.bayes.challenge.service.impl;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gg.bayes.challenge.dao.EventRepository;
import gg.bayes.challenge.dao.MatchRepository;
import gg.bayes.challenge.dao.entity.Event;
import gg.bayes.challenge.dao.entity.Match;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MatchServiceImpl implements MatchService {

	private final String []EVENTS = {"buys", "killed", "casts", "hits"};
	private MatchRepository matchRepository;
	private EventRepository eventRepository;
	
    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository, EventRepository eventRepository) {
    	this.matchRepository = matchRepository;
    	this.eventRepository = eventRepository;
    }

    @Override
    public Long ingestMatch(String payload) {
    	Match match = new Match();
    	match.setMatchName("Dota_"+LocalDateTime.now());
    	Match savedMatch = matchRepository.save(match);
    	String []eventRows = payload.split("\\r?\\n");
    	for(String eventRow : eventRows) {
    		if(!validateRow(eventRow))
    			continue;
    		Event event = new Event();
    		event.setMatch(savedMatch);
    		
    		String [] rowContents = eventRow.split("\\s");
    		//buy events
    		//[00:08:46.693] npc_dota_hero_snapfire buys item item_clarity
    		if(eventRow.contains(EVENTS[0])) {
    			event.setTimeStamp(getLongTime(rowContents[0]));
    			event.setHero(rowContents[1]);
    			event.setEventType(rowContents[2]);
    			event.setEventTarget(rowContents[4]);
    			
    		//[00:12:15.108] npc_dota_neutral_harpy_scout is killed by npc_dota_hero_pangolier
    		}else if(eventRow.contains(EVENTS[1])) {
    			event.setTimeStamp(getLongTime(rowContents[0]));
    			event.setHero(rowContents[5]);
    			event.setEventType(rowContents[3]);
    			event.setEventTarget(rowContents[1]);
    			
    		//[00:12:20.774] npc_dota_hero_rubick casts ability rubick_fade_bolt (lvl 1) on npc_dota_neutral_harpy_storm
    		}else if(eventRow.contains(EVENTS[2])) {
    			event.setTimeStamp(getLongTime(rowContents[0]));
    			event.setHero(rowContents[1]);
    			event.setEventType(rowContents[2]);
    			event.setEventTarget(rowContents[8]);
    			event.setSpell(rowContents[4]);
    			event.setLevel("Level "+ rowContents[6].substring(0, 1));
    		
    		//[00:12:21.807] npc_dota_hero_rubick hits npc_dota_hero_pangolier with rubick_fade_bolt for 42 damage (544->502)
    		}else {
    			event.setTimeStamp(getLongTime(rowContents[0]));
    			event.setHero(rowContents[1]);
    			event.setEventType(rowContents[2]);
    			event.setEventTarget(rowContents[3]);
    			event.setSpell(rowContents[5]);
    			event.setDamage(Integer.parseInt(rowContents[7]));
    		}
    		eventRepository.save(event);
    	}
    	log.info("Succesfully ingested the Match log with Id: {}", savedMatch.getMatchId());
    	return savedMatch.getMatchId();
    }
    
    /**
     * Validates if the row is task interested event
     * @param eventRow
     * @return
     */
    private boolean validateRow(String eventRow) {
    	for(String event : EVENTS) {
    		if(eventRow.contains(event)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Converts given Timestamp string to millis (long)
     * @param timestamp
     * @return
     */
    private long getLongTime(String timestamp) {
    	long timeInMillis = 0;
    	String []hmsArr = timestamp.substring(1, timestamp.length()-1).replace('.', ':').split(":");
    	for(int i=0; i<hmsArr.length; i++){
    		switch (i) {
			case 0:
				timeInMillis+=Integer.parseInt(hmsArr[i])*3600*1000;
				break;
			case 1:
				timeInMillis+=Integer.parseInt(hmsArr[i])*60*1000;
				break;
			case 2:
				timeInMillis+=Integer.parseInt(hmsArr[i])*1000;
				break;
			case 3:
				timeInMillis+=Integer.parseInt(hmsArr[i]);
				break;
			}
    	}
    	return timeInMillis;
    }

	@Override
	public List<HeroDamage> getDamage(Long matchId, String heroName) {
		
		List<Tuple> damageTuples = eventRepository.fetchDamages(matchId, heroName);
		
		return damageTuples.stream()
		.map(damage -> {
			final HeroDamage heroDamage = new HeroDamage();
			heroDamage.setTarget(damage.get("target", String.class)); 
			heroDamage.setDamageInstances(damage.get("damageInstances", BigInteger.class).intValue()); 
			heroDamage.setTotalDamage(damage.get("totalDamage", BigInteger.class).intValue());
			return heroDamage;
		})
		.collect(Collectors.toList());
	}

	@Override
	public List<HeroKills> getKills(Long matchId) {
		List<Tuple> killTuples = eventRepository.fetchKills(matchId);
		
		return killTuples.stream()
		.map(killTuple -> {
			final HeroKills heroKills = new HeroKills();
			heroKills.setHero(killTuple.get("hero", String.class)); 
			heroKills.setKills(killTuple.get("kills", BigInteger.class).intValue());
			return heroKills;
		})
		.collect(Collectors.toList());
	}

	@Override
	public List<HeroItems> getItems(Long matchId, String heroName) {
		List<Tuple> itemTuples = eventRepository.fetchItems(matchId, heroName);
		
		return itemTuples.stream()
		.map(itemTuple -> {
			final HeroItems heroItems = new HeroItems();
			heroItems.setItem(itemTuple.get("item", String.class)); 
			heroItems.setTimestamp(itemTuple.get("timestamp", BigInteger.class).longValue());
			return heroItems;
		})
		.collect(Collectors.toList());
	}

	@Override
	public List<HeroSpells> getSpells(Long matchId, String heroName) {
		List<Tuple> spellTuples = eventRepository.fetchSpells(matchId, heroName);
		
		return spellTuples.stream()
		.map(spellTuple -> {
			final HeroSpells heroSpells = new HeroSpells();
			heroSpells.setSpell(spellTuple.get("spell", String.class)); 
			heroSpells.setCasts(spellTuple.get("casts", BigInteger.class).intValue());
			return heroSpells;
		})
		.collect(Collectors.toList());
	}
}
