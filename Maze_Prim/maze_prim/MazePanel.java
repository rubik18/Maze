package maze_prim;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/**
 * ----------------- @author thamdinh1803 -----------------
 * ---------------------------------------------------------
 */
public class MazePanel extends JPanel implements ActionListener  {

	private static final long serialVersionUID = 1L;
	private JFrame frameAbout, frameHelp;
	private static final int MaxWidth = 120, MaxHeight = 80, BorderWidth = 8, DefaultCellWidth = 8;
	private static int panelOffsetX, panelOffsetY;
	
	private int cellWidth = DefaultCellWidth;
	private boolean isLooping = false;

	private final Color BackgroundColor = new Color(163, 168, 171), BorderColor = new Color(37, 40, 41);
	private boolean quickGen = false, quickSolve = false;
	
	private JLabel generateMazeBtn = new JLabel(), solveMazeBtn = new JLabel(), loopBtn = new JLabel(),
			generationAlgorithmLbl = new JLabel(), resetBtn = new JLabel(),  algorithmLbl = new JLabel(), 
			arrowLeftBtn = new JLabel(), arrowRightBtn = new JLabel(), cellWidthLbl = new JLabel(),
			widthLbl = new JLabel(), arrowUpBtn = new JLabel(), arrowDownBtn = new JLabel(),
			quickGenLbl = new JLabel(), quickGenBtn = new JLabel(), quickSolveLbl = new JLabel(), 
			quickSolveBtn = new JLabel();
	//private JLabel playLbl = new JLabel();
	private ImageIcon generateMazeIcon, generateMazeHoverIcon, solveMazeIcon, solveMazeHoverIcon,
		loopIcon, loopHoverIcon, resetIcon, resetHoverIcon, generationAlgorithmIcon, primsIcon, 
		arrowLeftIcon, arrowLeftHoverIcon, arrowRightIcon, arrowRightHoverIcon, arrowUpIcon,
		arrowUpHoverIcon, arrowDownIcon, arrowDownHoverIcon, cellWidthIcon, quickGenIcon,
		quickSolveIcon, checkboxYesIcon, checkboxNoIcon;
	//private ImageIcon playIcon ,playHoverIcon;
	private HashMap<Integer, ImageIcon> widthIcons = new HashMap<Integer, ImageIcon>();
	
	private final Dimension BigBtnDimension = new Dimension(200, 32),
			ArrowDimension = new Dimension(23, 23), CheckboxDimension = new Dimension(15, 15);
	
	private Timer timer;
	private State state = State.Idle;
	private Maze maze;
	private Solver solver;
	
	private enum State {
		Idle, Generating, Solving
	}
	
	public MazePanel() {
		setLayout(null);
		setIcons();
		
		MouseListener mouseListen = new MouseListener();
		timer = new Timer(0, new TimerListener());
		
		generateMazeBtn.setIcon(generateMazeIcon);
		generateMazeBtn.setLocation(panelOffsetX + 25, panelOffsetY + 40);
		generateMazeBtn.setSize(BigBtnDimension);
		generateMazeBtn.addMouseListener(mouseListen);
		add(generateMazeBtn);
		
		solveMazeBtn.setIcon(solveMazeIcon);
		solveMazeBtn.setLocation(panelOffsetX + 25, panelOffsetY + 90);
		solveMazeBtn.setSize(BigBtnDimension);
		solveMazeBtn.addMouseListener(mouseListen);
		add(solveMazeBtn);
		
		loopBtn.setIcon(loopIcon);
		loopBtn.setLocation(panelOffsetX + 25, panelOffsetY + 140);
		loopBtn.setSize(BigBtnDimension);
		loopBtn.addMouseListener(mouseListen);
		add(loopBtn);
		
		resetBtn.setIcon(resetIcon);
		resetBtn.setLocation(panelOffsetX + 25, panelOffsetY + 190);
		resetBtn.setSize(BigBtnDimension);
		resetBtn.addMouseListener(mouseListen);
		add(resetBtn);
		
//		playLbl.setIcon(playIcon);
//		playLbl.setLocation(panelOffsetX + 25, panelOffsetY + 240);
//		playLbl.setSize(BigBtnDimension);
//		resetBtn.addMouseListener(mouseListen);
//		add(playLbl);
		
		generationAlgorithmLbl.setIcon(generationAlgorithmIcon);
		generationAlgorithmLbl.setLocation(panelOffsetX + 45, panelOffsetY + 290);
		generationAlgorithmLbl.setSize(163, 15);
		add(generationAlgorithmLbl);
		
		algorithmLbl.setIcon(primsIcon);
		algorithmLbl.setLocation(panelOffsetX + 55, panelOffsetY + 322);
		algorithmLbl.setSize(140, 32);
		add(algorithmLbl);
		
		arrowLeftBtn.setIcon(arrowLeftIcon);
		arrowLeftBtn.setLocation(panelOffsetX + 24, panelOffsetY + 327);
		arrowLeftBtn.setSize(ArrowDimension);
		arrowLeftBtn.addMouseListener(mouseListen);
		add(arrowLeftBtn);
		
		arrowRightBtn.setIcon(arrowRightIcon);
		arrowRightBtn.setLocation(panelOffsetX + 203, panelOffsetY + 327);
		arrowRightBtn.setSize(ArrowDimension);
		arrowRightBtn.addMouseListener(mouseListen);
		add(arrowRightBtn);
		
		cellWidthLbl.setIcon(cellWidthIcon);
		cellWidthLbl.setLocation(panelOffsetX + 144, panelOffsetY + 402);
		cellWidthLbl.setSize(77, 12);
		add(cellWidthLbl);
		
		widthLbl.setIcon(widthIcons.get(cellWidth));
		widthLbl.setLocation(panelOffsetX + 146, panelOffsetY + 432);
		widthLbl.setSize(38, 32);
		add(widthLbl);
		
		arrowUpBtn.setIcon(arrowUpIcon);
		arrowUpBtn.setLocation(panelOffsetX + 196, panelOffsetY + 425);
		arrowUpBtn.setSize(ArrowDimension);
		arrowUpBtn.addMouseListener(mouseListen);
		add(arrowUpBtn);
		
		arrowDownBtn.setIcon(arrowDownIcon);
		arrowDownBtn.setLocation(panelOffsetX + 196, panelOffsetY + 449);
		arrowDownBtn.setSize(ArrowDimension);
		arrowDownBtn.addMouseListener(mouseListen);
		add(arrowDownBtn);
		
		quickGenLbl.setIcon(quickGenIcon);
		quickGenLbl.setLocation(panelOffsetX + 34, panelOffsetY + 392);
		quickGenLbl.setSize(82, 15);
		add(quickGenLbl);
		
		quickGenBtn.setIcon(checkboxNoIcon);
		quickGenBtn.setLocation(panelOffsetX + 68, panelOffsetY + 412);
		quickGenBtn.setSize(CheckboxDimension);
		quickGenBtn.addMouseListener(mouseListen);
		add(quickGenBtn);
		
		quickSolveLbl.setIcon(quickSolveIcon);
		quickSolveLbl.setLocation(panelOffsetX + 29, panelOffsetY + 439);
		quickSolveLbl.setSize(93, 14);
		add(quickSolveLbl);
		
		quickSolveBtn.setIcon(checkboxNoIcon);
		quickSolveBtn.setLocation(panelOffsetX + 68, panelOffsetY + 459);
		quickSolveBtn.setSize(CheckboxDimension);
		quickSolveBtn.addMouseListener(mouseListen);
		add(quickSolveBtn);
	}
	
	private void setIcons() {
		generateMazeIcon = new ImageIcon("/Image/generate_maze.jpg");
		generateMazeHoverIcon = new ImageIcon("/Image/generate_maze.png");
		solveMazeIcon = new ImageIcon("/Image/solve_maze.jpg");
		solveMazeHoverIcon = new ImageIcon("/Image/solve_maze.png");
		loopIcon = new ImageIcon("/Image/loop.jpg");
		loopHoverIcon = new ImageIcon("/Image/loop.png");
		resetIcon = new ImageIcon("/Image/reset.jpg");
		resetHoverIcon = new ImageIcon("/Image/reset.png");
//		playIcon = new ImageIcon("/Image/plays.jpg");
//		playHoverIcon = new ImageIcon("/Image/plays.png");
		generationAlgorithmIcon = new ImageIcon("generation_algorithm.png");
		primsIcon = new ImageIcon("/Image/prims.png");
		arrowLeftIcon = new ImageIcon("arrow_left.png");
		arrowLeftHoverIcon = new ImageIcon("arrow_left_hover.png");
		arrowRightIcon = new ImageIcon("arrow_right.png");
		arrowRightHoverIcon = new ImageIcon("arrow_right_hover.png");
		arrowUpIcon = new ImageIcon("arrow_up.png");
		arrowUpHoverIcon = new ImageIcon("arrow_up_hover.png");
		arrowDownIcon = new ImageIcon("arrow_down.png");
		arrowDownHoverIcon = new ImageIcon("arrow_down_hover.png");
		cellWidthIcon = new ImageIcon("cell_width.png");
		quickGenIcon = new ImageIcon("quick_gen.png");
		quickSolveIcon = new ImageIcon("quick_solve.png");
		checkboxYesIcon = new ImageIcon("checkbox_yes.png");
		checkboxNoIcon = new ImageIcon("checkbox_no.png");
		
		for(int i = 2; i < 34; i++) {
			String iconPath ="/Image/"+ i + "i.png";
			widthIcons.put(i, new ImageIcon(iconPath));
		}
	}
	
	public void buildMaze() {	
		if(quickGen) {
			while(!maze.expand());
			repaint();
			setIdle();
		} else {
			if(maze.expand()) {
				setIdle();
			}
			repaint();
		}
	}
	
	public void solveMaze() {
		if(quickSolve) {
			while(!solver.solve());
			repaint();
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				System.exit(1);
			}
			setIdle();
		} else {
			if(solver.solve()) {
				setIdle();
			}
			repaint();
		}
	}
	
	private void createMaze() {
		int width = (int)(MaxWidth * (DefaultCellWidth / (double)cellWidth));
		int height = (int)(MaxHeight * (DefaultCellWidth / (double)cellWidth));
		maze = new Maze(width, height, cellWidth);
		state = State.Generating;
	}
	
	private void setIdle() {
		if(isLooping) {
			if(state == State.Generating) {
				solver = new Solver(maze);
				state = State.Solving;
			} else {
				createMaze();
				state = State.Generating;
			}
		} else {
			timer.stop();
			state = State.Idle;
		}
	}
	
	public class TimerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			switch(state) {
			case Generating:
				buildMaze();
				break;
			case Solving:
				solveMaze();
				break;
			default:
				return;
			}
		}
	}
	
	public class MouseListener extends MouseAdapter {
		
		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getSource() == generateMazeBtn) {
				generateMazeBtn.setIcon(generateMazeIcon);
			} else if(e.getSource() == solveMazeBtn) {
				solveMazeBtn.setIcon(solveMazeIcon);
			} else if(e.getSource() == loopBtn) {
				loopBtn.setIcon(loopIcon);
			} else if(e.getSource() == resetBtn) {
				resetBtn.setIcon(resetIcon);
			}
//			else if(e.getSource() == playLbl) {
//				playLbl .setIcon(playIcon);
//			}
			else if(e.getSource() == arrowLeftBtn) {
				arrowLeftBtn.setIcon(arrowLeftIcon);
			} else if(e.getSource() == arrowRightBtn) {
				arrowRightBtn.setIcon(arrowRightIcon);
			} else if(e.getSource() == arrowUpBtn) {
				arrowUpBtn.setIcon(arrowUpIcon);
			} else if(e.getSource() == arrowDownBtn) {
				arrowDownBtn.setIcon(arrowDownIcon);
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			if(e.getSource() == generateMazeBtn) {
				generateMazeBtn.setIcon(generateMazeHoverIcon);
				if(state == State.Idle) {
					createMaze();
					timer.start();
				}
			} else if(e.getSource() == solveMazeBtn) {
				solveMazeBtn.setIcon(solveMazeHoverIcon);
				if(state == State.Idle && maze != null) {
					if(solver != null) solver.clearPath();
					solver = new Solver(maze);
					state = State.Solving;
					timer.start();
				} 
			} else if(e.getSource() == loopBtn) {
				loopBtn.setIcon(loopHoverIcon);
				if(state == State.Idle || isLooping) {
					isLooping = !isLooping;
					if(timer.isRunning()) {
						maze = null;
						solver = null;
						setIdle();
					} else {
						createMaze();
						timer.start();
					}
				}
			} else if(e.getSource() == resetBtn) {
				resetBtn.setIcon(resetHoverIcon);
				isLooping = false;
				maze = null;
				solver = null;
				setIdle();
				repaint();
			}
//			else if(e.getSource() == playLbl){
//				playLbl.setIcon(playHoverIcon);
//			}
			
			else if(e.getSource() == arrowLeftBtn) {
				arrowLeftBtn.setIcon(arrowLeftHoverIcon);
				// Arrow functionality will go here when more algs are added
			} else if(e.getSource() == arrowRightBtn) {
				arrowRightBtn.setIcon(arrowRightHoverIcon);
				// Arrow functionality will go here when more algs are added
			} else if(e.getSource() == arrowUpBtn) {
				arrowUpBtn.setIcon(arrowUpHoverIcon);
				if(state == State.Idle) {
					increaseCellWidth();
				}
			} else if(e.getSource() == arrowDownBtn) {
				arrowDownBtn.setIcon(arrowDownHoverIcon);
				if(state == State.Idle) {
					decreaseCellWidth();
				}
			} else if(e.getSource() == quickGenBtn && state == State.Idle) {
				quickGen = !quickGen;
				quickGenBtn.setIcon(quickGen? checkboxYesIcon : checkboxNoIcon);
			} else if(e.getSource() == quickSolveBtn && state == State.Idle) {
				quickSolve = !quickSolve;
				quickSolveBtn.setIcon(quickSolve? checkboxYesIcon : checkboxNoIcon);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			
			if(e.getSource() == generateMazeBtn) {
				generateMazeBtn.setIcon(generateMazeHoverIcon);
			} else if(e.getSource() == solveMazeBtn) {
				solveMazeBtn.setIcon(solveMazeHoverIcon);
			} else if(e.getSource() == loopBtn) {
				loopBtn.setIcon(loopHoverIcon);
			} else if(e.getSource() == resetBtn) {
				resetBtn.setIcon(resetHoverIcon);
			}
//			else if(e.getSource() == playLbl) {
//				playLbl.setIcon(playHoverIcon);
//			}
			else if(e.getSource() == arrowLeftBtn) {
				arrowLeftBtn.setIcon(arrowLeftHoverIcon);
			} else if(e.getSource() == arrowRightBtn) {
				arrowRightBtn.setIcon(arrowRightHoverIcon);
			} else if(e.getSource() == arrowUpBtn) {
				arrowUpBtn.setIcon(arrowUpHoverIcon);
			} else if(e.getSource() == arrowDownBtn) {
				arrowDownBtn.setIcon(arrowDownHoverIcon);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			
			if(e.getSource() == generateMazeBtn) {
				generateMazeBtn.setIcon(generateMazeIcon);
			} else if(e.getSource() == solveMazeBtn) {
				solveMazeBtn.setIcon(solveMazeIcon);
			} else if(e.getSource() == loopBtn) {
				loopBtn.setIcon(loopIcon);
			} else if(e.getSource() == resetBtn) { 
				resetBtn.setIcon(resetIcon);
			}
//			else if(e.getSource() == playLbl){
//				playLbl.setIcon(playIcon);
//			}
			else if(e.getSource() == arrowLeftBtn) {
				arrowLeftBtn.setIcon(arrowLeftIcon);
			} else if(e.getSource() == arrowRightBtn) {
				arrowRightBtn.setIcon(arrowRightIcon);
			} else if(e.getSource() == arrowUpBtn) {
				arrowUpBtn.setIcon(arrowUpIcon);
			} else if(e.getSource() == arrowDownBtn) {
				arrowDownBtn.setIcon(arrowDownIcon);
			}
		}
		
		private void decreaseCellWidth() {
			cellWidth = cellWidth == 2 ? 2 : cellWidth - 2;
			widthLbl.setIcon(widthIcons.get(cellWidth));
		}
		
		private void increaseCellWidth() {
			cellWidth = cellWidth == 32 ? 32 : cellWidth + 2;
			widthLbl.setIcon(widthIcons.get(cellWidth));
		}

	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(BackgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(BorderColor);
		g.fillRect(0, 0, getWidth(), BorderWidth);
		g.fillRect(getWidth() - BorderWidth, 0, BorderWidth, getHeight());
		g.fillRect(0, getHeight() - BorderWidth, getWidth(), BorderWidth);
		g.fillRect(0, 0, BorderWidth, getHeight());
		g.fillRect(panelOffsetX - BorderWidth, 0, BorderWidth, getHeight());
		
		// Paints the maze, offset by the border
		Graphics mazeG = g.create();
		mazeG.translate(BorderWidth - cellWidth, BorderWidth - cellWidth);
		if(maze != null) maze.paint(mazeG);
	}
	
	private  JMenuBar creatMenu() {
		JMenu menuHelp = new JMenu("HELP");
		menuHelp.setMnemonic(KeyEvent.VK_H);
		menuHelp.add(createMenuItem("հҽӀք", KeyEvent.VK_H, Event.CTRL_MASK));
		menuHelp.add(createMenuItem("about", KeyEvent.VK_A, Event.CTRL_MASK));
		
		JMenuBar menuBar = new JMenuBar();
//		
		menuBar.add(menuHelp);
		return menuBar;
	}
	private JMenuItem createMenuItem(String title, int keyEvent, int event) {
		JMenuItem mi = new JMenuItem(title);
		
		mi.setMnemonic(keyEvent);
		mi.setAccelerator(KeyStroke.getKeyStroke(keyEvent, event));
		mi.addActionListener(this);
		return mi;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command == "about") {
			showAbout();
		}
		if (command == "հҽӀք") {
			showHelp();
		}
	}
	private void showHelp() {
		if (frameHelp == null) {
			frameHelp = new HelpAndAbout(0, "ლℳα☡ℯლ--ɦℰ£ρ");
			frameHelp.setIconImage(new ImageIcon("icon.jpg").getImage());
		}
		frameHelp.setVisible(true);
	}

	private void showAbout() {
		if (frameAbout == null) {
			frameAbout = new HelpAndAbout(1, "ლℳα☡ℯლ - -ﾑ乃ouｲ");
			frameAbout.setIconImage(new ImageIcon("icon.jpg").getImage());
		}
		frameAbout.setVisible(true);
	}

	
	
	public  void frame() {
		JFrame frame = new JFrame();
		//frame.setSize(1240, 685);
		frame.setSize(1240, 709);
		frame.setIconImage(new ImageIcon("icon.jpg").getImage());
		
		panelOffsetX = frame.getWidth() - 264;
		panelOffsetY = 0;
		frame.add(creatMenu(), BorderLayout.PAGE_START);
		MazePanel panel = new MazePanel();
		frame.add(panel);
		frame.setTitle("๖ۣۜლℳα☡ℯლ");//
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}
