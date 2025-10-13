package Simulation;

import Structures.MaterialPoint;
import Structures.PointsParameters;
import Model.Models;

import java.util.Random;
import java.util.Vector;

public class SimulationInitializer {
    public static SimulationState init(int width, int height, Models models) {
        SimulationState state = new SimulationState();
        state.width = width;
        state.height = height;
        state.time = 0.0;
        state.points = new Vector<>();
        state.quantityPoints = 0;

        Random rand = new Random();
        for (PointsParameters pointsParameter : models.getPointsParameters()) {
            double molarMass = pointsParameter.getMolarMass(); // г/моль
            double temperature = pointsParameter.getTemperature() + 273; // К
            int quantity = pointsParameter.getQuantityPoints();
            double m = (molarMass / 1000.0) / 6.02214076e23;

            double kB = 1.380649e-23;
            double sigma = Math.sqrt(kB * temperature / m) / 100;

            for (int j = 0; j < quantity; ++j) {
                // генерация нормального распределения (Бокс–Мюллер)
                double u1 = rand.nextDouble();
                double u2 = rand.nextDouble();

                double sqrt = Math.sqrt(-2.0 * Math.log(u1));
                double z0 = sqrt * Math.cos(2 * Math.PI * u2);
                double z1 = sqrt * Math.sin(2 * Math.PI * u2);

                double vx = z0 * sigma;
                double vy = z1 * sigma;
                // Создание молекулы
                MaterialPoint point = new MaterialPoint(vx, vy, molarMass, pointsParameter.getColor(),
                        width, height, pointsParameter.getSpawnFunc(), pointsParameter.getType());
                pointsParameter.addPoint(point);
                state.points.add(point);
            }
            state.quantityPoints += quantity;
        }

        return state;
    }
}