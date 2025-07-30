package Simulation;

import Structures.MaterialPoint;

@FunctionalInterface
public interface CollisionHandler {
    void handleElasticCollision(MaterialPoint p);
}
