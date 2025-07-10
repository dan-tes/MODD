package Work;

import Simulation.*;
import Structures.MaterialPoint;

public class SimulationRunner implements Runnable {
    private SimulationState state;
    private Simulator simulator;
    private final WorkFrame workFrame;
    private boolean running = true;

    public SimulationRunner() {
        this.workFrame = new WorkFrame(this);
    }

    public void run() {
        while (running) {
            long ti = System.currentTimeMillis();
            simulator.tick();
            state.time += state.deltaTime;
            workFrame.updateParticles(state.points);
            long sleepTime = ti + (long)(state.deltaTime * 1000) - System.currentTimeMillis();
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
        simulator = new Simulator(state);
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }

    public MaterialPoint[] getPoints() {
        return state.points;
    }
}
