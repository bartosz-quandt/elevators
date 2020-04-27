import React from 'react';
import { StyleSheet, Text, View, Button, TextInput, ActivityIndicator } from 'react-native';
import ElevatorList from './components/ElevatorList';
import { ElevatorDto } from './constants/ElevatorDto';
import { Separator } from './components/Separator';

const API = 'http://localhost:8080/rest/v1/';

interface State {
  elevators: Array<ElevatorDto>;
  toFloor: string;
  isElevatorListLoading: boolean;
  isElevatorRequested: boolean;
}

export default class App extends React.Component<{}, State> {
  constructor(props: any) {
    super(props);
    this.state = {
      elevators: [],
      toFloor: '',
      isElevatorListLoading: true,
      isElevatorRequested: false
    };
  }
  render() {
    const props = {
      elevators: this.state.elevators as Array<ElevatorDto>,
      isLoading: this.state.isElevatorListLoading
    };
    return (
      <View style={styles.container}>
        <ElevatorList {...props} />
        <Separator />
        <Text>Please type the floor to which you would like to request elevator:</Text>
        <TextInput
          onChangeText={this.handleChange.bind(this)}
          value={this.state.toFloor}
          keyboardType='numeric'
          style={styles.TextInputStyle}
        />
        <Separator />
        {this.state.isElevatorRequested ?
          <React.Fragment>
            <Text>Elevator was requested</Text>
            <ActivityIndicator size="large" color="#0000ff" />
          </React.Fragment>
          :
          <Button
            onPress={this.onPressButton.bind(this)}
            title="Request elevator"
            color="#0000ff"
            accessibilityLabel="Request elevator"
          />
        }
      </View>
    );
  }

  async componentDidMount() {
    try {
      setInterval(async () => {
        const res = await fetch(API + 'elevators');
        const elevators = await res.json();

        this.setState({ elevators: elevators, isElevatorListLoading: false });
      }, 2000);
    } catch (e) {
      console.log(e);
    }
  }

  handleChange(event: string): void {
    this.setState({ toFloor: event });
  }

  onPressButton(): void {
    this.setState({ isElevatorRequested: true });
    fetch(API + 'request-elevator?to-floor=' + parseInt(this.state.toFloor))
      .then(response => response.json())
      .then(data => {
        console.log(data);
        this.setState({ isElevatorRequested: false });
      });
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
  TextInputStyle: {
    textAlign: 'center',
    height: 40,
    borderRadius: 10,
    borderWidth: 2,
    borderColor: '#009688',
    marginBottom: 10
  }
});
