/*
* Project: SmartGrid Load Shedding Optimizer
* Class: GridApiController.java
* Description: REST Controller class that provides HTTP API endpoints for web clients to add grid sectors, fetch real-time telemetry, and trigger load shedding optimization.
* @Author: Areeb Bhuiyan
* @Version: September 3, 2026
* @Citation: Spring Framework Documentation - Building a RESTful Web Service
* (https://spring.io/guides/gs/rest-service/)
*/
package com.smartgrid;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/grid")
@CrossOrigin(origins = "*") // Allows web browser calls from local/hosted frontends
public class GridApiController 
{

    private final GridController gridController = new GridController();

    @GetMapping("/zones")
    public ArrayList<ElectricalGridZone> getZones() 
    {
        return gridController.getManagedZones();
    }

    @GetMapping("/logs")
    public List<String> getLogs() 
    {
        return AppLogger.getLogs();
    }

    @PostMapping("/add-residential")
    public ArrayList<ElectricalGridZone> addResidential(@RequestParam String name, @RequestParam double load, @RequestParam double capacity, @RequestParam int thermostats) 
    {
        gridController.addZone(new ResidentialZone(name, load, capacity, thermostats));
        AppLogger.info("Added Residential Zone: " + name + " [Load: " + load + " kW, Max: " + capacity + " kW, Thermostats: " + thermostats + "]");
        return gridController.getManagedZones();
    }

    @PostMapping("/add-industrial")
    public ArrayList<ElectricalGridZone> addIndustrial(@RequestParam String name, @RequestParam double load, @RequestParam double capacity, @RequestParam int shifts) 
    {
        gridController.addZone(new IndustrialZone(name, load, capacity, shifts));
        AppLogger.info("Added Industrial Zone: " + name + " [Load: " + load + " kW, Max: " + capacity + " kW, Shifts: " + shifts + "]");
        return gridController.getManagedZones();
    }

    @PostMapping("/add-critical")
    public ArrayList<ElectricalGridZone> addCritical(@RequestParam String name, @RequestParam double load, @RequestParam double capacity, @RequestParam int generators) 
    {
        gridController.addZone(new CriticalInfrastructureZone(name, load, capacity, generators));
        AppLogger.info("Added Critical Infrastructure Zone: " + name + " [Load: " + load + " kW, Max: " + capacity + " kW, Generators: " + generators + "]");
        return gridController.getManagedZones();
    }

    @PostMapping("/optimize")
    public ArrayList<ElectricalGridZone> optimizeGrid() 
    {
        AppLogger.warn("Optimization initiated: Evaluating grid load thresholds and stability parameters.");
        gridController.loadShedding();
        AppLogger.info("Load shedding optimization algorithm executed successfully.");
        return gridController.getManagedZones();
    }

    @PostMapping("/reset")
    public ArrayList<ElectricalGridZone> resetGrid() 
    {
        gridController.clearZones();
        AppLogger.clear();
        AppLogger.info("Grid state reset. All active sectors cleared.");
        return gridController.getManagedZones();
    }

    @GetMapping("/")
    public String home() 
    {
        return "forward:/index.html";
    }
}