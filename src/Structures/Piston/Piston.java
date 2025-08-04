package Structures.Piston;

import Model.Models;
import Simulation.Handlers.CollisionHandler;
import Simulation.Handlers.DrawFunc;
import Simulation.Handlers.ThawingFunc;
import Structures.MaterialPoint;
import Structures.PointsParameters;

import java.awt.*;

public class Piston implements DrawFunc, ThawingFunc, CollisionHandler {
    PointsParameters[] a_p, b_p;

    double coordinate, coordinate0;

    public Piston(PointsParameters[] a_p, PointsParameters[] b_p, double x) {
        this.a_p = a_p;
        this.b_p = b_p;
        this.coordinate = x;
        this.coordinate0 = x;
    }

    @Override
    public void draw(Graphics g, int width, int height) {
        g.setColor(Color.BLUE);
        g.fillRect((int) (Models.xOffset + coordinate), Models.yOffset, Models.border, height + Models.radius * 2);
    }

    @Override
    public boolean thaw(MaterialPoint p, int width, int height) {
        if ((coordinate) <= p.getXFloat() && p.getXFloat() <= (coordinate + Models.border)) {
            double f_a = getForce(a_p, coordinate), f_b = getForce(b_p, width - coordinate);
            System.out.println("1: " + f_a);
            System.out.println("2: " + f_b);
            coordinate += (f_a - f_b) / (f_a + f_b);
            handleElasticCollision(p);
            return true;
        }
        return false;
    }

    double getForce(PointsParameters[] parameters, double l) {
        double f_average = 0;
        for (PointsParameters p1 : parameters) {
            f_average += p1.getFx();
        }
        f_average /= parameters.length;
        return f_average / l;
    }

    @Override
    public void handleElasticCollision(MaterialPoint p) {
        boolean flag = false;
        for (PointsParameters p1 : a_p) {
            if (p1.findPoint(p)) {
                p.setX(coordinate);
                flag = true;
            }
        }
        if (!flag) {
            p.setX(coordinate + Models.border);
        }
        p.setVx(-p.getVx());
    }

    public void stop() {
        coordinate = coordinate0;
    }

    public void setCoordinate(int value) {
        this.coordinate = (double) value / 2;
        this.coordinate0 = (double) value / 2;
    }
}
