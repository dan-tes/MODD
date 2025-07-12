package Simulation;

import Structures.MaterialPoint;

import java.util.Vector;

public class SimulationState {
    public int quantityPoints;
    public int width;
    public int height;
    public double velocity;
    public static final double deltaTime = 0.042;
    public static final int radius = 3;
    public double time;
    public Vector<MaterialPoint> points;
}