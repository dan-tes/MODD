package Structures;

import java.awt.*;

public class PointsParameters {
    int temperature;
    int quantityPoints;
    double velocity;
    Color color;

    public int getQuantityPoints() {
        return quantityPoints;
    }

    public void setQuantityPoints(int quantityPoints) {
        this.quantityPoints = quantityPoints;
    }

    double molarMass;

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

    public PointsParameters(int molarMass, int temperature, int quantityPoints, Color color) {
        this.molarMass = molarMass / 1000.0;
        this.temperature = temperature;
        this.quantityPoints = quantityPoints;
        this.velocity = 5.0 * Math.sqrt((temperature + 273) / this.molarMass);
        this.color = color;
    }

    public void setMolarMass(double molarMass) {
        this.molarMass = molarMass / 1000;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature + 273;
    }
}
