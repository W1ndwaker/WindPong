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
