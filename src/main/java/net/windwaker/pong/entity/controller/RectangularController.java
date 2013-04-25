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
