package Work;

import Simulation.DrawFunc;
import Simulation.ModelParametrs;
import Simulation.Simulator;
import Simulation.ThawingFunc;
import Structures.MaterialPoint;
import Structures.PointsParameters;

import java.awt.*;

public class StandardModel extends Models {
    int width = 500;
    int height = 500;
    public StandardModel() {

        ThawingFunc thawing_func = (MaterialPoint p) -> {
            if (p.getXFloat() <= 0) {
                p.setX(0.1);
                p.setVx(-p.getVx());
                return false;
            }
            if (p.getXFloat() >= this.width) {
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
        };
        DrawFunc draw_func = (Graphics g) -> {
            g.fillRect(xOffset, yOffset, width + radius * 2, height + radius * 2);
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
        };


        ModelParametrs modelParametrs = new ModelParametrs(
                new PointsParameters[]{
                        new PointsParameters(200, 20, 20, Color.GREEN)
                },
                thawing_func,
                draw_func,
                height,
                width
        );
        new SimulationRunner(modelParametrs);
    }
}
