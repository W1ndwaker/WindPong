package net.windwaker.pong.entity.controller;

import java.util.Random;

import net.windwaker.pong.GameState;
import net.windwaker.pong.WindPongGame;
import net.windwaker.pong.entity.Entity;
import net.windwaker.pong.entity.controller.paddle.PaddleController;

public class BallController extends RectangularController {
	public static final float INITIAL_SPEED = 0.006f;
	public static final float TOP = 0.98f;
	public static final float RATE_OF_SPEED = 0.03f;
	private float appliedForce = INITIAL_SPEED;
	private int timesHit = 0;
	private boolean updateVelocity;
	private final Random random = new Random();

	/*
	 * The speed of the ball is modeled as follows:
	 *
	 * S = speed
	 * a = initial speed (0.006)
	 * r = rate of growth (20%)
	 * t = times hit by paddle
	 *
	 * S = a(1 + r)^t
	 */

	public void newRound() {
		appliedForce = INITIAL_SPEED;
		timesHit = 0;
	}

	public float getAppliedForce() {
		return appliedForce;
	}

	public void setAppliedForce(float appliedForce) {
		this.appliedForce = appliedForce;
	}

	@Override
	public void attached() {
		float width = 7 / (float) WindPongGame.WIDTH;
		float height = 7 / (float) WindPongGame.HEIGHT;
		getOwner().setBounds(width, height);
	}

	@Override
	public void collided(Entity entity) {
		owner.setVelocityX(-owner.getVelocityX());
		appliedForce = (float) (INITIAL_SPEED * Math.pow(1 + RATE_OF_SPEED, ++timesHit));
		updateVelocity = true;
		owner.getGame().debug("Times hit: " + timesHit + "\nForce: " + appliedForce);
	}

	@Override
	public void update(float dt) {
		Entity owner = getOwner();
		float x = owner.getX(), y = owner.getY();
		float vx = owner.getVelocityX(), vy = owner.getVelocityY();
		float height = owner.getHeight();

		WindPongGame game = owner.getGame();
		PaddleController rightPaddle = game.getRightPaddle();
		PaddleController leftPaddle = game.getLeftPaddle();
		GameState state = game.getState();
		boolean scored = false;
		if (x <= 0) {
			if (state == GameState.INTRO) {
				owner.setVelocityX(-vx);
				return;
			}
			// went off left side
			game.debug("Right paddle scored!");
			rightPaddle.addScore(1);
			game.newRound();
			scored = true;
		}

		if (x >= 1) {
			if (state == GameState.INTRO) {
				owner.setVelocityX(-vx);
				return;
			}
			// went of right side
			game.debug("Left paddle scored!");
			leftPaddle.addScore(1);
			game.newRound();
			scored = true;
		}

		if (scored) {
			game.debug("Score:");
			game.debug("\tLeft paddle: " + leftPaddle.getScore());
			game.debug("\tRight paddle: " + rightPaddle.getScore());
			return;
		}

		if (y >= TOP || y <= height + 0.02f) {
			owner.setVelocityY(-vy);
			return;
		}

		if ((vx == 0 && vy == 0) || updateVelocity) {
			float vxx = appliedForce * (random.nextBoolean() ? -1 : 1);
			float vyy = random.nextFloat() * appliedForce * (random.nextBoolean() ? -1 : 1);
			owner.setVelocity(vxx, vyy);
			updateVelocity = false;
		}
	}
}
