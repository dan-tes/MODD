package Simulation;

import Structures.MaterialPoint;
import Model.Models;
import Graphics.WorkFrame;

import java.util.Vector;

public class SimulationRunner<Model extends Models> implements Runnable {
    private SimulationState state;
    private Simulator simulator;
    private final WorkFrame workFrame;

    public boolean isRunning() {
        return running;
    }

    private boolean running = true;
    private Model model;

    public SimulationRunner(Model model) {
        this.model = model;
        this.workFrame = new WorkFrame(this, model);
    }

    public void run() {
        while (running) {
            long ti = System.currentTimeMillis();
            simulator.tick();
            state.time += SimulationState.deltaTime;
            workFrame.updateParticles(state.points);
            long sleepTime = ti + (long)(SimulationState.deltaTime * 1000) - System.currentTimeMillis();
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void start(SimulationState state) {
        this.state = state;
        simulator = new Simulator(state, model);
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        running = false;
        try {
            model.getPiston().stop();
        } catch (Exception ex) {
        }
    }

    public Vector<MaterialPoint> getPoints() {
        return state.points;
    }
}
