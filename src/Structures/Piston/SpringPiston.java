package Structures.Piston;

import Model.Models;
import Structures.MaterialPoint;
import Structures.PointsParameters;

import java.awt.*;

public class SpringPiston extends Piston implements Spring {

    private int rigidity_a, rigidity_b;

    public SpringPiston(PointsParameters[] a_p, PointsParameters[] b_p, double x, int rigidity_a, int rigidity_b) {
        super(a_p, b_p, x);
        this.rigidity_a = rigidity_a;
        this.rigidity_b = rigidity_b;
    }

    @Override
    public void draw(Graphics g, int width, int height) {
        // Сам поршень
        g.setColor(Color.BLUE);
        g.fillRect((int) (Models.xOffset + coordinate), Models.yOffset, Models.border, height + Models.radius * 2);

        // Пружины/фон
        g.setColor(Color.YELLOW);
        g.fillRect(Models.xOffset, Models.yOffset + height / 2, width + Models.radius * 2, Models.border);
    }

    @Override
    public boolean thaw(MaterialPoint p, int width, int height) {
        if ((coordinate - (double) Models.border / 2) <= p.getXFloat() && p.getXFloat() <= (coordinate + Models.border)) {
            // Силы от частиц и пружины
            double f_a = getForce(a_p, coordinate) - (coordinate0 - coordinate) * rigidity_a;
            double f_b = getForce(b_p, width - coordinate) - (coordinate0 - coordinate) * rigidity_b;

            // Двигаем поршень
            coordinate += (f_a - f_b) / (f_a + f_b);

            // Плавное обновление скоростей всех частиц
            updateAllSpeeds(width, (f_a - f_b) / (f_a + f_b));

            // Столкновение конкретной частицы с поршнем
            handleElasticCollision(p);
            return true;
        }
        return false;
    }

    @Override
    public void setRgidity_a(int rigidity_a) {
        this.rigidity_a = rigidity_a;
    }

    @Override
    public void setRgidity_b(int rigidity_b) {
        this.rigidity_b = rigidity_b;
    }

//    /**
//     * Обновление скоростей всех частиц с плавным эффектом
//     */
//    private void updateAllSpeeds(int width) {
//        double currentDistanceA = coordinate;           // расстояние до левой стенки
//        double currentDistanceB = width - coordinate;   // расстояние до правой стенки
//
//        double targetFactorA = Math.cbrt(initialDistanceA / currentDistanceA);
//        double targetFactorB = Math.cbrt(initialDistanceB / currentDistanceB);
//
//        double smoothing = 1.0; // плавность изменения (0 < smoothing <= 1)
//        double factorA = 1 + (targetFactorA - 1) * smoothing;
//        double factorB = 1 + (targetFactorB - 1) * smoothing;
//
//        updateAllSpeed(factorA, factorB);
//    }
}
