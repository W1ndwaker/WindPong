package net.windwaker.pong;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;

import net.windwaker.pong.entity.Entity;
import net.windwaker.pong.entity.EntityManager;
import net.windwaker.pong.entity.controller.BallController;
import net.windwaker.pong.entity.controller.paddle.PaddleController;
import net.windwaker.pong.entity.controller.paddle.PlayerPaddleController;
import net.windwaker.pong.input.InputManager;
import net.windwaker.pong.resource.ResourceManager;
import net.windwaker.pong.resource.sound.Sound;
import net.windwaker.pong.resource.sound.SoundLoader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * A game of pong with pretty music.
 */
public class WindPongGame {
	private int width, height;
	private boolean debugMode;
	private long lastFrame;
	private final Logger logger = Logger.getLogger("WindPong");
	private final InputManager inputManager = new InputManager(this);
	private final EntityManager entityManager = new EntityManager(this);
	private final ResourceManager resourceManager = new ResourceManager();
	private PaddleController leftPaddle;
	private PaddleController rightPaddle;
	private BallController ball;
	private GameState state = GameState.INTRO;
	private int round = 1;
	private Sound music;
	private Sound[] blips = new Sound[2];

	/**
	 * Returns what state the game is in.
	 *
	 * @return game state
	 */
	public GameState getState() {
		return state;
	}

	/**
	 * Sets the state of the game.
	 *
	 * @param state to set
	 */
	public void setState(GameState state) {
		boolean introTransition = false;
		if (this.state == GameState.INTRO) {
			createEntities();
			resetEntityPositions();
			introTransition = true;
		}

		switch (state) {
			case PAUSED:
				debug("Paused the game.");
				if (music.isPlaying() && !introTransition) {
					debug("Pausing music.");
					music.pause();
				}
				break;
			case PLAYING:
				debug("Resume playing.");
				if (!music.isPlaying()) {
					debug("Playing music.");
					music.play();
				}
				break;
		}
		this.state = state;
	}

	/**
	 * Returns WindPong's soundtrack.
	 *
	 * @return music
	 */
	public Sound getMusic() {
		return music;
	}

	/**
	 * Returns the paddle on the left side of the screen.
	 *
	 * @return paddle on left side of screen
	 */
	public PaddleController getLeftPaddle() {
		return leftPaddle;
	}

	/**
	 * Returns the paddle on the right side of the screen.
	 *
	 * @return right side paddle
	 */
	public PaddleController getRightPaddle() {
		return rightPaddle;
	}

	/**
	 * Starts a new round.
	 */
	public void newRound() {
		state = GameState.PAUSED;
		resetEntityPositions();
		music.rewind();
		music.play();
		debug("Round: " + ++round);
	}

	/**
	 * Returns the width of the screen.
	 *
	 * @return width of screen
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the screen.
	 *
	 * @return screen height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns true if debug info should be printed.
	 *
	 * @return true if prints debug info
	 */
	public boolean isDebugMode() {
		return debugMode;
	}

	/**
	 * Prints debug info if the game is in debug mode.
	 *
	 * @param msg to send
	 */
	public void debug(String msg) {
		if (debugMode) {
			logger.info(msg);
		}
	}

	/**
	 * Returns the game logger.
	 *
	 * @return logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Returns the input manager of the game.
	 *
	 * @return input manager
	 */
	public InputManager getInputManager() {
		return inputManager;
	}

	/**
	 * Returns the entity manager of the game.
	 *
	 * @return entity manager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * Returns the resource manager of the game.
	 *
	 * @return resource manager
	 */
	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	/**
	 * Plays a random 'blip'. Used for when the ball hits a paddle.
	 */
	public void playRandomBlip() {
		Random random = new Random();
		blips[random.nextInt(2)].play();
	}

	/**
	 * Starts the game with the specified arguments.
	 *
	 * @param args to start with
	 */
	public void start(WindPongArguments args) {

		debugMode = args.debugMode;
		width = args.width;
		height = args.height;

		unpackNatives();
		createWindow();
		initGl();
		initAl();
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
						setState(GameState.PAUSED);
						break;
					case PAUSED:
						setState(GameState.PLAYING);
						break;
					case PLAYING:
						setState(GameState.PAUSED);
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
		// get input
		inputManager.poll();

		// do logic
		if (state != GameState.PAUSED) {
			entityManager.checkCollisions();
			entityManager.update(dt);
		}

		// render
		entityManager.render();
		renderMidLine();
	}

	private float getDelta() {
		long time = getTime();
		float dt = (float) (time - lastFrame) / 1000f;
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

	private void initAl() {
		Sound.initAl();
		resourceManager.registerLoader(new SoundLoader());
		music = (Sound) resourceManager.getResource("sound://sounds/music.wav");
		music.setLooping(true);
		music.play();
		blips[0] = (Sound) resourceManager.getResource("sound://sounds/blip_1.wav");
		blips[1] = (Sound) resourceManager.getResource("sound://sounds/blip_2.wav");
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
			Display.setDisplayMode(new DisplayMode(width, height));
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
			files = new String[]{
					"jinput-dx8_64.dll", "jinput-dx8.dll", "jinput-raw_64.dll",
					"jinput-raw.dll", "jinput-wintab.dll", "lwjgl.dll",
					"lwjgl64.dll", "OpenAL32.dll", "OpenAL64.dll"
			};
			dir = "windows";
		} else if (SystemUtils.IS_OS_MAC) {
			debug("Unpacking Mac natives...");
			files = new String[]{"libjinput-osx.jnilib", "liblwjgl.jnilib", "openal.dylib"};
			dir = "mac";
		} else if (SystemUtils.IS_OS_LINUX) {
			debug("Unpacking Linux natives...");
			files = new String[]{"liblwjgl.so", "liblwjgl64.so", "libopenal.so",
					"libopenal64.so", "libjinput-linux.so", "libjinput-linux64.so"
			};
			dir = "linux";
		} else {
			throw new IllegalStateException("Could not unpack LWJGL natives for OS: '" + os + "'.");
		}

		// copy the files into the 'natives/os' dir
		File f = new File("natives" + File.separator + dir);
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
		WindPongArguments pongArgs = WindPongArguments.parse(args);
		game.start(pongArgs);
	}
}
