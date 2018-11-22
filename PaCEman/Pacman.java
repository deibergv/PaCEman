
/* 
 * Deiber Granados Vega /// 2017159397
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.DataInputStream;

import javax.swing.JApplet;
import java.awt.*;
import java.util.*;
import java.lang.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class contains the entire game interface... most of the game logic is in
 * the Board class but this creates the GUI and captures mouse and keyboard
 * input, as well as controls the game states
 * 
 * @author deiber
 */
@SuppressWarnings({ "serial", "deprecation" })
public class Pacman extends JApplet implements MouseListener, KeyListener {

	/* Create a new board */
	Board b = new Board();

	/*
	 * These timers are used to kill title, game over, and win screens after a set
	 * idle period (5 seconds)
	 */
	long titleTimer = -1;
	long timer = -1;

	/* This timer is used to do request new frames be drawn */
	javax.swing.Timer frameTimer;

	/* This constructor creates the game structure */
	public Pacman() {
		b.requestFocus();

		/* Create and set up window frame */
		JFrame f = new JFrame();
		f.setSize(420, 460);

		/* Add the board to the frame */
		f.add(b, BorderLayout.CENTER);

		/* Set listeners for mouse actions and button clicks */
		b.addMouseListener(this);
		b.addKeyListener(this);

		/* Make frame visible, disable resizing */
		f.setVisible(true);
		f.setResizable(false);

		/* Set the New flag to 1 because this is a new game (== 1 validation) */
		b.New = 1;

		Multi t1 = new Multi();
		t1.start();

		/* Manually call the first frameStep to initialize the game. */
		stepFrame(true);

		/* Create a timer that calls stepFrame every 60 milliseconds */
		frameTimer = new javax.swing.Timer(60, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stepFrame(false);
			}
		});

		/* Start the timer */
		frameTimer.start();

		b.requestFocus();
	}

	/**
	 * This repaint function repaints only the parts of the screen that may have
	 * changed. Normally the area around every player ghost and the menu bars
	 */
	public void repaint() {
		if (b.player.teleport) {
			b.repaint(b.player.lastX - 20, b.player.lastY - 20, 80, 80);
			b.player.teleport = false;
		}
		b.repaint(0, 0, 600, 20);
		b.repaint(0, 420, 600, 40);
		b.repaint(b.player.x - 20, b.player.y - 20, 80, 80);
		b.repaint(b.blinky.x - 20, b.blinky.y - 20, 80, 80);
		b.repaint(b.clyde.x - 20, b.clyde.y - 20, 80, 80);
		b.repaint(b.inky.x - 20, b.inky.y - 20, 80, 80);
		b.repaint(b.pinky.x - 20, b.pinky.y - 20, 80, 80);
	}

	public void repaint(int blinkyX, int blinkyY, int clydeX, int clydeY, int inkyX, int inkyY, int pinkyX,
			int pinkyY) {

		b.blinky.x = blinkyX;
		b.blinky.y = blinkyY;
		b.clyde.x = clydeX;
		b.clyde.y = clydeY;
		b.inky.x = inkyX;
		b.inky.y = inkyY;
		b.pinky.x = pinkyX;
		b.pinky.y = pinkyY;

		if (b.player.teleport) {
			b.repaint(b.player.lastX - 20, b.player.lastY - 20, 80, 80);
			b.player.teleport = false;
		}
		b.repaint(0, 0, 600, 20);
		b.repaint(0, 420, 600, 40);
		b.repaint(b.player.x - 20, b.player.y - 20, 80, 80);
		b.repaint(b.blinky.x - 20, b.blinky.y - 20, 80, 80);
		b.repaint(b.clyde.x - 20, b.clyde.y - 20, 80, 80);
		b.repaint(b.inky.x - 20, b.inky.y - 20, 80, 80);
		b.repaint(b.pinky.x - 20, b.pinky.y - 20, 80, 80);
	}

	/**
	 * Steps the screen forward one frame
	 * 
	 * @param New ...(and is a boolean, uses for compare if is a new game or not)
	 */
	public void stepFrame(boolean New) {
		/*
		 * If we aren't on a special screen than the timers can be set to -1 to disable
		 * them
		 */
		if (!b.titleScreen && !b.winScreen && !b.overScreen) {
			timer = -1;
			titleTimer = -1;
		}

		/*
		 * If we are playing the dying animation, keep advancing frames until the
		 * animation is complete
		 */
		if (b.dying > 0) {
			b.repaint();
			return;
		}

		/*
		 * New can either be specified by the New parameter in stepFrame function call
		 * or by the state of b.New. Update New accordingly
		 */
		New = New || (b.New != 0);

		/*
		 * If this is the title screen, make sure to only stay on the title screen for 5
		 * seconds. If after 5 seconds the user hasn't started a game, start up demo
		 * mode
		 */
		if (b.titleScreen) {
			if (titleTimer == -1) {
				titleTimer = System.currentTimeMillis();
			}

			long currTime = System.currentTimeMillis();
			if (currTime - titleTimer >= 5000) {
				b.titleScreen = false;
				b.demo = true;
				titleTimer = -1;
			}
			b.repaint();
			return;
		}

		/*
		 * If this is the win screen or game over screen, make sure to only stay on the
		 * screen for 5 seconds. If after 5 seconds the user hasn't pressed a key, go to
		 * title screen
		 */
		else if (b.winScreen || b.overScreen) {
			if (timer == -1) {
				timer = System.currentTimeMillis();
			}

			long currTime = System.currentTimeMillis();
			if (currTime - timer >= 5000) {
				b.winScreen = false;
				b.overScreen = false;
				b.titleScreen = true;
				timer = -1;
			}
			b.repaint();
			return;
		}

		/* If we have a normal game state, move all pieces and update pellet status */
		if (!New) {
			/*
			 * The pacman player has two functions, demoMove if we're in demo mode and move
			 * if we're in user playable mode. Call the appropriate one here
			 */
			if (b.demo) {
				b.player.demoMove();
			} else {
				b.player.move();
			}

			if (b.demo) {
				/* Also move the ghosts, and update the pellet states */
				b.blinky.move();
				b.clyde.move();
				b.inky.move();
				b.pinky.move();
				b.player.updatePellet();
				b.blinky.updatePellet();
				b.clyde.updatePellet();
				b.inky.updatePellet();
				b.pinky.updatePellet();
			}
		}

		/*
		 * We either have a new game or the user has died, either way we have to reset
		 * the board
		 */
		if (b.stopped || New) {
			/* Temporarily stop advancing frames */
			frameTimer.stop();

			/* If user is dying ... */
			while (b.dying > 0) {
				/* Play dying animation. */
				stepFrame(false);
			}

			/* Move all game elements back to starting positions and orientations */
			b.player.currDirection = 'L';
			b.player.direction = 'L';
			b.player.desiredDirection = 'L';

			if (b.demo) {
				b.player.x = 200;
				b.player.y = 300;
				b.blinky.x = 200;
				b.blinky.y = 160;
				b.clyde.x = 200;
				b.clyde.y = 180;
				b.inky.x = 220;
				b.inky.y = 180;
				b.pinky.x = 180;
				b.pinky.y = 180;
			}

			/* Advance a frame to display main state */
			b.repaint(0, 0, 600, 600);

			/* Start advancing frames once again */
			b.stopped = false;
			frameTimer.start();
		}
		/* Otherwise we're in a normal state, advance one frame */
		else {
//			repaint();
			repaint(b.blinky.x, b.blinky.y, b.clyde.x, b.clyde.y, b.inky.x, b.inky.y, b.pinky.x, b.pinky.y);
		}
	}

	/* Handles user key presses */
	public void keyPressed(KeyEvent e) {
		/* Pressing a key in the title screen starts a game */
		if (b.titleScreen) {
			b.titleScreen = false;
			return;
		}
		/*
		 * Pressing a key in the win screen or game over screen goes to the title screen
		 */
		else if (b.winScreen || b.overScreen) {
			b.titleScreen = true;
			b.winScreen = false;
			b.overScreen = false;
			return;
		}
		/* Pressing a key during a demo kills the demo mode and starts a new game */
		else if (b.demo) {
			b.demo = false;
			/* Stop any pacman eating sounds */
			b.sounds.nomNomStop();
			b.New = 1;
			return;
		}

		/* Otherwise, key presses control the player! */
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			b.player.desiredDirection = 'L';
			break;
		case KeyEvent.VK_RIGHT:
			b.player.desiredDirection = 'R';
			break;
		case KeyEvent.VK_UP:
			b.player.desiredDirection = 'U';
			break;
		case KeyEvent.VK_DOWN:
			b.player.desiredDirection = 'D';
			break;
		}
		b.player.updatePellet();
		b.blinky.updatePellet();
		b.clyde.updatePellet();
		b.inky.updatePellet();
		b.pinky.updatePellet();
		repaint();
//		repaint(b.blinky.x, b.blinky.y, b.clyde.x, b.clyde.y, b.inky.x, b.inky.y, b.pinky.x, b.pinky.y);
	}

	/*
	 * This function detects user clicks on the menu items on the bottom of the
	 * screen
	 */
	public void mousePressed(MouseEvent e) {
		if (b.titleScreen || b.winScreen || b.overScreen) {
			/* If we aren't in the game where a menu is showing, ignore clicks */
			return;
		}

		/* Get coordinates of click */
		int x = e.getX();
		int y = e.getY();
		if (400 <= y && y <= 460) {
			if (100 <= x && x <= 150) {
				/* New game has been clicked */
				b.New = 1;
			} else if (180 <= x && x <= 300) {
				/* Clear high scores has been clicked */
				b.clearHighScores();
			} else if (350 <= x && x <= 420) {
				/* Exit has been clicked */
				System.exit(0);
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void server() {
		try {
			while (true) {
				ServerSocket socket = new ServerSocket(35557);

				// Se acepta una conexión con un cliente. Esta llamada se queda
				// bloqueada hasta que se arranque el cliente.
//				System.out.println("Esperando cliente");
				Socket cliente = socket.accept();
//				System.out.println("Conectado con cliente de " + cliente.getInetAddress());

				// Se hace que el cierre del socket sea "gracioso". Esta llamada sólo
				// es necesaria si cerramos el socket inmediatamente después de
				// enviar los datos (como en este caso).
				// setSoLinger() a true hace que el cierre del socket espere a que
				// el cliente lea los datos, hasta un máximo de 10 segundos de espera.
				// Si no ponemos esto, el socket se cierra inmediatamente y si el
				// cliente no ha tenido tiempo de leerlos, los datos se pierden.
				cliente.setSoLinger(true, 10);

				// Se prepara el flujo de entrada de datos, es decir, la clase encargada
				// de leer datos del socket.
				DataInputStream bufferEntrada = new DataInputStream(cliente.getInputStream());

				// Se crea un dato a leer y se le dice que se rellene con el flujo de
				// entrada de datos.
				DatoSocket aux = new DatoSocket("");
				aux.readObject(bufferEntrada);
//                System.out.println ("Servidor java: Recibido " + aux.toString());

				String aux2 = aux.toString();
				String[] words = aux2.split(" ");
				for (int i = 0; i <= words.length - 1; i++) {
//					System.out.println(words[i]);

					if (words[i].equals("G")) {
						repaint(Integer.parseInt(words[4]), Integer.parseInt(words[5]), Integer.parseInt(words[6]),
								Integer.parseInt(words[7]), Integer.parseInt(words[8]), Integer.parseInt(words[9]),
								Integer.parseInt(words[10]), Integer.parseInt(words[11]));
					} else if (words[i].equals("F")) {
						b.player.appearFruit = true;
						b.player.Fruitscore = Integer.parseInt(words[3]);
					} else if (words[i].equals("P")) {
						b.player.bigPelletX = Integer.parseInt(words[3]);
						b.player.bigPelletY = Integer.parseInt(words[4]);
						b.player.bigPelletON = true;
						b.player.totalPellets++;
					} else if (words[i].equals("R") || words[i].equals("L") || words[i].equals("U") || words[i].equals("D")) {
						b.blinky.move();
						b.clyde.move();
						b.inky.move();
						b.pinky.move();
						b.player.updatePellet();
						b.blinky.updatePellet();
						b.clyde.updatePellet();
						b.inky.updatePellet();
						b.pinky.updatePellet();
						repaint();
					} else if (words[i].equals("E")) {
						b.player.newLifeOn = true;
						b.player.lifesCounter = Integer.parseInt(words[3]);
					} else if (words[i].equals("O")) {
						b.player.death = true;
						b.player.lifesCounter = Integer.parseInt(words[3]);
//					} else if (words[i].equals("H")) {
//						score = Integer.parseInt(words[3]);
					} else if (words[i].equals("W")){
						
					}
				}

				// Se cierra el socket con el cliente.
				// La llamada anterior a setSoLinger() hará
				// que estos cierres esperen a que el cliente retire los datos.
				cliente.close();

				// Se cierra el socket encargado de aceptar clientes. Ya no
				// queremos más.
				socket.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class Multi extends Thread {
		public void run() {
			server();
		}
	}
	
	/* Main function simply creates a new pacman instance */
	public static void main(String[] args) {
		Pacman c = new Pacman();
	}
}
