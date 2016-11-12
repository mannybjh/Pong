/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FrameWork;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Jeff
 */
public class Ball {
    private int x, y, radius = 10, velX = -7, velY = -2;
    public boolean moving = false;
    
    public Ball(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public void tick(){
        if(moving){
            x += velX;
            if (x < 5){
                Game.paddle2Score++;
                Game.newRound(velX);
            }else if(x > 990){
                Game.paddle1Score++;
                Game.newRound(velX);
            }
            y += velY;
            if (y < 5){
                velY= -velY;
            }else if(y > 550) velY = -velY;
        }
    }
    
    public void render(Graphics g){
        g.setColor(Color.white);
        
        g.fillOval(x, y, radius*2, radius*2);
    }
    
    public void reverse(){
        velX = -velX;
    }
    
    public int getVelY(){
        return velY;
    }
    
    public void setVelY(int velY){
        this.velY = velY;
    }
    
    public int getX(){
      return x;
    }
    
    public int getY(){
        return y;
    }
    
    public void setVelX(int velX){
        this.velX = velX;
    }
    
    public void increaseVelX(int increaseVelX){
        if(velX > 0) velX += increaseVelX;
        else if(velX < 0) velX -= increaseVelX;
    }
}
