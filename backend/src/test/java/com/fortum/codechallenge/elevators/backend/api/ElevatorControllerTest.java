package com.fortum.codechallenge.elevators.backend.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fortum.codechallenge.elevators.backend.config.ConfigProperties;
import com.fortum.codechallenge.elevators.backend.impl.ElevatorControllerImpl;

@ExtendWith(SpringExtension.class)
public class ElevatorControllerTest {
	
	@TestConfiguration
    static class AnagramFinderTestContextConfiguration {
		
		@Bean
		public ConfigProperties configProperties() {
            ConfigProperties bean =  new ConfigProperties();
            bean.setNumberOfElevators(6);
            return bean;
        }
		
        @Bean
        public ElevatorController elevatorController() {
            return new ElevatorControllerImpl(configProperties());
        }
    }

	@Autowired
	private ElevatorController elevatorController;
	
	@Autowired
	private ConfigProperties configProperties;

	@Test
	public void shouldGetListOfElevetors() {
		assertThat(elevatorController.getElevators().size())
			.isEqualTo(configProperties.getNumberOfElevators());
	}
	
	@Test
	public void shouldRequestElevatorDown() throws InterruptedException, ExecutionException {
		int toFloor = -2;
		assertThat(elevatorController.requestElevator(toFloor).get().currentFloor())
		.isEqualTo(toFloor);
	}
	
	@Test
	public void shouldRequestElevatorUp() throws InterruptedException, ExecutionException {
		int toFloor = 2;
		assertThat(elevatorController.requestElevator(toFloor).get().currentFloor())
		.isEqualTo(toFloor);
	}
	
	@Test
	public void shouldPickTheSameElevator() throws InterruptedException, ExecutionException {
		int toFloor = 2;
		Elevator cachedElevator = elevatorController.requestElevator(toFloor).get();
		assertThat(elevatorController.requestElevator(toFloor).get())
		.isEqualTo(cachedElevator);
	}

}
