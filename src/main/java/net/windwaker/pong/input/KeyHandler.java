package net.windwaker.pong.input;

/**
 * A functional interface to handle a key being pressed or released.
 */
@FunctionalInterface
public interface KeyHandler {
	/**
	 * Called when the key this is bound to is pressed or released.
	 *
	 * @param pressed if the key is pressed
	 * @param c character the key represents
	 */
	public void handle(boolean pressed, char c);
}
