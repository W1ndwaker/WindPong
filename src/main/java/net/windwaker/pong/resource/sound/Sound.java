/*
 * (C) Copyright 2013 Walker Crouse.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 3 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.txt
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package net.windwaker.pong.resource.sound;

import java.nio.FloatBuffer;

import org.lwjgl.*;
import org.lwjgl.openal.*;

import static org.lwjgl.BufferUtils.*;
import static org.lwjgl.openal.AL10.*;

/**
 * Represents a sound that can be played on the client.
 */
public class Sound {
	private final int bufferId, sourceId;

	protected Sound(int bufferId, int sourceId) {
		this.bufferId = bufferId;
		this.sourceId = sourceId;
	}

	/**
	 * Initializes the sound and makes it able to play.
	 */
	public void init() {
		setInteger(AL_BUFFER, bufferId);
		alSource(sourceId, AL_POSITION, getEmptyBuffer3f());
		alSource(sourceId, AL_VELOCITY, getEmptyBuffer3f());
	}

	/**
	 * Returns true if the sound is playing.
	 *
	 * @return true if playing
	 */
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

	/**
	 * Returns the pitch of the sound. One is equivalent to one-hundred percent
	 * of the original pitch.
	 *
	 * @return sound pitch
	 */
	public float getPitch() {
		return getFloat(AL_PITCH);
	}

	/**
	 * Sets the pitch of the sound. One is equivalent to one-hundred percent of
	 * the original pitch.
	 *
	 * @param pitch to set
	 */
	public void setPitch(float pitch) {
		setFloat(AL_PITCH, pitch);
	}

	/**
	 * Returns the gain of the sound. One is equivalent to one-hundred percent of
	 * the original gain.
	 *
	 * @return gain
	 */
	public float getGain() {
		return getFloat(AL_GAIN);
	}

	/**
	 * Sets the gain of the sound. One is equivalent to one-hundred percent of
	 * the original gain.
	 *
	 * @param gain to set
	 */
	public void setGain(float gain) {
		setFloat(AL_GAIN, gain);
	}

	/**
	 * Returns true if this sound should start over when it is completed.
	 *
	 * @return true if loops
	 */
	public boolean isLooping() {
		return getInteger(AL_LOOPING) == AL_TRUE;
	}

	/**
	 * Sets if this sound should start over when it is completed.
	 *
	 * @param looping true if the sound should loop
	 */
	public void setLooping(boolean looping) {
		if (looping == isLooping()) {
			return;
		}
		setInteger(AL_LOOPING, looping ? AL_TRUE : AL_FALSE);
	}

	/**
	 * Returns the position for this sound to rewind to in seconds.
	 *
	 * @return position in seconds
	 */
	public float getPlaybackPosition() {
		return getFloat(AL11.AL_SEC_OFFSET);
	}

	/**
	 * Sets the position for this sound to rewind to in seconds.
	 *
	 * @param secs to start at
	 */
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

	private static FloatBuffer getEmptyBuffer3f() {
		FloatBuffer buff = createFloatBuffer(3).put(new float[]{0, 0, 0});
		buff.flip();
		return buff;
	}

	private static FloatBuffer getEmptyBuffer6f() {
		FloatBuffer buff = createFloatBuffer(6).put(new float[]{0, 0, -1, 0, 1, 0});
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
		alListener(AL_POSITION, getEmptyBuffer3f());
		alListener(AL_VELOCITY, getEmptyBuffer3f());
		alListener(AL_ORIENTATION, getEmptyBuffer6f());
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
