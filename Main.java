/*
* Project: SmartGrid Load Shedding Optimizer
* Class: Main.java
* Description: Class uses user input for zone metrics, loads them into GridController, and launches GUI.
* @Author: Areeb Bhuiyan
* @Version: June 7, 2026
* @Citation: Oracle Java Documentation - Lesson: Type COmparison Operator
* (https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op2.html)
*/

import java.util.Scanner;

public class Main 
{
    public static void main(String[] args) 
  {
    Scanner scan = new Scanner(System.in); // Create Scanner object

    // Initailize the grid controller and launches GUI window pane
    System.out.println("Launching SmartGrid Municipal Telemetry Monitor Engine window.");
    GridController systemController = new GridController();

    // Ask user to input number of grid zones to make
    System.out.println("How many grid zone would you like to make today? Enter an integer, or, to quit, enter 0.");
    int gridZoneCount = getValidInteger(scan, 0, Integer.MAX_VALUE);
    System.out.println();

    //Checks if user wants to quit
    if(gridZoneCount == 0)
    {
      System.out.println("Closing program...");
      scan.close();
      System.exit(0);
    }

    int zoneCounter = 1; // Use to print Zone #1, Zone #2 and so on.

    while(gridZoneCount > 0)
    {
      System.out.println("Configuring Grid Zone #" + zoneCounter);

      System.out.println("Enter Zone Name (String): ");
      String zoneName = scan.nextLine();

      System.out.println("Enter Current Load Draw (kW)[Double]: ");
      double currentLoad = getValidDouble(scan, 0.0, Double.MAX_VALUE);

      System.out.println("Enter Maximum Capacity (kW)[Double]");
      double maxCapacity = getValidDouble(scan, 0.0, Double.MAX_VALUE);

      int zoneType = 0;
      System.out.println("Select the zone type:\n1. Residential Sector\n2. Industrial Sector\n3. Critical Infrastructure Sector\nEnter integer (1-3)");
      zoneType = getValidInteger(scan, 1, 3);

      if(zoneType == 1)
      {
        System.out.println("Enter Smart Thermostat Count [Integer]: ");
        int thermostats = getValidInteger(scan, 1, Integer.MAX_VALUE);
        
        ResidentialZone resZone = new ResidentialZone(zoneName, currentLoad, maxCapacity, thermostats);
        systemController.addZone(resZone);
        System.out.println("Added Residential Zone: " + zoneName);
      } else if(zoneType == 2)
      {
        System.out.println("Enter Number of Factory Shifts [Integer]: ");
        int shifts = getValidInteger(scan, 0, Integer.MAX_VALUE);
        
        IndustrialZone indZone = new IndustrialZone(zoneName, currentLoad, maxCapacity, shifts);
        systemController.addZone(indZone);
        System.out.println("Added Industrial Zone: " + zoneName);
      } else
      {
        System.out.println("Enter Backup Generator Count [Integer]: ");
        int generators = getValidInteger(scan, 1, Integer.MAX_VALUE);
        
        CriticalInfrastructureZone critZone = new CriticalInfrastructureZone(zoneName, currentLoad, maxCapacity, generators);
        systemController.addZone(critZone);
        System.out.println("Added Critical Infrastructure Zone: " + zoneName);
      }
      System.out.println();

      // In/decrements variables for the outer loop
      gridZoneCount--;
      zoneCounter++;
    }

    while(true)
    {
      System.out.println("Do you need to edit any inputted sectors? Enter 1 for yes or 2 or no.");
      int editChoice = getValidInteger(scan, 1, 2);

      if(editChoice == 2)
      {
        break;
      }

      System.out.println("Enter the exact sector name that requires alterations.");
      String targetName = scan.nextLine();

      ElectricalGridZone selectedZone = null;
      for(ElectricalGridZone zone : systemController.getManagedZones())
      {
        if(zone.getZoneName().equalsIgnoreCase(targetName))
        {
          selectedZone = zone;
          break;
        }
      }

      if(selectedZone == null)
      {
        System.out.println("Error: No sector name with name \"" + targetName+ "\" found. Please try again.");
        continue;
      }

    System.out.println("Modify Current Load Draw (kW)[Doube]:");
    double newLoad = getValidDouble(scan, 0.0, Double.MAX_VALUE);

    System.out.println("Modify Maximum Capcity (kW)[Double]:");
    double newCapacity = getValidDouble(scan, 0.0, Double.MAX_VALUE);

    if(selectedZone instanceof ResidentialZone)
    {
      System.out.println("Enter Smart Thermostat Count");
      int newThermostats = getValidInteger(scan, 0, Integer.MAX_VALUE);
      ((ResidentialZone) selectedZone).setSmartThermostatCount(newThermostats);
    } else if(selectedZone instanceof IndustrialZone)
    {
      System.out.println("Enter Factory Shift Count");
      int newShifts = getValidInteger(scan, 0, Integer.MAX_VALUE);
      ((IndustrialZone) selectedZone).setShifts(newShifts);
    } else if(selectedZone instanceof CriticalInfrastructureZone)
    {
      System.out.println("Enter Backup Generator Count");
      int newGenerators = getValidInteger(scan, 0, Integer.MAX_VALUE);
      ((CriticalInfrastructureZone) selectedZone).setBackupGeneratorCount(newGenerators);
    }

    selectedZone.setCurrentLoadKW(newLoad);
    selectedZone.setMaxCapacityKW(newCapacity);

    selectedZone.setZoneInternalState(1);

    systemController.updateDashboardText();
    System.out.println("Success: " +selectedZone.getZoneName()+" Modified.");
    }

    scan.close();
    
    // Print our baseline report
    System.out.println("Initial Telemetry Log");
    systemController.displayGridReport();

    System.out.println("Setup complete.\nUse the desktop GUI to execute load optimization");
  }
  
  // Helper Method to validate integer inputs
  private static int getValidInteger(Scanner scan, int min, int max)
  {
    while(true)
    {
      if(scan.hasNextInt())
      {
        int input = scan.nextInt();
        scan.nextLine(); // Clears buffer input \n
        
        if(input >= min && input <= max)
        {
          return input;
        } 
      } 
      else
      {
        scan.nextLine();
      }
      
      System.out.println("Invalid input. Please enter an integer between " + min + " and " + max + ".");
    }
  }
  
  // Helper Method to validate double inputs
  private static double getValidDouble(Scanner scan, double min, double max)
  {
    while(true)
    {
      if(scan.hasNextDouble())
      {
        double input = scan.nextDouble();
        scan.nextLine();
        if(input >= min && input <= max)
        {
          return input;
        } 
      } 
      else
      {
        scan.nextLine();
      }
      
      System.out.println("Invalid input. Please enter a double between " + min + " and " + max + ".");
    }
  }
}