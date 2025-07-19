import Graphics.ChoiceFrame;
import Model.*;

class ApplicationRunner{
     public static Models[] models =  new Models[]{new StandardModel(), new BinModel(), new BinModel2(), new PistonModel()};
}

public class mainMofPinaG {
    public static void main(String[] args) {
        new ChoiceFrame(ApplicationRunner.models);
//        new PistonModel().run();
    }
}
