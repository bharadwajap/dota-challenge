package gg.bayes.challenge.service;

import java.util.List;

import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;

public interface MatchService {
	/**
	 * Ingests the match details from file to DB
	 * @param payload
	 * @return Match ID
	 */
    Long ingestMatch(String payload);
    
    /**
     * Gets list of the heroes within a match and the number of kills they made.
     * @param matchId
     * @return
     */
    List<HeroKills> getKills(Long matchId);
    
    /**
     * Each item purchase (time bought and the item name) made by the specified 
     * hero within the selected match
     * @param matchId
     * @param heroName
     * @return
     */
    List<HeroItems> getItems(Long matchId, String heroName);
    
    /**
     * For each different spell a hero casts in a particular match, 
     * the number of times they cast said spell. 
     * The spell name is the name as encountered in the log file.
     * @param matchId
     * @param heroName
     * @return
     */
    List<HeroSpells> getSpells(Long matchId, String heroName);
    
    /**
     * For each hero that was damaged by the specified hero, 
     * the number of times we damaged that hero, and the total damage done to that hero
     * @param matchId
     * @param heroName
     * @return
     */
    List<HeroDamage> getDamage(Long matchId, String heroName);
}
