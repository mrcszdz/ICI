package es.ucm.fdi.ici.c2425.practica0.SanchezDiez;

import java.awt.Color;

import pacman.controllers.PacmanController;
import pacman.game.Game;
import pacman.game.GameView;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class MsPacMan extends PacmanController{

	private int _limit = 50;
	private Color[] _colors = {Color.RED, Color.PINK, Color.CYAN, Color.ORANGE};
	    @Override
	    public MOVE getMove(Game game, long timeDue) {
	    	int pacManPos = game.getPacmanCurrentNodeIndex();
	    	GHOST nearestGhost = getNearestChasingGhost(_limit, pacManPos , game);
	    	
	    	if(nearestGhost != null) {
	    		GameView.addPoints(game, getColor(nearestGhost), game.getShortestPath(pacManPos, game.getGhostCurrentNodeIndex(nearestGhost)));
	    		return  game.getApproximateNextMoveAwayFromTarget(pacManPos, game.getGhostCurrentNodeIndex(nearestGhost), game.getPacmanLastMoveMade() , DM.PATH);
	    	}
	    	
	    	nearestGhost = getNearestEdibleGhost(_limit, pacManPos, game);
	    	if(nearestGhost != null) {
	    		GameView.addPoints(game, getColor(nearestGhost), game.getShortestPath(pacManPos, game.getGhostCurrentNodeIndex(nearestGhost)));
	    		return game.getApproximateNextMoveTowardsTarget(pacManPos, game.getGhostCurrentNodeIndex(nearestGhost), game.getPacmanLastMoveMade() , DM.PATH);
	    	}
	    	
	    	int nPill = min(getNearestPill(pacManPos, game), getNearestPowerPill(pacManPos, game));
	    	
	    	return game.getApproximateNextMoveTowardsTarget(pacManPos, nPill, game.getPacmanLastMoveMade() , DM.PATH);
	    }
	    
	    private Color getColor(GHOST g) {
	    	for	(GHOST ghostType : GHOST.values()) {
	    		if(ghostType.equals(g)) return _colors[ghostType.ordinal()];
	    	}
	    	return null;
	    }
	    
	    private GHOST getNearestChasingGhost(int limit, int pacManPos, Game game) {
	    	GHOST nGhost = null;
	    	double minDist = limit;
	    	for(GHOST ghostType : GHOST.values()) {
	    		int ghostPos = game.getGhostCurrentNodeIndex(ghostType);
	    		
	    		if(game.getGhostLairTime(ghostType) <= 0 && game.getGhostEdibleTime(ghostType) <= 0) {
	    			double dist = game.getDistance(pacManPos, ghostPos, DM.PATH);
	    			if(dist < minDist){
	    				minDist = dist;
	    				nGhost = ghostType;
	    			}
	    		}
	    	}
	    	return nGhost;
	    }
	    
	    private GHOST getNearestEdibleGhost(int limit, int pacManPos, Game game) {
	    	GHOST nGhost = null;
	    	double minDist = limit;
	    	for(GHOST ghostType : GHOST.values()) {
	    		int ghostPos = game.getGhostCurrentNodeIndex(ghostType);
	    		
	    		if(game.getGhostEdibleTime(ghostType) > 0) {
	    			double dist = game.getDistance(pacManPos, ghostPos, DM.PATH);
	    			if(dist < minDist){
	    				minDist = dist;
	    				nGhost = ghostType;
	    			}
	    		}
	    	}
	    	return nGhost;
	    }
	    
	    private int getNearestPill(int pacManPos, Game game) {
	    	double minDist = Integer.MAX_VALUE;
	    	int nPill = 0;
	    	for	(int pill : game.getActivePillsIndices()) {
	    		double dist = game.getDistance(pacManPos, pill, game.getPacmanLastMoveMade() , DM.PATH);
	    		if(dist < minDist) {
	    			minDist = dist;
	    			nPill = pill;
	    		}
	    	}
	    	return nPill;
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
	    
	   private int min(int n, int m) {
		   if(n < m) return n;
		   else return m;
	   }
	   
	    public String getName() {
	    	return "MsPacMan";
	    }
	    
}
