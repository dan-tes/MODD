package Simulation;

import Structures.MaterialPoint;

public class Simulator {
    private final SimulationState state;

    public Simulator(SimulationState state) {
        this.state = state;
    }

    public void tick() {
        for (int i = 0; i < state.quantityPoints; ++i) {
            MaterialPoint p1 = state.points.get(i);

            for (int j = i + 1; j < state.quantityPoints; ++j) {
                MaterialPoint p2 = state.points.get(j);
                handleElasticCollision(p1, p2);
            }
            if (Math.abs(p1.getVx()) <= 0.1 || Math.abs(  p1.getVy()) <= 0.1) {
                System.out.println(p1.getVx() + " " + p1.getVy());
                System.out.println(p1.getXFloat() + " " + p1.getYFloat());
            }

            handleWallCollision(p1);
            updatePosition(p1);
            updateDirection(p1);
        }
    }

    private void handleElasticCollision(MaterialPoint p1, MaterialPoint p2) {

//
        double dx = p2.getXFloat() - p1.getXFloat();
        double dy = p2.getYFloat() - p1.getYFloat();
        double distSq = dx * dx + dy * dy;
        double radiusSum = SimulationState.radius * 2;
//
        if (distSq < radiusSum * radiusSum) {

            double vx1 = p1.getVx(),  vy1 = p1.getVy();
            double random =0;//  Math.random() * (vx1 + vy1) / 100;
            p1.setVx(p2.getVx() + random);
            p1.setVy(p2.getVy() - random);
            p2.setVx(vx1 - random);
            p2.setVy(vy1 + random);

            double overlap = getOverlap(distSq, radiusSum);
            p1.setX((p1.getXFloat() - overlap));
            p1.setY((p1.getYFloat() + overlap));
            p2.setX((p2.getXFloat() + overlap));
            p2.setY((p2.getYFloat() - overlap));
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
        if (p.getXFloat() <= 0) {
            p.setX(0.1);
            p.setVx(-p.getVx());
        } else if (p.getXFloat() >= state.width) {
            p.setX(state.width - 0.1);
            p.setVx(-p.getVx());
        }

        if (p.getYFloat() <= 0) {
            p.setY(0.1);
            p.setVy(-p.getVy());
        } else if (p.getYFloat() >= state.height) {
            p.setY(state.height - 0.1);
            p.setVy(-p.getVy());
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
