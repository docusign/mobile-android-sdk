import { NativeModules } from 'react-native';
import {name as appName} from './app.json';

module.exports = NativeModules.DocuSignSDKBridge;

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  Pressable
} from 'react-native';

class ReactNativeSampleApp extends Component {
  render() {
    NativeModules.DocuSignSDKBridge.init();
    return (
      <View style={styles.centered}>
        <Pressable style={styles.button} onPress={ () => NativeModules.DocuSignSDKBridge.login() }>
              <Text style={styles.text}>Login</Text>
        </Pressable>
        <View style={{flexDirection:'column', gap: 50 }}>
          <View />
          <View />
        </View>
        <Pressable style={styles.button} onPress={ () => NativeModules.DocuSignSDKBridge.signOnline() }>
                      <Text style={styles.text}>Sign Online</Text>
        </Pressable>

      </View>
    );
  }
}

const styles = StyleSheet.create({
  centered: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  },
  button: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 12,
    paddingHorizontal: 32,
    borderRadius: 4,
    elevation: 3,
    backgroundColor: 'black',
  },
  text: {
    fontSize: 16,
    lineHeight: 21,
    fontWeight: 'bold',
    letterSpacing: 0.25,
    color: 'white',
  },
});

AppRegistry.registerComponent('ReactNativeSampleApp', () => ReactNativeSampleApp);
