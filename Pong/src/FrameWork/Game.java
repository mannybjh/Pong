/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FrameWork;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

/**
 *
 * @author Jeff
 */
public class Game extends Canvas implements Runnable{
    
    public static boolean running = false;
    public static Thread thread;
    public static JFrame f;
    public static int paddle1X = 50, paddle1Y = 25, paddle1Vel = 0, paddle1Score = 0;
    public static int paddle2X = 925, paddle2Y = 450, paddle2Vel = 0, paddle2Score = 0;
    public static Ball ball;
    
    public Game(){
    }
    
    private void init(){
        ball = new Ball(495,295);
    }
    
    public static void main(String[] args) {
        newWindow(new Game());
    }

    public synchronized void start(){
        if(running)
            return;
        
        running = true;
        thread = new Thread(this);
        thread.start();
    }
    
    @Override
    public void run(){
        init();
        this.requestFocus();
        this.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                
                switch(key){
                    case KeyEvent.VK_W:
                        if(ball.moving && running)
                            paddle1Vel = -5;
                        break;
                        
                    case KeyEvent.VK_S:
                        if(ball.moving && running)
                            paddle1Vel = 5;
                        break;
                        
                    case KeyEvent.VK_UP:
                        if(ball.moving && running)
                            paddle2Vel = -5;
                        break;
                        
                    case KeyEvent.VK_DOWN:
                        if(ball.moving && running)
                            paddle2Vel = 5;
                        break;
                        
                    case KeyEvent.VK_SPACE:
                        ball.moving = true;
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                }
                
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                
                switch(key){
                    case KeyEvent.VK_W:
                        paddle1Vel = 0;
                        break;
                    case KeyEvent.VK_S:
                        paddle1Vel = 0;
                        break;
                    case KeyEvent.VK_UP:
                        paddle2Vel = 0;
                        break;
                    case KeyEvent.VK_DOWN:
                        paddle2Vel = 0;
                        break;
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        int i = 0;
        while(running){
            
            long now = System.nanoTime();
            delta += (now-lastTime)/ ns;
            lastTime = now;
            while(delta >= 1){
                tick();
                updates++;
                delta--;
            }
            render();
            frames++;
        
            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println("FPS: " + frames + " TICKS: " + updates + " I: " + i);
                i++;
                if(i % 5 == 0) ball.increaseVelX(1);
                frames = 0;
                updates = 0;
            }
            if(paddle1Score == 5 || paddle2Score == 5) running = false;
        }
    }
    
    private void tick(){
        paddle1Tick();
        paddle2Tick();
        ball.tick();
    }
    
    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }
        
        Graphics g = bs.getDrawGraphics();
        ///////////////////////////
        //Draw Here
        
        g.setColor(Color.black);
        g.fillRect(0, 0, 1000, 600);
        
        g.setColor(Color.WHITE);
        
        int startingY = 0;
        
        while(startingY < 600){
            g.fillRect(495, startingY, 10, 20);
            
            startingY += 40;
        }
        
        
        
        g.setFont(new Font("TimesRoman", Font.PLAIN, 90));
        g.drawString(paddle1Score + "", 400, 100);
        g.drawString(paddle2Score + "", 600, 100);
        
        paddle1Render(g);
        paddle2Render(g);
        ball.render(g);
        
        //////////////////////////
        
        
        g.dispose();
        bs.show();
        
    }   
    
    private void paddle1Render(Graphics g){
        g.setColor(Color.white);
        g.fillRect(paddle1X, paddle1Y, 25, 100);
    }
    
    private void paddle2Render(Graphics g){
        g.setColor(Color.white);
        g.fillRect(paddle2X, paddle2Y, 25, 100);
    }
    
    private void paddle1Tick(){
        if(paddle1Y + paddle1Vel != 0 && paddle1Y + paddle1Vel != 470)
            paddle1Y += paddle1Vel;
        
        if(new Rectangle(paddle1X,paddle1Y,25,100).intersects(new Rectangle(ball.getX(), ball.getY(),20,20)))
            ball.reverse();
    }
    
    private void paddle2Tick(){
        if(paddle2Y + paddle2Vel != 0 && paddle2Y + paddle2Vel != 470)
            paddle2Y += paddle2Vel;
        
        if(new Rectangle(paddle2X,paddle2Y,25,100).intersects(new Rectangle(ball.getX(), ball.getY(),20,20)))
            ball.reverse();
    }
    
    public static void newRound(int velX){
        ball = new Ball(495,295);
        if(velX < 0){
            ball.setVelX(7);
            ball.setVelY(-ball.getVelY());
            
        }else
            ball.setVelX(-7);
        paddle1X = 50;
        paddle1Y = 25;
        paddle1Vel = 0;
        paddle2X = 925;
        paddle2Y = 450;
        paddle2Vel = 0;
    }
    
    public static void newWindow(Game game){
        f = new JFrame();
        f.setSize(1000,600);
        f.add(game);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setTitle("Pong - By Ben Hogan");
        
        f.setVisible(true);
        
        game.start();
    }
}
