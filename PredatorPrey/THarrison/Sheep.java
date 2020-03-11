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


public class Sheep extends TwoDimCell{
  public int[] location;
  private double lifeT = 3.0;
  private double repT = 4.5;
  private double moveT = 1.5;
  public double life;
  public double rep;
  private String[] nhood = new String[8];
  public boolean birth = false;
  public boolean alive = true;
  //public String name;

  public Sheep(String name, int x, int y)
	{
    super(new Pair(new Integer(x), new Integer(y)));
    location = new int[2];
    location[0] = x;
    location[1] = y;
    //this.name = name;
  //  this.setPos();
    addInport("in");
    addTestInput("in", new Pair(new Integer(33), new Integer(35)));
	}

  public void initialize() {
    super.initialize();
    //System.out.println(name + " Sheep Lives XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
    nhood[0] = "N";
    nhood[1] = "NE";
    nhood[2] = "E";
    nhood[3] = "SE";
    nhood[4] = "S";
    nhood[5] = "SW";
    nhood[6] = "W";
    nhood[7] = "NW";
    life = lifeT;
    rep = repT;
    birth = false;
    alive = true;
    holdIn("waitForDir", 0);

  }

  public int[] getDirectionToward(Pair inpair) {
    Integer i = (Integer) inpair.getKey();
    Integer j = (Integer) inpair.getValue();
    int[] dir = {
        i.intValue() - xcoord,
        j.intValue() - ycoord
    };
    return dir;
  }

  public void changeCoordNPos(int[] dir) {
    setCoordNPos(xcoord + dir[0], ycoord + dir[1]);
  }

  public void move(Pair inpair, int step) {
    int[] dir = getDirectionToward(inpair);
    dir[0] = step * dir[0];
    dir[1] = step * dir[1];
    changeCoordNPos(dir);
  }

  public void deltext(double e, message x) {
    Continue(e);
    if (somethingOnPort(x, "start")) {
      System.out.println("Start");
      holdIn("living", 0);
    }
    if (somethingOnPort(x, "in")) {
      System.out.println("OUT RECIEVED ############################################# ");
      holdIn("living", 0);
    }

  }

  public void deltint() {
//    passivateIn("living");
    if (phaseIs("waitForDir")) {
      holdIn("living", 1.5);
    }
  }


  public message out() {
    message m = super.out();
    if (phaseIs("living")) {
    //  System.out.println("Draw Message Sheep " + x_pos + "," + y_pos);
      m.add(makeContent("outDraw", new DrawCellEntity("drawCellToScale",
          x_pos, y_pos, Color.red, Color.red)));
    }
  //  System.out.println(m);
    return m;
  }

}
