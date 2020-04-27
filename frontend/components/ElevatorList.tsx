import React from 'react';
import { ActivityIndicator, View, Text, FlatList, TouchableOpacity } from 'react-native';
import { ElevatorDto } from '../constants/ElevatorDto';

interface Props {
    elevators: Array<ElevatorDto>;
    isLoading: boolean;
}

export default class ElevatorList extends React.Component<Props, {}> {

    render() {
        return <View>
            {this.props.isLoading ?
                <React.Fragment>
                    <ActivityIndicator size="large" color="#0000ff" />
                    <Text>Elevator list is being loaded</Text>
                </React.Fragment>
                :
                <FlatList
                    style={{ flex: 1, width: '100%' }}
                    data={this.props.elevators}
                    renderItem={({ item }) => <Item item={item} />}
                    keyExtractor={item => item.id.toString()}
                />

            }
        </View>
    }
}

function Item({ item }) {
    return (
        <View>
            <View style={{ alignItems: "center", flex: 1 }}>
                <Text>Id: {item.id} ToFloor: {item.addressedFloor} CurrentFloor: {item.currentFloor} Busy: {item.busy.toString()}</Text>
            </View>
        </View>
    );
}