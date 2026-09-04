/*
* Project: SmartGrid Load SHedding Optimizer
* CLass: ResidentialZone.java
* Description: Subclass represents residential power grid sector. Inherits ElectricalGridZone.
* @Author: Areeb Bhuiyan
* @Version: June 7, 2026
* Citation: Oracle Java Documentation - Lesson: Inheritance + Predefined Annotation Types
* (https://docs.oracle.com/javase/tutorial/java/IandI/subclass.html) 
* (https://docs.oracle.com/javase/tutorial/java/annotations/predefined.html)
*/
package com.smartgrid;

public class ResidentialZone extends ElectricalGridZone
{
  // Unique subclass instance variable
  private int smartThermostatCount;

  // Constructor
  public ResidentialZone(String zoneName, double currentLoadKW, double maxCapacityKW, int smartThermostatCount)
  {
    super(zoneName, currentLoadKW, maxCapacityKW);
    this.smartThermostatCount=smartThermostatCount;
  }

  // Overridng Polymorphic Methods

  /*
  * Simulates controlling smart thermostats to set the termperature of homes in order to easy engery demands
  */
  @Override
  public void partialReduction()
  {
    System.out.println("ALERT: Power THROTTLED in RESIDENTIAL sector " + this.getZoneName() + ". Number of Thermostats Eco-ed: " + this.getSmartThermostatCount()); // Prints an alert when this method is called
    
    // Drop current load by 30%
    double updatedLoad=this.getCurrentLoadKW()*0.70;
    this.setCurrentLoadKW(updatedLoad);

    // Update internal state to THROTTLED (2)
    this.setZoneInternalState(2);
  }

  /*
  * Drops neighborhood load completely to 0.
  */
  @Override
  public void totalShutdown()
  {
    System.out.println("ALERT: Power OFFLINE ordered in RESIDENTIAL sector " + this.getZoneName() + ". Number of Thermostats set to Safety Temperatures: " + this.getSmartThermostatCount()); // Prints an alert when this method is called
    
    // Cut power completely
    this.setCurrentLoadKW(0.0);

    //Update internal state to OFFLINE (3)
    this.setZoneInternalState(3);
  }

  //Getter and Setter Methods
  public int getSmartThermostatCount()
  {
    return this.smartThermostatCount;
  }
  public void setSmartThermostatCount(int smartThermostatCount)
  {
    this.smartThermostatCount = smartThermostatCount;
  }
}