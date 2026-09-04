/*
* Project: SmartGrid Load Shredding Optimizer
* Class: IndustrialZone.java
* Description: Subclass represents industrial power grid sector. Inherits from ElectricalGridZone and tracks manufacturing loads.
* @Author: Areeb Bhuiyan
* @Version: June 7, 2026
* Citation: Oracle Java Documentation - Lesson: Inheritance + Predefined Annotation Types
* (https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html)
* (https://docs.oracle.com/javase/tutorial/java/java00/annotations.html)
*/
package com.smartgrid;

public class IndustrialZone extends ElectricalGridZone
{
  // Unique subclass instance variable
  private int shifts;

  // Constructor
  public IndustrialZone(String zoneName, double currentLoadKW, double maxCapacityKW, int shifts)
  {
    // Links parameters to parent class constructor
    super(zoneName, currentLoadKW, maxCapacityKW);
    this.shifts = shifts;
  }

  //Overriding Polymorphic Methods

  /*
  * Postpones secondary manufacturing shifts. Reduces currentLoadKW by 45%.
  */
  @Override
  public void partialReduction()
  {
    System.out.println("ALERT: Power THROTTLED in INDUSTRIAL sector " + this.getZoneName()+". Number of Shortened Factory Shifts: " + getShifts());// Prints an alert for when this method is called
    // Reduce current load by 45%
    double updatedLoad = this.getCurrentLoadKW() * 0.55;
    this.setCurrentLoadKW(updatedLoad);

    // Update internal state to THROTTLED (2)
    this.setZoneInternalState(2);
  }

  /*
  * Isolates industrial sector
  */
  @Override
  public void totalShutdown()
  {
    System.out.println("ALERT: Power OFFLINE ordered in INDUSTRIAL sector "+ this.getZoneName()+ ". Number of Factory Shifts cancelled: " + getShifts()); // Prints an alert when this method is called
      
    // Set load to 0
    this.setCurrentLoadKW(0.0);

    //Update internal state to OFFLINE (3)
    this.setZoneInternalState(3);
  }

  //Getter and Setter Methods
  public int getShifts()
  {
    return this.shifts;
  }
  public void setShifts(int shifts)
  {
    this.shifts = shifts;
  }
}

