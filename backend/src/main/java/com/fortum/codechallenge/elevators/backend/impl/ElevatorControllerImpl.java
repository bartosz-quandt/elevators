package com.fortum.codechallenge.elevators.backend.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fortum.codechallenge.elevators.backend.api.Elevator;
import com.fortum.codechallenge.elevators.backend.api.ElevatorController;
import com.fortum.codechallenge.elevators.backend.config.ConfigProperties;

@Service
public class ElevatorControllerImpl implements ElevatorController {
	
	private static final Logger LOG = LoggerFactory.getLogger(ElevatorControllerImpl.class);
	

    private ConfigProperties configProperties;
	
	private List<Elevator> elevators;
	
    public ElevatorControllerImpl(ConfigProperties configProperties) {
    	this.configProperties = configProperties;
    	LOG.info("Creating number of elevators: {}", this.configProperties.getNumberOfElevators());
    	this.elevators = new ArrayList<>();
    	Stream.iterate(0, n -> n + 1)
        	.limit(configProperties.getNumberOfElevators())
        	.forEach(x -> this.elevators.add(new ElevatorImpl(x + 1)));
	}

	@Override
	@Async
    public CompletableFuture<Elevator> requestElevator(int toFloor) {
        LOG.info("Requesting elevator to floor: {}", toFloor);
        Elevator alreadyAddressedElevator = this.checkForAlreadyAddressedElevator(toFloor);
        if (alreadyAddressedElevator != null) {
        	LOG.info("Elevator {} already coming!", alreadyAddressedElevator.getId());
        	return CompletableFuture.completedFuture(alreadyAddressedElevator);
        }
        Elevator pickedElevator = this.pickBestElevator(toFloor);
        LOG.info("Picked best elevator: {}", pickedElevator.getId());
        pickedElevator.moveElevator(toFloor);
        this.simulateElevatorMovement(pickedElevator, toFloor);
        LOG.info("Elevator {} reached floor {}", pickedElevator.getId(), pickedElevator.currentFloor());
        return CompletableFuture.completedFuture(pickedElevator);
    }

    @Override
    public List<Elevator> getElevators() {
        return this.elevators;
    }

    @Override
    public void releaseElevator(Elevator elevator) {
        throw new UnsupportedOperationException("TODO");
    }
    
    private Elevator pickBestElevator(int toFloor) {
    	return this.getElevators().stream()
		.filter(Predicate.not(Elevator::isBusy))
		.min(Comparator.comparing(e -> Math.abs(e.currentFloor() - toFloor)))
		.orElseThrow(RuntimeException::new);
    }
    
    private Elevator checkForAlreadyAddressedElevator(int toFloor) {
    	return this.getElevators().stream()
    			.filter(e -> e.getAddressedFloor() == toFloor)
    			.findFirst()
    			.orElse(null);
    }
    
    private void simulateElevatorMovement(Elevator pickedElevator, int toFloor) {
    	Stream.iterate(0, n -> n + 1)
    	.limit(Math.abs(pickedElevator.currentFloor() - toFloor) + 1L)
    	.forEach(x -> {
    		LOG.debug("Elevator {} is moving to destination floor {} and current floor {}",
    				pickedElevator.getId(), toFloor, pickedElevator.currentFloor());
    		pickedElevator.changeFloor();
    		try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				LOG.warn("Interrupted", e1);
				Thread.currentThread().interrupt();
			}
    	});
    }
}
