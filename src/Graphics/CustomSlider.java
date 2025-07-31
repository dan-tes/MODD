package Graphics;

import java.util.function.Consumer;

public record CustomSlider(
        String label,
        int min,
        int max,
        int majorTick,
        int minorTick,
        int defaultValue,
        Consumer<Integer> consumer
) {}
