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
package net.windwaker.pong;

/**
 * Arguments to start the application with.
 */
public class WindPongArguments {
	protected boolean debugMode = false;
	protected boolean drawCurves = false;
	protected int width = 800, height = 500;

	private WindPongArguments() {
	}

	/**
	 * Parses the specified arguments.
	 *
	 * @param args to parse
	 * @return arguments
	 */
	public static WindPongArguments parse(String... args) {
		WindPongArguments wpa = new WindPongArguments();
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			switch (arg) {
				case "--debug":
				case "-d":
					wpa.debugMode = true;
					break;
				case "--width":
				case "-w":
					wpa.width = Integer.parseInt(args[i + 1]);
					break;
				case "--height":
				case "-h":
					wpa.height = Integer.parseInt(args[i + 1]);
					break;
				case "-dc":
				case "--drawcurves":
					wpa.drawCurves = true;
					break;
			}
		}
		return wpa;
	}
}
