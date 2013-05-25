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
package net.windwaker.pong.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.windwaker.pong.WindPongGame;
import net.windwaker.pong.exception.entity.ControllerAlreadyAttachedException;
import net.windwaker.pong.exception.entity.ControllerNotFoundException;
import net.windwaker.pong.exception.entity.ControllerParentMismatchException;

/**
 * An entity within the game.
 */
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

	/**
	 * Returns the game.
	 *
	 * @return the game
	 */
	public WindPongGame getGame() {
		return game;
	}

	/**
	 * Returns the unique identifier of the entity.
	 *
	 * @return unique id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the position of the entity.
	 *
	 * @param x 0-1 on the screen
	 * @param y 0-1 on the screen
	 */
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Sets the x coordinate.
	 *
	 * @param x 0-1 on the screen
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Returns the x coordinate on the screen.
	 *
	 * @return x coordinate
	 */
	public float getX() {
		return x;
	}

	/**
	 * Sets the y coordinate on the screen.
	 *
	 * @param y coordinate
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Returns the y coordinate on the screen.
	 *
	 * @return y coordinate
	 */
	public float getY() {
		return y;
	}

	/**
	 * Sets the entity's bounds; used for collisions.
	 *
	 * @param width of entity
	 * @param height of entity
	 */
	public void setBounds(float width, float height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Sets the width of the entity.
	 *
	 * @param width of entity
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * Returns the width of the entity.
	 *
	 * @return entity width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Sets the height of the entity.
	 *
	 * @param height of entity
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * Returns the height of the entity.
	 *
	 * @return entity height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * Inverts the current velocity of this entity.
	 */
	public void invertVelocity() {
		setVelocity(-velocityX, -velocityY);
	}

	/**
	 * Sets the velocity of the entity.
	 *
	 * @param velocityX amount to move along x every tick
	 * @param velocityY amount to move along y every tick
	 */
	public void setVelocity(float velocityX, float velocityY) {
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	}

	/**
	 * Sets the velocity of the entity.
	 *
	 * @param velocityX amount to move along x every tick
	 */
	public void setVelocityX(float velocityX) {
		this.velocityX = velocityX;
	}

	/**
	 * Returns the velocity along x.
	 *
	 * @return velocity along x
	 */
	public float getVelocityX() {
		return velocityX;
	}

	/**
	 * Sets the velocity along y.
	 *
	 * @param velocityY velocity along y
	 */
	public void setVelocityY(float velocityY) {
		this.velocityY = velocityY;
	}

	/**
	 * Returns the velocity along y.
	 *
	 * @return velocity along y
	 */
	public float getVelocityY() {
		return velocityY;
	}

	/**
	 * Returns true if this entity intersects with the specified entity.
	 *
	 * @param other
	 * @return
	 */
	public boolean intersects(Entity other) {
		return other.x >= x && other.x <= x + width
				&& other.y <= y && other.y >= y - height;
	}

	/**
	 * Returns a set of all this entity's controllers.
	 *
	 * @return entity controllers
	 */
	public Set<Controller> getControllers() {
		return Collections.unmodifiableSet(controllers);
	}

	/**
	 * Attaches a controller to this entity.
	 *
	 * @param controller to attach
	 */
	public void attach(Controller controller) {
		if (controller == null)
			throw new IllegalArgumentException("Specified controller cannot be null.");
		if (controller.getOwner() != null)
			throw new ControllerAlreadyAttachedException("This controller is already attached to an entity.");

		controllers.add(controller);
		controller.owner = this; // give the controller a reference to the entity
		controller.attached(); // notify the controller of the attachment
	}

	/**
	 * Detaches a controller from this entity.
	 *
	 * @param controller to detach
	 */
	public void detach(Controller controller) {
		if (controller == null)
			throw new IllegalArgumentException("Specified controller cannot be null.");
		if (controller.getOwner() == null || !controller.getOwner().equals(this))
			throw new ControllerParentMismatchException("Specified controller is not attached to this entity.");
		if (!controllers.remove(controller))
			throw new ControllerNotFoundException("This controller is not attached to this entity.");

		controller.detached(); // notify the controller of the detachment
		controller.owner = null; // remove the reference of this entity
	}

	/**
	 * Renders the controllers.
	 */
	public void render() {
		for (Controller controller : controllers) {
			controller.render();
		}
	}

	/**
	 * Updates the position and the controllers.
	 *
	 * @param dt secs since last tick
	 */
	public void update(float dt) {

		// velocity updates
		x += velocityX;
		y += velocityY;

		for (Controller controller : controllers) {
			controller.update(dt);
		}
	}

	/**
	 * Checks if this entity is intersecting with any other entities.
	 */
	public void checkCollisions() {
		for (Entity entity : game.getEntityManager().getEntities()) {
			if (!entity.equals(this) && entity.intersects(this)) {
				for (Controller controller : controllers) {
					controller.collided(entity);
				}
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Entity && ((Entity) obj).id == id;
	}

	@Override
	public int hashCode() {
		return id;
	}
}
