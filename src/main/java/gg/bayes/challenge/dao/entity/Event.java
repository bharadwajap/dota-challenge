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

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "event_id")
	private long eventId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "match_id")
	private Match match;
	
	@Column(name = "hero")
	private String hero;
	
	@Column(name = "event_type")
	private String eventType;
	
	@Column(name = "event_target")
	private String eventTarget;
	
	@Column(name = "spell")
	private String spell;
	
	@Column(name = "level")
	private String level;
	
	@Column(name = "damage")
	private int damage;
	
	@Column(name = "timestamp")
	private long timeStamp;
}
