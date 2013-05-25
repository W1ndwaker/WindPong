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
package net.windwaker.pong.util.math;

import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class Curve {
	private final float degree;
	private final float vertexX, vertexY;
	private final float slope;

	public Curve(float slope, float degree, float vertexX, float vertexY) {
		this.slope = slope;
		this.degree = degree;
		this.vertexX = vertexX;
		this.vertexY = vertexY;
	}

	public float getSlope() {
		return slope;
	}

	public float getDegree() {
		return degree;
	}

	public float getVertexX() {
		return vertexX;
	}

	public float getVertexY() {
		return vertexY;
	}

	public float getY(float x) {
		return (float) (slope * Math.pow(x - vertexX, degree) + vertexY);
	}

	public void draw() {
		glBegin(GL_POINTS); {
			for (float i = 0; i < 1; i += 0.01f) {
				glVertex2f(i, getY(i));
			}
		}
		glEnd();
	}
}
