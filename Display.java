public class Display {
  private char[][] dispMatrix;
  private String eLine = ""; //100 equals in a string
  private int tick;

  //initialize display
  public Display() {
    dispMatrix = new char[15][100];
    for(int i = 0; i<100; i++)
      eLine += "=";
  }

  public void init() {
    clearDisplay();
    tick = 0;
  }

  public void clearDisplay() {
    int i, j;
    for(i = 0; i < dispMatrix.length; i++)
      for(j = 0; j < dispMatrix[i].length; j++)
        if(i == dispMatrix.length-1)
          dispMatrix[i][j] = '=';
        else
          dispMatrix[i][j] = ' ';
  }

  public void setLoc(int x, int y, char newVal) {
    dispMatrix[x][y] = newVal;
  }

  public String toString() {
    int i,j;
    String printStr = "[H[J \n";
    printStr += "|" + eLine + "|\n";
    printStr += "| Score: " + tick;
    for(i = 0; i < 100 - ((" Score: " + tick).length()); i++) {
      printStr+=" ";
    }
    printStr+="|\n";
    printStr+="|" + eLine + "|\n";
    for(i = 0; i < dispMatrix.length; i++) {
      printStr += "|";
      for(j = 0; j < dispMatrix[i].length; j++)
        printStr += dispMatrix[i][j];
      printStr += "|\n";
    }
    tick++;
    return printStr;
  }
}
