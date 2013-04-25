package net.windwaker.pong;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.windwaker.pong.entity.Entity;
import net.windwaker.pong.entity.EntityManager;
import net.windwaker.pong.entity.controller.BallController;
import net.windwaker.pong.entity.controller.paddle.PaddleController;
import net.windwaker.pong.entity.controller.paddle.PlayerPaddleController;
import net.windwaker.pong.input.InputManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public class WindPongGame {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 450;
	private boolean debugMode;
	private long lastFrame;
	private final Logger logger = Logger.getLogger("WindPong");
	private final InputManager inputManager = new InputManager(this);
	private final EntityManager entityManager = new EntityManager(this);
	private PaddleController leftPaddle;
	private PaddleController rightPaddle;
	private BallController ball;
	private GameState state = GameState.INTRO;
	private int round = 1;

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public PaddleController getLeftPaddle() {
		return leftPaddle;
	}

	public PaddleController getRightPaddle() {
		return rightPaddle;
	}

	public void newRound() {
		state = GameState.PAUSED;
		resetEntityPositions();
		debug("Round: " + ++round);
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public void debug(String msg) {
		if (debugMode) {
			logger.info(msg);
		}
	}

	public Logger getLogger() {
		return logger;
	}

	public InputManager getInputManager() {
		return inputManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void start(boolean debugMode) {
		this.debugMode = debugMode;
		unpackNatives();
		createWindow();
		initGl();
		createBall();
		initBindings();
		startGameLoop();
	}

	private void renderMidLine() {
		if (state == GameState.INTRO) {
			return;
		}

		glLineStipple(10, (short) 0xAAAA);
		glEnable(GL_LINE_STIPPLE);
		glBegin(GL_LINES); {
			glVertex2f(0.5f, 1);
			glVertex2f(0.5f, 0);
		}
		glEnd();
	}

	private void initBindings() {
		inputManager.bind(Keyboard.KEY_RETURN, (pressed, c) -> {
			if (!pressed) {
				switch (state) {
					case INTRO:
						state = GameState.PAUSED;
						createEntities();
						resetEntityPositions();
						break;
					case PAUSED:
						state = GameState.PLAYING;
						break;
					case PLAYING:
						state = GameState.PAUSED;
						break;
				}
			}
		});
	}

	private void createBall() {
		entityManager.createEntity(ball = new BallController(), 0.5f, 0.5f);
	}

	private void createEntities() {
		entityManager.createEntity(rightPaddle = new PlayerPaddleController(), 0.975f, 0.6f);
		entityManager.createEntity(leftPaddle = new PlayerPaddleController(Keyboard.KEY_W, Keyboard.KEY_S), 0.01f, 0.6f);
	}

	private void resetEntityPositions() {
		Entity e1 = rightPaddle.getOwner();
		e1.setPosition(0.975f, 0.6f);
		e1.setVelocity(0, 0);

		Entity e2 = leftPaddle.getOwner();
		e2.setPosition(0.01f, 0.6f);
		e2.setVelocity(0, 0);

		Entity e3 = ball.getOwner();
		ball.newRound();
		e3.setPosition(0.5f, 0.5f);
		e3.setVelocity(0, 0);
	}

	private void update(float dt) {
		inputManager.poll();
		entityManager.update(dt);
		renderMidLine();
	}

	private float getDelta() {
		long time = getTime();
		float dt = (float)(time - lastFrame) / 1000f;
		lastFrame = time;
		return dt;
	}

	private long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	private void startGameLoop() {
		debug("Starting game loop...");

		// initialize timing
		lastFrame = getTime();

		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT);
			update(getDelta());
			Display.update();
			Display.sync(60);
		}

		debug("Game loop ending.");
		Display.destroy();
		System.exit(0);
	}

	private void initGl() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 1, 0, 1, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

	private void createWindow() {
		debug("Creating window...");
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(2);
		}
		debug("Window created.");
	}

	private void unpackNatives() {
		String os = System.getProperty("os.name");
		debug("Unpacking LWJGL natives for OS: '" + os + "'.");
		String[] files;
		String dir;

		// get the files required for the user's os
		if (SystemUtils.IS_OS_WINDOWS) {
			debug("Unpacking Windows natives...");
			files = new String[] {
					"jinput-dx8_64.dll", "jinput-dx8.dll", "jinput-raw_64.dll",
					"jinput-raw.dll", "jinput-wintab.dll", "lwjgl.dll",
					"lwjgl64.dll", "OpenAL32.dll", "OpenAL64.dll"
			};
			dir = "windows";
		} else if (SystemUtils.IS_OS_MAC) {
			debug("Unpacking Mac natives...");
			files = new String[] {"libjinput-osx.jnilib", "liblwjgl.jnilib", "openal.dylib"};
			dir = "mac";
		} else if (SystemUtils.IS_OS_LINUX) {
			debug("Unpacking Linux natives...");
			files = new String[]{ "liblwjgl.so", "liblwjgl64.so", "libopenal.so",
					"libopenal64.so", "libjinput-linux.so", "libjinput-linux64.so"
			};
			dir = "linux";
		} else {
			throw new IllegalStateException("Could not unpack LWJGL natives for OS: '" + os + "'.");
		}

		// copy the files into the 'natives/os' dir
		File f = new File("target/natives" + File.separator + dir);
		f.mkdirs();
		debug("Copying natives into: '" + f.getPath() + "'.");
		for (String file : files) {
			File newFile = new File(f, file);
			if (!newFile.exists()) {
				try {

					FileUtils.copyInputStreamToFile(WindPongGame.super.getClass().getResourceAsStream("/" + file), newFile);
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
		}

		String path = f.getAbsolutePath();
		System.setProperty("org.lwjgl.librarypath", path);
		System.setProperty("net.java.games.input.librarypath", path);
		debug("Finished unpacking LWJGL natives.");
	}

	public static void main(String[] args) {
		WindPongGame game = new WindPongGame();
		game.start(args.length > 0 && (args[0].equalsIgnoreCase("-d") || args[0].equals("--debug")));
	}
}
