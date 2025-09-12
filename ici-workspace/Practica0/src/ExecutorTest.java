import es.ucm.fdi.ici.c2425.practica0.grupoIndividual.GhostsAgressive;
import es.ucm.fdi.ici.c2425.practica0.grupoIndividual.GhostsRandom;
import es.ucm.fdi.ici.c2425.practica0.grupoIndividual.MsPacManRandom;
import es.ucm.fdi.ici.c2425.practica0.grupoIndividual.MsPacManRunAway;
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

        PacmanController pacMan = new MsPacManRunAway();
        GhostController ghosts = new GhostsAgressive();
        
        System.out.println( 
            executor.runGame(pacMan, ghosts, 50) //last parameter defines speed
        );     
    }
	
}
