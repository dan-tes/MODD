package Simulation;

import Structures.MaterialPoint;

import java.util.Vector;

public class SimulationState {
    public int quantityPoints;
    public int width;
    public int height;
    public int radius;
    public double velocity;
    public static double deltaTime = 0.042;
    public double time;
    public Vector<MaterialPoint> points;
}