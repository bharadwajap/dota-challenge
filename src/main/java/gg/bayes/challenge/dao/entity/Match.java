package gg.bayes.challenge.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "match")
public class Match {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "match_id")
	private long matchId;
	
	@Column(name = "match_name")
	private String matchName;
}
