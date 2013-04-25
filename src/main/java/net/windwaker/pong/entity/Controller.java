package net.windwaker.pong.entity;

public abstract class Controller {
	protected Entity owner;

	public Entity getOwner() {
		return owner;
	}

	public void attached() {
	}

	public void detached() {
	}

	public void collided(Entity collided) {
	}

	public abstract void render();

	public void update(float dt) {
	}
}
