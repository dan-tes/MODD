package Simulation;

import Model.Models;
import Structures.PointsParameters;

public class Piston {
    PointsParameters [] a_p, b_p;
    double x;
    public Piston(PointsParameters[] a_p, PointsParameters[] b_p, double x) {
        this.a_p = a_p;
        this.b_p = b_p;
        this.x = x;

    }

}
