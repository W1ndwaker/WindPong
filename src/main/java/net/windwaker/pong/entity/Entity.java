package net.windwaker.pong.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.windwaker.pong.WindPongGame;

public class Entity {
	private final int id;
	private final Set<Controller> controllers = new HashSet<>();
	private float x, y;
	private float width, height;
	private float velocityX, velocityY;
	private WindPongGame game;

	protected Entity(WindPongGame game, int id, float x, float y) {
		this.game = game;
		this.id = id;
		this.x = x;
		this.y = y;
	}

	public WindPongGame getGame() {
		return game;
	}

	public int getId() {
		return id;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getX() {
		return x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getY() {
		return y;
	}

	public void setBounds(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getWidth() {
		return width;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getHeight() {
		return height;
	}

	public void setVelocity(float velocityX, float velocityY) {
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	}

	public void setVelocityX(float velocityX) {
		this.velocityX = velocityX;
	}

	public float getVelocityX() {
		return velocityX;
	}

	public void setVelocityY(float velocityY) {
		this.velocityY = velocityY;
	}

	public float getVelocityY() {
		return velocityY;
	}

	public boolean intersects(Entity other) {
		return other.x >= x && other.x <= x + width
				&& other.y <= y && other.y >= y - height;
	}

	public Set<Controller> getControllers() {
		return Collections.unmodifiableSet(controllers);
	}

	public void attach(Controller controller) {
		controllers.add(controller);
		controller.owner = this; // give the controller a reference to the entity
		controller.attached(); // notify the controller of the attachment
	}

	public void detach(Controller controller) {
		controllers.remove(controller);
		controller.detached(); // notify the controller of the detachment
		controller.owner = null; // remove the reference of this entity
	}

	public void render() {
		for (Controller controller : controllers) {
			controller.render();
		}
	}

	public void update(float dt) {

		// velocity updates
		x += velocityX;
		y += velocityY;

		for (Controller controller : controllers) {
			controller.update(dt);
		}
	}

	public void checkCollisions() {
		for (Entity entity : game.getEntityManager().getEntities()) {
			if (entity.equals(this) || !entity.intersects(this)) {
				continue;
			}
			for (Controller controller : controllers) {
				controller.collided(entity);
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Entity && ((Entity) obj).id == id;
	}
}
