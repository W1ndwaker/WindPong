package net.windwaker.pong.entity.controller.paddle;

import net.windwaker.pong.WindPongGame;
import net.windwaker.pong.entity.Entity;
import net.windwaker.pong.entity.controller.RectangularController;

public abstract class PaddleController extends RectangularController {
	public static final float TOP = 0.98f;
	private int score = 0;

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void addScore(int amount) {
		score += amount;
	}

	@Override
	public void update(float dt) {
		Entity owner = getOwner();
		float y = owner.getY();
		float vy = owner.getVelocityY();
		float height = owner.getHeight();

		// check if we hit the wall, conserve momentum
		if (y >= TOP || y <= height) {
			owner.setVelocityY(-vy);
			return;
		}
	}

	@Override
	public void attached() {
		float width = 10 / (float) WindPongGame.WIDTH;
		float height = 100 / (float) WindPongGame.HEIGHT;
		getOwner().setBounds(width, height);
	}
}
