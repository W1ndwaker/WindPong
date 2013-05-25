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

import net.windwaker.pong.entity.Controller;
import net.windwaker.pong.entity.Entity;

import static org.lwjgl.opengl.GL11.*;

public abstract class RectangularController extends Controller {
	@Override
	public void render() {

		Entity owner = getOwner();
		float x = owner.getX();
		float y = owner.getY();
		float width = owner.getWidth();
		float height = owner.getHeight();

		glBegin(GL_QUADS); {
			glVertex2f(x, y); // top left
			glVertex2f(x + width, y); // top right
			glVertex2f(x + width, y - height); // bottom right
			glVertex2f(x, y - height); // bottom left
		}
		glEnd();
	}
}
