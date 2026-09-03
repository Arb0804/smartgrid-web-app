/*
* Project: SmartGrid Load Shedding Optimizer
* Class: GridController.java
* Description: Class stores grid sectors. Evalutes power draw and loops polymorphic reduction rules.
* @Author: Areeb Bhuiyan
* @Version: June 7,2026
* @Citation: Oracle Java Documentation - Lesson: Swing + Type Comparison Operator
* (https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op2.html)
* (https://docs.oracle.com/javase/tutorial/uiswing/)
*/

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class GridController
{
  // Instance Variables
  private ArrayList<ElectricalGridZone> managedZones;

  private static final double SHUTDOWN_THRESHOLD_MULTIPLIER = 2.0;
  private static final int MAX_REDUCTION_LIMIT = 10;

  // GUI Window Components
  private JFrame guiFrame;
  private JTextArea dashboardDisplayArea;
  private JButton optimizationTriggerButton;
  private JButton exitProgramButton;

  //Constructor
  public GridController()
  {
    this.managedZones = new ArrayList<ElectricalGridZone>(); // Creates and set managedZone into an Arraylist
    setupGridDashboardGUI(); //Initialize Java Swing Graphic Layout WIndow
  }

  /*
  * Adds an instaniated grid sector subclass object to list.
  * @param newZone The ElectricalGridZone subclass object (Residential, Industrial, Critical)
  */
  public void addZone(ElectricalGridZone newZone)
  {
    managedZones.add(newZone); //Adds electrical GridZone object to the list
    updateDashboardText(); //Refreshs GUI window
  }

  /*
  * Core Engine Loop. Scans every sector in ArrayList managedZone. Continously applies partial shedding reductions until zone load falls below maximum capacity or hits the hard limit of shedding attempts
  */
  public void loadShedding()
  {
    for(ElectricalGridZone currentZone : managedZones)
    {
      if(currentZone.getCurrentLoadKW() > (SHUTDOWN_THRESHOLD_MULTIPLIER * currentZone.getMaxCapacityKW()))
      {
        currentZone.totalShutdown();
      } else if(currentZone.getCurrentLoadKW() > currentZone.getMaxCapacityKW())
      {
        int sheddingCounts = 0;

        while(currentZone.getCurrentLoadKW() > currentZone.getMaxCapacityKW())
        {
          sheddingCounts++;
          currentZone.partialReduction();
          if(sheddingCounts>=MAX_REDUCTION_LIMIT) // Gives a hard limit to the loop to prevent infinte looping
          {
            System.out.println("ALERT: THROTTLED has been called 10 times on sector " + currentZone.getZoneName()+ ". Escalating: Power shutdown required. Breaking out of potential infinite loop...");
            currentZone.totalShutdown();
            break; // Break out of the while loop to prevent infinite loop
          }
        }
      }
    }
    updateDashboardText(); // Refresh dashboard with the statistics to the Swing window
  }

  /*
  * Builds and structures java Swing Panel
  */
  private void setupGridDashboardGUI()
  {
    // Creates top level panel framwork
    guiFrame = new JFrame("SmartGrid Municipal Monitor Engine");
    guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    guiFrame.setSize(650, 450);
    guiFrame.setLayout(new BorderLayout(10, 10));
    
    // Setup for updates area
    dashboardDisplayArea = new JTextArea();
    dashboardDisplayArea.setEditable(false);
    dashboardDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
    dashboardDisplayArea.setBackground(new Color(245, 245, 245));
    dashboardDisplayArea.setMargin(new Insets(10, 10, 10, 10));

    JScrollPane scrollPane = new JScrollPane(dashboardDisplayArea);
    guiFrame.add(scrollPane, BorderLayout.CENTER);

    //Construct button interface
    optimizationTriggerButton = new JButton("Run Load Optimization");
    optimizationTriggerButton.setFont(new Font("Arial", Font.BOLD, 16));
    optimizationTriggerButton.setBackground(new Color(220, 53, 69));
    optimizationTriggerButton.setForeground(Color.BLACK);

    // Link action listener event trigger to loadShedding method
    optimizationTriggerButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        loadShedding(); // Calls optimizer method
      }
    });

    // Construct Exit Button
    exitProgramButton = new JButton("exit Simulation");
    exitProgramButton.setFont(new Font("Arial", Font.BOLD, 16));
    exitProgramButton.setBackground(Color.GRAY);
    exitProgramButton.setForeground(Color.BLACK);

    exitProgramButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        System.out.println("Exit Button Pressed. Closing Program...");
        System.exit(0);
      }
    });
  
    // Creat panel that holds both Optimizer Button and Exit Button
    JPanel buttonControlPanel = new JPanel();
    buttonControlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
    buttonControlPanel.add(optimizationTriggerButton);
    buttonControlPanel.add(exitProgramButton);

    guiFrame.add(buttonControlPanel, BorderLayout.SOUTH);

    dashboardDisplayArea.setText("Ready for simulation. Load grid objects into Main.java.");
    guiFrame.setVisible(true);
  }

  // Iterates through active zones inside the ArrayList
  public void updateDashboardText()
  {
    StringBuilder contentBuilder = new StringBuilder();
    contentBuilder.append("REAL TIME MUNICIPAL POWER GRID SIMULATION TELEMETRY OVERVIEW\n\n");

    double overallSystemDraw = 0.0;
    double overallMaxCapacity = 0.0;

    for(ElectricalGridZone zone : managedZones)
    {

      String zoneTypeString = "Unknown Sector";
      if(zone instanceof ResidentialZone)
      {
        zoneTypeString = "Residential Sector";
      } else if(zone instanceof IndustrialZone)
      {
        zoneTypeString = "Industrial Sector";
      } else if(zone instanceof CriticalInfrastructureZone)
      {
        zoneTypeString = "Critical Infrastructure Sector";
      }
      
      contentBuilder.append(" -> ").append(zone.getZoneName()).append(" (").append(zoneTypeString).append(")").append(" | Load: ").append(zone.getCurrentLoadKW()).append(" kW").append(" / Max: ").append(zone.getMaxCapacityKW()).append(" kW").append(" | State: [").append(zone.getZoneInternalStateString()).append("]\n");

      overallSystemDraw += zone.getCurrentLoadKW();
      overallMaxCapacity += zone.getMaxCapacityKW();
    }
    
    contentBuilder.append("\nCombined System Performance Charts Summary Metrics:\n");
    contentBuilder.append(" -> Consolidated Grid Consumption: ").append(overallSystemDraw).append(" kW\n");
    contentBuilder.append(" -> Total Allocated Infrastructure Margin: ").append(overallMaxCapacity).append(" kW\n");

    double denominator = overallMaxCapacity;
    if(overallMaxCapacity == 0) // Make sure that the program doesn't divide by 0
    {
      contentBuilder.append("No Metric capacity Avaiable (Capacity is equal to 0)");
    } else
    {
      // Text bar repsentation of pre vs post shed load ratios
      contentBuilder.append(" -> System Load Profile Chart: [");
      double calculationRatio = overallSystemDraw / overallMaxCapacity;
      int visualBars = (int) (calculationRatio * 20);
      for(int j = 0; j < 20; j++)
      {
        if(j < visualBars)
        {
          contentBuilder.append("#");
        } else 
        {
          contentBuilder.append("-");
        }
      }
      contentBuilder.append("] ").append(String.format("%.1f", calculationRatio *100)).append("%\n");
    }

    // Refresh the GUI to show comiled string
    dashboardDisplayArea.setText(contentBuilder.toString());
  }
  // Displays summary report for all sectors 
  public void displayGridReport()
  {
    System.out.println("Current Municipal Power Grid Simulation Telemetry");

    for(ElectricalGridZone zone : managedZones)
    {
      System.out.println("Sector Name: " + zone.getZoneName() + " | Current Load: " + zone.getCurrentLoadKW() + " kW | Capacity: " + zone.getMaxCapacityKW() + " kW | System Status: " + zone.getZoneInternalStateString());
    }
  }

  // Getter and Setter Methods
  public ArrayList<ElectricalGridZone> getManagedZones()
  {
    return this.managedZones;
  }
}