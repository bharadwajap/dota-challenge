package gg.bayes.challenge.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import gg.bayes.challenge.dao.EventRepository;
import gg.bayes.challenge.dao.MatchRepository;
import gg.bayes.challenge.dao.entity.Match;
import gg.bayes.challenge.service.impl.MatchServiceImpl;

@ExtendWith(MockitoExtension.class) 
public class MatchServiceTest {

	@Mock
	private MatchRepository matchRepository;
	
	@Mock
	private EventRepository eventRepository;
	
	@InjectMocks
	private MatchServiceImpl matchService;
	
	@Test
	public void testIngestMatch() throws IOException, URISyntaxException {
		StringBuilder strB = new StringBuilder();
    	Files.lines( Paths.get(ClassLoader.getSystemResource("combatlog_1.txt").toURI()), StandardCharsets.UTF_8);
		Match match = new Match();
		match.setMatchName("TestMatch");
		match.setMatchId(1);
		Mockito.when(matchRepository.save(any(Match.class))).thenReturn(match);
		long matchId = matchService.ingestMatch(strB.toString());
		
		assertThat(matchId).isEqualTo(1);
	}
	
}
