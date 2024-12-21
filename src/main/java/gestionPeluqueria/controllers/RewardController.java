package gestionPeluqueria.controllers;

import gestionPeluqueria.entities.Reward;
import gestionPeluqueria.services.impl.RewardServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/peluquerias/recompensas")
public class RewardController {

    private final RewardServiceImpl rewardService;

    public RewardController(RewardServiceImpl rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Reward>> getAllRewards() {
        try {
            List<Reward> rewards = rewardService.findAll();

            if (rewards.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(rewards, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /*
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reward> getReward(@PathVariable("id") long id) {
        try {
            Reward reward = rewardService.findById(id);

            if (reward == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(reward, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reward> createReward(@RequestBody Reward reward) {
        try {
            Reward rewardCreated = rewardService.createReward(reward);

            if (rewardCreated == null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(rewardCreated);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reward> updateReward(@PathVariable("id") long id, @RequestBody Reward reward) {
        try {
            Reward rewardUpdated = rewardService.updateReward(id, reward);

            if (rewardUpdated == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(rewardUpdated, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteReward(@PathVariable("id") long id) {
        try {
            if (rewardService.findById(id) == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            rewardService.deleteReward(id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}