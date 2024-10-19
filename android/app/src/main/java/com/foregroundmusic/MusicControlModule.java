package com.foregroundmusic;

import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class MusicControlModule extends ReactContextBaseJavaModule {
    private static MediaPlayer mediaPlayer;
    private static String currentUrl;

    public MusicControlModule(ReactApplicationContext context) {
        super(context);
    }

    @Override
    public String getName() {
        return "MusicControlModule"; // This will be used in JavaScript
    }

    // Method to play music
    @ReactMethod
    public void play(final String url, final Promise promise) {
        try {
            // If a different URL is passed, stop and release the old media player
            if (mediaPlayer != null && !url.equals(currentUrl)) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            // Initialize the MediaPlayer if not already
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(url);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepareAsync();  // Prepare asynchronously to avoid blocking UI
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();  // Start playing when prepared
                        promise.resolve("Playing music from URL: " + url);
                    }
                });
                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        promise.reject("Playback error", "Error occurred during playback: " + what);
                        return true;
                    }
                });
                currentUrl = url;
                Log.d("MusicControlModule", "Playing music from URL: " + url);
            } else {
                mediaPlayer.start();  // Resume playback
                promise.resolve("Resumed playing music");
            }
        } catch (Exception e) {
            Log.e("MusicControlModule", "Error playing music", e);
            promise.reject("Error", "Error playing music: " + e.getMessage());
        }
    }

    // Method to pause music
    @ReactMethod
    public void pause(final Promise promise) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            promise.resolve("Music paused");
        } else {
            promise.reject("Error", "No music is playing to pause");
        }
    }

    // Method to stop music
    @ReactMethod
    public void stop(final Promise promise) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            currentUrl = null;
            promise.resolve("Music stopped");
        } else {
            promise.reject("Error", "No music is playing to stop");
        }
    }

    // Method to start the foreground service
    @ReactMethod
    public void startForegroundService(String input) {
        Intent serviceIntent = new Intent(getReactApplicationContext(), MusicService.class);
        serviceIntent.putExtra("inputExtra", input);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            getReactApplicationContext().startForegroundService(serviceIntent); // For Android O and above
        } else {
            getReactApplicationContext().startService(serviceIntent); // For lower versions
        }
    }

    // Method to stop the foreground service
    @ReactMethod
    public void stopForegroundService() {
        Intent serviceIntent = new Intent(getReactApplicationContext(), MusicService.class);
        getReactApplicationContext().stopService(serviceIntent);
    }
}
