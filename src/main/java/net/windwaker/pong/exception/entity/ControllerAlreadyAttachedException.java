package net.windwaker.pong.exception.entity;

public class ControllerAlreadyAttachedException extends RuntimeException {
	public ControllerAlreadyAttachedException(String message) {
		super(message);
	}
}
