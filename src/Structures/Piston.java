package Structures;

import Model.Models;
import Simulation.DrawFunc;
import Simulation.ThawingFunc;

import java.awt.*;

public class Piston implements DrawFunc, ThawingFunc {
    PointsParameters[] a_p, b_p;

    double x, x0;

    public Piston(PointsParameters[] a_p, PointsParameters[] b_p, double x) {
        this.a_p = a_p;
        this.b_p = b_p;
        this.x = x;
        this.x0 = x;
    }

    @Override
    public void draw(Graphics g, int width, int height) {
        g.setColor(Color.BLUE);
        g.fillRect((int) (Models.xOffset + x), Models.yOffset, Models.border, height + Models.radius * 2);
    }

    @Override
    public boolean thaw(MaterialPoint p, int width, int height) {
        if ((x) <= p.getXFloat() && p.getXFloat() <= (x + Models.border)) {
            double f_a = getForce(a_p, x), f_b = getForce(b_p, width - x);
            System.out.println("1: " + f_a);
            System.out.println("2: " + f_b);
            x += (f_a - f_b) / (f_a + f_b);
            handleElasticCollision(p);
            return true;
        }
        return false;
    }

    private double getForce(PointsParameters[] parameters, double l) {
        double f_average = 0;
        for (PointsParameters p1 : parameters) {
            f_average += p1.getFx();
        }
        f_average /= parameters.length;
        return f_average / l;
    }

    private void handleElasticCollision(MaterialPoint p) {
        boolean flag = false;
        for (PointsParameters p1 : a_p) {
            if (p1.findPoint(p)) {
                p.setX(x);
                flag = true;
            }
        }
        if (!flag) {
            p.setX(x + Models.border);
        }
        p.setVx(-p.getVx());
    }

    public void stop() {
        x = x0;
    }

    public void setX(int value) {
        this.x = (double) value / 2;
        this.x0 = (double) value / 2;
    }
}
