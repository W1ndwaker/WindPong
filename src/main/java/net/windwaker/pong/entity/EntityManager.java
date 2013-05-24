package net.windwaker.pong.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.windwaker.pong.WindPongGame;

public class EntityManager {
	private final Set<Entity> entities = new HashSet<>();
	private int lastId = -1;
	private final WindPongGame game;

	public EntityManager(WindPongGame game) {
		this.game = game;
	}

	public void checkCollisions() {
		for (Entity entity: entities) {
			entity.checkCollisions();
		}
	}

	public void update(float dt) {
		for (Entity entity : entities) {
			entity.update(dt);
		}
	}

	public void render() {
		for (Entity entity : entities) {
			entity.render();
		}
	}

	public Set<Entity> getEntities() {
		return Collections.unmodifiableSet(entities);
	}

	public Entity createEntity(Controller controller, float x, float y) {
		Entity entity = new Entity(game, ++lastId, x, y);
		entity.attach(controller);
		entities.add(entity);
		entity.render();
		return entity;
	}

	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}
}
