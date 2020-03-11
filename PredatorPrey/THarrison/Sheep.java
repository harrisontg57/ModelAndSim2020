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
  public String name;

  public Sheep(String name, int x, int y)
	{
    super(new Pair(new Integer(x), new Integer(y)));
    location = new int[2];
    location[0] = x;
    location[1] = y;
    this.name = name;
    addOutport("outputt");
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
    holdIn("living", 1.5);
  }

  public void deltint() {
  //  System.out.println(name + " Sheep Self " + getPhase() + life + " " + this.getSigma() + " " + rep);
    if (phaseIs("living")) {
  //    System.out.println(name + " Sheep Moves " + life + " " + this.getSigma() + " " + rep);
      life = life - moveT;
      rep = rep - moveT;
      if (life <= 0) {
  //      System.out.println(name + " Sheep Dies");
        alive = false;
        passivateIn("dead"); //Trigger new death action
      } else {

      }
      if (rep <= 0) {
        rep = repT;
        birth = true;
      }
      holdIn("living", moveT);
    }
  }


  public message out() {
    message m = super.out();

    return m;
  }

  public void feed() {
    //this.setSigma(lifeT);
    life = lifeT;
  //  holdIn("living", moveT);
  }

}
