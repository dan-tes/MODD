package Graphics;

import Physics.MechanicalParameters;
import Physics.WorkMain;

public class GraphicsEngine {
    private final FrameB frame;

    public GraphicsEngine(WorkMain model) {
        this.frame = new FrameB(model);
    }

    public void render(MechanicalParameters parameters) {
        frame.paint(parameters);
    }
}
