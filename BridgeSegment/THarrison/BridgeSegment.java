package Homework2020.BridgeSegment.THarrison;

import simView.*;

import java.lang.*;
import genDevs.modeling.*;
import genDevs.simulation.*;
import GenCol.*;
import BridgeSegment.AbstractBridgeSystem;

public class BridgeSegment extends ViewableAtomic{

protected DEVSQueue qWB;
protected DEVSQueue qEB;
protected entity car,currentCar = null;
protected double sigalT;
protected double lightTime;
protected double travelTime;

public BridgeSegment() {this("Bridge");}

public BridgeSegment(String name){
  super(name);
  addInport("westbound_in");
  addInport("eastbound_in");
  addOutport("westbound_out");
  addOutport("eastbound_out");
  travelTime = 3.0;
  //this.initialize();
}

public void initialize(){
  qWB = new DEVSQueue();
  qEB = new DEVSQueue();
  if(this.name=="BridgeSegment1"){
    lightTime = AbstractBridgeSystem.BridgeSystemSetting.Bridge1TrafficLightDurationTime;
  if(AbstractBridgeSystem.BridgeSystemSetting.Bridge1InitialState==AbstractBridgeSystem.BridgeState.WEST_TO_EAST) {
    holdIn("westToEastGreen", AbstractBridgeSystem.BridgeSystemSetting.Bridge1TrafficLightDurationTime);
  }
  else {
    holdIn("eastToWestGreen", AbstractBridgeSystem.BridgeSystemSetting.Bridge1TrafficLightDurationTime);
  }
  }
  if(this.name=="BridgeSegment2"){
    lightTime = AbstractBridgeSystem.BridgeSystemSetting.Bridge2TrafficLightDurationTime;
  if(AbstractBridgeSystem.BridgeSystemSetting.Bridge2InitialState==AbstractBridgeSystem.BridgeState.WEST_TO_EAST) {
    holdIn("westToEastGreen", AbstractBridgeSystem.BridgeSystemSetting.Bridge2TrafficLightDurationTime);
  }
  else {
    holdIn("eastToWestGreen", AbstractBridgeSystem.BridgeSystemSetting.Bridge2TrafficLightDurationTime);
  }
  }
  if(this.name=="BridgeSegment3"){
    lightTime = AbstractBridgeSystem.BridgeSystemSetting.Bridge3TrafficLightDurationTime;
  if(AbstractBridgeSystem.BridgeSystemSetting.Bridge3InitialState==AbstractBridgeSystem.BridgeState.WEST_TO_EAST) {
    holdIn("westToEastGreen", AbstractBridgeSystem.BridgeSystemSetting.Bridge3TrafficLightDurationTime);
  }
  else {
    holdIn("eastToWestGreen", AbstractBridgeSystem.BridgeSystemSetting.Bridge3TrafficLightDurationTime);
  }
  }
  sigalT = lightTime;
  //System.out.println(this.name);
  //System.out.println("#######");
}

public void  deltext(double e,message x){
  Continue(e);
//printCall();
//System.out.println(this.name + " deltaEX");
  for (int i=0; i< x.getLength();i++){
    if (messageOnPort(x, "eastbound_in", i)) {
      //System.out.println("EAST BOUND on " + this.name);
      if (phaseIs("westToEastGreen") || phaseIs("westToEastGreenCar") && currentCar==null) {
          sigalT = sigalT - e;
        if (qEB.isEmpty()) {
          if (sigalT >= travelTime) {
            car = x.getValOnPort("eastbound_in", i);
            currentCar = car;
            holdIn("westToEastGreenCar", travelTime);
          //System.out.println(car + "EAST BOUND on " + this.name);
        } else {
          car = x.getValOnPort("eastbound_in", i);
          qEB.add(car);
        }
        }
        else {
          car = x.getValOnPort("eastbound_in", i);
          qEB.add(car);
          //System.out.println(car + " IN QUEUE on " + this.name);
        }

      }
      else {
        car = x.getValOnPort("eastbound_in", i);
        qEB.add(car);
        //System.out.println(car + " IN QUEUE on " + this.name);
      }

    }
    else if(true){
      //System.out.println("WEST BOUND on " + this.name);
      if (phaseIs("eastToWestGreen") || phaseIs("eastToWestGreenCar") && currentCar==null) {

        sigalT = sigalT - e;
      if (qWB.isEmpty()) {
        if (sigalT >= travelTime) {
          car = x.getValOnPort("westbound_in", i);
          currentCar = car;
          holdIn("eastToWestGreenCar", travelTime);
        //System.out.println(car + "EAST BOUND on " + this.name);
      } else {
        car = x.getValOnPort("westbound_in", i);
        qWB.add(car);
      }
      }
      else {
        car = x.getValOnPort("westbound_in", i);
        qWB.add(car);
        //System.out.println(car + " IN QUEUE on " + this.name);
      }

    }
      else {
        //System.out.println("Added to WB QUEUE LIGHT " + this.name);
        car = x.getValOnPort("westbound_in", i);
        qWB.add(car);
      }
    }
  }

//@@@@@@@@@@@@

//@@@@@@@@@@@@@
//printCall();
//System.out.println();
}

public void  deltint( )
{
  //System.out.println(sigalT + " time left on " + this.name);

//printCall();
//System.out.println(this.name + " DELTAINT");
if(phaseIs("westToEastGreenCar")){
  if (sigalT <= travelTime) {
    //System.out.println("fuck");
    //System.out.println(sigalT + " lightSwitchWest " + this.name);
    holdIn("westToEastGreen", sigalT);
  }
  else if (sigalT >= travelTime) {
    //System.out.println(" STILL TIME EAST BOUND ON " + this.name);
    sigalT = sigalT - travelTime;
    if (!qEB.isEmpty()) {

      car = (entity)qEB.first();
      currentCar = car;
      qEB.remove();
      holdIn("westToEastGreenCar", travelTime);

    } else {
      holdIn("westToEastGreen", sigalT);
      currentCar = null;
    }
  }
  else {
    holdIn("westToEastGreen", sigalT);
    //currentCar = null;
  }
}
else if(phaseIs("eastToWestGreenCar")) {
  if (sigalT <= travelTime) {
    //System.out.println("fuck EW");
    //System.out.println(sigalT + " lightSwitchEast " + this.name);
    holdIn("eastToWestGreen", sigalT);
  }
  else if (sigalT >= travelTime) {
    //System.out.println(" STILL TIME WEST BOUND ON " + this.name);
    sigalT = sigalT - travelTime;
    if (!qWB.isEmpty()) {

      car = (entity)qWB.first();
      currentCar = car;
      qWB.remove();
      holdIn("eastToWestGreenCar", travelTime);

    } else {
      holdIn("eastToWestGreen", sigalT);
      currentCar = null;
    }
  }
  else {
    holdIn("eastToWestGreen", sigalT);
    //currentCar = null;
  }
  }
else if(phaseIs("eastToWestGreen") || phaseIs("westToEastGreen")) {
    //System.out.println("Switching??????");
    sigalT = lightTime;
    if(phaseIs("eastToWestGreen")) {
      if(qEB.isEmpty()){
        holdIn("westToEastGreen", lightTime);
      } else {
        car = (entity)qEB.first();
        currentCar = car;
        holdIn("westToEastGreenCar", travelTime);
        qEB.remove();
      }
    } else {
      if(qWB.isEmpty()){
        holdIn("eastToWestGreen", lightTime);
      } else {
        car = (entity)qWB.first();
        currentCar = car;
        holdIn("eastToWestGreenCar", travelTime);
        qWB.remove();
      }
    }

  }
  //sigalT = sigalT - sigma;
//printCall();
//System.out.println();
}

public message  out( )
{
 message  m = new message();
if (phaseIs("eastToWestGreenCar")) {
  content con = makeContent("westbound_out", currentCar);
  m.add(con);
}
else if(phaseIs("westToEastGreenCar")) {
 content con = makeContent("eastbound_out", currentCar);
 m.add(con);
}

 return m;
}

public void printCall() {
  //+ getPhase()
  //System.out.println(this.name);
  System.out.println(this.name + " " + getPhase() + " " + currentCar + " " + " EB: " + qEB + "| WB: " + qWB + " " + sigalT + " " + getSigma() + " ");
}

}
