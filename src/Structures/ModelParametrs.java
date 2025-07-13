package Structures;

import java.awt.*;

public class ModelParametrs {

    ModelParametrs(PointsParameters [] ps, Runnable thawing_func, Runnable draw_func, int height, int weight){

    }
}

class StandardModel {


    Runnable thawing_func = new Runnable() {
        @Override
        public void run() {

        }
    };
    Runnable draw_func = new Runnable() {
        @Override
        public void run() {
            // логика отрисовки
        }
    };

    ModelParametrs modelParametrs = new ModelParametrs(
            new PointsParameters[]{
                    new PointsParameters(200, 20, 20, Color.GREEN)
            },
            thawing_func,
            draw_func,
            500,
            500
    );
}
