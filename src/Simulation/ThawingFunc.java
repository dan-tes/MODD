package Simulation;

import Structures.MaterialPoint;

import java.awt.*;

@FunctionalInterface
public interface ThawingFunc {
    boolean thaw(MaterialPoint p);
}

