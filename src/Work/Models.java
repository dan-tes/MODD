package Work;

import Simulation.DrawFunc;
import Simulation.ThawingFunc;
import Structures.PointsParameters;

public class Models {
    static final int xOffset = 70;
    static final int yOffset = 70;
    int weight, height;
    static final int border = 15;
    public static final int radius = 3;
    ThawingFunc thawing_func;
    DrawFunc draw_func;
    PointsParameters[] points_parameters;

    protected Models(ThawingFunc thawing_func, DrawFunc draw_func, PointsParameters[] points_parameters, int weight, int height) {
        this.thawing_func = thawing_func;
        this.draw_func = draw_func;
        this.points_parameters = points_parameters;
        this.weight = weight;
        this.height = height;
    }
}

