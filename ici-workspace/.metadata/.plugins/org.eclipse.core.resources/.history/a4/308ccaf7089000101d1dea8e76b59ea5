import es.ucm.fdi.ici.c2425.practica0.grupoIndividual.GhostsRandom;
import es.ucm.fdi.ici.c2425.practica0.grupoIndividual.MsPacManRandom;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.HumanController;
import pacman.controllers.KeyBoardInput;
import pacman.controllers.PacmanController;

public class ExecutorTest {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(3.0)
                .build();

        PacmanController pacMan = new HumanController(new KeyBoardInput());
        GhostController ghosts = new GhostsRandom();
        
        System.out.println( 
            executor.runGame(pacMan, ghosts, 50) //last parameter defines speed
        );     
    }
	
}
