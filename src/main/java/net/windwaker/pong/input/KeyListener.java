package net.windwaker.pong.input;

@FunctionalInterface
public interface KeyListener {
	public void handle(boolean pressed, char c);
}
