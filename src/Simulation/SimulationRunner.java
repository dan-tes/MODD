package Simulation;

import Structures.MaterialPoint;
import Model.Models;
import Graphics.FrameMKT;
import com.google.gson.Gson;

import java.util.*;

public class SimulationRunner<Model extends Models> implements Runnable {
    private SimulationState state;
    private Simulator simulator;
    private final FrameMKT workFrame;

    public boolean isRunning() {
        return running;
    }

    private boolean running = true;
    private Model model;

    public SimulationRunner(Model model) {
        this.model = model;
        this.workFrame = new FrameMKT(this, model);
    }

    public void run() {
        while (running) {
            long ti = System.currentTimeMillis();
            simulator.tick();
            simulator.history();
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

    public ArrayList<Double []> getHistorySpeed(){
        return simulator.speed;
    }
    public String getJSON() {
        List<Map<String, Object>> graphs = new ArrayList<>();

        // Пример: только simulator.speed
        List<Double[]> ysData = simulator.speed;

        if (ysData.isEmpty()) {
            return "{}"; // нет данных
        }

        int dimensions = ysData.get(0).length; // количество координат (например, x, y, z)

        // Создаём отдельный график для каждой координаты
        for (int dim = 0; dim < dimensions; dim++) {
            List<Double> xData = new ArrayList<>();
            List<Double> yData = new ArrayList<>();

            for (int i = 0; i < ysData.size(); i++) {
                xData.add((double) i);
                yData.add(ysData.get(i)[dim]);
            }

            Map<String, Object> graph = new HashMap<>();
            graph.put("x", xData);
            graph.put("y", yData);
            graph.put("label", "Газ " + (dim + 1));

            graphs.add(graph);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("graphs", graphs);

        Gson gson = new Gson();
        return gson.toJson(data);
    }
}
