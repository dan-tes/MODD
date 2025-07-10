package Simulation;

import Structures.MaterialPoint;

public class Simulator {
    private final SimulationState state;

    public Simulator(SimulationState state) {
        this.state = state;
    }

    public void tick() {
        for (int i = 0; i < state.quantityPoints; ++i) {
            MaterialPoint p1 = state.points[i];

            for (int j = i + 1; j < state.quantityPoints; ++j) {
                MaterialPoint p2 = state.points[j];
                handleElasticCollision(p1, p2);
            }


            handleWallCollision(p1);
            updatePosition(p1);
            updateDirection(p1);
        }
    }

    private void handleElasticCollision(MaterialPoint p1, MaterialPoint p2) {

//
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        double distSq = dx * dx + dy * dy;
        double radiusSum = state.radius * 2;
//
        if (distSq < radiusSum * radiusSum) {

            double vx1 = p1.getVx(),  vy1 = p1.getVy();
            double random =  Math.random() * (vx1 + vy1) / 100;
            p1.setVx(p2.getVx() + random);
            p1.setVy(p2.getVy() - random);
            p2.setVx(vx1- random);
            p2.setVy(vy1 + random);

            double overlap = getOverlap(distSq, radiusSum);
            p1.setX((int)(p1.getX() - overlap));
            p1.setY((int)(p1.getY() + overlap));
            p2.setX((int)(p2.getX() + overlap));
            p2.setY((int)(p2.getY() - overlap));
        }
    }

    private static double getOverlap(double distSq, double radiusSum) {
        double dist = Math.sqrt(distSq);
        if (dist == 0) dist = 0.001;
//
//            // Нормализованный вектор направления
//            double nx = dx / dist;
//            double ny = dy / dist;
//
//            // Разность скоростей
//            double vxRel = p1.getVx() - p2.getVx();
//            double vyRel = p1.getVy() - p2.getVy();
//
//            double velAlongNormal = vxRel * nx + vyRel * ny;
////            if (velAlongNormal > 0) return; // уже расходятся
//
//            // Импульс
//            double impulse = -2 * velAlongNormal / 2;
//
//            double impulseX = impulse * nx;
//            double impulseY = impulse * ny;
//
//            p1.setVx(p1.getVx() + impulseX);
//            p1.setVy(p1.getVy() + impulseY);
//            p2.setVx(p2.getVx() - impulseX);
//            p2.setVy(p2.getVy() - impulseY);
//
//            // Раздвигаем, чтобы не залипли
        double overlap = (radiusSum - dist);
        return overlap;
    }

    private void handleWallCollision(MaterialPoint p) {
        boolean collided = false;

        if (p.getX() < 0) {
            p.setX(0);
            p.setVx(-p.getVx());
            collided = true;
        } else if (p.getX() > state.width) {
            p.setX(state.width);
            p.setVx(-p.getVx());
            collided = true;
        }

        if (p.getY() < 0) {
            p.setY(0);
            p.setVy(-p.getVy());
            collided = true;
        } else if (p.getY() > state.height) {
            p.setY(state.height);
            p.setVy(-p.getVy());
            collided = true;
        }
//
//        if (collided) {
//            updateDirection(p);
//        }
    }

    private void updatePosition(MaterialPoint p) {
        p.setX((int)(p.getX() + p.getVx() * state.deltaTime));
        p.setY((int)(p.getY() - p.getVy() * state.deltaTime));
    }

    private void updateDirection(MaterialPoint p) {
        double g = Math.toDegrees(Math.atan2(p.getVy(), p.getVx()));
        if (g < 0) g += 360;
        p.setG(g);
    }
}
