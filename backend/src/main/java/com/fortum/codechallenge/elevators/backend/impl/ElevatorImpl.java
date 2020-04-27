package com.fortum.codechallenge.elevators.backend.impl;

import com.fortum.codechallenge.elevators.backend.api.Elevator;

public class ElevatorImpl implements Elevator {

	private Direction direction;

	private int addressedFloor;

	private int id;

	private boolean isBusy;

	private int currentFloor;

	public ElevatorImpl(int id) {
		super();
		this.id = id;
	}

	@Override
	public Direction getDirection() {
		return this.direction;
	}

	@Override
	public int getAddressedFloor() {
		return this.addressedFloor;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public void moveElevator(int toFloor) {
		this.isBusy = true;
		this.addressedFloor = toFloor;
		if (this.currentFloor - toFloor == 0) {
			this.direction = Direction.NONE;
		} else if (toFloor - this.currentFloor > 0) {
			this.direction = Direction.UP;
		} else {
			this.direction = Direction.DOWN;
		}
	}

	@Override
	public boolean isBusy() {
		return this.isBusy;
	}

	@Override
	public int currentFloor() {
		return this.currentFloor;
	}
	
	

	@Override
	public int getCurrentFloor() {
		return this.currentFloor;
	}

	@Override
	public void changeFloor() {
		if (this.currentFloor != this.addressedFloor) {
			this.currentFloor = ((this.addressedFloor - this.currentFloor) > 0)
					? this.currentFloor + 1
					: this.currentFloor - 1;
		} else {
			this.isBusy = false;
			this.direction = Direction.NONE;
		}
	}

}
