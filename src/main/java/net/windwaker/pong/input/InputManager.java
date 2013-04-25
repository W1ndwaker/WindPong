package net.windwaker.pong.input;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.windwaker.pong.WindPongGame;
import org.lwjgl.input.Keyboard;

public class InputManager {
	private final Map<Integer, KeyListener> bindings = new HashMap<Integer, KeyListener>();
	private final WindPongGame game;

	public InputManager(WindPongGame game) {
		this.game = game;
	}

	public Map<Integer, KeyListener> getBindings() {
		return Collections.unmodifiableMap(bindings);
	}

	public void bind(int key, KeyListener listener) {
		bindings.put(key, listener);
	}

	public void poll() {
		while (Keyboard.next()) {
			int key = Keyboard.getEventKey();
			boolean pressed = Keyboard.getEventKeyState();
			char c = Keyboard.getEventCharacter();
			game.debug("Key " + Keyboard.getKeyName(key) + " was " + (pressed ? "pressed" : "released") + ".");
			KeyListener listener = bindings.get(key);
			if (listener != null) {
				listener.handle(pressed, c);
			}
		}
	}
}
