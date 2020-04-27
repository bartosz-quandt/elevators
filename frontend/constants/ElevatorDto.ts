export interface ElevatorDto {
    direction: string;
    addressedFloor: number;
    id: number;
    currentFloor: number;
    busy: boolean;
}