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

import net.windwaker.pong.entity.Entity;
import org.lwjgl.input.*;

public class PlayerPaddleController extends PaddleController {
	private int upKey;
	private int downKey;

	public PlayerPaddleController() {
		this(Keyboard.KEY_UP, Keyboard.KEY_DOWN);
	}

	public PlayerPaddleController(int upKey, int downKey) {
		this.upKey = upKey;
		this.downKey = downKey;
	}

	public int getUpKey() {
		return upKey;
	}

	public void setUpKey(int upKey) {
		this.upKey = upKey;
	}

	public int getDownKey() {
		return downKey;
	}

	public void setDownKey(int downKey) {
		this.downKey = downKey;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		Entity owner = getOwner();
		float y = owner.getY();
		float vy = owner.getVelocityY();
		float height = owner.getHeight();

		// do input
		if (Keyboard.isKeyDown(upKey) && y < TOP) {
			owner.setVelocityY(Math.min(MAX_VELOCITY, vy + APPLIED_FORCE));
		} else if (Keyboard.isKeyDown(downKey) && y > height + 0.02f) {
			owner.setVelocityY(Math.max(-MAX_VELOCITY, vy - APPLIED_FORCE));
		}
	}
}
