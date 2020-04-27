package com.fortum.codechallenge.elevators.backend;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fortum.codechallenge.elevators.backend.config.ConfigProperties;
import com.fortum.codechallenge.elevators.backend.resources.ElevatorControllerEndPoints;

/**
 * Boiler plate test class to get up and running with a test faster.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IntegrationTest {
	
	@Autowired
	private ElevatorControllerEndPoints endpoints;
	
	@Autowired
	private ConfigProperties configProperties;

	@Test
	public void simulateAnElevatorShaft() {
		assertThat(endpoints.ping()).isEqualTo("pong");
	}

	@Test
	public void shouldGetToFloor() throws InterruptedException, ExecutionException {
		int toFloor = 2;
		assertThat(endpoints.requestElevator(toFloor).get().getCurrentFloor())
			.isEqualTo(toFloor);
	}
	
	@Test
	public void shouldGetElevetors() {
		assertThat(endpoints.getElevators().size())
			.isEqualTo(configProperties.getNumberOfElevators());
	}

}
