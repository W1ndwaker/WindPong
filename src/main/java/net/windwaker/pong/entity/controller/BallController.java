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
package net.windwaker.pong.entity.controller;

import java.util.Random;

import net.windwaker.pong.GameState;
import net.windwaker.pong.WindPongGame;
import net.windwaker.pong.entity.Entity;
import net.windwaker.pong.entity.controller.paddle.PaddleController;
import net.windwaker.pong.resource.sound.Sound;

public class BallController extends RectangularController {
	public static final float INITIAL_SPEED = 0.006f;
	public static final float TOP = 0.98f;
	public static final float RATE_OF_SPEED = 0.03f;
	private int timesHit = 0;
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

	/**
	 * Restarts the ball's path and speed
	 */
	public void newRound() {
		timesHit = 0;
		updateVelocity(posOrNeg(random), posOrNeg(random));
		owner.getGame().getMusic().setPitch(1);
	}

	private void updateVelocity(int xf, int yf) {
		float appliedForce = (float) (INITIAL_SPEED * Math.pow(1 + RATE_OF_SPEED, timesHit));
		float vx = appliedForce * xf;
		float vy = random.nextFloat() * appliedForce * yf;
		owner.setVelocity(vx, vy);
	}

	private int posOrNeg(Random random) {
		return random.nextBoolean() ? -1 : 1;
	}

	private int opp(float f) {
		return f < 0 ? 1 : -1;
	}

	@Override
	public void attached() {
		WindPongGame game = getOwner().getGame();
		float width = 7 / (float) game.getWidth();
		float height = 7 / (float) game.getHeight();
		getOwner().setBounds(width, height);
	}

	@Override
	public void collided(Entity entity) {
		// invert velocity
		timesHit++;
		float vx = owner.getVelocityX(), vy = owner.getVelocityY();
		updateVelocity(opp(vx), opp(vy));
		WindPongGame game = owner.getGame();
		game.debug("Times hit: " + timesHit);
		game.playRandomBlip();

		if (timesHit != 0 && timesHit % 5 == 0) {
			Sound music = game.getMusic();
			music.setPitch(music.getPitch() + 0.1f);
			music.rewind();
			music.play();
		}
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
				owner.invertVelocity();
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
				owner.invertVelocity();
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

		// bounce of ceil
		if (y >= TOP || y <= height + 0.02f) {
			owner.setVelocityY(-vy);
			return;
		}

		// start it up
		if (vx == 0 && vy == 0) {
			updateVelocity(posOrNeg(random), posOrNeg(random));
		}
	}
}
