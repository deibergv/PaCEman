
/* 
 * Deiber Granados Vega /// 2017159397
 */

import java.awt.*;
import javax.imageio.*;
import javax.swing.JPanel;
import java.lang.Math;
import java.net.Socket;
import java.util.*;
import java.io.*;

/* Both Player and Ghost inherit Mover.  Has generic functions relevant to both*/
class Mover {
	/* Framecount is used to count animation frames */
	int frameCount = 0;

	/* State contains the game map */
	boolean[][] state;

	/*
	 * gridSize is the size of one square in the game. max is the height/width of
	 * the game. increment is the speed at which the object moves, 1 increment per
	 * move() call
	 */
	int gridSize;
	int max;
	int increment;

	/* Generic constructor */
	public Mover() {
		gridSize = 20;
		increment = 4;
		max = 400;
		state = new boolean[20][20];
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				state[i][j] = false;
			}
		}
	}

	/* Updates the state information */
	public void updateState(boolean[][] state) {
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				this.state[i][j] = state[i][j];
			}
		}
	}

	/* Determines if a set of coordinates is a valid destination. */
	public boolean isValidDest(int x, int y) {
		/*
		 * The first statements check that the x and y are inbounds. The last statement
		 * checks the map to see if it's a valid location
		 */
		if ((((x) % 20 == 0) || ((y) % 20) == 0) && 20 <= x && x < 400 && 20 <= y && y < 400
				&& state[x / 20 - 1][y / 20 - 1]) {
			return true;
		}
		return false;
	}
}

/* This is the pacman object */
class Player extends Mover {
	/*
	 * Direction is used in demoMode, currDirection and desiredDirection are used in
	 * non demoMode
	 */
	char direction;
	char currDirection;
	char desiredDirection;

	/* Keeps track of pellets eaten to determine end of game */
	int pelletsEaten;
	int totalPellets = 173;

	/* Last location */
	int lastX;
	int lastY;

	/* Current location */
	int x;
	int y;

	/* Which pellet the pacman is on top of */
	int pelletX;
	int pelletY;
	
	boolean bigPelletON = false;
	int bigPelletX;
	int bigPelletY;

	/* teleport is true when travelling through the teleport tunnels */
	boolean teleport;

	/* Stopped is set when the pacman is not moving or has been killed */
	boolean stopped = false;

	/* Flag for can eat the fruit */
	boolean appearFruit = false;

	/* Fruit score */
	int Fruitscore = 0;
	
	int lifesCounter = 2;
	boolean newLifeOn = false;

	boolean death = false;
	
	/* Constructor places pacman in initial location and orientation */
	public Player(int x, int y) {

		teleport = false;
		pelletsEaten = 0;
		pelletX = x / gridSize - 1;
		pelletY = y / gridSize - 1;
		this.lastX = x;
		this.lastY = y;
		this.x = x;
		this.y = y;
		currDirection = 'L';
		desiredDirection = 'L';
	}

	/*
	 * This function is used for demoMode. It is copied from the Ghost class. See
	 * that for comments
	 */
	public char newDirection() {
		int random;
		char backwards = 'U';
		int newX = x, newY = y;
		int lookX = x, lookY = y;
		Set<Character> set = new HashSet<Character>();
		switch (direction) {
		case 'L':
			backwards = 'R';
			break;
		case 'R':
			backwards = 'L';
			break;
		case 'U':
			backwards = 'D';
			break;
		case 'D':
			backwards = 'U';
			break;
		}
		char newDirection = backwards;
		while (newDirection == backwards || !isValidDest(lookX, lookY)) {
			if (set.size() == 3) {
				newDirection = backwards;
				break;
			}
			newX = x;
			newY = y;
			lookX = x;
			lookY = y;
			random = (int) (Math.random() * 4) + 1;
			if (random == 1) {
				newDirection = 'L';
				newX -= increment;
				lookX -= increment;
			} else if (random == 2) {
				newDirection = 'R';
				newX += increment;
				lookX += gridSize;
			} else if (random == 3) {
				newDirection = 'U';
				newY -= increment;
				lookY -= increment;
			} else if (random == 4) {
				newDirection = 'D';
				newY += increment;
				lookY += gridSize;
			}
			if (newDirection != backwards) {
				set.add(new Character(newDirection));
			}
		}
		return newDirection;
	}

	/*
	 * This function is used for demoMode. It is copied from the Ghost class. See
	 * that for comments
	 */
	public boolean isChoiceDest() {
		if (x % gridSize == 0 && y % gridSize == 0) {
			return true;
		}
		return false;
	}

	/*
	 * This function is used for demoMode. It is copied from the Ghost class. See
	 * that for comments
	 */
	public void demoMove() {
		lastX = x;
		lastY = y;
		if (isChoiceDest()) {
			direction = newDirection();
		}
		switch (direction) {
		case 'L':
			if (isValidDest(x - increment, y)) {
				x -= increment;
			} else if (y == 9 * gridSize && x < 2 * gridSize) {
				x = max - gridSize * 1;
				teleport = true;
			}
			break;
		case 'R':
			if (isValidDest(x + gridSize, y)) {
				x += increment;
			} else if (y == 9 * gridSize && x > max - gridSize * 2) {
				x = 1 * gridSize;
				teleport = true;
			}
			break;
		case 'U':
			if (isValidDest(x, y - increment))
				y -= increment;
			break;
		case 'D':
			if (isValidDest(x, y + gridSize))
				y += increment;
			break;
		}
		currDirection = direction;
		frameCount++;
	}

	/* The move function moves the pacman for one frame in non demo mode */
	public void move() {
		int gridSize = 20;
		lastX = x;
		lastY = y;

		/* Try to turn in the direction input by the user */
		/* Can only turn if we're in center of a grid */
		if (x % 20 == 0 && y % 20 == 0 ||
		/* Or if we're reversing */
				(desiredDirection == 'L' && currDirection == 'R') || (desiredDirection == 'R' && currDirection == 'L')
				|| (desiredDirection == 'U' && currDirection == 'D')
				|| (desiredDirection == 'D' && currDirection == 'U')) {
			switch (desiredDirection) {
			case 'L':
				if (isValidDest(x - increment, y))
					x -= increment;
				break;
			case 'R':
				if (isValidDest(x + gridSize, y))
					x += increment;
				break;
			case 'U':
				if (isValidDest(x, y - increment))
					y -= increment;
				break;
			case 'D':
				if (isValidDest(x, y + gridSize))
					y += increment;
				break;
			}
		}
		/*
		 * If we haven't moved, then move in the direction the pacman was headed anyway
		 */
		if (lastX == x && lastY == y) {
			switch (currDirection) {
			case 'L':
				if (isValidDest(x - increment, y))
					x -= increment;
				else if (y == 9 * gridSize && x < 2 * gridSize) {
					x = max - gridSize * 1;
					teleport = true;
				}
				break;
			case 'R':
				if (isValidDest(x + gridSize, y))
					x += increment;
				else if (y == 9 * gridSize && x > max - gridSize * 2) {
					x = 1 * gridSize;
					teleport = true;
				}
				break;
			case 'U':
				if (isValidDest(x, y - increment))
					y -= increment;
				break;
			case 'D':
				if (isValidDest(x, y + gridSize))
					y += increment;
				break;
			}
		}

		/* If we did change direction, update currDirection to reflect that */
		else {
			currDirection = desiredDirection;
		}

		/* If we didn't move at all, set the stopped flag */
		if (lastX == x && lastY == y)
			stopped = true;

		/*
		 * Otherwise, clear the stopped flag and increment the frameCount for animation
		 * purposes
		 */
		else {
			stopped = false;
			frameCount++;
		}
	}

	/* Update what pellet the pacman is on top of */
	public void updatePellet() {
		if (x % gridSize == 0 && y % gridSize == 0) {
			pelletX = x / gridSize - 1;
			pelletY = y / gridSize - 1;
		}
	}
}

/* Ghost class controls the ghost. */
class Ghost extends Mover {
	/* Direction ghost is heading */
	char direction;

	/* Last ghost location */
	int lastX;
	int lastY;

	/* Current ghost location */
	int x;
	int y;

	/* The pellet the ghost is on top of */
	int pelletX, pelletY;

	/* The pellet the ghost was last on top of */
	int lastPelletX, lastPelletY;

	/* state of the ghost */
	boolean scaredGhostActive;

	/* Constructor places ghost and updates states */
	public Ghost(int x, int y) {
		direction = 'L';
		pelletX = x / gridSize - 1;
		pelletY = x / gridSize - 1;
		lastPelletX = pelletX;
		lastPelletY = pelletY;
		this.lastX = x;
		this.lastY = y;
		this.x = x;
		this.y = y;
		scaredGhostActive = false;
	}

	/* update pellet status */
	public void updatePellet() {
		int tempX, tempY;
		tempX = x / gridSize - 1;
		tempY = y / gridSize - 1;
		if (tempX != pelletX || tempY != pelletY) {
			lastPelletX = pelletX;
			lastPelletY = pelletY;
			pelletX = tempX;
			pelletY = tempY;
		}

	}

	/* Determines if the location is one where the ghost has to make a decision */
	public boolean isChoiceDest() {
		if (x % gridSize == 0 && y % gridSize == 0) {
			return true;
		}
		return false;
	}

	/* Chooses a new direction randomly for the ghost to move */
	public char newDirection() {
		int random;
		char backwards = 'U';
		int newX = x, newY = y;
		int lookX = x, lookY = y;
		Set<Character> set = new HashSet<Character>();
		switch (direction) {
		case 'L':
			backwards = 'R';
			break;
		case 'R':
			backwards = 'L';
			break;
		case 'U':
			backwards = 'D';
			break;
		case 'D':
			backwards = 'U';
			break;
		}

		char newDirection = backwards;
		/* While we still haven't found a valid direction */
		while (newDirection == backwards || !isValidDest(lookX, lookY)) {
			/* If we've tried every location, turn around and break the loop */
			if (set.size() == 3) {
				newDirection = backwards;
				break;
			}

			newX = x;
			newY = y;
			lookX = x;
			lookY = y;

			/* Randomly choose a direction */
			random = (int) (Math.random() * 4) + 1;
			if (random == 1) {
				newDirection = 'L';
				newX -= increment;
				lookX -= increment;
			} else if (random == 2) {
				newDirection = 'R';
				newX += increment;
				lookX += gridSize;
			} else if (random == 3) {
				newDirection = 'U';
				newY -= increment;
				lookY -= increment;
			} else if (random == 4) {
				newDirection = 'D';
				newY += increment;
				lookY += gridSize;
			}
			if (newDirection != backwards) {
				set.add(new Character(newDirection));
			}
		}
		return newDirection;
	}

	/* Random move function for ghost */
	public void move() {
		lastX = x;
		lastY = y;

		/* If we can make a decision, pick a new direction randomly */
		if (isChoiceDest()) {
			direction = newDirection();
		}

		/* If that direction is valid, move that way */
		switch (direction) {
		case 'L':
			if (isValidDest(x - increment, y))
				x -= increment;
			break;
		case 'R':
			if (isValidDest(x + gridSize, y))
				x += increment;
			break;
		case 'U':
			if (isValidDest(x, y - increment))
				y -= increment;
			break;
		case 'D':
			if (isValidDest(x, y + gridSize))
				y += increment;
			break;
		}
	}

	/* Random move function for ghost */
	public void move(char direction) { // server provide random direction
		lastX = x;
		lastY = y;

		/* If that direction is valid, move that way */
		switch (direction) {
		case 'L':
			if (isValidDest(x - increment, y))
				x -= increment;
			break;
		case 'R':
			if (isValidDest(x + gridSize, y))
				x += increment;
			break;
		case 'U':
			if (isValidDest(x, y - increment))
				y -= increment;
			break;
		case 'D':
			if (isValidDest(x, y + gridSize))
				y += increment;
			break;
		}
	}

}

/*
 * This board class contains the player, ghosts, pellets, and most of the game
 * logic.
 */
public class Board extends JPanel {

	/* Initialize the images */
	Image pacmanImage = Toolkit.getDefaultToolkit().getImage("img/Pacman.png");
	Image pacmanUpImage = Toolkit.getDefaultToolkit().getImage("img/PacmanUp.png");
	Image pacmanUpImage2 = Toolkit.getDefaultToolkit().getImage("img/PacmanUp2.png");
	Image pacmanDownImage = Toolkit.getDefaultToolkit().getImage("img/PacmanDown.png");
	Image pacmanDownImage2 = Toolkit.getDefaultToolkit().getImage("img/PacmanDown2.png");
	Image pacmanLeftImage = Toolkit.getDefaultToolkit().getImage("img/PacmanLeft.png");
	Image pacmanLeftImage2 = Toolkit.getDefaultToolkit().getImage("img/PacmanLeft2.png");
	Image pacmanRightImage = Toolkit.getDefaultToolkit().getImage("img/PacmanRight.png");
	Image pacmanRightImage2 = Toolkit.getDefaultToolkit().getImage("img/PacmanRight2.png");
	Image redUp = Toolkit.getDefaultToolkit().getImage("img/RedUp.png");
	Image redDown = Toolkit.getDefaultToolkit().getImage("img/RedDown.png");
	Image redLeft = Toolkit.getDefaultToolkit().getImage("img/RedLeft.png");
	Image redRight = Toolkit.getDefaultToolkit().getImage("img/RedRight.png");
	Image yellowUp = Toolkit.getDefaultToolkit().getImage("img/YellowUp.png");
	Image yellowDown = Toolkit.getDefaultToolkit().getImage("img/YellowDown.png");
	Image yellowRight = Toolkit.getDefaultToolkit().getImage("img/YellowRight.png");
	Image yellowLeft = Toolkit.getDefaultToolkit().getImage("img/YellowLeft.png");
	Image lightBlueUp = Toolkit.getDefaultToolkit().getImage("img/LightBlueUp.png");
	Image lightBlueDown = Toolkit.getDefaultToolkit().getImage("img/LightBlueDown.png");
	Image lightBlueRight = Toolkit.getDefaultToolkit().getImage("img/LightBlueRight.png");
	Image lightBlueLeft = Toolkit.getDefaultToolkit().getImage("img/LightBlueLeft.png");
	Image pinkUp = Toolkit.getDefaultToolkit().getImage("img/PinkUp.png");
	Image pinkDown = Toolkit.getDefaultToolkit().getImage("img/PinkDown.png");
	Image pinkRight = Toolkit.getDefaultToolkit().getImage("img/PinkRight.png");
	Image pinkLeft = Toolkit.getDefaultToolkit().getImage("img/PinkLeft.png");
	Image titleScreenImage = Toolkit.getDefaultToolkit().getImage("img/titleScreen.jpg");
	Image gameOverImage = Toolkit.getDefaultToolkit().getImage("img/GameOver.png");
	Image winScreenImage = Toolkit.getDefaultToolkit().getImage("img/winScreen.jpg");

	/* Scared ghosts animations */
	Image scaredghostBlue = Toolkit.getDefaultToolkit().getImage("img/ScaredGhostBlue.png");
	Image scaredghostWhite = Toolkit.getDefaultToolkit().getImage("img/ScaredGhostWhite.png");

	/* Piece for a map */
	Image across1 = Toolkit.getDefaultToolkit().getImage("img/Across1.png");
	Image across2 = Toolkit.getDefaultToolkit().getImage("img/Across2.png");
	Image vertical1 = Toolkit.getDefaultToolkit().getImage("img/Vertical1.png");
	Image vertical2 = Toolkit.getDefaultToolkit().getImage("img/Vertical2.png");

	/* Fruis */
	Image cherry = Toolkit.getDefaultToolkit().getImage("img/Cherry.png");
	Image strawberry = Toolkit.getDefaultToolkit().getImage("img/Strawberry.png");
	Image apple = Toolkit.getDefaultToolkit().getImage("img/Apple.png");
	Image peach = Toolkit.getDefaultToolkit().getImage("img/Peach.png");

	/* Initialize the player and ghosts */
	Player player = new Player(200, 300);
	Ghost blinky = new Ghost(180, 180);
	Ghost clyde = new Ghost(200, 180);
	Ghost inky = new Ghost(220, 180);
	Ghost pinky = new Ghost(220, 180);

	/* Timer is used for playing sound effects and animations */
	long timer = System.currentTimeMillis();

	/*
	 * Dying is used to count frames in the dying animation. If it's non-zero,
	 * pacman is in the process of dying
	 */
	int dying = 0;

	/* Score information */
	int currScore;
	int highScore;
	int newLive;

	/*
	 * if the high scores have been cleared, we have to update the top of the screen
	 * to reflect that
	 */
	boolean clearHighScores = false;

	/* Number of lifes */
	int numLives = 2;

	/* Contains the game map, passed to player and ghosts */
	boolean[][] state;

	/* Contains the state of all pellets */
	boolean[][] pellets;

	/* Contains the state of all big pellets */
//	boolean[][] bigPellets = new boolean[20][20];

	/* Game dimensions */
	int gridSize;
	int max;

	/* State flags */
	boolean stopped;
	boolean titleScreen;
	boolean winScreen = false;
	boolean overScreen = false;
	boolean demo = false;
	int New;

	/* Used to call sound effects */
	GameSounds sounds;

	int lastPelletEatenX = 0;
	int lastPelletEatenY = 0;

	/* This is the font used for the menus */
	Font font = new Font("Monospaced", Font.BOLD, 12);

	/* Constructor initializes state flags etc. */
	public Board() {
		initHighScores();
		sounds = new GameSounds();
		currScore = 0;
		newLive = 0;
		stopped = false;
		max = 400;
		gridSize = 20;
		New = 0;
		titleScreen = true;
	}

	/* Reads the high scores file and saves it */
	public void initHighScores() {
		File file = new File("highScores.txt");
		Scanner sc;
		try {
			sc = new Scanner(file);
			highScore = sc.nextInt();
			sc.close();
		} catch (Exception e) {
		}
	}

	/* Writes the new high score to a file and sets flag to update it on screen */
	public void updateScore(int score) {
		PrintWriter out;
		try {
			out = new PrintWriter("highScores.txt");
			out.println(score);
			out.close();
		} catch (Exception e) {
		}
		highScore = score;
		clearHighScores = true;
	}

	/* Wipes the high scores file and sets flag to update it on screen */
	public void clearHighScores() {
		PrintWriter out;
		try {
			out = new PrintWriter("highScores.txt");
			out.println("0");
			out.close();
		} catch (Exception e) {
		}
		highScore = 0;
		clearHighScores = true;
	}

	/* Reset occurs on a new game */
	public void reset() {
		player.x = 200;
		player.y = 300;
		blinky.x = 200;
		blinky.y = 160;
		clyde.x = 200;
		clyde.y = 180;
		inky.x = 220;
		inky.y = 180;
		pinky.x = 180;
		pinky.y = 180;	
		if (!demo)
		SocketCliente("R");
		numLives = 2;
		player.lifesCounter = 2;
		state = new boolean[20][20];
		pellets = new boolean[20][20];

		/* Clear state and pellets arrays */
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				state[i][j] = true;
				pellets[i][j] = true;
			}
		}

		/* Handle the weird spots with no pellets */
		for (int i = 5; i < 14; i++) {
			for (int j = 5; j < 12; j++) {
				pellets[i][j] = false;
			}
		}
		pellets[9][7] = false;
		pellets[8][8] = false;
		pellets[9][8] = false;
		pellets[10][8] = false;

	}

	/*
	 * Function is called during drawing of the map. Whenever the a portion of the
	 * map is covered up with a barrier, the map and pellets arrays are updated
	 * accordingly to note that those are invalid locations to travel or put pellets
	 */
	public void updateMap(int x, int y, int width, int height) {
		for (int i = x / gridSize; i < x / gridSize + width / gridSize; i++) {
			for (int j = y / gridSize; j < y / gridSize + height / gridSize; j++) {
				state[i - 1][j - 1] = false;
				pellets[i - 1][j - 1] = false;
			}
		}
	}

	/*
	 * Draws the appropriate number of lives on the bottom left of the screen. Also
	 * draws the menu
	 */
	public void drawLives(Graphics g) {
		numLives = player.lifesCounter;
		g.setColor(Color.BLACK);

		/* Clear the bottom bar */
		g.fillRect(0, max + 5, 600, gridSize);
		for (int i = 0; i < numLives; i++) {
			/* Draw each life */
			g.drawImage(pacmanLeftImage, gridSize * (i + 1), max + 6, null, null);
		}
		/* Draw the menu items */
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("Reset", 100, max + 5 + gridSize);
		g.drawString("Clear High Scores", 180, max + 5 + gridSize);
		g.drawString("Exit", 350, max + 5 + gridSize);
	}

	/*
	 * This function draws the board. The pacman board is really complicated and can
	 * only feasibly be done manually. Whenever I draw a wall, I call updateMap to
	 * invalidate those coordinates. This way the pacman and ghosts know that they
	 * can't traverse this area
	 */
	public void drawBoard(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 600, 600);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 420, 420);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 20, 600);
		g.fillRect(0, 0, 600, 20);
		g.setColor(Color.WHITE);
		g.drawRect(19, 19, 382, 382);
		g.setColor(Color.BLUE);

		g.fillRect(40, 40, 60, 20);
		updateMap(40, 40, 60, 20);
		g.drawImage(across1, 40, 40, null, null);

		g.fillRect(120, 40, 60, 20);
		updateMap(120, 40, 60, 20);
		g.drawImage(across1, 120, 40, null, null);

		g.fillRect(200, 20, 20, 40); // vertical
		updateMap(200, 20, 20, 40);
		g.drawImage(vertical1, 200, 20, null, null);

		g.fillRect(240, 40, 60, 20);
		updateMap(240, 40, 60, 20);
		g.drawImage(across1, 240, 40, null, null);

		g.fillRect(320, 40, 60, 20);
		updateMap(320, 40, 60, 20);
		g.drawImage(across1, 320, 40, null, null);

		g.fillRect(40, 80, 60, 20);
		updateMap(40, 80, 60, 20);
		g.drawImage(across1, 40, 80, null, null);

		g.fillRect(160, 80, 100, 20);
		updateMap(160, 80, 100, 20);
		g.drawImage(across2, 160, 80, null, null);

		g.fillRect(200, 80, 20, 60);
		updateMap(200, 80, 20, 60); // vertical
		g.drawImage(vertical2, 200, 79, null, null);

		g.fillRect(320, 80, 60, 20);
		updateMap(320, 80, 60, 20);
		g.drawImage(across1, 320, 80, null, null);

		g.fillRect(20, 120, 80, 60);
		updateMap(20, 120, 80, 60);

		g.fillRect(320, 120, 80, 60);
		updateMap(320, 120, 80, 60);

		g.fillRect(20, 200, 80, 60);
		updateMap(20, 200, 80, 60);

		g.fillRect(320, 200, 80, 60);
		updateMap(320, 200, 80, 60);

		g.fillRect(160, 160, 40, 20);
		updateMap(160, 160, 40, 20);
		g.fillRect(220, 160, 40, 20);
		updateMap(220, 160, 40, 20);
		g.fillRect(160, 180, 20, 20);
		updateMap(160, 180, 20, 20);
		g.fillRect(160, 200, 100, 20);
		updateMap(160, 200, 100, 20);
		g.fillRect(240, 180, 20, 20);
		updateMap(240, 180, 20, 20);
		g.setColor(Color.BLUE);

		g.fillRect(120, 120, 60, 20);
		updateMap(120, 120, 60, 20);
		g.fillRect(120, 80, 20, 100);
		updateMap(120, 80, 20, 100);
		g.fillRect(280, 80, 20, 100);
		updateMap(280, 80, 20, 100);
		g.fillRect(240, 120, 60, 20);
		updateMap(240, 120, 60, 20);

		g.fillRect(280, 200, 20, 60);
		updateMap(280, 200, 20, 60);
		g.fillRect(120, 200, 20, 60);
		updateMap(120, 200, 20, 60);
		g.fillRect(160, 240, 100, 20);
		updateMap(160, 240, 100, 20);
		g.fillRect(200, 260, 20, 40);
		updateMap(200, 260, 20, 40);

		g.fillRect(120, 280, 60, 20);
		updateMap(120, 280, 60, 20);
		g.fillRect(240, 280, 60, 20);
		updateMap(240, 280, 60, 20);

		g.fillRect(40, 280, 60, 20);
		updateMap(40, 280, 60, 20);
		g.fillRect(80, 280, 20, 60);
		updateMap(80, 280, 20, 60);
		g.fillRect(320, 280, 60, 20);
		updateMap(320, 280, 60, 20);
		g.fillRect(320, 280, 20, 60);
		updateMap(320, 280, 20, 60);

		g.fillRect(20, 320, 40, 20);
		updateMap(20, 320, 40, 20);
		g.fillRect(360, 320, 40, 20);
		updateMap(360, 320, 40, 20);
		g.fillRect(160, 320, 100, 20);
		updateMap(160, 320, 100, 20);
		g.fillRect(200, 320, 20, 60);
		updateMap(200, 320, 20, 60);

		g.fillRect(40, 360, 140, 20);
		updateMap(40, 360, 140, 20);
		g.fillRect(240, 360, 140, 20);
		updateMap(240, 360, 140, 20);
		g.fillRect(280, 320, 20, 40);
		updateMap(280, 320, 20, 60);
		g.fillRect(120, 320, 20, 60);
		updateMap(120, 320, 20, 60);
		drawLives(g);
	}

	/* Draws the pellets on the screen */
	public void drawPellets(Graphics g) {
		g.setColor(Color.WHITE);
		for (int i = 1; i < 20; i++) {
			for (int j = 1; j < 20; j++) {
				if (pellets[i - 1][j - 1])
					g.fillOval(i * 20 + 8, j * 20 + 8, 4, 4);
			}
		}
	}

	/* Draws the big pellets on the screen */
	public void drawBigPellets(Graphics g, int x, int y) { // example: drawBigPellets(g);
		g.setColor(Color.WHITE);
		g.fillOval(x, y, 8, 8);
	}

	/*
	 * Draws one individual pellet. Used to redraw pellets that ghosts have run over
	 */
	public void fillPellet(int x, int y, Graphics g, boolean type) {
		g.setColor(Color.WHITE);
		if (type == true) {
			g.fillOval(x * 20 + 28, y * 20 + 28, 8, 8);
		} else {
			g.fillOval(x * 20 + 28, y * 20 + 28, 4, 4);
		}
	}

	/* Draw the fruit on the screen */
	public void drawFruit(Graphics g, Image fruit) { // example: drawFruit(g, cherry);
		g.drawImage(fruit, 200, 220, null, null);
	}

	/* Draw the scared effect on ghosts on the screen */
	public void scaredEffect(Graphics g, Ghost ghost) {
		if (ghost.frameCount < 100) {
			g.drawImage(scaredghostBlue, ghost.x, ghost.y, null, null);
		} else if (ghost.frameCount <= 120) {
			g.drawImage(scaredghostWhite, ghost.x, ghost.y, null, null);
		} else if (ghost.frameCount <= 128) {
			g.drawImage(scaredghostBlue, ghost.x, ghost.y, null, null);
		} else if (ghost.frameCount <= 133) {
			g.drawImage(scaredghostWhite, ghost.x, ghost.y, null, null);
		} else if (ghost.frameCount <= 137) {
			g.drawImage(scaredghostBlue, ghost.x, ghost.y, null, null);
		} else if (ghost.frameCount <= 142) {
			g.drawImage(scaredghostWhite, ghost.x, ghost.y, null, null);
		} else if (ghost.frameCount <= 147) {
			g.drawImage(scaredghostBlue, ghost.x, ghost.y, null, null);
		} else if (ghost.frameCount <= 152) {
			g.drawImage(scaredghostWhite, ghost.x, ghost.y, null, null);
			ghost.frameCount = 0;
			ghost.scaredGhostActive = false;
		} else {
			ghost.frameCount++;
		}
		ghost.frameCount++;
	}

	/* Draw the effect for eat ghost on the screen */
	public void eatGhost(Graphics g, Ghost ghost) {

		/* Delete the ghost */
		g.setColor(Color.BLACK);
		g.fillRect(ghost.lastX, ghost.lastY, 20, 20);

		/* Play death sound */
		sounds.death();

		/* Increment the score */
		currScore += 100;
		newLive += 100;

		/* Reset the status */
		ghost.x = 200;
		ghost.y = 160;
		ghost.scaredGhostActive = false;
		
		SocketCliente("G");
	}

	public void SocketCliente(String mensaje) {
        try {
            Socket socket = new Socket ("192.168.100.3", 35558);
            socket.setSoLinger (true, 10);
            DataOutputStream bufferSalida = new DataOutputStream (socket.getOutputStream());

           DatoSocket aux = new DatoSocket (mensaje);
                  aux.writeObject (bufferSalida);        
            socket.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
	
	/* This is the main function that draws one entire frame of the game */
	public void paint(Graphics g) {
		/*
		 * If we're playing the dying animation, don't update the entire screen. Just
		 * kill the pacman
		 */
		if (dying > 0) {
			/* Stop any pacman eating sounds */
			sounds.nomNomStop();

			/* Draw the pacman */
			g.drawImage(pacmanImage, player.x, player.y, null, null);
			g.setColor(Color.BLACK);

			/* Kill the pacman */
			if (dying == 4)
				g.fillRect(player.x, player.y, 20, 7);
			else if (dying == 3)
				g.fillRect(player.x, player.y, 20, 14);
			else if (dying == 2)
				g.fillRect(player.x, player.y, 20, 20);
			else if (dying == 1) {
				g.fillRect(player.x, player.y, 20, 20);
			}

			/*
			 * Take .1 seconds on each frame of death, and then take 2 seconds for the final
			 * frame to allow for the sound effect to end
			 */
			long currTime = System.currentTimeMillis();
			long temp;
			if (dying != 1)
				temp = 100;
			else
				temp = 2000;
			/* If it's time to draw a new death frame... */
			if (currTime - timer >= temp) {
				dying--;
				timer = currTime;
				/* If this was the last death frame... */
				if (dying == 0) {
					if (numLives == -1) {
						/* Demo mode has infinite lives, just give it more lives */
						if (demo)
							numLives = 2;
						else {
							/* Game over for player. If relevant, update high score. Set gameOver flag */
							if (currScore > highScore) {
								updateScore(currScore);
							}
							overScreen = true;
						}
					}
				}
			}
			return;
		}

		/* If this is the title screen, draw the title screen and return */
		if (titleScreen) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 600, 600);
			g.drawImage(titleScreenImage, 0, 0, Color.BLACK, null);

			/* Stop any pacman eating sounds */
			sounds.nomNomStop();
			New = 1;
			return;
		}

		/* If this is the win screen, draw the win screen and return */
		else if (winScreen) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 600, 600);
			g.drawImage(winScreenImage, 0, 0, Color.BLACK, null);
			New = 1;
			/* Stop any pacman eating sounds */
			sounds.nomNomStop();
			return;
		}

		/* If this is the game over screen, draw the game over screen and return */
		else if (overScreen) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 600, 600);
			g.drawImage(gameOverImage, 160, 180, Color.BLACK, null);
			New = 1;
			/* Stop any pacman eating sounds */
			sounds.nomNomStop();
			return;
		}

		/* If need to update the high scores, redraw the top menu bar */
		if (clearHighScores) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 600, 18);
			g.setColor(Color.WHITE);
			g.setFont(font);
			clearHighScores = false;
			if (demo)
				g.drawString("DEMO MODE|PRESS ANY KEY TO START A GAME|High Score: " + highScore, 20, 10);
			else
				g.drawString("Score: " + (currScore) + "\t \t \t \t \t High Score: " + highScore, 20, 10);
		}

		/* oops is set to true when pacman has lost a life */
		boolean oops = false;

		/* Game initialization */
		if (New == 1) {
			reset();
			player = new Player(200, 300);
			blinky = new Ghost(200, 160);
			clyde = new Ghost(200, 180);
			inky = new Ghost(220, 180);
			pinky = new Ghost(180, 180);
			currScore = 0;
			newLive = 0;
			drawBoard(g);
			drawPellets(g);
			drawLives(g);
			/* Send the game map to player and all ghosts */
			player.updateState(state);
			/* Don't let the player go in the ghost box */
			player.state[9][7] = false;
			blinky.updateState(state);
			clyde.updateState(state);
			inky.updateState(state);
			pinky.updateState(state);
			player.appearFruit = false;

			/* Draw the top menu bar */
			g.setColor(Color.WHITE);
			g.setFont(font);
			if (demo)
				g.drawString("DEMO MODE|PRESS ANY KEY TO START A GAME|High Score: " + highScore, 20, 10);
			else
				g.drawString("Score: " + (currScore) + "\t \t \t \t \t High Score: " + highScore, 20, 10);
			New++;
		}
		/* Second frame of new game */
		else if (New == 2) {
			New++;
		}
		/* Third frame of new game */
		else if (New == 3) {
			New++;
			/* Play the newGame sound effect */
			sounds.newGame();
			timer = System.currentTimeMillis();
			return;
		}
		/* Fourth frame of new game */
		else if (New == 4) {
			/* Stay in this state until the sound effect is over */
			long currTime = System.currentTimeMillis();
			if (currTime - timer >= 5000) {
				New = 0;
			} else
				return;
		}

		/* Drawing optimization */
		g.copyArea(player.x - 20, player.y - 20, 80, 80, 0, 0);
		g.copyArea(blinky.x - 20, blinky.y - 20, 80, 80, 0, 0);
		g.copyArea(clyde.x - 20, clyde.y - 20, 80, 80, 0, 0);
		g.copyArea(inky.x - 20, inky.y - 20, 80, 80, 0, 0);
		g.copyArea(pinky.x - 20, pinky.y - 20, 80, 80, 0, 0);

		/* Detect collisions */
		if (player.x == blinky.x && Math.abs(player.y - blinky.y) < 10) {
			if (blinky.scaredGhostActive == true) {
				eatGhost(g, blinky);
			} else {
				oops = true;
			}
		} else if (player.x == clyde.x && Math.abs(player.y - clyde.y) < 10) {
			if (clyde.scaredGhostActive == true) {
				eatGhost(g, clyde);
			} else {
				oops = true;
			}
		} else if (player.x == inky.x && Math.abs(player.y - inky.y) < 10) {
			if (inky.scaredGhostActive == true) {
				eatGhost(g, inky);
			} else {
				oops = true;
			}
		} else if (player.x == pinky.x && Math.abs(player.y - pinky.y) < 10) {
			if (pinky.scaredGhostActive == true) {
				eatGhost(g, pinky);
			} else {
				oops = true;
			}
		} else if (player.y == blinky.y && Math.abs(player.x - blinky.x) < 10) {
			if (blinky.scaredGhostActive == true) {
				eatGhost(g, blinky);
			} else {
				oops = true;
			}
		} else if (player.y == clyde.y && Math.abs(player.x - clyde.x) < 10) {
			if (clyde.scaredGhostActive == true) {
				eatGhost(g, clyde);
			} else {
				oops = true;
			}
		} else if (player.y == inky.y && Math.abs(player.x - inky.x) < 10) {
			if (inky.scaredGhostActive == true) {
				eatGhost(g, inky);
			} else {
				oops = true;
			}
		} else if (player.y == pinky.y && Math.abs(player.x - pinky.x) < 10) {
			if (pinky.scaredGhostActive == true) {
				eatGhost(g, pinky);
			} else {
				oops = true;
			}
		}

		/* Kill the pacman */
		if (oops && !stopped) {
			if (player.death == true) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 600, 600);
			g.drawImage(gameOverImage, 160, 180, Color.BLACK, null);
			New = 1;
			/* Stop any pacman eating sounds */
			sounds.nomNomStop();
			player.death = false;
			player.x = 200;
			player.y = 300;
			blinky.x = 200;
			blinky.y = 160;
			clyde.x = 200;
			clyde.y = 180;
			inky.x = 220;
			inky.y = 180;
			pinky.x = 180;
			pinky.y = 180;			
			paint(g);
			return;
			}
			player.x = 200;
			player.y = 300;
			blinky.x = 200;
			blinky.y = 160;
			clyde.x = 200;
			clyde.y = 180;
			inky.x = 220;
			inky.y = 180;
			pinky.x = 180;
			pinky.y = 180;
			
			if (!demo)
			SocketCliente("M");
			
			/* 4 frames of death */
			dying = 4;

			/* Play death sound effect */
			sounds.death();
			/* Stop any pacman eating sounds */
			sounds.nomNomStop();

			/*
			 * Decrement lives, update screen to reflect that. And set appropriate flags and
			 * timers
			 */
			numLives--;
			stopped = true;
			drawLives(g);
			timer = System.currentTimeMillis();
		}
		
		if (player.newLifeOn == true) {
			player.newLifeOn = false;
			drawLives(g);
		}

		/* Delete the players and ghosts */
		g.setColor(Color.BLACK);
		g.fillRect(player.lastX, player.lastY, 20, 20);
		g.fillRect(blinky.lastX, blinky.lastY, 20, 20);
		g.fillRect(clyde.lastX, clyde.lastY, 20, 20);
		g.fillRect(inky.lastX, inky.lastY, 20, 20);
		g.fillRect(pinky.lastX, pinky.lastY, 20, 20);

		if (player.appearFruit == true) {
			drawFruit(g, cherry);
		}
		
		if (player.bigPelletON == true) {
			drawBigPellets(g, player.bigPelletX, player.bigPelletY);
		}
		
		/* Detect fruit eat */
		if (player.appearFruit == true && player.x == 200 && Math.abs(player.y - 220) < 10) {
			player.appearFruit = false;
			/* Play eating sound */
			sounds.nomNom();

			/* Increment the score */
			currScore += player.Fruitscore;
			newLive += player.Fruitscore;
			
			if (!demo)
			SocketCliente("F");
		}

		/* Eat Big pellets */
		if (player.bigPelletON == true && player.x == player.bigPelletX && player.y == player.bigPelletY && New != 2 && New != 3) {
			lastPelletEatenX = player.pelletX;
			lastPelletEatenY = player.pelletY;

			player.bigPelletON = false;
			
			/* Play eating sound */
			sounds.nomNom();

			/* Increment pellets eaten value to track for end game */
			player.pelletsEaten++;

			/* Delete the pellet */
			pellets[player.pelletX][player.pelletY] = false;

			/* Increment the score */
			currScore += 50;
			newLive += 50;

			/* Update the screen to reflect the new score */
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 600, 20);
			g.setColor(Color.WHITE);
			g.setFont(font);
			if (demo)
				g.drawString("DEMO MODE|PRESS ANY KEY TO START A GAME|High Score: " + highScore, 20, 10);
			else
				g.drawString("Score: " + (currScore) + "\t \t \t \t \t High Score: " + highScore, 20, 10);

			/* If this was the last pellet */
			if (player.pelletsEaten == 173) {
				/* Demo mode can't get a high score */
				if (!demo) {
					if (currScore > highScore) {
						updateScore(currScore);
					}
					winScreen = true;
				} else {
					titleScreen = true;
				}
				return;
			}

			/* Life Up when the score +10000 */
			if (newLive == 10000) {
				numLives++;
				drawLives(g);
				newLive = 0;
			}

			/* Activation the scared ghosts effect */
			blinky.scaredGhostActive = true;
			clyde.scaredGhostActive = true;
			inky.scaredGhostActive = true;
			pinky.scaredGhostActive = true;
			
			if (!demo)
			SocketCliente("T");

		}
		/* Eat pellets */
		else if (pellets[player.pelletX][player.pelletY] && New != 2 && New != 3) {
			lastPelletEatenX = player.pelletX;
			lastPelletEatenY = player.pelletY;

			if (!demo)
			SocketCliente("P");
			
			/* Play eating sound */
			sounds.nomNom();

			/* Increment pellets eaten value to track for end game */
			player.pelletsEaten++;

//			System.out.println("llevo esta cantidad de bolas comidas: " + player.pelletsEaten);
			
			/* Delete the pellet */
			pellets[player.pelletX][player.pelletY] = false;

			/* Increment the score */
			currScore += 10;
			newLive += 10;

			/* Update the screen to reflect the new score */
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 600, 20);
			g.setColor(Color.WHITE);
			g.setFont(font);
			if (demo)
				g.drawString("DEMO MODE|PRESS ANY KEY TO START A GAME|High Score: " + highScore, 20, 10);
			else
				g.drawString("Score: " + (currScore) + "\t \t \t \t \t High Score: " + highScore, 20, 10);

			/* If this was the last pellet */
			if (player.pelletsEaten == player.totalPellets) {
				/* Demo mode can't get a high score */
				if (!demo) {
					if (currScore > highScore) {
						updateScore(currScore);
					}
					winScreen = true;
				} else {
					titleScreen = true;
				}
				return;
			}

			/* Life Up when the score +10000 */
			if (newLive == 10000) {
				numLives++;
				drawLives(g);
				newLive = 0;
			}
		}
		/* If we moved to a location without pellets, stop the sounds */
		else if ((player.pelletX != lastPelletEatenX || player.pelletY != lastPelletEatenY) || player.stopped) {
			/* Stop any pacman eating sounds */
			sounds.nomNomStop();
		}

		if (blinky.x == player.bigPelletX && blinky.y == player.bigPelletY) {
			fillPellet(blinky.lastPelletX, blinky.lastPelletY, g, true);
		} else if (pellets[blinky.lastPelletX][blinky.lastPelletY]) {
			fillPellet(blinky.lastPelletX, blinky.lastPelletY, g, false);
		}
		if (clyde.x == player.bigPelletX && clyde.y == player.bigPelletY) {
			fillPellet(clyde.lastPelletX, clyde.lastPelletY, g, true);
		} else if (pellets[clyde.lastPelletX][clyde.lastPelletY]) {
			fillPellet(clyde.lastPelletX, clyde.lastPelletY, g, false);
		}
		if (inky.x == player.bigPelletX && inky.y == player.bigPelletY) {
			fillPellet(inky.lastPelletX, inky.lastPelletY, g, true);
		} else if (pellets[inky.lastPelletX][inky.lastPelletY]) {
			fillPellet(inky.lastPelletX, inky.lastPelletY, g, false);
		}
		if (pinky.x == player.bigPelletX && pinky.y == player.bigPelletY) {
			fillPellet(pinky.lastPelletX, pinky.lastPelletY, g, true);
		} else if (pellets[pinky.lastPelletX][pinky.lastPelletY]) {
			fillPellet(pinky.lastPelletX, pinky.lastPelletY, g, false);
		}

		/* Draw the ghosts */
		if (blinky.scaredGhostActive == true) {
			scaredEffect(g, blinky);
		} else {
			switch (blinky.direction) { // red Ghost
			case 'L':
				g.drawImage(redLeft, blinky.x, blinky.y, null, null);
				break;
			case 'R':
				g.drawImage(redRight, blinky.x, blinky.y, null, null);
				break;
			case 'U':
				g.drawImage(redUp, blinky.x, blinky.y, null, null);
				break;
			case 'D':
				g.drawImage(redDown, blinky.x, blinky.y, null, null);
				break;
			}
		}
		if (clyde.scaredGhostActive == true) {
			scaredEffect(g, clyde);
		} else {
			switch (clyde.direction) { // yellow Ghost
			case 'L':
				g.drawImage(yellowLeft, clyde.x, clyde.y, null, null);
				break;
			case 'R':
				g.drawImage(yellowRight, clyde.x, clyde.y, null, null);
				break;
			case 'U':
				g.drawImage(yellowUp, clyde.x, clyde.y, null, null);
				break;
			case 'D':
				g.drawImage(yellowDown, clyde.x, clyde.y, null, null);
				break;
			}
		}
		if (inky.scaredGhostActive == true) {
			scaredEffect(g, inky);
		} else {
			switch (inky.direction) { // lightBlue Ghost
			case 'L':
				g.drawImage(lightBlueLeft, inky.x, inky.y, null, null);
				break;
			case 'R':
				g.drawImage(lightBlueRight, inky.x, inky.y, null, null);
				break;
			case 'U':
				g.drawImage(lightBlueUp, inky.x, inky.y, null, null);
				break;
			case 'D':
				g.drawImage(lightBlueDown, inky.x, inky.y, null, null);
				break;
			}
		}
		if (pinky.scaredGhostActive == true) {
			scaredEffect(g, pinky);
		} else {
			switch (pinky.direction) { // pink Ghost
			case 'L':
				g.drawImage(pinkLeft, pinky.x, pinky.y, null, null);
				break;
			case 'R':
				g.drawImage(pinkRight, pinky.x, pinky.y, null, null);
				break;
			case 'U':
				g.drawImage(pinkUp, pinky.x, pinky.y, null, null);
				break;
			case 'D':
				g.drawImage(pinkDown, pinky.x, pinky.y, null, null);
				break;
			}
		}

		/* Draw the pacman */
		if (player.frameCount < 5) {
			/* Draw mouth closed */
			switch (player.currDirection) {
			case 'L':
				g.drawImage(pacmanLeftImage, player.x, player.y, null, null);
				break;
			case 'R':
				g.drawImage(pacmanRightImage, player.x, player.y, null, null);
				break;
			case 'U':
				g.drawImage(pacmanUpImage, player.x, player.y, null, null);
				break;
			case 'D':
				g.drawImage(pacmanDownImage, player.x, player.y, null, null);
				break;
			}
		} else {
			/* Draw mouth open in appropriate direction */
			if (player.frameCount >= 10)
				player.frameCount = 0;

			switch (player.currDirection) {
			case 'L':
				g.drawImage(pacmanLeftImage2, player.x, player.y, null, null);
				break;
			case 'R':
				g.drawImage(pacmanRightImage2, player.x, player.y, null, null);
				break;
			case 'U':
				g.drawImage(pacmanUpImage2, player.x, player.y, null, null);
				break;
			case 'D':
				g.drawImage(pacmanDownImage2, player.x, player.y, null, null);
				break;
			}
		}

		/*
		 * Draw the border around the game in case it was overwritten by ghost movement
		 * or something
		 */
		g.setColor(Color.BLUE);
		g.drawRect(19, 19, 382, 382);

	}
}
