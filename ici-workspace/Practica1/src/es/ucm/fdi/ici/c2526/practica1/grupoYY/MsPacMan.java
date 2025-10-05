package es.ucm.fdi.ici.c2526.practica1.grupoYY;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class MsPacMan extends PacmanController{

	private int _dist = 40;
	private int _d = 20;
	
    @Override
    public MOVE getMove(Game game, long timeDue) {
    	
    	int pacManPos = game.getPacmanCurrentNodeIndex();
    	MOVE nMove = null; 
    	
    	//Comprobar si tiene 2 o mas fantasmas a menos de D distancia
    	int nearG = getNearGhosts(game, pacManPos, _dist);
    	
    	//si >= 2 va hacia la powerPill mas cercana
    	if(nearG >= 2) {
    		int nPP = getNearestPowerPill(pacManPos, game);
    		
			if (nPP != -1) {
				int[] path = game.getShortestPath(pacManPos, nPP);
				int ghostCollide = hayFantasmaEnMedio(game, path, pacManPos, nPP);
				
				//si se encuentra un fantasma en medio del camino que tiene que coger para ir a la PP huye del fantasma
	    		 if(ghostCollide != -1) {
	    			 GameView.addLines(game, Color.red, pacManPos, nPP);
	    			return game.getApproximateNextMoveAwayFromTarget(pacManPos,ghostCollide , game.getPacmanLastMoveMade() , DM.PATH);
	    		 }
	    		 
	    		 else 
	    			 GameView.addLines(game, Color.red, pacManPos, nPP);
	    			 return game.getApproximateNextMoveTowardsTarget(pacManPos, nPP, game.getPacmanLastMoveMade() , DM.PATH);
			}
    	}
    	
    	//si no, intenta perseguir un fantasma que se pueda comer
    	else{
    		int ghostToChase = fantasmaComestible(game, pacManPos);
    		
    		if(ghostToChase != -1) {
    			
    			int[] path = game.getShortestPath(pacManPos, ghostToChase);
    			int ghostCollide = hayFantasmaEnMedio(game, path, pacManPos, ghostToChase);
    	   		 
    			//igual que antes si hay algun fantasma en medio lo evita
    	   		 if(ghostCollide != -1 && ghostCollide != ghostToChase) {
    	   			GameView.addLines(game, Color.YELLOW, pacManPos, ghostCollide);
    	   			return game.getApproximateNextMoveAwayFromTarget(pacManPos,ghostCollide , game.getPacmanLastMoveMade() , DM.PATH);
    	   		 }
    	   		 GameView.addLines(game, Color.red, pacManPos, ghostToChase);
    			return game.getApproximateNextMoveTowardsTarget(pacManPos, ghostToChase, game.getPacmanLastMoveMade(), DM.PATH);
    		}
    		//va hacia la pill mas cercana que no tenga fantasmas a cierta distancia d
    		int nP = nextPillSegura(game, pacManPos);
    		GameView.addLines(game, Color.RED, pacManPos, nP);
    		return game.getApproximateNextMoveTowardsTarget(pacManPos, nP, game.getPacmanLastMoveMade() , DM.PATH);
    	}
    	return null;
    }
    
    private int fantasmaComestible(Game game, int pacManPos) {
    	GHOST ghostToChase = null;
    	double dist = Integer.MAX_VALUE;
    	for(GHOST ghostType : GHOST.values()) {
    		if(game.isGhostEdible(ghostType) && 
    				game.getDistance(game.getGhostCurrentNodeIndex(ghostType), pacManPos, game.getPacmanLastMoveMade(), DM.PATH) < dist) {
    			ghostToChase = ghostType;
    			dist = game.getDistance(game.getGhostCurrentNodeIndex(ghostType), pacManPos, game.getPacmanLastMoveMade(), DM.PATH);
    		}
    	}
    	if(ghostToChase == null) return -1;
    	return game.getGhostCurrentNodeIndex(ghostToChase);
    }
    
    private int hayFantasmaEnMedio(Game game, int[] path, int pacManPos, int nPP) {
    	List<Integer> ghostPos = new ArrayList<Integer>();
    	for	(GHOST ghostType : GHOST.values())
    		if(!game.isGhostEdible(ghostType) && game.getGhostLairTime(ghostType) <= 0) {
	    		ghostPos.add(game.getGhostCurrentNodeIndex(ghostType));
	    	
	    	for(int i : path) {
	    		if (ghostPos.contains(i)) return i;
	    	}
    	}
    	return -1;
    	
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
    	return getNearGhosts(game, pill, _d) == 0;
    }
    
    private int getNearGhosts(Game game, int pacManPos, int d) {
    	int i = 0;
    	
    	for(GHOST ghostType : GHOST.values()) {
    		int ghostPos = game.getGhostCurrentNodeIndex(ghostType);
    		
    		if(game.getGhostLairTime(ghostType) <= 0 && game.getGhostEdibleTime(ghostType) <= 0) {
    			double dist = game.getDistance(pacManPos, ghostPos, DM.PATH);
    			if(dist < d){
    				i++;
    				GameView.addLines(game, Color.BLUE, pacManPos, ghostPos);
    			}
    		}
    	}
    	return i;
    }
    
    private int getNearestPowerPill(int pacManPos, Game game) {
    	double minDist = Integer.MAX_VALUE;
    	int nPpill = 0;
    	//Si no hay PowerPills activas devuelve -1
    	if(game.getNumberOfActivePowerPills() == 0) return -1;
    	
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
