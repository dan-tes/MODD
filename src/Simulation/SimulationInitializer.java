package Simulation;

import Structures.MaterialPoint;
import Structures.PointsParameters;

import java.util.Vector;

public class SimulationInitializer {
    public static SimulationState init(int width, int height, PointsParameters [] pointsParameters) {
        SimulationState state = new SimulationState();


        state.width = width;
        state.height = height;

        state.time = 0.0;
        state.points = new Vector<>();
        for (PointsParameters pointsParameter : pointsParameters) {
            state.velocity = 5.0 * Math.sqrt((pointsParameter.getTemperature() + 273) / pointsParameter.getMolarMass());

            for (int j = 0; pointsParameter.getQuantityPoints() > j; ++j) {
                MaterialPoint point = new MaterialPoint(height, width, pointsParameter.getColor());
                double angleRad = Math.toRadians(point.getG());
                point.setVx(Math.cos(angleRad) * state.velocity);
                point.setVy(Math.sin(angleRad) * state.velocity);
                state.points.add(point);
            }
            state.quantityPoints =  pointsParameter.getQuantityPoints();
        }

        return state;
    }
}