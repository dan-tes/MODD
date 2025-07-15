package Structures;

import Simulation.SimulationState;
import Work.Models;

import java.awt.*;
import java.util.Objects;
import java.util.Random;


public class MaterialPoint {
    private double V;
    double x;

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    double y;
    double Xo;
    double Yo;
    double g;
    double Vx;
    double Vy;
    double time = 0.0F;

    public Color getColor() {
        return color;
    }

    Color color;

    public MaterialPoint(double vx, double vy, double mass , Color color, int width, int height) {

        Random random = new Random();

        do {
            this.x = random.nextInt() % width;
        } while(this.x < 0);

        do {
            this.y = random.nextInt() % height;
        } while(this.y < 0);

        this.Xo = x;
        this.Yo = y;
        this.Vx = vx;
        this.Vy = vy;
        this.g = random.nextInt(360);
        this.color = color;
    }

    public double getTime() {
        return this.time;
    }

    public void setTime(double time) {
        if (time != (double) -1.0F) {
            this.time += time;
        } else {
            this.time = SimulationState.deltaTime / (double) 2.0F;
        }

    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            MaterialPoint point = (MaterialPoint) o;
            return this.x == point.x && this.y == point.y && this.Xo == point.Xo && this.Yo == point.Yo && Double.compare(point.g, this.g) == 0 && Double.compare(point.Vx, this.Vx) == 0 && Double.compare(point.Vy, this.Vy) == 0 && Double.compare(point.time, this.time) == 0;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(this.x, this.y, this.Xo, this.Yo, this.g, this.Vx, this.Vy, this.time);
    }

    public void setXo(int xo) {
        this.Xo = xo;
    }

    public void setYo(int yo) {
        this.Yo = yo;
    }

    public double getVx() {
        return this.Vx;
    }

    public int getXo() {
        return (int) this.Xo;
    }

    public int getYo() {
        return (int) this.Yo;
    }

    public double getVy() {
        return this.Vy;
    }

    public void setVx(double vx) {
        this.Vx = vx;
    }

    public void setVy(double vy) {
        this.Vy = vy;
    }

    public double getG() {
        return this.g;
    }

    public double getXFloat() {
        return this.x;
    }

    public double getYFloat() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setG(double g) {
        this.g = g;
    }

    public void setV(double v) {
        this.V = v;
        setVx(Math.cos(getG()) * v);
        setVy(Math.sin(getG()) * v);
    }

    public double getV() {
        return V;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
