package Simulation;

import Structures.MaterialPoint;

import static Work.Models.radius;

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

            handleWallCollision(p1);
            updatePosition(p1);
            updateDirection(p1);
        }
    }

    private void handleElasticCollision(MaterialPoint p1, MaterialPoint p2) {
        double dx = p2.getXFloat() - p1.getXFloat();
        double dy = p2.getYFloat() - p1.getYFloat();
        double distSq = dx * dx + dy * dy;
        double radiusSum = radius * 2;

        if (distSq < radiusSum * radiusSum) {

            double vx1 = p1.getVx(),  vy1 = p1.getVy();
            p1.setVx(p2.getVx());
            p1.setVy(p2.getVy());
            p2.setVx(vx1);
            p2.setVy(vy1);

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
