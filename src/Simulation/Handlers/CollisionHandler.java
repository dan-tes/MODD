package Simulation.Handlers;

import Structures.MaterialPoint;

@FunctionalInterface
public interface CollisionHandler {
    void handleElasticCollision(MaterialPoint p);
}
