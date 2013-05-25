package net.windwaker.pong.entity;

/**
 * Something that modifies behavior of an {@link Entity}.
 */
public abstract class Controller {
	protected Entity owner;

	/**
	 * Returns the {@link Entity} this controller is attached to.
	 *
	 * @return attached entity
	 */
	public Entity getOwner() {
		return owner;
	}

	/**
	 * Called when this controller is attached to an entity.
	 */
	public void attached() {
	}

	/**
	 * Called when this controller is detached from an entity.
	 */
	public void detached() {
	}

	/**
	 * Called when this controllers entity collides with another entity.
	 *
	 * @param collided entity
	 */
	public void collided(Entity collided) {
	}

	/**
	 * Does game logic.
	 *
	 * @param dt secs since last update
	 */
	public void update(float dt) {
	}

	/**
	 * Renders the entity.
	 */
	public abstract void render();
}
