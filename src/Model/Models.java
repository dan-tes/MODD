package Model;

import Simulation.DrawFunc;
import Simulation.ThawingFunc;
import Structures.PointsParameters;

public class Models {
    public static final int xOffset = 70;
    public static final int yOffset = 70;

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int weight;
    public int height;
    static final int border = 15;
    public static final int radius = 3;
    public ThawingFunc thawing_func;
    public DrawFunc draw_func;
    public PointsParameters[] points_parameters;

    protected Models(ThawingFunc thawing_func, DrawFunc draw_func, PointsParameters[] points_parameters, int weight, int height) {
        this.thawing_func = thawing_func;
        this.draw_func = draw_func;
        this.points_parameters = points_parameters;
        this.weight = weight;
        this.height = height;
    }
}

