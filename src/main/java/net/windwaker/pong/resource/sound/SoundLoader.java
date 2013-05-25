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

import java.io.InputStream;

import net.windwaker.pong.resource.ResourceLoader;
import org.lwjgl.util.*;

import static org.lwjgl.openal.AL10.*;

/**
 * Loads WAV files.
 */
public class SoundLoader extends ResourceLoader<Sound> {
	public SoundLoader() {
		super("sound");
	}

	@Override
	public Sound load(InputStream in) {
		// generate the buffer
		int bufferId = alGenBuffers();
		Sound.checkErrors();

		// create the metadata and bind it to the buffer
		WaveData data = WaveData.create(in);
		alBufferData(bufferId, data.format, data.data, data.samplerate);
		data.dispose();

		// bind the sound to a source
		// sources will be static so no need to encapsulate this anywhere else
		int sourceId = alGenSources();
		Sound.checkErrors();
		Sound sound = new Sound(bufferId, sourceId);
		sound.init();
		sound.setPitch(1);
		sound.setGain(1);
		Sound.checkErrors();

		return sound;
	}
}
