package net.windwaker.pong;

public class WindPongArguments {
	private boolean debugMode = false;
	private int width = 800, height = 500;

	private WindPongArguments() {
	}

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

	public boolean isDebugMode() {
		return debugMode;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
