package net.windwaker.pong.resource.sound;

import java.io.InputStream;

import net.windwaker.pong.resource.ResourceLoader;
import org.lwjgl.util.WaveData;

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
		System.out.println("Sound id: " + bufferId);
		alBufferData(bufferId, data.format, data.data, data.samplerate);
		data.dispose();

		// bind the sound to a source
		// sources will be static so no need to encapsulate this anywhere else
		int sourceId = alGenSources();
		System.out.println("Source id: " + sourceId);
		Sound.checkErrors();
		Sound sound = new Sound(bufferId, sourceId);
		sound.init();
		sound.setPitch(1);
		sound.setGain(1);
		Sound.checkErrors();

		return sound;
	}
}
