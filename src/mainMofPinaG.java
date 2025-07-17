import Graphics.ChoiceFrame;
import Model.Models;
import Model.StandardModel;
class ApplicationRunner{
     public static Models[] models =  new Models[]{(Models) new StandardModel()};
}

public class mainMofPinaG {
    public static void main(String[] args) {
        new ChoiceFrame(ApplicationRunner.models);
//        new StandardModel().run();
    }
}
