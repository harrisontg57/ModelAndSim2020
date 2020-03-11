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
    addInport("inn"); //For Sheep
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
    //  System.out.println(mySheep);
      holdIn("newsheep", 0); //change time to mySheep.getNextEvent()
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
      if (c.getValue() instanceof Sheep) {

          mySheep = (Sheep)c.getValue();
          System.out.println(mySheep.name + " SHEEEP " + mySheep + " " + mySheep.birth + mySheep.alive + " " + mySheep.rep);
          if (mySheep.birth && mySheep.alive) {
            System.out.println(mySheep.name + " SHEEEP BIRTHHHHH " + mySheep);
            mySheep.birth = false;
            holdIn("birth", 0);
          } else if (mySheep.phaseIs("living")) {
            sheep = true;
            if (grass) {
              mySheep.feed();
              grass = false;
            }
            holdIn("newsheep", 0);
        } else if (mySheep.phaseIs("dead")) {
          System.out.println(mySheep.name + " SHEEEP DEAD " + this);
          sheep = false;
          mySheep = null;
          holdIn("clear", 0);
        }

      } else {
      String inn = c.toString();
      //System.out.println(inn);
      if (inn.contains("grow")) {
        //System.out.println("ok");
        grass = true;
        holdIn("grass", repR);
      } }

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
    else if (phaseIs("sheep")){
      sheep = false;
      mySheepSend = mySheep;
      mySheep = null;
      //mySheepSend.setSigma(mySheepSend.getSigma() - 1.5);
      holdIn("moved", 0);
    } else if (phaseIs("moved")) {
      //passivateIn("clear");
      sheep = false;
      holdIn("clear", 0);
    }
    else if (phaseIs("newsheep")){
      sheep = true;
      holdIn("sheep", 1.5);
    } else if (phaseIs("birth")) {
      sheep = true;
      holdIn("sheep", 1.5);
    }
  }

//  public void deltcon(double e, message x) {
    //deltint();
//  }

  public message out() {
    message m = super.out();
//    System.out.println("MSG " + this + "  " + getSimulationTime() + ": " + this.myLoc[0] + "," + this.myLoc[1] + " mySheep:" + mySheep + "," + sheep);
    if (phaseIs("newsheep")) {
      //System.out.println(mySheep.getName() + " NEWEWW " + this + "  " + getSimulationTime() + ": " + this.myLoc[0] + "," + this.myLoc[1] + " :" + mySheep + mySheep.birth);
      m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale",
          x_pos, y_pos, Color.blue, Color.blue)));
    } else if (phaseIs("grass")) {
  //    m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale",
  //        x_pos, y_pos, Color.green, Color.green)));

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
    } else if (phaseIs("moved")){
      sheep = false;
      System.out.println("??????" + mySheepSend + " " + mySheepSend.name);
      neighbors = new ArrayList();
      for (int n = 0; n < 8; n++) {
        int[] neighbor = world.getNeighborXYCoord(this, n);
        if (world.grid.hasGrass(neighbor)) {
          neighbors.add(n);
        }
      }
      if (!neighbors.isEmpty()) {
        int nn = world.r.nextInt(neighbors.size());
        int loc = (int)neighbors.get(nn);

        m.add(makeContent("out" + nhood[loc], mySheepSend));
      } else {
        neighbors = new ArrayList();
        for (int nm = 0; nm < 8; nm++) {
          int[] nneighbor = world.getNeighborXYCoord(this, nm);
          if (world.grid.isEmpty(nneighbor)) {
            neighbors.add(nm);
          }
        }
        if (!neighbors.isEmpty()) {
          int nn = world.r.nextInt(neighbors.size());
          int loc = (int)neighbors.get(nn);
          m.add(makeContent("out" + nhood[loc], mySheepSend));
        }

      }
      //mySheep = null;
    } else if (phaseIs("birth")){

      neighbors = new ArrayList();
      ArrayList nDirection = new ArrayList();
      for (int n = 0; n < 8; n++) {
        int[] neighbor = world.getNeighborXYCoord(this, n);
        if (world.grid.hasNoSheep(neighbor)) {
          neighbors.add(neighbor);
          nDirection.add(n);
        }
      }
      if (!neighbors.isEmpty()) {
        int nn = world.r.nextInt(neighbors.size());
        int[] loc = (int[])neighbors.get(nn);
        Sheep baby = world.makeSheep(loc[0],loc[1]);
        //world.add(baby);
        //baby.alive = true;
        baby.initialize();
        //baby.holdIn("living",0);
        m.add(makeContent("out" + nhood[(int)nDirection.get(nn)], baby));
      }
    }

    if (false) {
      //System.out.println("STILL " + this + "  " + getSimulationTime() + ": " + this.myLoc[0] + "," + this.myLoc[1]);
      m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale",
          x_pos, y_pos, Color.blue, Color.blue)));
    } else if (grass) {
      m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale",
              x_pos, y_pos, Color.green, Color.green)));
    } else if (!sheep && !grass) {
      //System.out.println("CLEAR " + this + "  " + getSimulationTime() + ": " + this.myLoc[0] + "," + this.myLoc[1]);
      m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale",
          x_pos, y_pos, Color.white, Color.white)));
    }

    return m;
  }

//  public String toString(){
//    String ss = new String();
//    ss = "Grass:" + this.grass + " Sheep:" + this.sheep;
//    return ss;
//  }

}
