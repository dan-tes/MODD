package Structures;

import Model.Models;
import Simulation.CollisionHandler;
import Simulation.DrawFunc;
import Simulation.ThawingFunc;

import java.awt.*;

public class VerticalPiston extends Piston implements DrawFunc, ThawingFunc, CollisionHandler {
    public VerticalPiston(PointsParameters[] a_p, PointsParameters[] b_p, double y) {
        super(a_p, b_p, y);
        coord = Coordinate.Y;
    }
    @Override
    public void draw(Graphics g, int width, int height) {
        g.setColor(Color.BLUE);
        g.fillRect(Models.xOffset, (int) (Models.yOffset + coordinate), width + Models.radius * 2, Models.border);
    }

    @Override
    public boolean thaw(MaterialPoint p, int width, int height) {
        if ((coordinate) <= p.getYFloat() && p.getYFloat() <= (coordinate + Models.border)) {
            double g = 9.8, mass = 0;
            for (PointsParameters parameters : this.a_p) {
                mass += parameters.molarMass * parameters.quantityPoints;
            }

            double f_a = getForce(a_p, coordinate) + mass * g, f_b = getForce(b_p, width - coordinate);
            coordinate += (f_a - f_b) / (f_a + f_b);
            handleElasticCollision(p);
            return true;
        }
        return false;
    }

    @Override
    public void handleElasticCollision(MaterialPoint p) {
        boolean flag = false;
        for (PointsParameters p1 : a_p) {
            if (p1.findPoint(p)) {
                p.setY(coordinate);
                flag = true;
            }
        }
        if (!flag) {
            p.setY(coordinate + Models.border);
        }
        p.setVy(-p.getVy());
    }
}
