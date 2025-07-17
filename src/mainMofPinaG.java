import Graphics.ChoiceFrame;
import Model.BinModel;
import Model.BinModel2;
import Model.Models;
import Model.StandardModel;
class ApplicationRunner{
     public static Models[] models =  new Models[]{new StandardModel(), new BinModel(), new BinModel2()};
}

public class mainMofPinaG {
    public static void main(String[] args) {
        new ChoiceFrame(ApplicationRunner.models);
//        new BinModel().run();
    }
}
