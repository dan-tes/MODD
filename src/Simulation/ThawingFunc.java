package Simulation;

import Structures.MaterialPoint;

import java.awt.*;

@FunctionalInterface
public interface ThawingFunc {
    boolean thaw(MaterialPoint p,  int width, int height);
}

