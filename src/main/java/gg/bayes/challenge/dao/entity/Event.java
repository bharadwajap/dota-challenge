package gg.bayes.challenge.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "event")
public class Event {

	private static final String HERO_PREFIX = "npc_dota_hero_";
	private static final String ITEM_PREFIX = "item_";
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "event_id")
	private long eventId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "match_id")
	private Match match;
	
	@Column(name = "hero")
	private String hero;
	
	public void setHero(String hero) {
		this.hero = hero.replace(HERO_PREFIX, "");
	}
	
	@Column(name = "event_type")
	private String eventType;
	
	@Column(name = "event_target")
	private String eventTarget;
	
	public void setEventTarget(String eventTarget) {
		if(eventTarget!=null && eventTarget.contains(ITEM_PREFIX)) 
			this.eventTarget = eventTarget.replace(ITEM_PREFIX, "");
		if(eventTarget!=null && eventTarget.contains(HERO_PREFIX)) 
			this.eventTarget = eventTarget.replace(HERO_PREFIX, "");
		else
			this.eventTarget = eventTarget;
	}
	
	@Column(name = "spell")
	private String spell;
	
	@Column(name = "level")
	private String level;
	
	@Column(name = "damage")
	private int damage;
	
	@Column(name = "timestamp")
	private long timeStamp;
}
