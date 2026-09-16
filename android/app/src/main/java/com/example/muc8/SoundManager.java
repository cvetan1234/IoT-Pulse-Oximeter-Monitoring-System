package com.example.muc8;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

/**
 * Manages the playback of heartbeat sound using SoundPool.
 */
public class SoundManager {

    private SoundPool soundPool; // SoundPool instance for playing sounds
    private int heartbeatSoundId; // Sound ID for the heartbeat sound
    private int streamId; // Stream ID for controlling playback

    /**
     * Constructs a SoundManager with the specified context.
     *
     * @param context The context to use
     */
    public SoundManager(Context context) {
        // Define audio attributes for SoundPool
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA) // Set usage for media playback
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION) // Set content type for sonification
                .build();

        // Create a SoundPool instance
        soundPool = new SoundPool.Builder()
                .setMaxStreams(1) // Set maximum number of simultaneous streams
                .setAudioAttributes(audioAttributes) // Set the audio attributes
                .build();

        // Load the heartbeat sound from resources into SoundPool
        heartbeatSoundId = soundPool.load(context, R.raw.heartbeat, 1);
    }

    /**
     * Plays the heartbeat sound with the given BPM value.
     *
     * @param bpmValue The BPM value used to calculate playback rate
     */
    public void playSound(float bpmValue) {
        // Calculate playback rate based on BPM value
        float playbackRate = bpmValue / 80.0f;

        // Check if heartbeat sound is loaded and play it indefinitely with the calculated playback rate
        if (heartbeatSoundId != 0) {
            streamId = soundPool.play(heartbeatSoundId, 1.0f, 1.0f, 1, -1, playbackRate);
        }
    }

    /**
     * Stops the currently playing sound.
     */
    public void stopSound() {
        // Stop the sound playback if there is a valid stream ID
        if (streamId != 0) {
            soundPool.stop(streamId);
        }
    }

    /**
     * Releases the SoundPool resources.
     */
    public void release() {
        // Release SoundPool resources when no longer needed
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}
