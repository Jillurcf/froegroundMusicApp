// import React from 'react';
// import { Alert, Button, View } from 'react-native';
// import { NativeModules } from 'react-native';

// const { MusicControlModule } = NativeModules;
// const App = () => {
//   const playMusic = () => {
//     console.log("CLECK");
//     if (MusicControlModule) {
     
//     const url = 'https://d38nvwmjovqyq6.cloudfront.net/va90web25003/companions/Foundations%20of%20Rock/1.01.mp3'
//       MusicControlModule.play(url);
//     } else {
//       Alert.alert('Error', 'MusicControlModule is not available');
//     }
//   };

//   const pauseMusic = () => {
//     if (MusicControlModule) {
//       MusicControlModule.pause();
//     } else {
//       Alert.alert('Error', 'MusicControlModule is not available');
//     }
//   };

//   const stopMusic = () => {
//     if (MusicControlModule) {
//       MusicControlModule.stop();
//     } else {
//       Alert.alert('Error', 'MusicControlModule is not available');
//     }
//   };

//   return (
//     <View>
//       <Button title="Play" onPress={playMusic} />
//       <Button title="Pause" onPress={pauseMusic} />
//       <Button title="Stop" onPress={stopMusic} />
//     </View>
//   );
// };

// export default App;


import React from 'react';
import { Button, View, Alert } from 'react-native';
import { NativeModules } from 'react-native';

const { MusicControlModule } = NativeModules;

const App = () => {
  const startMusicService = () => {
    MusicControlModule.startForegroundService('Playing Music...')
    console.log("play");
      // .then(() => console.log('Foreground service started'))
      // .catch((error) => console.log('Error starting foreground service:', error));
  };

  const stopMusicService = () => {
    MusicControlModule.stopForegroundService()
      // .then(() => console.log('Foreground service stopped'))
      // .catch((error) => console.log('Error stopping foreground service:', error));
  };

  const playMusic = () => {
    // const url = 'https://example.com/path/to/audio.mp3'; // Replace with the correct URL
    const url = './audio.mp3';
    MusicControlModule.play(url)
      .then((message) => console.log(message))
      .catch((error) => Alert.alert('Error', 'Unable to play music: ' + error));
  };

  const pauseMusic = () => {
    MusicControlModule.pause()
      .then((message) => console.log(message))
      .catch((error) => Alert.alert('Error', 'Unable to pause music: ' + error));
  };

  const stopMusic = () => {
    MusicControlModule.stop()
      .then((message) => console.log(message))
      .catch((error) => Alert.alert('Error', 'Unable to stop music: ' + error));
  };

  return (
    <View>
      <Button title="Start Foreground Music Service" onPress={startMusicService} />
      <Button title="Stop Foreground Music Service" onPress={stopMusicService} />
      <Button title="Play Music" onPress={playMusic} />
      <Button title="Pause Music" onPress={pauseMusic} />
      <Button title="Stop Music" onPress={stopMusic} />
    </View>
  );
};

export default App;
