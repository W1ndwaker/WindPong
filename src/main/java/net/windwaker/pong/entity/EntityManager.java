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

/**
 * Handles entities.
 */
public class EntityManager {
	private final Set<Entity> entities = new HashSet<Entity>();
	private int lastId = -1;
	private final WindPongGame game;

	public EntityManager(WindPongGame game) {
		this.game = game;
	}

	/**
	 * Checks the collisions of all entities and makes needed adjustments.
	 */
	public void checkCollisions() {
		for (Entity entity : entities) {
			entity.checkCollisions();
		}
	}

	/**
	 * Updates all the entities with the delta.
	 *
	 * @param dt secs since last tick
	 */
	public void update(float dt) {
		for (Entity entity : entities)
			entity.update(dt);
	}

	/**
	 * Renders all the entities.
	 */
	public void render() {
		for (Entity entity : entities) {
			entity.render();
		}
	}

	/**
	 * Returns all the entities.
	 *
	 * @return entities
	 */
	public Set<Entity> getEntities() {
		return Collections.unmodifiableSet(entities);
	}

	/**
	 * Creates a new entity with the specified controller at the specified
	 * location.
	 *
	 * @param controller to attach
	 * @param x coordinate
	 * @param y coordinate
	 * @return new entity
	 */
	public Entity createEntity(Controller controller, float x, float y) {
		Entity entity = new Entity(game, ++lastId, x, y);
		entity.attach(controller);
		entities.add(entity);
		entity.render();
		return entity;
	}

	/**
	 * Removes the specified entity.
	 *
	 * @param entity to remove
	 */
	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}
}
