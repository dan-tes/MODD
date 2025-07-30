package Simulation.Handlers;

import Structures.MaterialPoint;

@FunctionalInterface
public interface ThawingFunc {
    boolean thaw(MaterialPoint p,  int width, int height);
}

