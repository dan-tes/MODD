package Structures;

import java.util.Objects;
import java.util.Random;

public class MaterialPoint {
    int x;
    int y;
    int Xo;
    int Yo;
    double g;
    double Vx;
    double Vy;
    double time = 0.0F;
    static int Y1;
    static int X1;
    static double delTime;
    boolean collision = false;
    short type;

    public MaterialPoint(int Y, int X, double time) {
        Random random = new Random();

        do {
            this.x = random.nextInt() % X;
        } while(this.x < 0);

        do {
            this.y = random.nextInt() % Y;
        } while(this.y < 0);

        this.Xo = this.x;
        this.Yo = this.y;
        Y1 = Y;
        X1 = X;
        delTime = time;
        this.g = random.nextInt(360);
        type = (short) (random.nextInt(4));
    }

    public double getTime() {
        return this.time;
    }

    public void setTime(double time) {
        if (time != (double)-1.0F) {
            this.time += time;
        } else {
            this.time = delTime / (double)2.0F;
        }

    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            MaterialPoint point = (MaterialPoint)o;
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
        return this.Xo;
    }

    public int getYo() {
        return this.Yo;
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

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setG(double g) {
        this.g = g;
    }

    public boolean getCollision() {
        return this.collision;
    }

    public void setCollision(boolean b) {
        this.collision = b;
    }
}
