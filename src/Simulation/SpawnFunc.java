package Simulation;

import Structures.MaterialPoint;

@FunctionalInterface
public interface SpawnFunc {
    boolean thaw(MaterialPoint p,  int width, int height);
}

