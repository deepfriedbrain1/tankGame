package src;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;


public final class TankGame extends JPanel implements Runnable  {
    private Thread thread;
    protected static final int SPEED = 6;
    private BufferedImage background, player1screen, player2screen, img, title;
    private LinkedList<Wall> walls;
    protected static LinkedList<Missile> missiles;
    private LinkedList<Blast> blasts;
    private GSound backgroundS;
    protected static GameAction player1, player2;
    private BufferedReader map;
    private int w, h;
    private int sizeX, sizeY;
    private static LinkedList<BreakableWall> bWall;
    private PowerUp powerup;
    private static LinkedList<PowerUp> powerUpList;
    private static  LinkedList<BreakableWall> backup_bWall;
    private static LinkedList<PowerUp> backup_powerUpList;
    private int player1score = 0, player2score = 0;
    private int counter = 90;
    private static boolean gameOver = false;
   
    public TankGame() throws IOException{
        this.setFocusable(true);
        String line;  
        this.sizeX = 0;
        this.sizeY = 0;
        
        try{
            background = ImageIO.read(TankGame.class.getResource("resources/Background.bmp"));
            title = ImageIO.read(TankGame.class.getResource("resources/Title.gif"));
        }catch(IOException ioe){
            System.out.println("*** IOException: " + ioe.getMessage());
        }//end try-catch
        
        // Set background sound 
        backgroundS = new GSound(1, "resources/castle.mid");
        
        // Set default background color
        setBackground(Color.WHITE);
  
        // Create missiles.
	missiles = new LinkedList<Missile>();
        
        // Create walls.
        walls = new LinkedList<Wall>();
        bWall = new LinkedList<BreakableWall>();
        
        // Create backup breakable walls for repaint.
        backup_bWall = new LinkedList<BreakableWall>();
        
        // Create blasts.
        blasts = new LinkedList<Blast>();
		
        // Create PowerUps
        powerUpList = new LinkedList<PowerUp>();
        
        // Create backup powerups for repaint
        backup_powerUpList = new LinkedList<PowerUp>();
        
        // Set motion controls
	addKeyListener(new Controller());

        // Build game level, constructor throws IOException
        map = new BufferedReader(new InputStreamReader(TankGame.class.getResource("resources/level.2.txt").openStream()));
      
	try {
            line = map.readLine();
            w = line.length();
            h = 0;
                while(line != null){
                    for(int i = 0, n = line.length() ; i < n ; i++) { 
                        char c = line.charAt(i); 
				    
                        if(c == '1'){
                            Wall w1 = new Wall(32*i,32*h,32,32,0);
                            walls.add(w1);
                        }
				    
                        if(c == '2'){
                            BreakableWall bw = new BreakableWall(32*i,32*h,32,32,0);
                            bWall.add(bw);
                            backup_bWall.add(bw);   
                        }
            
                        if(c == '3'){
                            player1= new GameAction(this, i*32, h*32,SPEED, Direction.RIGHT);
                        }
            
                        if(c == '4'){
                            player2= new GameAction(this, i*32, h*32,SPEED, Direction.RIGHT);
                        }
                        
                        if(c == '5'){
                            powerup = new PowerUp(32*i, 32*h, 32, 32, 0);
                            powerUpList.add(powerup);
                            backup_powerUpList.add(powerup);
                        }
                    }//end for
                    h++;
                    line = map.readLine();
                }//end while
        map.close();
        }catch(IOException ioe){
            System.out.println("*****IOException : TankGame loadMap: " + ioe.getMessage());
        }//end try-catch
    }//end constructor
   
    public LinkedList<Wall> getWalls() {
	      return walls;
    }//end getWalls
    
    public LinkedList<BreakableWall> getBWalls() {
	      return bWall;
    }//end getWalls
    
    public LinkedList<PowerUp> getPowerUps(){
        return powerUpList;
    }//end getPowerUps    
     
    public void setDimensions(int w, int h){
    	this.sizeX = w;
    	this.sizeY = h;
    }//end setDimensions
    
    public int getPlayer1score(){
        return player1score;
    }//end getPlayer1score
    
    public int getPlayer2score(){
        return player2score;
    }//end getPlayer2score
    
    public void setPlayer1score(int points){
        player1score += points;
    }//end setPlayer1score
    
    public void setPlayer2score(int points){
        player2score += points;
    }//end setPlayer2score
    
    public void zeroPlayerScores(){
        player1score = 0;
        player2score = 0;
    }//end zeroPlayerScores
    
    // Reloads the backup powerups and backup breakable wall into originals LinkedList
    public static void reLoadElements(){
        bWall.addAll(backup_bWall);
        powerUpList.addAll(backup_powerUpList);
        gameOver = false;
    }//end reLoadElements 
    
    public static boolean isGameOver(){
        return gameOver;
    }//end isGameOver
    
public void drawBackground(Graphics2D g2d){
      
    double backdirection = 0.;
    int backspeed = 0;
    int movex = 0, movey = 0;
    h = 1150;
    w = 1184;
    int TileWidth = background.getWidth(this);
    int TileHeight = background.getHeight(this);
    int NumberX = (int) (w / TileWidth);
    int NumberY = (int) (h / TileHeight);

    movex += (int) (backspeed * Math.sin(backdirection));
    movey += (int) (backspeed * Math.cos(backdirection));

    for (int i = -1; i <= NumberY + 1; i++){
        for (int j = -1; j <= NumberX + 1; j++){
            g2d.drawImage(background, j * TileWidth + (movex % TileWidth),
                i * TileHeight + (movey % TileHeight), TileWidth,
                    TileHeight, this);
        }
    }
}//end drawBackgrond
    
public Graphics2D createGraphics2D(int w, int h) {
    Graphics2D g2 = null;
    if (img == null || img.getWidth() != w || img.getHeight() != h) {
        img = (BufferedImage) createImage(w, h);
    }
    g2 = img.createGraphics();
    g2.setBackground(getBackground());
    g2.setRenderingHint(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);
    g2.clearRect(0, 0, w, h);   
        return g2;
}//end createGraphics2D
    
    
@Override
public void paint(Graphics g) {  
    Dimension screenSize = getSize();
    Graphics2D g2 = createGraphics2D(1184, 1150);
    drawBackground(g2);

    if (player1.Live())
        player1.move(g2);
    if (player2.Live())
	player2.move(g2);
        
    //if player2 is not alive but still has lives
    if((!player2.Live()) && (player2.hasLives())){
        int rX = 0, rY = 0;
        if(player2.getNumberOfLives() == 2){
            rX = 520;
            rY = 500;
        }else if(player2.getNumberOfLives() == 1){
            rX = 320;
            rY = 950;
        }
            
        player2.setLive(true);
        player2.setHealth();
        player2.setLocation(rX, rY);
        player2.move(g2);
    }//end if 
        
    //if player1 is not alive, but still has lives
    if((!player1.Live()) && (player1.hasLives())){
        int rX = 0, rY = 0;
        if(player1.getNumberOfLives() == 2){
            rX = 480;
            rY = 110;
        }else if(player1.getNumberOfLives() == 1){
            rX = 950;
            rY = 875;
        }
            
        player1.setLive(true);
        player1.setHealth();
        player1.setLocation(rX, rY);
        player1.move(g2);
    }
		 
    // Paint walls
    Iterator<Wall> itrWall = walls.iterator();	   
    while (itrWall.hasNext()) {
        Wall wall = itrWall.next();
        wall.draw(g2);
    }//end while
        
    //paint breakeable wall
    Iterator<BreakableWall> itrWalls = bWall.iterator();	   
    while (itrWalls.hasNext()) {
        BreakableWall wall = itrWalls.next();
        wall.draw(g2);
    }//end while
        
    // Paint PowerUps
    Iterator<PowerUp> itrPowerUp = powerUpList.iterator();
    while(itrPowerUp.hasNext()){
        PowerUp powerUp = itrPowerUp.next();
        powerUp.draw(g2);
    }//end while
        
    // Paint missiles.
    Iterator<Missile> itrMissile = missiles.iterator();
    while (itrMissile.hasNext()) {
	Missile missile = itrMissile.next();
	missile.paint(g2);
        if(missile.hitObjects()) {
            if(!player1.Live() || !player2.Live()){
                blasts.add(missile.produceBigBlast());
                itrMissile.remove();
            }else{        
                blasts.add(missile.produceBlast());
                itrMissile.remove();
            }//end if-else
	}//end if
    }//end while
        
    // Paint blasts.
    Iterator<Blast> itrBlast = blasts.iterator();
	while (itrBlast.hasNext()) {
            Blast blast = itrBlast.next();
            blast.paint(g2);
                if(blast.blastOver())
                    itrBlast.remove();
	}//end while  

     //create a subimage of the map for each player to make a divided view and mini map  
    int player1x = player1.getX() - screenSize.width/4 > 0 ? player1.getX() - screenSize.width/4 : 0;
    int player1y = player1.getY() - screenSize.height/2 > 0 ? player1.getY() - screenSize.height/2 : 0;
    if(player1x > 1184-screenSize.width/2){
       	player1x = 1184-screenSize.width/2;
    }
    if(player1y > 1150-screenSize.height){
      	player1y = 1150-screenSize.height;
    }   
        
    int player2x = player2.getX() - screenSize.width/4 > 0 ? player2.getX() - screenSize.width/4 : 0;
    int player2y = player2.getY() - screenSize.height/2 > 0 ? player2.getY() - screenSize.height/2 : 0;
        
    if(player2x > 1184-screenSize.width/2){
      	player2x = 1184-screenSize.width/2;
    }
    if(player2y > 1150-screenSize.height){
       	player2y = 1150-screenSize.height;
    }
        
    player1screen = img.getSubimage(player1x, player1y, screenSize.width/2, screenSize.height);
    player2screen = img.getSubimage(player2x, player2y, screenSize.width/2, screenSize.height);
    g.drawImage(player1screen, 0, 0, null);
    g.drawImage(player2screen, screenSize.width/2, 0, null);
    g.drawRect(screenSize.width/2-1, 0, 1, screenSize.height);
    g.drawRect(screenSize.width/2-76, 499, 151, 151);
    g.drawImage(img, screenSize.width/2-75, 500, 150, 150, null);
     
    // Draw actived shield in corners of player screens
    if(player1.hasShield() && !player1.wasShieldUsed() && player1.shieldHealth() > 90){
        g.drawImage(Sprite.loadSprite("Shield1"), 0, 0, this);
    }else if(player1.hasShield() && player1.shieldHealth() <= 90 && player1.shieldHealth() > 0){
        g.drawImage(Sprite.loadSprite("Shield2"), 0, 0, this);
    }
        
    if(player2.hasShield() && !player2.wasShieldUsed() && player2.shieldHealth() > 90){
        g.drawImage(Sprite.loadSprite("Shield1"), 650, 0, this);
    }else if(player2.shieldHealth() <= 90 && player2.shieldHealth() > 0){
        g.drawImage(Sprite.loadSprite("Shield2"), 650, 0, this);
    }
       
    //draw the health bar for player 1
    g.setColor(Color.WHITE);
    g.fillRect(19, 629, 112, 22);
    
    g.setFont(new Font("Calibri", Font.PLAIN, 24));
    if(player1.getHealth()>40){
      	g.setColor(Color.GREEN);
        g.drawString("HEALTH", 20, 628);
    }else if(player1.getHealth()>20){
      	g.setColor(Color.YELLOW);
        g.drawString("HEALTH", 20, 628);
    } else{
        g.setColor(Color.RED);
        g.drawString("HEALTH", 20, 628);
    }
    //move the bar to the bottom of the game 
    g.fillRect(20, 630, (int) Math.round(player1.getHealth()*1.1), 20);
        
    //health bar player 2
    g.setColor(Color.WHITE);
    g.fillRect(599, 629, 112, 22);  
    
    g.setFont(new Font("Calibri", Font.PLAIN, 24));
    if(player2.getHealth()>40){
      	g.setColor(Color.GREEN);
        g.drawString("HEALTH", 600, 628);
    }else if(player2.getHealth()>20){
        g.setColor(Color.YELLOW);
        g.drawString("HEALTH", 600, 628);
    }else if(player2.getHealth()<20){
        g.setColor(Color.RED);
        g.drawString("HEALTH", 600, 628);
    }else if(player2.getHealth()==0){
        player2.setLive(false);
    }
    //move the bar to the bottom of the game 
    g.fillRect(600, 630, (int) Math.round(player2.getHealth()*1.1), 20);
        
    // Draw icon of PowerUp next to Health and Lives
    if (player1.isMultiFireOn() && player1.drawMissile()) {
        g.drawImage(Sprite.loadSprite("Rocket"), 220, 625, 25, 25, null);
    }
    if(player1.hasShield() && player1.wasShieldUsed())
        g.drawImage(Sprite.loadSprite("Shield1"), 220, 625, 25, 25, null);
    if(player2.isMultiFireOn() && player2.drawMissile())
        g.drawImage(Sprite.loadSprite("Rocket"), 485, 625, 25, 25, null);
    if(player2.hasShield() && player2.wasShieldUsed())
        g.drawImage(Sprite.loadSprite("Shield1"), 485, 625, 25, 25, null);
    
    // Draw score for player 1 and player 2
    g.setFont(new Font("Calibri", Font.PLAIN, 20)); 
    g.setColor(Color.GREEN);
    g.drawString("SCORE " + getPlayer1score(), 260, 20);
    g.drawString("SCORE " + getPlayer2score(), 380, 20);
    
     
    //create circle for lives count
    g.setFont(new Font("Calibri", Font.PLAIN, 24));
    g.setColor(Color.WHITE);
    g.fillOval(149, 629, 22, 22);
    g.fillOval(169, 629, 22, 22);
    g.fillOval(189, 629, 22, 22);
    
    g.fillOval(519, 629, 22, 22);
    g.fillOval(539, 629, 22, 22);
    g.fillOval(559, 629, 22, 22);
    
    switch (player1.getNumberOfLives()) {
        case 3:
            g.setColor(Color.BLUE);
            g.drawString("LIVES", 150, 628);
            g.fillOval(150,630,20,20);
            g.setColor(Color.BLUE);
            g.fillOval(170,630,20,20);
            g.setColor(Color.BLUE);
            g.fillOval(190,630,20,20);    
                break;
        case 2:
            g.setColor(Color.BLUE);
            g.drawString("LIVES", 150, 628);
            g.fillOval(150,630,20,20);
            g.setColor(Color.BLUE);
            g.fillOval(170,630,20,20);
                break;
        case 1:
            g.setColor(Color.BLUE);
            g.drawString("LIVES", 150, 628);
            g.fillOval(150,630,20,20);
                break;
        default:
            break;
    }
    g.setFont(new Font("Calibri", Font.PLAIN, 24));
    switch (player2.getNumberOfLives()) {
        case 3:
            g.setColor(Color.BLUE);
            g.drawString("LIVES", 520, 628);
            g.fillOval(520,630,20,20);
            g.setColor(Color.BLUE);
            g.fillOval(540,630,20,20);
            g.setColor(Color.BLUE);
            g.fillOval(560,630,20,20);
                break;
        case 2:
            g.setColor(Color.BLUE);
            g.drawString("LIVES", 520, 628);
            g.setColor(Color.BLUE);
            g.fillOval(540,630,20,20);
            g.setColor(Color.BLUE);
            g.fillOval(560,630,20,20);
                break;
        case 1:
            g.setColor(Color.BLUE);
            g.drawString("LIVES", 520, 628);
            g.setColor(Color.BLUE);
            g.fillOval(560,630,20,20);
                break;
        default:
                break;
    }
    
    // title display and timer
    if(counter > 0){
        g.drawImage(title, 125, 200, null);
        counter--;
    }
    
    // gameover display
    gameOver = (!player1.Live() && !player1.hasLives()) || (!player2.Live() && !player2.hasLives());
    if(gameOver){
        player1.setEnableMultiFire(false);
        player2.setEnableMultiFire(false);
        g.setColor(Color.BLUE);
        g.fillRect(250, 280, 240, 65);
        g.setColor(Color.WHITE);
        g.drawString("GAME OVER", 300, 300);
        g.drawString("Press Enter to Play Again", 250, 320);
        g.drawString("Press Q to Quit", 275, 340);
        g.setColor(Color.GREEN);
        if(player1score > player2score){
            g.drawString("PLAYER 1 WINS!", 150, 200);
        }else if(player1score < player2score){
            g.drawString("PLAYER 2 WINS!", 500, 200);
        }else if(player1score == player2score){
            g.drawString("PLAYERS TIE!", 300, 200);
        }
    }
    g.dispose();
                
}//end paint
        
    public GameAction getPlayerTank() {
        return player1;
    }//end getPlayerTank
    
    public GameAction getPlayerTank2() {
	return player2;
    }//end getPlayerTank2
   
    @Override
    public void run()
    {
        Thread me = Thread.currentThread();
        setFocusable(true);
        while (thread == me){
            repaint();
            try {
                Thread.sleep(30);
            }catch(InterruptedException ie){
                break;
            }
        }
    }//end run
    
    public void start(){
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }//end start
        
    public static void main(String[] args){
        
        try{
            TankGame tankGame = new TankGame();
            
            JFrame f = new JFrame("Tank Wars");
            f.addWindowListener(new WindowAdapter() {
        
            @Override
            public void windowGainedFocus(WindowEvent e) {
		tankGame.requestFocusInWindow();
            }//end windowGainedFocus
            
	    });//end WindowAdapter

	    f.getContentPane().add("Center", tankGame);
	    f.pack();
	    f.setSize(new Dimension(740, 700));
	    tankGame.start();
	    f.setVisible(true);
            f.setLocation(300,10);
	    f.setResizable(false);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
        }catch(IOException ioe){
              Logger.getLogger(TankGame.class.getName()).log(Level.SEVERE, null, ioe);  
        }//end try-catch
        
    }//end main */
}//end TankGame