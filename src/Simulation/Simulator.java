package Simulation;

import Simulation.Handlers.ThawingFunc;
import Structures.MaterialPoint;
import Model.Models;
import Structures.PointsParameters;

import static Model.Models.radius;

public class Simulator {
    private final SimulationState state;
    Models models;

    public Simulator(SimulationState state, Models models) {
        this.state = state;
        this.models = models;
    }

    public void tick() {
        for (int i = 0; i < state.quantityPoints; ++i) {
            MaterialPoint p1 = state.points.get(i);

            for (int j = i + 1; j < state.quantityPoints; ++j) {
                MaterialPoint p2 = state.points.get(j);
                handleElasticCollision(p1, p2);
            }

            for (ThawingFunc func : models.getThawingFunc())
                func.thaw(p1, models.getWeight(), models.getHeight());
            updatePosition(p1);
            updateDirection(p1);
        }
        for (PointsParameters parameters :models.getPointsParameters()){
            double v_average = 0;
            for (MaterialPoint p1 : state.points){
                v_average += p1.getV();
            }
            v_average /= state.quantityPoints;
            parameters.setV(v_average);
            parameters.update();
        }
    }

    private void handleElasticCollision(MaterialPoint p1, MaterialPoint p2) {
        double dx = p2.getXFloat() - p1.getXFloat();
        double dy = p2.getYFloat() - p1.getYFloat();
        double distSq = dx * dx + dy * dy;
        double radiusSum = radius * 2;

        if (distSq < radiusSum * radiusSum) {
            double distance = Math.sqrt(distSq);
            if (distance == 0) distance = 0.01;  // во избежание деления на 0

            // Нормализованный вектор столкновения
            double nx = dx / distance;
            double ny = dy / distance;

            double vxRel = p1.getVx() - p2.getVx();
            double vyRel = p1.getVy() - p2.getVy();
            double velAlongNormal = vxRel * nx + vyRel * ny;

            if (velAlongNormal > 0) return;

            double m1 = p1.mass;
            double m2 = p2.mass;

            double impulse = (2 * velAlongNormal) / (m1 + m2);

            p1.setVx(p1.getVx() - impulse * m2 * nx);
            p1.setVy(p1.getVy() - impulse * m2 * ny);
            p2.setVx(p2.getVx() + impulse * m1 * nx);
            p2.setVy(p2.getVy() + impulse * m1 * ny);

            double overlap = radiusSum - distance;
            double correctionRatio = 0.5;
            p1.setX(p1.getXFloat() - nx * overlap * correctionRatio);
            p1.setY(p1.getYFloat() - ny * overlap * correctionRatio);
            p2.setX(p2.getXFloat() + nx * overlap * (1 - correctionRatio));
            p2.setY(p2.getYFloat() + ny * overlap * (1 - correctionRatio));
        }
    }


    private void updatePosition(MaterialPoint p) {
        p.setX((p.getXFloat() + p.getVx() * SimulationState.deltaTime));
        p.setY((p.getYFloat() - p.getVy() * SimulationState.deltaTime));
    }

    private void updateDirection(MaterialPoint p) {
        double g = Math.toDegrees(Math.atan2(p.getVy(), p.getVx()));
        if (g < 0) g += 360;
        p.setG(g);
    }
}
