package net.windwaker.pong.exception.entity;

public class ControllerNotFoundException extends RuntimeException {
	public ControllerNotFoundException(String message) {
		super(message);
	}
}
