package Structures.Piston;

import Model.Models;
import Simulation.Handlers.CollisionHandler;
import Simulation.Handlers.DrawFunc;
import Simulation.Handlers.ThawingFunc;
import Structures.MaterialPoint;
import Structures.PointsParameters;

import java.awt.*;

public class Piston implements DrawFunc, ThawingFunc, CollisionHandler {

    final PointsParameters[] a_p, b_p; // частицы по обе стороны поршня
    double coordinate;    // текущая позиция поршня
    double coordinate0;   // начальная позиция поршня
    double persent = 50;

    double initialDistanceA; // начальное расстояние от поршня до левой стенки
    double initialDistanceB; // начальное расстояние от поршня до правой стенки

    public Piston(PointsParameters[] a_p, PointsParameters[] b_p, double x) {
        this.a_p = a_p;
        this.b_p = b_p;
        this.coordinate = x;
        this.coordinate0 = x;

        // Сохраняем начальные "объёмы" слева и справа
        this.initialDistanceA = x;           // расстояние до левой стенки
        this.initialDistanceB = x;   // расстояние до правой стенки
    }

    @Override
    public void draw(Graphics g, int width, int height) {
        g.setColor(Color.BLUE);
        g.fillRect((int) (Models.xOffset + coordinate), Models.yOffset, Models.border, height + Models.radius * 2);
    }

    @Override
    public boolean thaw(MaterialPoint p, int width, int height) {
        if ((coordinate - (double) Models.border / 2) <= p.getXFloat() && p.getXFloat() <= (coordinate + Models.border)) {
            double f_a = getForce(a_p, coordinate);
            double f_b = getForce(b_p, width - coordinate);

            // Двигаем поршень
            coordinate += (f_a - f_b) / (f_a + f_b);

            // Обновляем скорости всех частиц по обе стороны
            updateAllSpeeds(width);

            // Столкновение частицы с поршнем
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

    void updateAllSpeeds(int width) {
        double currentDistanceA = coordinate;
        double currentDistanceB = width - coordinate;

        // целевые коэффициенты (не применяются напрямую)
        double targetFactorA = Math.pow(initialDistanceA / currentDistanceA, 0.1);
        double targetFactorB = Math.pow(initialDistanceB / currentDistanceB, 0.1);

        double smoothing = 0.01; // скорость "подстройки"

        updateAllSpeedSmooth(targetFactorA, targetFactorB, smoothing);
    }

    private void updateAllSpeedSmooth(double targetA, double targetB, double smoothing) {
        for (PointsParameters pSet : a_p) {
            for (MaterialPoint p : pSet.getPoints()) {
                double vx = p.getVx();
                double vy = p.getVy();

                // плавно подстраиваем под новую скорость, не умножая каждый кадр
                double newVx = vx + (vx * (targetA - 1)) * smoothing;
                double newVy = vy + (vy * (targetA - 1)) * smoothing;

                p.setVx(newVx);
                p.setVy(newVy);
            }
        }

        for (PointsParameters pSet : b_p) {
            for (MaterialPoint p : pSet.getPoints()) {
                double vx = p.getVx();
                double vy = p.getVy();

                double newVx = vx + (vx * (targetB - 1)) * smoothing;
                double newVy = vy + (vy * (targetB - 1)) * smoothing;

                p.setVx(newVx);
                p.setVy(newVy);
            }
        }
    }


    @Override
    public void handleElasticCollision(MaterialPoint p) {
        boolean flag = false;
        for (PointsParameters p1 : a_p) {
            if (p1.findPoint(p)) {
                p.setX(coordinate - (double) Models.border / 2);
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
        this.coordinate = (double) value * persent / 100;
        this.coordinate0 = (double) value * persent / 100;
    }

    public void setPersent(double persent) {
        double value = coordinate * 100  / this.persent;
        this.persent = persent;
        this.coordinate = (double) value * persent / 100;
        this.coordinate0 = (double) value * persent / 100;
    }

    public double getPersent() {
        return persent;
    }
}
