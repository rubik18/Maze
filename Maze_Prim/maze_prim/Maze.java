package maze_prim;
import java.awt.Color;
import java.awt.Graphics;
import java.util.*;
/**
 * ----------------- @author thamdinh1803 -----------------
 * ---------------------------------------------------------
 */

public class Maze {

	private ArrayList<Cell> wallCells = new ArrayList<Cell>();
	private Cell startCell, endCell = null, lastCell;
	private final Color StartColor = new Color(36, 173, 222), EndColor = new Color(138, 189, 0), 
			LastColor = new Color(204, 0, 0), PathColor = new Color(179, 104, 217);
	private Random rand = new Random();
	private Cell[][] maze;
	private final int CellWidth;

	public enum Dir {
		Left, Up, Right, Down
	}
	
	public Maze(int width, int height, int cellWidth) {
		this.CellWidth = cellWidth;
		
		maze = new Cell[width + 2][height + 2];	// +2 them border ngan loi  null
		
		//  mac dinh cac o chua duoc truy cap toi là -1
		for(int col = 1; col < maze.length - 1; col++) {
			for(int row = 0; row < maze[0].length; row++) {
				maze[col][row] = new Cell(col, row, -1);
			}
		}
		

		//Cung cap mot đuong vien  duy nhat ngan tham chieu ra khoi khung gioi han
		for(int col = 0; col < maze.length; col++) {
			for(int row = 0; row < maze[0].length; row += maze[0].length - 1) {
				maze[col][row] = new Cell(col, row, -2);
			}
		}
		
		for(int col = 0; col < maze.length; col += maze.length - 1) {
			for(int row = 0; row < maze[0].length; row++) {
				maze[col][row] = new Cell(col, row, -2);
			}
		}
		
		// chon ngau nhien mot o de bat dau me cung
		//Selects a random cell as the start of the maze
		int x = rand.nextInt(width - 2) + 1;
		int y = rand.nextInt(height - 2) + 1;
		maze[x][y].setVal(1);
		startCell = maze[x][y];
		addPassage(startCell);
	}

	/**
	 * Generates a maze using Prim's Algorithm
	 * 
	 * @return true if the maze is fully expanded
	 */
	public boolean expand() {
		int randIndex = rand.nextInt(wallCells.size());
		addPassage(wallCells.remove(randIndex));
		
		if(wallCells.isEmpty()) {
			endCell = lastCell;
			lastCell = null;
		}
		
		return wallCells.isEmpty();	// Done expanding when there are no more unchecked wall cells
	}
	
	/**
	 * đập tường thành lối đi
	 */
	private void addPassage(Cell cell) {
		cell.setPassage();
		addWalls(cell);
	}
	
	/**
	 * thêm các bức tường liền kề với ô cho trước
	 */
	private void addWalls(Cell cell) {
		cell.setWall(Dir.Left);
		cell.setWall(Dir.Up);
		cell.setWall(Dir.Right);
		cell.setWall(Dir.Down);
	}
	
	/**
	 * Paints the maze
	 */
	public void paint(Graphics g) {
		for(Cell[] row : maze) {
			for(Cell cell : row) {
				cell.paintCell(g);
			}
		}

		for(Cell[] row : maze) {
			for(Cell cell : row) {
				cell.paintWalls(g);
			}
		}
	}
	
	public Cell getStartCell() {
		return startCell;
	}

	public Cell getEndCell() {
		return endCell;
	}
	
	/**
	 * Holds a given location in the maze, allowing or easy manipulation of neighboring cells
	 */
	class Cell {
		private final int x, y, paintX, paintY;
		private HashMap<Dir, Boolean> passable = new HashMap<Dir, Boolean>();
		
		private Dir prev;
		private boolean inPath = false;
		private int val;
		
		public Cell(int x, int y, int val) {
			this.x = x;
			paintX = x * CellWidth;
			this.y = y;
			paintY = y * CellWidth;
			this.val = val;
			passable.put(Dir.Left, false);
			passable.put(Dir.Up, false);
			passable.put(Dir.Right, false);
			passable.put(Dir.Down, false);
		}
		
		// đặt ô là một lối đi nếu nó là duy nhất (hay không tạo các ngã ba, không có hướng đi nào khác)
		
		 
		public boolean setPassage() {
			maze[x][y].setVal(0);
			lastCell = maze[x][y];
			if(prev != null) openPrevious();
			return true;
		}
		
		private void openPrevious() {
			openPassage(prev);
			switch(prev) {
			case Left:
				openPassage(Dir.Left);
				getNeighbor(Dir.Left).openPassage(Dir.Right);
				break;
			case Up:
				openPassage(Dir.Up);
				getNeighbor(Dir.Up).openPassage(Dir.Down);
				break;
			case Right:
				openPassage(Dir.Right);
				getNeighbor(Dir.Right).openPassage(Dir.Left);
				break;
			case Down:
				openPassage(Dir.Down);
				getNeighbor(Dir.Down).openPassage(Dir.Up);
			}
		}
		
		public void openPassage(Dir dir) {
			passable.put(dir, true);
		}
		
		public void setWall(Dir dir) {
			Cell cell;
			switch(dir) {
			case Left:
				cell = getNeighbor(Dir.Left);
				cell.setPrev(Dir.Right);
				break;
			case Up:
				cell = getNeighbor(Dir.Up);
				cell.setPrev(Dir.Down);
				break;
			case Right:
				cell = getNeighbor(Dir.Right);
				cell.setPrev(Dir.Left);
				break;
			case Down:
				cell = getNeighbor(Dir.Down);
				cell.setPrev(Dir.Up);
				break;
			default:
				return;
			}
			
			if(cell.getVal() == -1) {
				cell.setVal(1);
				wallCells.add(cell);
			}
		}
		
		
		public boolean isIntersection() {
			int passTotal = 0;
			for(boolean pass : passable.values()) {
				if(pass) passTotal++;
			}
			return passTotal > 2;
		}
		
		public Cell getNeighbor(Dir dir) {
			switch(dir) {
			case Left:
				return maze[x - 1][y];
			case Up:
				return maze[x][y - 1];
			case Right:
				return maze[x + 1][y];
			default:
				return maze[x][y + 1];
			}
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public int getVal() {
			return val;
		}
		
		public HashMap<Dir, Boolean> getPassable() {
			return passable;
		}
		
		public void setVal(int val) {
			this.val = val;
		}
		
		public void setInPath(boolean inPath) {
			this.inPath = inPath;
		}
		
		public void setPrev(Dir prev) {
			this.prev = prev;
		}
		
		public void paintCell(Graphics g) {
			if(this == startCell) {
				g.setColor(StartColor);
			} else if(this == lastCell) {
				g.setColor(LastColor);
			} else if(endCell != null && this == endCell) {
				g.setColor(EndColor);
			} else if(inPath) {
				g.setColor(PathColor);
			} else if(val == 0) {
				g.setColor(Color.WHITE);
			} else {
				return;
			}
			
			g.fillRect(paintX, paintY, CellWidth, CellWidth);
		}
		
		public void paintWalls(Graphics g) {
			if(val == 0) {
				g.setColor(Color.BLACK);
				if(!passable.get(Dir.Left)) {
					g.drawLine(x * CellWidth, y * CellWidth, x * CellWidth, (y + 1) * CellWidth);
				}
				
				if(!passable.get(Dir.Up)) {
					g.drawLine(x * CellWidth, y * CellWidth, (x + 1) * CellWidth, y * CellWidth);			
				}
				
				if(!passable.get(Dir.Right)) {
					g.drawLine((x + 1) * CellWidth, y * CellWidth, (x + 1) * CellWidth, (y + 1) * CellWidth);
				}
				
				if(!passable.get(Dir.Down)) {
					g.drawLine(x * CellWidth, (y + 1) * CellWidth, (x + 1) * CellWidth, (y + 1) * CellWidth);
				}
			}
		}		
		
	}

}