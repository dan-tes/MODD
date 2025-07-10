package Simulation;

import Structures.MaterialPoint;

public class SimulationInitializer {
    public static SimulationState init(int molarMass, int temperature, int quantityPoints, int width, int height, int radius) {
        SimulationState state = new SimulationState();
        state.molarMass = molarMass;
        state.temperature = temperature;
        state.quantityPoints = quantityPoints;
        state.width = width;
        state.height = height;
        state.radius = radius;
        state.deltaTime = 0.042;
//        state.deltaTime = 0.001;
        state.time = 0.0;

        double Mm = (double) molarMass / 1000.0;
        state.velocity = 5.0 * Math.sqrt((temperature + 273) / Mm);

        state.points = new MaterialPoint[quantityPoints];
        for (int i = 0; i < quantityPoints; ++i) {
            state.points[i] = new MaterialPoint(height, width, state.deltaTime);
            double angleRad = Math.toRadians(state.points[i].getG());
            state.points[i].setVx(Math.cos(angleRad) * state.velocity);
            state.points[i].setVy(Math.sin(angleRad) * state.velocity);
        }

        return state;
    }
}