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
package net.windwaker.pong.input;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.windwaker.pong.WindPongGame;
import org.lwjgl.input.*;

/**
 * Handles input from the Keyboard.
 */
public class InputManager {
	private final Map<Integer, KeyHandler> bindings = new HashMap<Integer, KeyHandler>();
	private final WindPongGame game;

	public InputManager(WindPongGame game) {
		this.game = game;
	}

	/**
	 * Returns all the key bindings.
	 *
	 * @return key bindings
	 */
	public Map<Integer, KeyHandler> getBindings() {
		return Collections.unmodifiableMap(bindings);
	}

	/**
	 * Binds the specified {@link KeyHandler} to the specified key.
	 *
	 * @param key to bind
	 * @param handler to bind to
	 */
	public void bind(int key, KeyHandler handler) {
		bindings.put(key, handler);
	}

	/**
	 * Polls the input manager for more key press events. Called every tick.
	 */
	public void poll() {
		while (Keyboard.next()) {
			int key = Keyboard.getEventKey();
			boolean pressed = Keyboard.getEventKeyState();
			char c = Keyboard.getEventCharacter();
			game.debug("Key " + Keyboard.getKeyName(key) + " was " + (pressed ? "pressed" : "released") + ".");
			KeyHandler listener = bindings.get(key);
			if (listener != null) {
				listener.handle(pressed, c);
			}
		}
	}
}
