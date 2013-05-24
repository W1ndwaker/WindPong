package net.windwaker.pong.resource.sound;

import java.nio.FloatBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.OpenALException;

import static org.lwjgl.BufferUtils.createFloatBuffer;
import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_FALSE;
import static org.lwjgl.openal.AL10.AL_GAIN;
import static org.lwjgl.openal.AL10.AL_LOOPING;
import static org.lwjgl.openal.AL10.AL_NO_ERROR;
import static org.lwjgl.openal.AL10.AL_ORIENTATION;
import static org.lwjgl.openal.AL10.AL_PITCH;
import static org.lwjgl.openal.AL10.AL_PLAYING;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_SOURCE_STATE;
import static org.lwjgl.openal.AL10.AL_TRUE;
import static org.lwjgl.openal.AL10.AL_VELOCITY;
import static org.lwjgl.openal.AL10.alGetError;
import static org.lwjgl.openal.AL10.alGetSourcef;
import static org.lwjgl.openal.AL10.alGetSourcei;
import static org.lwjgl.openal.AL10.alListener;
import static org.lwjgl.openal.AL10.alSource;
import static org.lwjgl.openal.AL10.alSourcePause;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourceRewind;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alSourcef;
import static org.lwjgl.openal.AL10.alSourcei;

/**
 * Represents a sound that can be played on the client.
 */
public class Sound {
	private final int bufferId, sourceId;

	protected Sound(int bufferId, int sourceId) {
		this.bufferId = bufferId;
		this.sourceId = sourceId;
	}

	public void init() {
		setInteger(AL_BUFFER, bufferId);
		alSource(sourceId, AL_POSITION, getEmptyBufferf3());
		alSource(sourceId, AL_VELOCITY, getEmptyBufferf3());
	}

	public boolean isPlaying() {
		return getInteger(AL_SOURCE_STATE) == AL_PLAYING;
	}

	/**
	 * Plays the sound.
	 */
	public void play() {
		alSourcePlay(sourceId);
	}

	/**
	 * Pauses the sound.
	 */
	public void pause() {
		alSourcePause(sourceId);
	}

	/**
	 * Stops the sound.
	 */
	public void stop() {
		alSourceStop(sourceId);
	}

	/**
	 * Rewinds the sound to the beginning.
	 */
	public void rewind() {
		alSourceRewind(sourceId);
	}

	public float getPitch() {
		return getFloat(AL_PITCH);
	}

	public void setPitch(float pitch) {
		setFloat(AL_PITCH, pitch);
	}

	public float getGain() {
		return getFloat(AL_GAIN);
	}

	public void setGain(float gain) {
		setFloat(AL_GAIN, gain);
	}

	public boolean isLooping() {
		return getInteger(AL_LOOPING) == AL_TRUE;
	}

	public void setLooping(boolean looping) {
		if (looping == isLooping()) {
			return;
		}
		setInteger(AL_LOOPING, looping ? AL_TRUE : AL_FALSE);
	}

	public float getPlaybackPosition() {
		return getFloat(AL11.AL_SEC_OFFSET);
	}

	public void setPlaybackPosition(float secs) {
		setFloat(AL11.AL_SEC_OFFSET, secs);
	}

	private float getFloat(int p) {
		return alGetSourcef(sourceId, p);
	}

	private void setFloat(int p, float v) {
		alSourcef(sourceId, p, v);
	}

	private int getInteger(int p) {
		return alGetSourcei(sourceId, p);
	}

	private void setInteger(int p, int v) {
		alSourcei(sourceId, p, v);
	}

	private static FloatBuffer getEmptyBufferf3() {
		FloatBuffer buff = createFloatBuffer(3).put(new float[] {0, 0, 0});
		buff.flip();
		return buff;
	}

	private static FloatBuffer getEmptyBufferf6() {
		FloatBuffer buff = createFloatBuffer(6).put(new float[] {0, 0, -1, 0, 1, 0});
		buff.flip();
		return buff;
	}

	/**
	 * Initializes OpenAL.
	 */
	public static void initAl() {
		try {
			AL.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(4);
			return;
		}
		checkErrors();

		// one static listener for pong
		// sounds are static as well
		alListener(AL_POSITION, getEmptyBufferf3());
		alListener(AL_VELOCITY, getEmptyBufferf3());
		alListener(AL_ORIENTATION, getEmptyBufferf6());
	}

	/**
	 * Makes sure nothing has gone wrong in OpenAL.
	 */
	public static void checkErrors() {
		int error = alGetError();
		if (error != AL_NO_ERROR) {
			throw new OpenALException(error);
		}
	}
}
