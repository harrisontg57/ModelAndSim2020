package Homework2020.BridgeSegment.THarrison;

import BridgeSegment.AbstractBridgeSystem;
import java.awt.*;
import java.io.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import simView.*;

public class BridgeSystem extends AbstractBridgeSystem{
  public BridgeSystem(String name)
  {
    super(name);
    BridgeSystemConst();
  }
  public void BridgeSystemConst(){
    this.addOutport("WEST_OUT");
    this.addOutport("EAST_OUT");
    add(this.westCarGenerator);
    add(this.eastCarGenerator);

    BridgeSegment bridgeSegment1 = new BridgeSegment("BridgeSegment1");
    BridgeSegment bridgeSegment2 = new BridgeSegment("BridgeSegment2");
    BridgeSegment bridgeSegment3 = new BridgeSegment("BridgeSegment3");
    add(bridgeSegment1);
    add(bridgeSegment2);
    add(bridgeSegment3);
    add(this.transduser);

    addCoupling(this.westCarGenerator,"out",bridgeSegment3,"westbound_in");
    addCoupling(this.eastCarGenerator,"out",bridgeSegment1,"eastbound_in");

    addCoupling(bridgeSegment3,"westbound_out",bridgeSegment2,"westbound_in");
    addCoupling(bridgeSegment2,"westbound_out",bridgeSegment1,"westbound_in");

    addCoupling(bridgeSegment1,"eastbound_out",bridgeSegment2,"eastbound_in");
    addCoupling(bridgeSegment2,"eastbound_out",bridgeSegment3,"eastbound_in");

    addCoupling(bridgeSegment3,"eastbound_out",this,"EAST_OUT");
    addCoupling(bridgeSegment1,"westbound_out",this,"WEST_OUT");


    addCoupling(bridgeSegment1, "eastbound_out", this.transduser, "Bridge1_EastOut");
    addCoupling(bridgeSegment1, "westbound_out", this.transduser, "Bridge1_WestOut");
    addCoupling(bridgeSegment2, "eastbound_out", this.transduser, "Bridge2_EastOut");
    addCoupling(bridgeSegment2, "westbound_out", this.transduser, "Bridge2_WestOut");
    addCoupling(bridgeSegment3, "eastbound_out", this.transduser, "Bridge3_EastOut");
    addCoupling(bridgeSegment3, "westbound_out", this.transduser, "Bridge3_WestOut");

    addCoupling(this.westCarGenerator,"out",this.transduser,"WGEN");
    addCoupling(this.eastCarGenerator,"out",this.transduser,"EGEN");




  }
}
