package Simulation;

import Structures.PointsParameters;
import Work.Models;

public class ModelParametrs extends Models {
    PointsParameters [] pointsParameters;
    ThawingFunc thawingFunc;
    DrawFunc drawFunc;
    int height;
    int width;
    public ModelParametrs(PointsParameters[] ps, ThawingFunc thawing_func, DrawFunc draw_func, int height, int weight){
        pointsParameters = ps;
        thawingFunc = thawing_func;
        drawFunc = draw_func;
        this.height = height;
        this.width = weight;
    }
}
