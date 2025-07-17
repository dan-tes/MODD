package Structures;

import Simulation.SpawnFunc;

import java.awt.*;
import java.util.function.Consumer;

public class PointsParameters {
    int temperature;
    int quantityPoints;
    double velocity;
    Color color;
    double molarMass;
    SpawnFunc spawnFunc;
    short type;

    public PointsParameters(int molarMass, int temperature, int quantityPoints, Color color, SpawnFunc spawnFunc, int type) {
        this.molarMass = molarMass / 1000.0;
        this.temperature = temperature;
        this.quantityPoints = quantityPoints;
        this.velocity = 5.0 * Math.sqrt((temperature + 273) / this.molarMass);
        this.color = color;
        this.spawnFunc = spawnFunc;
        this.type = (short) type;
    }

    public SpawnFunc getSpawnFunc() {
        return spawnFunc;
    }

    public int getQuantityPoints() {
        return quantityPoints;
    }

    public short getType() {
        return type;
    }

    public void setQuantityPoints(int quantityPoints) {
        this.quantityPoints = quantityPoints;
    }

    public double getMolarMass() {
        return molarMass;
    }

    public double getVelocity() {
        return velocity;
    }

    public int getTemperature() {
        return temperature;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setMolarMass(double molarMass) {
        this.molarMass = molarMass / 1000;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature + 273;
    }
}
