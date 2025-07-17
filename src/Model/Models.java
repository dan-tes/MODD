package Model;

import Simulation.DrawFunc;
import Simulation.SimulationRunner;
import Simulation.ThawingFunc;
import Structures.PointsParameters;

public abstract class Models {
    public static final int xOffset = 70;
    public static final int yOffset = 70;
    public static final int border = 15;
    public static final int radius = 3;

    protected int weight;
    protected int height;

    public abstract ThawingFunc getThawingFunc();
    public abstract DrawFunc getDrawFunc();
    public abstract PointsParameters[] getPointsParameters();
    public abstract String getDescription();

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
