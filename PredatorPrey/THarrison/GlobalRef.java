
package Homework2020.PredatorPrey.THarrison;

import genDevs.modeling.*;
import java.io.*;
import java.util.*;


/**
 * This class defines an GlobalRef that makes it easy to find a cell and its
 * information, such as current state of the cell, and the cell's reference
 *
 * @author  Xiaolin Hu
 * @Date: Sept. 2007
 */
public class GlobalRef {
  protected static int xDim;
  protected static int yDim;
  protected static GlobalRef _instance=null;
  public Random r = new Random(1234576543);

  public String[][] state;
  public SheepGrassCell[][] cell_ref;

  private GlobalRef(){}  // construction function

  public static GlobalRef getInstance(){
    if(_instance!=null) return _instance;
    else {
      _instance = new GlobalRef();
      return _instance;
    }
  }

  public void setDim(int x, int y){
    xDim = x;
    yDim = y;
    state = new String[xDim][yDim];
    cell_ref = new SheepGrassCell[xDim][yDim];
  }

  public void addCell(SheepGrassCell c, int x, int y) {
    state[x][y] = "c";
    cell_ref[x][y] = c;
  }

  public SheepGrassCell getSCell(int[] neighbor) {
    return cell_ref[neighbor[0]][neighbor[1]];
  }

  public boolean isEmpty(int[] neighbor) {
    if (!cell_ref[neighbor[0]][neighbor[1]].sheep && !cell_ref[neighbor[0]][neighbor[1]].grass) {
      return true;
    }
    return false;
  }

  public boolean hasGrass(int[] neighbor) {
    if (cell_ref[neighbor[0]][neighbor[1]].grass) {
      return true;
    }
    return false;
  }

  public boolean hasNoSheep(int[] neighbor) {
    if (cell_ref[neighbor[0]][neighbor[1]].sheep) {
      return false;
    }
    return true;
  }

}
