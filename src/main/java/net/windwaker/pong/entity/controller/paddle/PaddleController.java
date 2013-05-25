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
		WindPongGame game = getOwner().getGame();
		float width = 10 / (float) game.getWidth();
		float height = 100 / (float) game.getHeight();
		getOwner().setBounds(width, height);
	}
}
