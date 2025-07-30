package Simulation;

import Structures.MaterialPoint;

import java.awt.*;

@FunctionalInterface
public interface DrawFunc {
    void draw(Graphics g, int width, int height);
}

