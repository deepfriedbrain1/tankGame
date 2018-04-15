package src;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static src.TankGame.isGameOver;
import static src.TankGame.missiles;
import static src.TankGame.player1;
import static src.TankGame.player2;
import static src.TankGame.reLoadElements;

public final class Controller extends KeyAdapter {

    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private boolean w;
    private boolean s;
    private boolean a;
    private boolean d;

    private void setMyTankDirection() {
        // Controls for player 1
        if (up && !down && !left && !right) {
            player2.setDirection(Direction.UP);
            player2.setMoving(true);
        } else if (!up && down && !left && !right) {
            player2.setDirection(Direction.DOWN);
            player2.setMoving(true);
        } else if (!up && !down && left && !right) {
            player2.setDirection(Direction.LEFT);
            player2.setMoving(true);
        } else if (!up && !down && !left && right) {
            player2.setDirection(Direction.RIGHT);
            player2.setMoving(true);
        } else if (up && !down && left && !right) {
            player2.setDirection(Direction.LEFT_UP);
            player2.setMoving(true);
        } else if (!up && down && left && !right) {
            player2.setDirection(Direction.LEFT_DOWN);
            player2.setMoving(true);
        } else if (up && !down && !left && right) {
            player2.setDirection(Direction.RIGHT_UP);
            player2.setMoving(true);
        } else if (!up && down && !left && right) {
            player2.setDirection(Direction.RIGHT_DOWN);
            player2.setMoving(true);
        } else {
            player2.setMoving(false);
        }

        //Controls for player 2
        if (w && !d && !a && !s) {
            player1.setDirection(Direction.UP);
            player1.setMoving(true);
        } else if (!w && s && !a && !d) {
            player1.setDirection(Direction.DOWN);
            player1.setMoving(true);
        } else if (!w && !d && a && !s) {
            player1.setDirection(Direction.LEFT);
            player1.setMoving(true);
        } else if (!w && !s && !a && d) {
            player1.setDirection(Direction.RIGHT);
            player1.setMoving(true);
        } else if (w && !d && a && !s) {
            player1.setDirection(Direction.LEFT_UP);
            player1.setMoving(true);
        } else if (!w && !d && a && s) {
            player1.setDirection(Direction.LEFT_DOWN);
            player1.setMoving(true);
        } else if (w && !s && !a && d) {
            player1.setDirection(Direction.RIGHT_UP);
            player1.setMoving(true);
        } else if (!w && d && !a && s) {
            player1.setDirection(Direction.RIGHT_DOWN);
            player1.setMoving(true);
        } else {
            player1.setMoving(false);
        }
    }//end setMyTankDirection

    @Override
    public synchronized void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_Q:
                if (isGameOver()) {
                    System.exit(0);
                }
                break;
            case KeyEvent.VK_SPACE:
                if (player2.Live()) {
                    missiles.add(player2.fireMissile());
                }
                break;
            case KeyEvent.VK_ENTER: //power up for player2
                if (player2.Live() && player2.isMultiFireOn()) {
                    missiles.addAll(player2.fireMissiles());
                    player2.reducePowerUp();
                }
                if (player2.Live() && player2.hasShield()) {
                    player2.useShield();
                }
                if (isGameOver()) {
                    player2.resetPowerUps();
                    player2.setPlayer2score(0);
                    player2.setLive(true);
                    player2.disableMultiFireActivation();
                    player2.setHealth();
                    player2.setNumberOfLives(3);
                    player2.setLocation(40, 40);

                    player1.resetPowerUps();
                    player1.setPlayer1score(0);
                    player1.setLive(true);
                    player1.disableMultiFireActivation();
                    player1.setHealth();
                    player1.setNumberOfLives(3);
                    player1.setLocation(1070, 1070);

                    reLoadElements();
                }
                break;
            case KeyEvent.VK_F:
                if (player1.Live()) {
                    missiles.add(player1.fireMissile());
                }
                break;
            case KeyEvent.VK_G: //power up for player1
                if (player1.Live() && player1.isMultiFireOn()) {
                    missiles.addAll(player1.fireMissiles());
                    player1.reducePowerUp();
                }
                if (player1.Live() && player1.hasShield()) {
                    player1.useShield();
                }
                break;
            case KeyEvent.VK_1:
                player1.setLive(true);
                break;
            case KeyEvent.VK_2:
                player1.setLive(true);
                break;
            case KeyEvent.VK_UP:
                up = true;
                break;
            case KeyEvent.VK_DOWN:
                down = true;
                break;
            case KeyEvent.VK_LEFT:
                left = true;
                break;
            case KeyEvent.VK_RIGHT:
                right = true;
                break;
            case KeyEvent.VK_W:
                w = true;
                break;
            case KeyEvent.VK_D:
                d = true;
                break;
            case KeyEvent.VK_A:
                a = true;
                break;
            case KeyEvent.VK_S:
                s = true;
                break;
        }//end switch
        setMyTankDirection();
    }//end keyPressed

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                up = false;
                break;
            case KeyEvent.VK_DOWN:
                down = false;
                break;
            case KeyEvent.VK_LEFT:
                left = false;
                break;
            case KeyEvent.VK_RIGHT:
                right = false;
                break;
            case KeyEvent.VK_W:
                w = false;
                break;
            case KeyEvent.VK_D:
                d = false;
                break;
            case KeyEvent.VK_A:
                a = false;
                break;
            case KeyEvent.VK_S:
                s = false;
                break;
        }//end switch
        setMyTankDirection();
    }//end keyReleased   

}//end Controller
