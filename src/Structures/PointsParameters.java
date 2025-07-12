package Structures;

public class PointsParameters {
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

    int temperature;
    int quantityPoints;
    double velocity;
    public PointsParameters(int molarMass, int temperature, int quantityPoints)
    {
        this.molarMass = molarMass/ 1000.0;
        this.temperature = temperature;
        this.quantityPoints = quantityPoints;
        this.velocity = 5.0 * Math.sqrt((temperature + 273) / this.molarMass);

    }

}
