import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

public class Player extends Entity {
  public static final File file = new File("SaveData.csv");
  private static String name;
  private static int lives = 1;
  private static double money;

  //inherits entMatrix,display, and location from Entity
    public Player(int startX, int startY, Display display,int lifes) {
	init(startX, startY, display, lifes);
  }

    public void init(int startX, int startY, Display display,int lifes) {
    this.display = display;
    lives = lifes;
    String charStr = " O /|\\ | / \\";
    entMatrix = new char[4][3];
    int cnt = 0;
    for (int i = 0; i < entMatrix.length; i++)
      for (int j = 0; j < entMatrix[i].length; j++) {
        entMatrix[i][j] = charStr.charAt(cnt);
        cnt++;
      }

    //Set starting location
    location[0] = startX;
    location[1] = startY;
  }

  //cX >= this.location[0] && cX <= this.location + 3
  public boolean colliding(Entity entity) {
    if (entity instanceof Cactus) {
      return ((entity.getX() >= this.location[0] && entity.getX() <= this.location[0] + 3)
          || (entity.getX() + 1 >= this.location[0] && entity.getX() + 1 <= this.location[0] + 3))
          && this.location[1] > entity.getY() - 4;
    } else {
      return ((entity.getX() >= this.location[0] && entity.getX() <= this.location[0] + 3)
          || (entity.getX() + 1 >= this.location[0] && entity.getX() + 1 <= this.location[0] + 3))
          && this.location[1] > entity.getY() - 2 && this.location[1] < entity.getY() + 1;
    }
  }

  public void jump(int jumpD) {
    if ((0 < jumpD && jumpD <= 3) || jumpD == 5)
      this.location[1] -= 1;
    else if (9 == jumpD || jumpD >= 11)
      this.location[1] += 1;
  }

  public void takeDamage() {
    lives--;
    if (lives <= 0) {

    }
  }

  public String getName() {
    return name;
  }

  public String setName(String newName) {
    String oldName = name;
    name = newName;
    return oldName;
  }
  
  //ACCESSORS
  public int getMoney() throws FileNotFoundException, IOException {
    Scanner scanner = new Scanner(file);
    scanner.nextLine(); //Skip the first (Title) line

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if (line.equals("*")) { //To prevent reading the same line twice
        break;
      }
      if (line.split(",")[0].equals(this.name)) {
        return Integer.parseInt(line.split(",")[2]);
      }
    }
    return 0;
  }
  
  public int getLives(){
    return lives;
  }

  public int setLives(int newLives){
    int oldLives = lives;
    lives = newLives;
    return oldLives;
  }
  
  //MUTATORS
  public void save(int s, int l) throws FileNotFoundException, IOException { //Takes score as input
    Scanner scanner = new Scanner(file);
    scanner.nextLine(); //Skip the first (Title) line

    int loot = l; //Money to be awarded to the player based on their score
    boolean playerExists = false;
    String playerData = "";

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();

      if (line.equals("*")) { //To prevent reading the same line twice
        break;
      }

      if (line.split(",")[0].equals(this.name)) { //Checks if entry for playerName already exists 
        playerExists = true; //EXISTS!
        playerData = line; //Copying the line string to a temporary playerData
        loot += Integer.parseInt(line.split(",")[2]); //Getting the money value they already have
        break;
      }
    }

    if (playerExists) {
      String oText = new Scanner(file).useDelimiter("\\A").next(); //Copies the entire txt file into a string using a delimiter

      if (s > Integer.parseInt(playerData.split(",")[1])) {//If current score is higher than their previous score
        loot += (int) (s / 7); //Bonus Money for improvement 
        oText = oText.replace(playerData, (this.name + "," + s + "," + loot)); //Replaces old playerData with new
      } else {
        loot += (int) (s / 10);
        oText = oText.replace(playerData, (this.name + "," + playerData.split(",")[1] + "," + loot)); //Replaces old playerData with new
      }

      Writer writer = new FileWriter(file);
      writer.write(oText); 
      writer.close();
    } else {
      loot = (int) (s / 7); //First time playing bonus
      Writer writer = new FileWriter(file, true); //the second parameter signifies that this is appending to the file instead of copying its contents and returning a slight variation of it
      writer.write(this.name + "," + s + "," + loot);
      writer.close();
    }

  }

  public static int oneUp() {
    return ++lives;
  }
}
