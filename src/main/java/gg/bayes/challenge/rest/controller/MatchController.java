package gg.bayes.challenge.rest.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.bayes.challenge.exception.dto.ErrorResponse;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Api
@Slf4j
@Valid
@RestController
@RequestMapping("/api/match")
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @ApiOperation(
            value = "Injest combat log ",
            notes = "Make a POST request to Injest combat log and save match details",
            response = String.class,
            httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Match Created Successfully", response = String.class),
            @ApiResponse(code = 404, message = "Resource not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Unexpected Internal Error", response = ErrorResponse.class)})
    @PostMapping(consumes = "text/plain")
    public ResponseEntity<Long> ingestMatch(@RequestBody @NotNull @NotBlank String payload) {
        final Long matchId = matchService.ingestMatch(payload);
        return ResponseEntity.ok(matchId);
    }

    @ApiOperation(
            value = "Fetch Kills by Hero",
            notes = "Get request to fetch list of the heroes within a match and the number of kills they made.",
            response = HeroKills.class,
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "matchId", dataType = "long", required = true,
                value = "Match ID")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Kills retrieved succesfully", response = HeroKills.class),
            @ApiResponse(code = 404, message = "Resource not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Unexpected Internal Error", response = ErrorResponse.class)})
    @GetMapping("{matchId}")
    public ResponseEntity<List<HeroKills>> getMatch(@PathVariable("matchId") Long matchId) {
    	log.info("Fetching Kills for the give MatchId: {}", matchId);
    	return ResponseEntity.ok(matchService.getKills(matchId));
    }

    @ApiOperation(
            value = "Fetch purchases made by Hero",
            notes = "Get request to fetch Each item purchase (time bought and the item name) made by the specified hero within the selected match",
            response = HeroItems.class,
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "matchId", dataType = "long", required = true,
                value = "Match ID"),
            @ApiImplicitParam(name = "heroName", dataType = "String", required = true,
            value = "Name of Hero")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Purchases retrieved succesfully", response = HeroItems.class),
            @ApiResponse(code = 404, message = "Resource not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Unexpected Internal Error", response = ErrorResponse.class)})
    @GetMapping("{matchId}/{heroName}/items")
    public ResponseEntity<List<HeroItems>> getItems(@PathVariable("matchId") Long matchId,
                                                    @PathVariable("heroName") String heroName) {
    	log.info("Fetching Items for the give MatchId: {} and Hero: {}", matchId, heroName);
    	return ResponseEntity.ok(matchService.getItems(matchId, heroName));
    }

    @ApiOperation(
            value = "Fetch Spells made by Hero",
            notes = "Get request - For each different spell a hero casts in a particular match, "
            		+ "the number of times they cast said spell. The spell name is the name as encountered in the log file.",
            response = HeroSpells.class,
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "matchId", dataType = "long", required = true,
                value = "Match ID"),
            @ApiImplicitParam(name = "heroName", dataType = "String", required = true,
            value = "Name of Hero")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Purchases retrieved succesfully", response = HeroSpells.class),
            @ApiResponse(code = 404, message = "Resource not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Unexpected Internal Error", response = ErrorResponse.class)})
    @GetMapping("{matchId}/{heroName}/spells")
    public ResponseEntity<List<HeroSpells>> getSpells(@PathVariable("matchId") Long matchId,
                                                      @PathVariable("heroName") String heroName) {
    	log.info("Fetching Spells for the give MatchId: {} and Hero: {}", matchId, heroName);
    	return ResponseEntity.ok(matchService.getSpells(matchId, heroName));
    }

    @ApiOperation(
            value = "Fetch Damages made by Hero",
            notes = "For each hero that was damaged by the specified hero, "
            		+ "the number of times we damaged that hero, and the total damage done to that hero",
            response = HeroDamage.class,
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "matchId", dataType = "long", required = true,
                value = "Match ID"),
            @ApiImplicitParam(name = "heroName", dataType = "String", required = true,
            value = "Name of Hero")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Damages retrieved succesfully", response = HeroDamage.class),
            @ApiResponse(code = 404, message = "Resource not found", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Unexpected Internal Error", response = ErrorResponse.class)})
    @GetMapping("{matchId}/{heroName}/damage")
    public ResponseEntity<List<HeroDamage>> getDamage(@PathVariable("matchId") Long matchId,
                                                      @PathVariable("heroName") String heroName) {
    	log.info("Fetching Damage for the give MatchId: {} and Hero: {}", matchId, heroName);
    	return ResponseEntity.ok(matchService.getDamage(matchId, heroName));
    }
}
