package es.ucm.fdi.ici.c2425.practica0.SanchezDiez;

import java.util.Random;

import pacman.controllers.PacmanController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class MsPacManRunAway extends PacmanController{

	    @Override
	    public MOVE getMove(Game game, long timeDue) {
	    	int pacManPos = game.getPacmanCurrentNodeIndex();
	    	GHOST closeGhost = getCloseGhost(pacManPos, game);
	    	
	        return game.getApproximateNextMoveAwayFromTarget(pacManPos, game.getGhostCurrentNodeIndex(closeGhost), game.getPacmanLastMoveMade() , DM.PATH);
	    }

	    private GHOST getCloseGhost(int pacManPos, Game game) {
	    	double minDist = game.getDistance(pacManPos , game.getGhostCurrentNodeIndex(GHOST.values()[0]), DM.PATH);
	    	GHOST closeGhost = GHOST.values()[0];
	    	
	    	for (GHOST ghostType : GHOST.values()) {
	    		double currGhostDist = game.getDistance(pacManPos, game.getGhostCurrentNodeIndex(ghostType), DM.PATH);
	            if (minDist > currGhostDist) {
	            	closeGhost = ghostType;
	            	minDist =  currGhostDist;
	            }
	        }
	    	return closeGhost;
	    }
	    
	    public String getName() {
	    	return "MsPacManRunAway";
	    }
	    
}
