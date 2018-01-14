import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

/*
  ================================================
  TERMINAL JUMPER
  VERSION 1.0

  by
  Oliver Frank,
  Shayan Chowdhury,
  Piotr Cwalina
  ================================================
  A game similar to google chrome's dinosaur jump
  game, where the player must time their jumps to
  make it over cacti (and hopefully other objects
  in later versions). This is only version 1 of
  this project, and there will be many more
  features to come, as well as improvement of the
  core game mechanics.
  ================================================
  Liscenced under the GNU General Public License,
  December 2017
  ================================================
*/
public class Run {
	private static int jumpD = 0; //jump delta, ticks since last jump, keeps track of place in jump
	private static boolean running; //whether or not game is running

	private static Player player;
	private static String playerName;
	private static Display display;
	private static CactusHandler CHandler;
	private static BirdHandler BHandler;
	private static HighScore highScore;

	public static final File save = new File("SaveData.csv");

	//Asks for alias/name of the player before each game
	private static void namePrompt() {
		System.out.println("What's yer name, m8io?");
		playerName = IOTools.readLine().replaceAll("[^A-Za-z]+", ""); //Removes any non-alphabetical (A-Z) characters using regular expressions
	}

	//Actions to be performed at the start of each game
	private static void newGame() throws IOException {
		if (!save.exists()) {
			save.createNewFile();
		}

		//(Re)create game
		namePrompt();
		display.init(); //reinititialize the display
		player.init(4, 10, display); //reinititialize the player
		CHandler.init(display); //reinitialize the cactusHandler
		BHandler.init(display);//reinitialize the birdhandler
	}

	//main game functionality
	private static void playGame() throws InterruptedException {
		//main game loop
		while (true) {
			//game should not continue (even by one update) if there is a collision
			if (CHandler.detectCollision(player) || BHandler.detectCollision(player)) { //if there is a collision then stop the game
				break;
			}
			//visuals
			display.clearDisplay(); //clear the display
			if ((int) (Math.random() * 2) < 1)
				CHandler.spawnEntity(); //Create new cacti if necessary
			else {
				BHandler.spawnEntity();//Create new birds if necessary
			}

			CHandler.updateEntities(); //move and draw cacti
			BHandler.updateEntities();//move and draw birds
			player.draw(); //draw the player to the display matrix
			System.out.println(display); //draw the display to the console
			//Check if spacebar is pressed and not already jumping
			if ((IOTools.checkSpace() && jumpD == 0) || (0 < jumpD && jumpD <= 12))
				jumpD++;
			else
				jumpD = 0;

			//Player jumps if jumpD is greater than 0
			player.jump(jumpD);

			//monitor updates per second
			Thread.sleep(60);
		}
	}

	//Clean up after the game
	private static void endGame() throws FileNotFoundException, IOException {
		jumpD = 0; //reset jumpD

		System.out.println("Congrats, " + playerName + "! Your score was " + display.getScore() + "!\n"); //Congratulates the player for accomplishments

		player.save(display.getScore()); //saves score and money to SaveData
		highScore.instantiate(); //Instantiating the work of the HighScore class
		System.out.println("The current leaderboard stands at: ");
		highScore.printScores(); //Prints out the top 5 high scores

		System.out.println("\nWould ye like to buy something from the shop? (y/n)");
		boolean answering = true;
		while (answering) { //make sure question is answered properly before moving on
			String ans = IOTools.readString(); //read from input
			if (ans.equals("y")) { //continue playing
				System.out.println("You currently have " + player.getMoney() + " terminal credits.");
				System.out.println("Shop machine broke. Please come again later.\n");
				break;
			} else if (ans.equals("n")) { //stop playing
				answering = false;
				break;
			} else //invalid input, ask question again
				System.out.println("Invalid input, please enter y or n.");
		}
		
		System.out.println("Would you like to keep playing? (y/n)");
		answering = true;
		while (answering) { //make sure question is answered properly before moving on
			String ans = IOTools.readString(); //read from input
			if (ans.equals("y")) { //continue playing
				running = true;
				answering = false;
			} else if (ans.equals("n")) { //stop playing
				running = false;
				answering = false;
			} else //invalid input, ask question again
				System.out.println("Invalid input, please enter y or n.");
		}

	}

	//Entry point
	public static void main(String[] args) throws InterruptedException, IOException {
		//instantiate the display, player, and cactus handler
		display = new Display();
		player = new Player(4, 10, display);
		CHandler = new CactusHandler(display);
		BHandler = new BirdHandler(display);

		running = true;
		//run games until player says n
		while (running) {
			//init the game
			newGame();
			//run through the game once
			playGame();
			//cleanUp game, reset variables
			endGame();
			//running = false; //only play one game
		}

	} //end main()

	//ACCESSORS
	public static String getName() {
		return playerName;
	}

} //end class
