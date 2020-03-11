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

import twoDCellSpace.*;


import genDevs.simulation.heapSim.HeapCoord;
import genDevs.simulation.special.*;

public class SheepGrassCellSpace extends TwoDimCellSpace {

//		Make constructor which creates cells and couples them properly (TwoDimCellSpace has an add cell function)
private int senario;
protected Random r;
public int sheep_count = 0;
public ArrayList<Sheep> sheeps = new ArrayList<Sheep>();
protected DEVSQueue unborn = new DEVSQueue();;

public GlobalRef grid;
	public SheepGrassCellSpace()
	{
		this("Sheepland");

	}

	public SheepGrassCellSpace(String name)
	{
		super(name, 30, 30);
		senario = 5;
		SheepGrassConst();
	}

	public void SheepGrassConst() {

		grid = GlobalRef.getInstance();
		grid.setDim(31,31);

		for(int x = 0; x <= this.xDimCellspace; x++) {
			for(int y = 0; y <= this.yDimCellspace; y++) {
				SheepGrassCell c = new SheepGrassCell(x,y,this);
				addCell(c,x,y);
				grid.addCell(c,x,y);
			}
		}
		loadSheep();
		r = new Random(1234576543);
		doNeighborToNeighborCoupling();
		DoBoundaryToBoundaryCoupling();
		coupleOneToAll(this, "stop", "stop");
    coupleOneToAll(this, "start", "start");

		CellGridPlot t = new CellGridPlot("SheepGrassCellSpace", 0.1,
                                      "", 400, "", 400);

    t.setCellSize(10);
    t.setCellGridViewLocation(570, 100);
    add(t);
    // t.setHidden(false);
		coupleAllTo("outDraw", t, "drawCellToScale");

		switch(senario) {
      case 1:
				grid.cell_ref[13][13].setMode("grass");
        break;
      case 2:
				grid.cell_ref[r.nextInt(30)][r.nextInt(30)].setMode("grass");
				grid.cell_ref[r.nextInt(30)][r.nextInt(30)].setMode("grass");
        break;
			case 3:
				Sheep k = makeSheep(13,13);
				k.initialize();
			//	add(k);
	      break;
			case 4:
				Sheep k1 = makeSheep(13,13);
				Sheep k2 = makeSheep(3,20);
				k1.initialize();
				k2.initialize();
				//add(k1);
				//add(k2);
		    break;
			case 5:
				grid.cell_ref[11][13].setMode("grass");
				grid.cell_ref[12][13].setMode("grass");
				Sheep k3 = makeSheep(13,13);
				//k3.alive = true;
				k3.initialize();
				//add(k3);
				break;
    }

	}
	public void loadSheep() {
		int max = this.xDimCellspace * this.yDimCellspace * 3; //Make it a lot * this.yDimCellspace;
		for (int jk = 0; jk <= max; jk++) {
			Sheep k = new Sheep(getSheepCountString(), 0, 0);
			addSheep();
			add(k);
			//System.out.println(k);
			unborn.add(k);
		}
	}
	public Sheep makeSheep(int x, int y) {
		grid.state[x][y] = "sheep";
		Sheep k = (Sheep)unborn.first();
		unborn.remove();

		SheepGrassCell cc = grid.cell_ref[x][y];
		cc.sheep = true;
		cc.grass = false;
//		System.out.println(cc + "   k");
//		Sheep k = new Sheep(getSheepCountString(), x, y);
//		addSheep();
		cc.mySheep = k;
//		sheeps.add(k);
//		add(k);
		//k.initialize();
//		System.out.println(cc +" "+ cc.mySheep +"   ok");
		return k;
	}

	public void addSheep() {
		sheep_count = sheep_count + 1;
	}

	public int getSheepCount() {
		return sheep_count;
	}

	public String getSheepCountString() {
		return String.valueOf(sheep_count);
	}

	  public static void main(String args[]){
		//  coordinator r = new coordinator(new SheepGrassCellSpace());
	    TunableCoordinator r = new TunableCoordinator(new SheepGrassCellSpace());
	    r.setTimeScale(0.3);

	  r.initialize();

	  r.simulate(10000);
	  //System.exit(0);
	  }


///////////////////////////////////////////////////////////////////////////////////////
// The following are two utility functions that can be useful for you to finish the homework

/**
 * Add couplings among boundary cells to make the cell space wrapped
 */
	private void DoBoundaryToBoundaryCoupling()
    {
        //top and bottom rows
        for( int x = 1; x < xDimCellspace-1; x++ )
        {
            // (x,0) -- bottom to top
            addCoupling(withId(x, 0), "outS", withId(x, yDimCellspace-1), "inN");
            addCoupling(withId(x, 0), "outSW", withId(x-1, yDimCellspace-1), "inNE");
            addCoupling(withId(x, 0), "outSE", withId(x+1, yDimCellspace-1), "inNW");

            // (x,29) -- top to bottom
            addCoupling(withId(x, yDimCellspace-1), "outN", withId(x, 0), "inS");
            addCoupling(withId(x, yDimCellspace-1), "outNE", withId(x+1, 0), "inSW");
            addCoupling(withId(x, yDimCellspace-1), "outNW", withId(x-1, 0), "inSE");
        }

        //west and east columns
        for( int y = 1; y < yDimCellspace-1; y++ )
        {
            // (0,y) -- West - east
            addCoupling(withId(0, y), "outW", withId(xDimCellspace-1, y), "inE");
            addCoupling(withId(0, y), "outSW", withId(xDimCellspace-1, y-1), "inNE");
            addCoupling(withId(0, y), "outNW", withId(xDimCellspace-1, y+1), "inSE");

            // (29,y) -- West - east
            addCoupling(withId(xDimCellspace-1, y), "outE", withId(0, y), "inW");
            addCoupling(withId(xDimCellspace-1, y), "outNE", withId(0, y+1), "inSW");
            addCoupling(withId(xDimCellspace-1, y), "outSE", withId(0, y-1), "inNW");
        }
        //corners
        //(0, 0)
        addCoupling(withId(0, 0), "outNW", withId(xDimCellspace-1, 1), "inSE");
        addCoupling(withId(0, 0), "outW", withId(xDimCellspace-1, 0), "inE");
        addCoupling(withId(0, 0), "outSW", withId(xDimCellspace-1, yDimCellspace-1), "inNE");
        addCoupling(withId(0, 0), "outS", withId(0, yDimCellspace-1), "inN");
        addCoupling(withId(0, 0), "outSE", withId(1, yDimCellspace-1), "inNW");
        //(29, 0)
        addCoupling(withId(xDimCellspace-1, 0), "outSW", withId(xDimCellspace-2, yDimCellspace-1), "inNE");
        addCoupling(withId(xDimCellspace-1, 0), "outE", withId(0, 0), "inW");
        addCoupling(withId(xDimCellspace-1, 0), "outSE", withId(0, yDimCellspace-1), "inNW");
        addCoupling(withId(xDimCellspace-1, 0), "outS", withId(xDimCellspace-1, yDimCellspace-1), "inN");
        addCoupling(withId(xDimCellspace-1, 0), "outNE", withId(0, 1), "inSW");
        //(0, 29)
        addCoupling(withId(0, yDimCellspace-1), "outSW", withId(xDimCellspace-1, yDimCellspace-2), "inNE");
        addCoupling(withId(0, yDimCellspace-1), "outW", withId(xDimCellspace-1, yDimCellspace-1), "inE");
        addCoupling(withId(0, yDimCellspace-1), "outNE", withId(1, 0), "inSW");
        addCoupling(withId(0, yDimCellspace-1), "outN", withId(0, 0), "inS");
        addCoupling(withId(0, yDimCellspace-1), "outNW", withId(xDimCellspace-1, 0), "inSE");
        //(29, 29)
        addCoupling(withId(xDimCellspace-1, yDimCellspace-1), "outNW", withId(xDimCellspace-2, 0), "inSE");
        addCoupling(withId(xDimCellspace-1, yDimCellspace-1), "outE", withId(0, yDimCellspace-1), "inW");
        addCoupling(withId(xDimCellspace-1, yDimCellspace-1), "outSE", withId(0, yDimCellspace-2), "inNW"); // Xiaolin Hu, 10/16/2016
        addCoupling(withId(xDimCellspace-1, yDimCellspace-1), "outN", withId(xDimCellspace-1, 0), "inS");
        addCoupling(withId(xDimCellspace-1, yDimCellspace-1), "outNE", withId(0, 0), "inSW");
    }

	/**
	 * Get the x and y coordinate (int[2]) of a neighbor cell based on the direction in a wrapped cell space
	 * @param myCell: the center cell
	 * @param direction: the direction defines which neighbor cell to get. 0 - N; 1 - NE; 2 - E; ... (clokewise)
	 * @return the x and y coordinate
	 */
    public int[] getNeighborXYCoord(TwoDimCell myCell, int direction )
    {
        int[] myneighbor = new int[2];
        int tempXplus1 = myCell.getXcoord()+1;
        int tempXminus1 = myCell.getXcoord()-1;
        int tempYplus1 = myCell.getYcoord()+1;
        int tempYminus1 = myCell.getYcoord()-1;

        if( tempXplus1 >= xDimCellspace)
            tempXplus1 = 0;

        if( tempXminus1 < 0 )
            tempXminus1 = xDimCellspace-1;

        if( tempYplus1 >= yDimCellspace)
            tempYplus1 = 0;

        if( tempYminus1 < 0 )
            tempYminus1 = yDimCellspace-1;

        // N
        if( (direction == 0) )
        {
            myneighbor[0] = myCell.getXcoord();
            myneighbor[1] = tempYplus1;
        }
        // NE
        else if( (direction == 1) )
        {
            myneighbor[0] = tempXplus1;
            myneighbor[1] = tempYplus1;
        }
        // E
        else if( (direction == 2) )
        {
            myneighbor[0] = tempXplus1;
            myneighbor[1] = myCell.getYcoord();
        }
        // SE
        else if( (direction == 3) )
        {
            myneighbor[0] = tempXplus1;
            myneighbor[1] = tempYminus1;
        }
        // S
        else if( (direction == 4) )
        {
            myneighbor[0] = myCell.getXcoord();
            myneighbor[1] = tempYminus1;
        }
        // SW
        else if( (direction == 5) )
        {
            myneighbor[0] = tempXminus1;
            myneighbor[1] = tempYminus1;
        }
        // W
        else if( (direction == 6) )
        {
            myneighbor[0] = tempXminus1;
            myneighbor[1] = myCell.getYcoord();
        }
        // NW
        //( (direction == 7) )
        else
        {
            myneighbor[0] = tempXminus1;
            myneighbor[1] = tempYplus1;
        }
        return myneighbor;
    }




}
// End SheepGrassCellSpace
