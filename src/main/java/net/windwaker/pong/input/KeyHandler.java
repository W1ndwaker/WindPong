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

/**
 * A functional interface to handle a key being pressed or released.
 */
public interface KeyHandler {
	/**
	 * Called when the key this is bound to is pressed or released.
	 *
	 * @param pressed if the key is pressed
	 * @param c character the key represents
	 */
	public void handle(boolean pressed, char c);
}
