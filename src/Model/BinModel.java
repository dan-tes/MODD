package Model;

import Simulation.DrawFunc;
import Simulation.SpawnFunc;
import Simulation.ThawingFunc;
import Structures.MaterialPoint;
import Structures.PointsParameters;

import java.awt.*;

public class BinModel extends Models {


    public BinModel() {
        SpawnFunc spawnFunc1 = (MaterialPoint point, int width, int height) -> {
            double x = (double) point.getX() / width, y = (double) point.getY() / height;
            return x < 0.45;
        };
        SpawnFunc spawnFunc2 = (MaterialPoint point, int width, int height) -> {
            double x = (double) point.getX() / width, y = (double) point.getY() / height;
            return x > 0.55;
        };
        super(200, 300, new PointsParameters[]{
                new PointsParameters(200, 20, 20, Color.GREEN, spawnFunc1, 0),
                new PointsParameters(300, 40, 40, Color.BLACK, spawnFunc2, 1)},
                (p, width, height) -> {
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
                    if (p.getType() == 1) {
                        if (p.getXFloat() <= (double) width / 2 + (double) border / 2) {
                            p.setX((double) width / 2 - 0.1 + (double) border / 2);
                            p.setVx(-p.getVx());
                            return false;
                        }

                    }
                    return true;
                },
                (Graphics g, int width, int height1) -> {
                    g.setColor(Color.WHITE);
                    width /= 2;
                    g.fillRect(0, 0, 5000, 5000);
                    g.setColor(Color.BLACK);
                    g.fillRect(xOffset - border, yOffset - border, (width + border + radius * 2) * 2, border);
                    g.fillRect(xOffset - border, yOffset - border, border, height1 + border + radius * 2);
                    g.fillRect((xOffset + width + radius * 2 - border * 2) * 2, yOffset - border, border, height1 + border * 2 + radius * 2);
                    g.fillRect(xOffset - border, yOffset + height1 + radius * 2, (width + border + radius * 2) * 2, border);
                    g.setColor(Color.RED);
                    g.fillRect((xOffset + width + radius * 2 - border * 2) + border, yOffset, border, height1 + radius * 2);
                });
    }

    @Override
    public String getDescription() {
        return "Двойная моделька";
    }
}
