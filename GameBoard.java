

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;
/*
 * game logic class
 */
public class GameBoard {
	
	private boolean hasStarted;
	static final int ROWS = 4;
	static final int COLS= 4;
	private final int STARTING_TILES = 2;
	private Tile[][] board; 
	private boolean dead;
	private boolean win;
	private BufferedImage gameBoard; 
	private BufferedImage finalBoard;
	private int x,y;
	private int score = 0;
	private Font scoreFont;
	private int numMoves = 0;
	private int maxTile = 2;
	private static int spacing = 10;
	public static int boardWidth = (COLS+1) * spacing + COLS * Tile.WIDTH;
	public static int boardHeight = (ROWS+1) * spacing + ROWS * Tile.HEIGHT;
	
	public GameBoard(int x, int y) {
		scoreFont = Game.main.deriveFont(24f);
		this.x = x;
		this.y = y;
		board = new Tile[ROWS][COLS];
		gameBoard = new BufferedImage(boardWidth, boardHeight, BufferedImage.TYPE_INT_RGB);
		finalBoard = new BufferedImage(boardWidth, boardHeight, BufferedImage.TYPE_INT_RGB);
		createBoardImage();
		start();
	}
	/*
	 * draws board
	 */
	private void createBoardImage() {
		Graphics2D g = (Graphics2D) gameBoard.getGraphics();
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, boardWidth, boardHeight);
		g.setColor(Color.LIGHT_GRAY);
		for(int r = 0; r < ROWS; r++) {
			for(int c = 0; c < COLS; c++) {
				int x = spacing + spacing * c + Tile.WIDTH * c; 
				int y = spacing + spacing * r + Tile.HEIGHT * r;
				g.fillRoundRect(x, y, Tile.WIDTH, Tile.HEIGHT, Tile.ARC_WIDTH, Tile.ARC_HEIGHT);
				
			}
		}
	}
	/*
	 * draws tiles and board
	 */
	public void render(Graphics2D g) {
		Graphics2D g2d = (Graphics2D)finalBoard.getGraphics();
		g2d.drawImage(gameBoard, 0,0,null);
		
		for(int r = 0; r<ROWS; r++) {
			for(int c = 0; c<COLS; c++) {
				Tile current = board[r][c];
				if(current == null)
					continue;
				current.render(g2d);
			}
		}
		
		g.drawImage(finalBoard, x, y, null); //draw to screen 
		g2d.dispose();
		g.setColor(Color.LIGHT_GRAY);
		g.setFont(scoreFont);
		g.drawString("" + score, 30, 40);
	}
	/*
	 * updates the board state whenever thread is updated
	 */
	public void update() {
		checkDead();
		if(dead == true) {
			System.out.println("You lose, thanks for playing. Score: " + score);
			System.exit(0);
		}
		checkKeys();
		for(int r = 0; r<ROWS; r++) {
			for(int c = 0; c<COLS; c++) {
				Tile current = board[r][c];
				if(current == null)
					continue;
				current.update();
				resetPosition(current, r, c);
				if(current.getValue() == 2048)
					win = true;
				
			}
		}
		
	}
	/*
	 * resets board tile position 
	 */
	private void resetPosition(Tile current, int r, int c) {
		if(current == null)
			return;
		int x = getTileX(c);
		int y = getTileY(r);
		
		int distX = current.getX() - x;
		int distY = current.getY() - y;
		
		if(Math.abs(distX) < Tile.SLIDE_SPEED) {
			current.setX(current.getX() - distX);
		}
		if(Math.abs(distY) < Tile.SLIDE_SPEED) {
			current.setY(current.getY() - distY);
		}
		if(distX < 0) {
			current.setX(current.getX() + Tile.SLIDE_SPEED);
		}
		if(distY < 0) {
			current.setY(current.getY() + Tile.SLIDE_SPEED);
		}
		if(distX > 0) {
			current.setX(current.getX() - Tile.SLIDE_SPEED);
		}
		if(distY > 0) {
			current.setY(current.getY() - Tile.SLIDE_SPEED);
		}
	}
	/*
	 * this was my initial way to handle key inputs,
	 * i later handled several inputs in the Game class
	 * ideally they would all be handled in once place
	 */
	private void checkKeys() {
		if(Keyboard.typed(KeyEvent.VK_LEFT)) {
			//move left
			moveTiles(Direction.LEFT);
			if(!hasStarted)
				hasStarted = true;
		}
		if(Keyboard.typed(KeyEvent.VK_RIGHT)) {
			//move right
			moveTiles(Direction.RIGHT);
			if(!hasStarted)
				hasStarted = true;
		}
		if(Keyboard.typed(KeyEvent.VK_UP)) {
			//move up
			moveTiles(Direction.UP);
			if(!hasStarted)
				hasStarted = true;
		}
		if(Keyboard.typed(KeyEvent.VK_DOWN)) {
			//move down
			moveTiles(Direction.DOWN);
			if(!hasStarted)
				hasStarted = true;
		}
		
		
		
	}
	/*
	 * implements the game logic of moving and combining tiles 
	 * uses move method to actually move the tiles
	 */
	private void moveTiles(Direction dir) {
		boolean canMove = false;
		int horizontalDirection = 0;
		int verticalDirection = 0;
		if(dir == Direction.LEFT) {
			horizontalDirection = -1;
			for(int r = 0; r<ROWS; r++) {
				for(int c = 0; c<COLS; c++) {
					if(!canMove) {
						canMove = move(r,c,horizontalDirection, verticalDirection, dir);
					}
					else
						move(r,c,horizontalDirection, verticalDirection, dir);
					
				}
			}
		}
		else if(dir == Direction.RIGHT) {
			horizontalDirection = 1;
			for(int r = 0; r<ROWS; r++) {
				for(int c = COLS-1; c>=0; c--) {
					if(!canMove) {
						canMove = move(r,c,horizontalDirection, verticalDirection, dir);
					}
					else
						move(r,c,horizontalDirection, verticalDirection, dir);
				}
			}
		}
		else if(dir == Direction.UP) {
			verticalDirection = -1;
			for(int r = 0; r<ROWS; r++) {
				for(int c = 0; c<COLS; c++) {
					if(!canMove) {
						canMove = move(r,c,horizontalDirection, verticalDirection, dir);
					}
					else
						move(r,c,horizontalDirection, verticalDirection, dir);
				}
			}
		}
		else if(dir == Direction.DOWN) {
			verticalDirection = 1;
			for(int r = ROWS-1; r>=0; r--) {
				for(int c = 0; c<COLS; c++) {
					if(!canMove) {
						canMove = move(r,c,horizontalDirection, verticalDirection, dir);
					}
					else
						move(r,c,horizontalDirection, verticalDirection, dir);
				}
			}
		}
		else
			System.out.println(dir + " is not a valid direction");
		for(int r = 0; r<ROWS; r++) {
			for(int c = 0; c<COLS; c++) {
				Tile current = board[r][c];
				if(current == null) continue;
				if(current.getValue()>maxTile) {
					maxTile = current.getValue();
				}
			}
		}
		System.out.println("Max Tile: " + maxTile);
		System.out.println("Move: " + ++numMoves);
		for(int r = 0; r<ROWS; r++) {
			for(int c = 0; c<COLS; c++) {
				Tile current = board[r][c];
				if(current == null)
					continue;
				current.setCanCombine(true);
				
				
			}
		}
		if(canMove) {
			spawnRandom();
			checkDead();
		}
		
	}
	/*
	 * checks if you lose the game
	 */
	private void checkDead() {
		for(int r = 0; r<ROWS; r++) {
			for(int c = 0; c<COLS; c++) {
				if(board[r][c] == null)
					return;
				if(checkSurroundingTiles(r,c,board[r][c]))
					return;
			}
		}
		dead = true;
		//set score
	}
	/*
	 * checks to see if tile can combine
	 */
	private boolean checkSurroundingTiles(int r, int c, Tile tile) {
		if(r>0) {
			Tile check = board[r-1][c];
			if(check == null)
				return true;
			if(tile.getValue() == check.getValue())
				return true;
		}
			if(r<ROWS-1) {
				Tile check = board[r+1][c];
				if(check == null)
					return true;
				if(tile.getValue() == check.getValue())
					return true;
			}
			if(c>0) {
				Tile check = board[r][c-1];
				if(check == null)
					return true;
				if(tile.getValue() == check.getValue())
					return true;
			}
			if(c<COLS-1) {
				Tile check = board[r][c+1];
				if(check == null)
					return true;
				if(tile.getValue() == check.getValue())
					return true;
			}
			return false;
		
	}
	/*
	 * calculates how the tiles are to be moved and combined 
	 */
	private boolean move(int r, int c, int horizontalDirection, int verticalDirection, Direction dir) {
		boolean canMove = false;
		Tile current = board[r][c];
		if(current == null)
			return false;
		boolean move = true;
		int newCol = c;
		int newRow = r;
		while(move) {
			newCol += horizontalDirection; 
			newRow += verticalDirection;
			if(checkOutOfBounds(dir, newRow, newCol))
				break;
			if(board[newRow][newCol] == null) { 
				board[newRow][newCol] = current;
				board[newRow-verticalDirection][newCol-horizontalDirection] = null;
				board[newRow][newCol].setSlideTo(new Point(newRow,newCol));
				canMove = true;
			}
			else if(board[newRow][newCol].getValue()== current.getValue() && board[newRow][newCol].canCombine()) {
				board[newRow][newCol].setCanCombine(false);
				board[newRow][newCol].setValue(board[newRow][newCol].getValue()*2);
				canMove = true;
				board[newRow-verticalDirection][newCol-horizontalDirection] = null;
				board[newRow][newCol].setSlideTo(new Point(newRow,newCol));
				board[newRow][newCol].setCombinedAnimation(true); //if im gonna animate
				score+=board[newRow][newCol].getValue();//add score
			}
			else
				move = false;  //should never be reached
			
		}
		
		return canMove;
	}
	/*
	 * checks edge cases
	 */
	private boolean checkOutOfBounds(Direction dir, int row, int col) {
		if(dir == Direction.LEFT)
			return col<0;
		else if(dir == Direction.RIGHT)
			return col>COLS-1;
		else if (dir == Direction.UP)
			return row<0;
		else if (dir == Direction.DOWN)
			return row > ROWS-1;
		return false;
	}
	/*
	 * fills initial board
	 */
	private void start() {
		for(int i = 0; i<STARTING_TILES; i++) {
			spawnRandom();
		}
	}
	/* spawns 2 random tiles into board 
	 * 80% chance of 2, 20% of 4
	 */
	private void spawnRandom() {
		Random random = new Random();
		boolean notValid = true;
		while(notValid) {
			int location = random.nextInt(ROWS*COLS);
			int row = location/ROWS; //single dimension to double 
			int col = location%COLS; //remainder is the col 
			Tile current = board[row][col];
			if(current == null) {
				int value = random.nextInt(10)<8?2:4; //80% chance of a 2, 10% 4
				Tile tile = new Tile(value, getTileX(col), getTileY(row));
				board[row][col] = tile;
				notValid = false;
			}
		}
		
	}
	/*
	 * getters
	 */
	private int getTileY(int row) {
		return spacing + row * Tile.HEIGHT + row * spacing;
	}

	private int getTileX(int col) {
		return spacing + col * Tile.WIDTH + col * spacing;
	}
	

}

