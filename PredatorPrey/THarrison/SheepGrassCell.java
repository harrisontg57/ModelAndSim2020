package Homework2020.PredatorPrey.THarrison;

import simView.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import genDevs.simulation.realTime.*;
import GenCol.*;
import genDevs.plots.*;
import java.util.*;
import java.awt.*;

import java.text.*;
import java.io.*;
import statistics.*;

import twoDCellSpace.*;

public class SheepGrassCell extends TwoDimCell {
  public boolean grass = false;
  public boolean sheep = false;
  private double repR = 2.0;
  private int[] myLoc = new int[2];
  //protected Random r;
  public SheepGrassCellSpace world;
  private String[] nhood = new String[8];
  public Sheep mySheep = null;
  public Sheep mySheepSend = null;
  private ArrayList neighbors;


//  public SheepGrassCell()
//  {
//		this(0,0);
//	}

  public SheepGrassCell(int x, int y, SheepGrassCellSpace g){
    super(new Pair(new Integer(x), new Integer(y)));
    myLoc[0] = x;
    myLoc[1] = y;
    world = g;
  //  addInport("inn"); //For Sheep
	}

  public void setMode(String s) {
    switch(s) {
      case "grass":
        this.grass = true;
        break;
      case "sheep":
        this.sheep = true;
        break;
    }
  }

  public void initialize() {
    //super.initialize();
    //System.out.println(this.grass + " " + this.sheep);
    nhood[0] = "N";
    nhood[1] = "NE";
    nhood[2] = "E";
    nhood[3] = "SE";
    nhood[4] = "S";
    nhood[5] = "SW";
    nhood[6] = "W";
    nhood[7] = "NW";

    //r = new Random(5);
    if (!this.sheep && !this.grass) {
      holdIn("clear", 0);
    } else if (this.grass) {
      holdIn("grass", 0);
    }
    else if (this.sheep){
      //FIX LATER
      //create sheep instance
      //mySheep = new Sheep(world.getSheepCountString(), myLoc[0], myLoc[1]);
      System.out.println(mySheep);
      holdIn("newsheep", 1.5); //change time to mySheep.getNextEvent()
    }
    //System.out.println(this);
  }

  public void deltext(double e, message x) {
    Continue(e);
    //System.out.println(x);
    //System.out.println(world.grid.cell_ref);
    for (int i=0; i< x.getLength();i++){

      //String inn = x.read(i).toString();
      content c = x.read(i);
      String inn = c.toString();
      //System.out.println(inn);
    //  System.out.println("HERE: " + c);
    //System.out.println(c);
      if (inn.contains("grow")) {
        //System.out.println(c.getValue());
        grass = true;
        holdIn("grass", repR);
      } else if (false) { //Change to c.getValue() == type Sheep??
          System.out.println(c.getValue());
      }

    }

  }

  public void deltint() {
//if (mySheep != null) {System.out.println(mySheep.name + " SHIT " + mySheep + " " + mySheep.birth + " " + mySheep.alive + " " + mySheep.rep + " " + mySheep.life);}
    if (phaseIs("clear")) {
      sheep = false;
      passivateIn("clear");
    }
    else if (phaseIs("grass")){
      //System.out.println(n);
      holdIn("grass", repR);
    }
  }

//  public void deltcon(double e, message x) {
    //deltint();
//  }

  public message out() {
    message m = super.out();
//    System.out.println("MSG " + this + "  " + getSimulationTime() + ": " + this.myLoc[0] + "," + this.myLoc[1] + " mySheep:" + mySheep + "," + sheep);
    if (phaseIs("grass")) {

      neighbors = new ArrayList();
      for (int n = 0; n < 8; n++) {
        int[] neighbor = world.getNeighborXYCoord(this, n);
        if (world.grid.isEmpty(neighbor)) {
          neighbors.add(n);
        }
      }
      if (!neighbors.isEmpty()) {
        int nn = world.r.nextInt(neighbors.size());
        int loc = (int)neighbors.get(nn);
        m.add(makeContent("out" + nhood[loc], new entity("grow")));
      }

    //  m.add(makeContent("outNW", new entity("grow")));

  } else if (phaseIs("clear")) {
    //  m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale",
    //      x_pos, y_pos, Color.white, Color.white)));
    }

    if (grass) {
      System.out.println("Draw Message Grass " + x_pos + "," + y_pos);
      m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale",
              x_pos, y_pos, Color.green, Color.green)));
    }else if (sheep) {
    //  System.out.println("CLEAR ");
    //  m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale",
    //      x_pos, y_pos, Color.red, Color.red)));
      m.add(makeContent("outCoord", new entity("sheep")));
    //  m.add(makeContent("outNE", new entity("sheep")));
      //System.out.println(m);
    } else if (!sheep && !grass) {
      //System.out.println("CLEAR " + this + "  " + getSimulationTime() + ": " + this.myLoc[0] + "," + this.myLoc[1]);
      //m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale",
      //    x_pos, y_pos, Color.white, Color.white)));
    }

    return m;
  }

}
