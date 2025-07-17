import Graphics.ChoiceFrame;
import Model.BinModel;
import Model.Models;
import Model.StandardModel;
class ApplicationRunner{
     public static Models[] models =  new Models[]{new StandardModel(), new BinModel()};
}

public class mainMofPinaG {
    public static void main(String[] args) {
//        new ChoiceFrame(ApplicationRunner.models);
        new BinModel().run();
    }
}
