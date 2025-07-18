package Model;

import Simulation.DrawFunc;
import Simulation.Piston;
import Simulation.SpawnFunc;
import Simulation.ThawingFunc;
import Structures.MaterialPoint;
import Structures.PointsParameters;

import java.awt.*;

public class PistonModel extends Models {
    private static final SpawnFunc spawnFunc1 = (MaterialPoint point, int width, int height) -> {
        return true;
    };

    private static final SpawnFunc spawnFunc2 = (MaterialPoint point, int width, int height) -> {
        return true;
    };

    public PistonModel() {
        PointsParameters[] points_parameters = {
                new PointsParameters(200, 20, 20, Color.GREEN, spawnFunc1, 0),
                new PointsParameters(300, 40, 40, Color.BLACK, spawnFunc2, 0)
        };
        Piston piston = new Piston(new PointsParameters[]{points_parameters[0]},
                new PointsParameters[]{points_parameters[1]}, 250);
        super(500, 500, points_parameters, piston);
    }

    @Override
    public ThawingFunc[] getThawingFunc() {
        return new ThawingFunc[]{(MaterialPoint p, int width, int height) -> {
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
        }};
    }

    @Override
    public DrawFunc[] getDrawFunc() {
        return new DrawFunc[]{
                (Graphics g, int width, int height) -> {
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
                }
        };
    }

    @Override
    public String getDescription() {
        return "Основная моделька";
    }
}
