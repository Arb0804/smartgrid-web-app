/*
* Project: SmartGrid Load Shedding Optimizer
* Class: CriticalInfrastructureZone.java
* Description: Subclass represents high priority infrastructure (Hospitials, Emergency Services).
* @Author: Areeb Bhuiyan
* @Version: June 7, 2026
* @Citation: Oracle Java Documentation - Lesson: Inheritance + Predefined Annotation Types
* (https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html)
* (https://docs.oracle.com/javase/tutorial/java/annotations/predefined.html)
*/

public class CriticalInfrastructureZone extends ElectricalGridZone
{
  // Unique subclass instance variable
  private int backupGeneratorCount;

  // Constructor
  public CriticalInfrastructureZone(String zoneName, double currentLoadKW, double maxCapacityKW, int backupGeneratorCount)
  {
    super(zoneName, currentLoadKW, maxCapacityKW);
    this.backupGeneratorCount=backupGeneratorCount;
  }

  //Overriding Polymorhpic Methods
  /*
  * Reduces power consumption of non-essential system by 10%
  */
  @Override
  public void partialReduction()
  {
    System.out.println("WARNING: Power THROTTLED in CRITICAL sector: " + this.getZoneName() + "\nPower Draw reduced by 10%"); //Prints a warning when this method is called
    
    //Reduce current load by 10%
    double updatedLoad = this.getCurrentLoadKW() * 0.9;
    this.setCurrentLoadKW(updatedLoad);

    //Update internal state to THROTTLED (2)
    this.setZoneInternalState(2);
  }
  /*
  * Shuts power down to 0
  */
  @Override
  public void totalShutdown()
  {
    System.out.println("WARNING: Power OFFLINE ordered for CRITICAL sector: " + this.getZoneName() + "\nWARNING: Power to life saving sector has been cut. Auxiliary generators active: " + this.backupGeneratorCount); // Prints warning when this method is called

    // Set load to 0
    this.setCurrentLoadKW(0.0);

    // Update internal state to OFFLINE (3)
    this.setZoneInternalState(3);
  }

  // Getter and Setter Methods
  public int getBackupGeneratorCount()
  {
    return this.backupGeneratorCount;
  }
  public void setBackupGeneratorCount(int backupGeneratorCount)
  {
    this.backupGeneratorCount = backupGeneratorCount;
  }
}