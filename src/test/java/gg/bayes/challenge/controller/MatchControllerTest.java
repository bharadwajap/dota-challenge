package gg.bayes.challenge.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import gg.bayes.challenge.rest.controller.MatchController;
import gg.bayes.challenge.service.MatchService;

@ExtendWith(MockitoExtension.class) 
public class MatchControllerTest {

    private MockMvc mockMvc;
    
    @Mock
    private MatchService matchService;
    
    @InjectMocks
    private MatchController matchController;
    
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
        		.standaloneSetup(matchController)
        		.build();
    }

    @Test
    public void testIngestMatch() throws Exception {
    	StringBuilder strB = new StringBuilder();
    	Files.lines( Paths.get(ClassLoader.getSystemResource("combatlog_1.txt").toURI()), StandardCharsets.UTF_8)
    	.forEach(line -> strB.append(line).append("\n"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/match")
        		.content(strB.toString())
        		.contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isOk());
    }
    
    public void testGetItems() throws Exception {
    	 mockMvc.perform(MockMvcRequestBuilders.get("/api/match/1/abyssal/items")
         		.contentType(MediaType.APPLICATION_JSON_VALUE))
                 .andExpect(status().isOk());
    }
}
