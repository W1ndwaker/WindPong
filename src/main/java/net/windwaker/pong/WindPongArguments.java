package net.windwaker.pong;

/**
 * Arguments to start the application with.
 */
public class WindPongArguments {
	protected boolean debugMode = false;
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
			}
		}
		return wpa;
	}
}
