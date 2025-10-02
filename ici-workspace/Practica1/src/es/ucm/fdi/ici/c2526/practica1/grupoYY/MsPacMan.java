package es.ucm.fdi.ici.c2526.practica1.grupoYY;

import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacMan extends PacmanController{

	private int _dist;
	
    @Override
    public MOVE getMove(Game game, long timeDue) {
        //Falta comprobar que no haya fantasmas en el camino hacia la PowerPill
    	
    	int pacManPos = game.getPacmanCurrentNodeIndex();
    	MOVE nMove = null; 
    	
    	//Comprobar si tiene 2 o mas fantasmas a menos de D distancia
    	int nearG = getNearGhosts(game, pacManPos);
    	
    	//si >= 2 va hacia la powerPill mas cercana
    	if(nearG >= 2) {
    		int nPP = getNearestPowerPill(pacManPos, game);
    		nMove = game.getApproximateNextMoveTowardsTarget(pacManPos, nPP, game.getPacmanLastMoveMade() , DM.PATH);
    	}
    	
    	//si no, va hacia la pill mas cercana que no tenga fantasmas cerca
    	else {
    		int nP = nextPillSegura(game, pacManPos);
    		nMove = game.getApproximateNextMoveTowardsTarget(pacManPos, nP, game.getPacmanLastMoveMade() , DM.PATH);
    	}
    	
    	return nMove;
    }
    
    private int nextPillSegura(Game game, int pacManPos) {
    	
    	double minDist = Integer.MAX_VALUE;
    	int nPill = 0;
    	for	(int pill : game.getActivePillsIndices()) {
    		double dist = game.getDistance(pacManPos, pill, game.getPacmanLastMoveMade() , DM.PATH);
    		if(dist < minDist && esSegura(game, pill)) {
    			minDist = dist;
    			nPill = pill;
    		}
    	}
    	return nPill;
    }
    
    private Boolean esSegura(Game game, int pill) {
    	return getNearGhosts(game, pill) == 0;
    }
    
    private int getNearGhosts(Game game, int pacManPos) {
    	int i = 0;
    	
    	for(GHOST ghostType : GHOST.values()) {
    		int ghostPos = game.getGhostCurrentNodeIndex(ghostType);
    		
    		if(game.getGhostLairTime(ghostType) <= 0 && game.getGhostEdibleTime(ghostType) <= 0) {
    			double dist = game.getDistance(pacManPos, ghostPos, DM.PATH);
    			if(dist < _dist){
    				i++;
    			}
    		}
    	}
    	return i;
    }
    
    private int getNearestPowerPill(int pacManPos, Game game) {
    	double minDist = Integer.MAX_VALUE;
    	int nPpill = 0;
    	for	(int ppill : game.getActivePowerPillsIndices()) {
    		double dist = game.getDistance(pacManPos, ppill, game.getPacmanLastMoveMade() , DM.PATH);
    		if(dist < minDist) {
    			minDist = dist;
    			nPpill = ppill;
    		}
    	}
    	return nPpill;
    }
    
    public String getName() {
    	return "MsPacManNeutral";
    }

}
