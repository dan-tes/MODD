package Structures.Piston;

import Model.Models;
import Structures.MaterialPoint;
import Structures.PointsParameters;

import java.awt.*;

public class SpringPiston extends Piston implements Spring {
    int rigidity_a, rigidity_b;
    public SpringPiston(PointsParameters[] a_p, PointsParameters[] b_p, double x, int rigidity_a, int rigidity_b)
    {
        super(a_p, b_p, x);
        this.rigidity_a = rigidity_a;
        this.rigidity_b = rigidity_b;
    }
    @Override
    public void draw(Graphics g, int width, int height) {
        g.setColor(Color.YELLOW);
        g.fillRect(Models.xOffset,  Models.yOffset + height / 2, width + Models.radius * 2, Models.border);
        g.setColor(Color.BLUE);
        g.fillRect((int) (Models.xOffset + coordinate), Models.yOffset, Models.border, height + Models.radius * 2);
    }

    @Override
    public boolean thaw(MaterialPoint p, int width, int height) {
        if ((coordinate - (double) Models.border / 2) <= p.getXFloat() && p.getXFloat() <= (coordinate + Models.border)) {
            double f_a = getForce(a_p, coordinate) - (coordinate0 - coordinate) * rigidity_a, f_b = getForce(b_p, width - coordinate) - (coordinate0 - coordinate) * rigidity_b;
            System.out.println("1: " + f_a);
            System.out.println("2: " + f_b);
            coordinate += (f_a - f_b) / (f_a + f_b);
            handleElasticCollision(p);
            return true;
        }
        return false;
    }

    @Override
    public void setRgidity_a(int rgidity_a) {
        this.rigidity_a = rgidity_a;
    }

    @Override
    public void setRgidity_b(int rgidity_b) {
        this.rigidity_b = rgidity_b;
    }
}
