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
			if (arg.equalsIgnoreCase("--debug") || arg.equalsIgnoreCase("-d")) {
				wpa.debugMode = true;
			} else if (arg.equalsIgnoreCase("--width") || arg.equalsIgnoreCase("-w")) {
				wpa.width = Integer.parseInt(args[i + 1]);
			} else if (arg.equalsIgnoreCase("--height") || arg.equalsIgnoreCase("-h")) {
				wpa.height = Integer.parseInt(args[i + 1]);
			} else if (arg.equalsIgnoreCase("--drawcurves") || arg.equalsIgnoreCase("-dc")) {
				wpa.drawCurves = true;
			}
		}
		return wpa;
	}
}
