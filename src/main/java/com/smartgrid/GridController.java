/*
* Project: SmartGrid Load Shedding Optimizer
* Class: GridController.java
* Description: Core engine class that manages grid sector objects, evaluates power draw thresholds, and executes polymorphic load shedding algorithms.
* @Author: Areeb Bhuiyan
* @Version: September 3, 2026
* @Citation: Oracle Java Documentation - Lesson: Type Comparison Operator
* (https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op2.html)
*/
package com.smartgrid;

import java.util.ArrayList;

public class GridController {
  private ArrayList<ElectricalGridZone> managedZones;

  private static final double SHUTDOWN_THRESHOLD_MULTIPLIER = 2.0;
  private static final int MAX_REDUCTION_LIMIT = 10;

  public GridController() {
    this.managedZones = new ArrayList<ElectricalGridZone>();
  }

  public void addZone(ElectricalGridZone newZone) {
    managedZones.add(newZone);
  }

  public void clearZones() {
    managedZones.clear();
  }

  public void loadShedding() {
    for (ElectricalGridZone currentZone : managedZones) {
      if (currentZone.getCurrentLoadKW() > (SHUTDOWN_THRESHOLD_MULTIPLIER * currentZone.getMaxCapacityKW())) {
        currentZone.totalShutdown();
      } else if (currentZone.getCurrentLoadKW() > currentZone.getMaxCapacityKW()) {
        int sheddingCounts = 0;
        while (currentZone.getCurrentLoadKW() > currentZone.getMaxCapacityKW()) {
          sheddingCounts++;
          currentZone.partialReduction();
          if (sheddingCounts >= MAX_REDUCTION_LIMIT) {
            currentZone.totalShutdown();
            break;
          }
        }
      }
    }
  }

  public ArrayList<ElectricalGridZone> getManagedZones() {
    return this.managedZones;
  }
}