package Model;

import Simulation.Handlers.SpawnFunc;
import Structures.MaterialPoint;
import Structures.Piston.Piston;
import Structures.Piston.SpringPiston;
import Structures.PointsParameters;
import Graphics.CustomSlider;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SpringPistonModel extends Models {
    SpringPiston piston;
    private static final SpawnFunc spawnFunc1 = (MaterialPoint point, int width, int height) -> {
        double x = point.getXFloat() / width;
        return x < 0.45;
    };

    private static final SpawnFunc spawnFunc2 = (MaterialPoint point, int width, int height) -> {
        double x = point.getXFloat() / width;
        return x > 0.55;
    };

    public SpringPistonModel() {
        PointsParameters[] points_parameters = {
                new PointsParameters(200, 20, 20, Color.GREEN, spawnFunc1, 0),
                new PointsParameters(300, 40, 40, Color.BLACK, spawnFunc2, 0)
        };
        SpringPiston piston = new SpringPiston(new PointsParameters[]{points_parameters[0]},
                new PointsParameters[]{points_parameters[1]}, 250, 20, 20);
        super(500, 500, points_parameters, (MaterialPoint p, int width, int height) -> {
            if (p.getXFloat() <= 0) {
                p.setX(0.1);
                p.setVx(-p.getVx());
                return false;
            }
            if (p.getXFloat() >= width) {
                p.setX(width - 0.1);
                p.setVx(-p.getVx());
                return false;
            }
            if (p.getYFloat() <= 0) {
                p.setY(0.1);
                p.setVy(-p.getVy());
                return false;
            }
            if (p.getYFloat() >= height) {
                p.setY(height - 0.1);
                p.setVy(-p.getVy());
                return false;
            }
            return true;
        }, (Graphics g, int width, int height) -> {
            g.setColor(Color.BLACK);
            g.fillRect(xOffset - border, yOffset - border, width + border + radius * 2, border);
            g.fillRect(xOffset - border, yOffset - border, border, height + border + radius * 2);
            g.fillRect(xOffset + width + radius * 2, yOffset - border, border, height + border * 2 + radius * 2);
            g.fillRect(xOffset - border, yOffset + height + radius * 2, width + border + radius * 2, border);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, xOffset - border, 2000);
            g.fillRect(xOffset - border, 0, 2000, yOffset - border);
            g.fillRect(xOffset - border, height + yOffset + radius * 2 + border, 1000, 1000);
            g.fillRect(xOffset + width + radius * 2 + border, yOffset - border, 1000, 1000);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 10, 5000);
        }, piston);
        this.piston = piston;
    }

    @Override
    public void setWeight(int weight) {
        this.weight = weight;
        piston.setCoordinate(weight);
    }

    @Override
    public String getDescription() {
        return "Модель с пружинками";
    }

    @Override
    public List<CustomSlider> getCustomSliders() {
        return List.of(new CustomSlider("X", 5, 555, 50, 10, this.getWeight(), this::setWeight),
                new CustomSlider("Y", 5, 555, 50, 10, this.getHeight(), this::setHeight),
                new CustomSlider("Жесткость левой пружины", 0, 100, 25, 5, 20, piston::setRgidity_a),
                new CustomSlider("Жесткость правой пружины", 0, 100, 25, 5, 20, piston::setRgidity_b));
    }
}
