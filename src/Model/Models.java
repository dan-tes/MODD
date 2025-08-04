package Model;

import Graphics.CustomSlider;
import Simulation.Handlers.DrawFunc;
import Structures.Piston.Piston;
import Simulation.SimulationRunner;
import Simulation.Handlers.ThawingFunc;
import Structures.PointsParameters;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

public abstract class Models {
    public static final int xOffset = 70;
    public static final int yOffset = 70;
    public static final int border = 15;
    public static final int radius = 3;

    protected int weight;
    protected int height;
    protected PointsParameters[] pointsParameters;

    public Piston getPiston() {
        return piston;
    }

    protected Piston piston;

    Vector<ThawingFunc> thawing_func = new Vector<>();
    Vector<DrawFunc> draw_func = new Vector<>();

    public abstract String getDescription();

    Models(int weight, int height, PointsParameters[] pointsParameters, ThawingFunc thawingFunc, DrawFunc drawFunc, Piston piston) {
        this.weight = weight;
        this.height = height;
        this.pointsParameters = pointsParameters;
        this.piston = piston;
        thawing_func.add(piston::thaw);
        draw_func.add(piston::draw);
        thawing_func.add(thawingFunc);
        draw_func.add(drawFunc);

    }

    Models(int weight, int height, PointsParameters[] pointsParameters, ThawingFunc thawingFunc, DrawFunc drawFunc) {
        this.weight = weight;
        this.height = height;
        this.pointsParameters = pointsParameters;
        thawing_func.add(thawingFunc);
        draw_func.add(drawFunc);
    }

    public List<CustomSlider> getCustomSliders() {
        return List.of(new CustomSlider("X", 5, 555, 50, 10, this.getWeight(), this::setWeight),
                new CustomSlider("Y", 5, 555, 50, 10, this.getHeight(), this::setHeight));
    }

    public Vector<ThawingFunc> getThawingFunc() {
        return thawing_func;
    }

    public Vector<DrawFunc> getDrawFunc() {
        return draw_func;
    }

    public PointsParameters[] getPointsParameters() {
        return pointsParameters;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void run() {
        new SimulationRunner(this);
    }
}
