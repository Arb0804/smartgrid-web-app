/*
* Project: SmartGrid Load Shedding Optimizer
* Class: ElectricalGridZone.java
* Description: Parent class representation power grid sector. Foundation for inheritance/polymorphism.
* @Author: Areeb Bhuiyan
* @Version: June 7, 2026
* Citation: Oracle Java Documentation - Lesson: Inheritance + Predefined Annotation Types
* (https://docs.oracle.com/javase/tutorial/java/landl/subclass.html)
* (https:// docs.oracle.com/javase/tutorial/java/annotations/predefined.html)
*/

public class ElectricalGridZone
{
  // Instance Variables
  private String zoneName;
  private double currentLoadKW;
  private double maxCapacityKW;
  private int zoneInternalState; // 1=Active, 2=Throttled, 3=Offline

  // Constructor
  public ElectricalGridZone(String zoneName, double currentLoadKW, double maxCapacityKW)
  {
    this.zoneName = zoneName;
    this.currentLoadKW = currentLoadKW;
    this.maxCapacityKW = maxCapacityKW;
    this.zoneInternalState = 1; // All zones start at active state (1)
  }

  // Polymorphic Methods
  /*
  * First applied when a power reduction is needed
  * Left empty here so that child classes can override in their own classes
  */
  public void partialReduction()
  {
    // Does nothing. Will be overriden by child subclass
  }
  /*
  * Applied when an immediate power reduction is needed
  * Left empty so that child classes can override in their own classes
  */
  public void totalShutdown()
  {
    // Does nothing. Will be overridne by child subclass
  }

  // Getter and Setter Methods
  public String getZoneName()
  {
    return this.zoneName;
  }
  public double getCurrentLoadKW()
  {
    return this.currentLoadKW;
  }
  public void setCurrentLoadKW(double currentLoadKW)
  {
    this.currentLoadKW=currentLoadKW;
  }
  public double getMaxCapacityKW()
  {
    return this.maxCapacityKW;
  }
  public void setMaxCapacityKW(double maxCapacityKW)
  {
    this.maxCapacityKW=maxCapacityKW;
  }
  public int getZoneInternalStateInt()
  {
    return this.zoneInternalState;
  }
  /*
  * Protected setter method only allows child subclasses to update internal state while blocking external access
  * @param zonInternalState
  */
  protected void setZoneInternalState(int zoneInternalState)
  {
    this.zoneInternalState = zoneInternalState;
  }
  public String getZoneInternalStateString()
  {
    if(this.zoneInternalState == 1)
    {
      return "ACTIVE";
    } else if (this.zoneInternalState == 2)
    {
      return "THROTTLED";
    } else 
    {
      return "OFFLINE";
    }
  }
  public String getZoneReport() 
  {
    return "Sector: " + this.zoneName + " | Load: " + this.currentLoadKW + " kW | Max: " + this.maxCapacityKW + " kW | Status: " + this.getZoneInternalStateString();
  }
}