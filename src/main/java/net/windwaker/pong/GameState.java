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
package net.windwaker.pong;

/**
 * Represents a certain state that the game is in.
 */
public enum GameState {
	/**
	 * The game is in this state prior to hitting ENTER for the first time
	 * after starting the game; after which, the game will then be paused.
	 */
	INTRO,
	/**
	 * The game keeps entities rendered but does not update them.
	 */
	PAUSED,
	/**
	 * The game behaves as expected.
	 */
	PLAYING
}
