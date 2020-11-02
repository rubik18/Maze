package maze_prim;

import java.util.HashMap;
import maze_prim.Maze.Cell;
import maze_prim.Maze.Dir;
import stack.LinkedListStack;

/**
 * ----------------- @author thamdinh1803 -----------------
 * ---------------------------------------------------------
 */
public class Solver {

	private Cell startCell, endCell;
	private LinkedListStack<Cell> path = new LinkedListStack<Cell>();
	private HashMap<Cell, HashMap<Dir, Boolean>> checked = 
			new HashMap<Cell, HashMap<Dir, Boolean>>(); 
	
	private boolean isBacktracking = false;
	
	public Solver(Maze maze) {
		this.startCell = maze.getStartCell();
		this.endCell = maze.getEndCell();
		path.push(startCell);
	}
	
	/**
	 * giai quyet me cung su dung stack
	 * 
	 * tra ve true neu me cung da duoc giai quyet xong
	 */
	public boolean solve() {
		if(isBacktracking) {
			if(getNextDir(path.peek()) != null) {
				isBacktracking = false;
			} else {
				path.pop().setInPath(false);
			}
			return false;
		}
		
		Cell current = path.peek();
		if(!checked.containsKey(current)) {
			expandChecked(current);
		}
		
		Dir nextDir = getNextDir(current);
		if(nextDir == null) {
			isBacktracking = true;
			path.pop().setInPath(false);
			return false;
		} else {
			Cell nextCell = current.getNeighbor(nextDir);
			nextCell.setInPath(true);
			path.push(nextCell);
			checked.get(current).put(nextDir, true);
			expandChecked(nextCell);
			checked.get(nextCell).put(getOppositeDir(nextDir), true);
		}

		if(path.peek() == endCell) {
			return true;
		} else {
			return false;
		}
	}
	
	
	private void expandChecked(Cell cell) {
		HashMap<Dir, Boolean> passable = new HashMap<Dir, Boolean>(cell.getPassable());
		for(Dir dir : passable.keySet()) {
			passable.put(dir, !passable.get(dir));
		}
		checked.put(cell, passable);
	}
	
	/**
	 * tra ve huong tiep theo se di de tiep tuc kham pha me cung
	 */
	private Dir getNextDir(Cell cell) {
		HashMap<Dir, Boolean> passed = checked.get(cell);
		if(!passed.get(Dir.Left)) {
			return Dir.Left;
		} else if(!passed.get(Dir.Up)){
			return Dir.Up;
		} else if(!passed.get(Dir.Right)) {
			return Dir.Right;
		} else if(!passed.get(Dir.Down)){
			return Dir.Down;
		} else {
			return null;
		}
	}
	
	private Dir getOppositeDir(Dir dir) {
		switch(dir) {
		case Left:
			return Dir.Right;
		case Up:
			return Dir.Down;
		case Right:
			return Dir.Left;
		default:
			return Dir.Up;
		}
	}
	
	
	public void clearPath() {
		for(Cell cell : path) {
			cell.setInPath(false);
		}
	}
	
}
