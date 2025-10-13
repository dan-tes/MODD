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
            if (distance == 0) distance = 0.001; // избегаем деления на 0

            // Нормаль столкновения
            double nx = dx / distance;
            double ny = dy / distance;

            // Тангенциальный вектор
            double tx = -ny;
            double ty = nx;

            // Проекции скоростей на нормаль и тангенс
            double v1n = nx * p1.getVx() + ny * p1.getVy();
            double v1t = tx * p1.getVx() + ty * p1.getVy();
            double v2n = nx * p2.getVx() + ny * p2.getVy();
            double v2t = tx * p2.getVx() + ty * p2.getVy();

            // Новые нормальные скорости (упругое столкновение)
            double m1 = p1.mass;
            double m2 = p2.mass;

            double v1nAfter = (v1n * (m1 - m2) + 2 * m2 * v2n) / (m1 + m2);
            double v2nAfter = (v2n * (m2 - m1) + 2 * m1 * v1n) / (m1 + m2);

            // Собираем обратно векторы скоростей
            double v1nX = v1nAfter * nx;
            double v1nY = v1nAfter * ny;
            double v1tX = v1t * tx;
            double v1tY = v1t * ty;

            double v2nX = v2nAfter * nx;
            double v2nY = v2nAfter * ny;
            double v2tX = v2t * tx;
            double v2tY = v2t * ty;

            p1.setVx(v1nX + v1tX);
            p1.setVy(v1nY + v1tY);
            p2.setVx(v2nX + v2tX);
            p2.setVy(v2nY + v2tY);

            // Раздвигаем частицы, чтобы не пересекались
            double overlap = radiusSum - distance;
            double correction = overlap / 2.0;
            p1.setX(p1.getXFloat() - nx * correction);
            p1.setY(p1.getYFloat() - ny * correction);
            p2.setX(p2.getXFloat() + nx * correction);
            p2.setY(p2.getYFloat() + ny * correction);
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
