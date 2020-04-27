package com.fortum.codechallenge.elevators.backend.resources;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fortum.codechallenge.elevators.backend.api.Elevator;
import com.fortum.codechallenge.elevators.backend.api.ElevatorController;

/**
 * Rest Resource.
 */
@RestController
@RequestMapping("/rest/v1")
public final class ElevatorControllerEndPoints {
	
	private static final Logger LOG = LoggerFactory.getLogger(ElevatorControllerEndPoints.class);
	
    @Autowired
    private ElevatorController elevatorController;

    /**
     * Ping service to test if we are alive.
     *
     * @return String pong
     */
    @GetMapping(value = "/ping")
    public String ping() {
        return "pong";
    }
    
    @CrossOrigin(origins = "*")
    @GetMapping("/elevators")
    public List<Elevator> getElevators() {
    	return this.elevatorController.getElevators();
    }
    
    @CrossOrigin(origins = "*")
    @GetMapping("/request-elevator")
    public CompletableFuture<Elevator> requestElevator(@RequestParam(name = "to-floor") int toFloor) {
    	LOG.info("GET requested elevator to floor: {}", toFloor);
    	return this.elevatorController.requestElevator(toFloor);
    }
}
