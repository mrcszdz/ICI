package es.ucm.fdi.ici.c2526.practica1.grupoYY;

import java.util.EnumMap;
import java.util.ArrayList;
import java.util.Random;

import pacman.controllers.GhostController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public final class Ghosts extends GhostController {
    private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
    private MOVE[] allMoves = MOVE.values();
    private boolean isSomeOneEdible;
    private GHOST blinky;
    private Random rnd = new Random();
    final float distanceToBlinky = 2; // distancia a la que estar de blinky para bajar la flag
    private Boolean passedBlinky = true;

    public Ghosts(){
    	super();
    	for (GHOST ghost : GHOST.values()) {
        	if(ghost.name() == "BLINKY")
        		blinky = ghost;
        }
    }
    
    @Override
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
        moves.clear();
        
        isSomeOneEdible = false;
        // me gustaria quitar ese break por una busqueda con un while (mas eficiente)
        for (GHOST ghost : GHOST.values()) {
        	if(game.isGhostEdible(ghost)) 
        	{
        		isSomeOneEdible = true;
        	}
        }
        
        for (GHOST ghost : GHOST.values()) {
        	 //si no comprobamos así blinky no persigue a MsPacman justo al tomar una pill
        	/*if(ghost != blinky && !game.doesGhostRequireAction(ghost))
        		continue;*/
        	
        	// Si eres blinky siempre vas a acosar a Pacman, además eres el kamikaze que ayudará más adelante
        	if(ghost == blinky) {
        		moves.put(blinky, game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(blinky),
        				game.getPacmanCurrentNodeIndex(),game.getGhostLastMoveMade(blinky),DM.PATH));
        	}
        	else { 
        		//Si no esta levantada la flag y no eres comestible no quieres acercarte a Blinky ni alejarte de 
        		//Ms.PacMan para acorralarle
        		if(!game.isGhostEdible(ghost)) 
        		{
        			MOVE acercarseAlPacman = game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
        					game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH);
        			
        			if(acorralado(game,ghost)) {
        				moves.put(ghost, acercarseAlPacman);
        				continue;
        			}
        			else {
            			moves.put(ghost, intentaAcorralar(game,ghost));
            			}
        		}
        		else if(game.isGhostEdible(ghost) && isSomeOneEdible)
        		{
        			//huyes hacia blinky si no esta muerto y no es la direccion hacia Ms.Pacman
        			if(game.getGhostLairTime(blinky) <= 0 && dontGoTowardPacman(game,ghost)) {
        				moves.put(ghost, game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
        						game.getGhostCurrentNodeIndex(blinky), game.getGhostLastMoveMade(ghost), DM.PATH));
        			}
        			else {//si no huyes de Ms.PacMan
        				moves.put(ghost, game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost),
        						game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH));
        			}
        			
        			if(game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), 
        					game.getGhostCurrentNodeIndex(blinky)) > distanceToBlinky) {
        				isSomeOneEdible = false;
        			}
        		}
        		else { // si eres edible pero la flag ha caido huyes de Ms.Pacman
        			moves.put(ghost, game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost),
    						game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH));
        		}
        	}
        }
        return moves;
    }
    
    private boolean dontGoTowardPacman(Game game, GHOST ghost) {
    	return  game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
				game.getGhostCurrentNodeIndex(blinky), game.getGhostLastMoveMade(ghost), DM.PATH) 
    			!= 
				game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
				game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH);
    }
    
    private boolean acorralado(Game game, GHOST ghost) {
    	/*return (game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getGhostCurrentNodeIndex(blinky)) 
    			> game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getPacmanCurrentNodeIndex()));*/
    	if(game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), game.getGhostCurrentNodeIndex(blinky))<= 0) {
    		return false;
    	}
    		
    	int[] path = game.getShortestPath(game.getGhostCurrentNodeIndex(ghost), game.getGhostCurrentNodeIndex(blinky));
    	
    	for(int i: path) {
    		if(i == game.getPacmanCurrentNodeIndex())
    			return true;
    	}
    	return false;
    }
    
    private MOVE intentaAcorralar(Game game, GHOST ghost) {
    	
    	for(MOVE move: game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghost))) {
    		if(move != game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost),
					game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH) && 
    				move != game.getNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghost),
        					game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH))
    			return move;
    	}
    	
    	return game.getNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghost),
				game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghost), DM.PATH);
    }
    
    public String getName() {
    	return "GhostsNeutral";
    }
}
