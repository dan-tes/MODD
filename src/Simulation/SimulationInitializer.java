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
            double molarMass = pointsParameter.getMolarMass(); // Молярная масса в г/моль
            double temperature = pointsParameter.getTemperature() + 273; // Температура в Кельвинах
            int quantity = pointsParameter.getQuantityPoints();

            // Среднеквадратичная скорость (масштабируем для симуляции)
            double v_rms = Math.sqrt(3 * 8.314 * temperature / (molarMass / 1000)) / 100;

            for (int j = 0; j < quantity; ++j) {
                // Случайное направление скорости
                double angle = rand.nextDouble() * 2 * Math.PI;
                double vx = v_rms * Math.cos(angle);
                double vy = v_rms * Math.sin(angle);

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