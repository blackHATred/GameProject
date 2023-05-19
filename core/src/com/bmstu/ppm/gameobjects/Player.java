package com.bmstu.ppm.gameobjects;

import com.bmstu.ppm.SuperDuperMath;

import java.util.ArrayList;

public class Player {
    private float hp = 100;
    private double posX;
    private double posY;
    private double rotate = 0;
    private final double VFOV_const = 30;
    private final double rotateSpeed = 5d;
    private final double walkSpeed = 100d;
    private final double runSpeed = 200d;
    private double speed = walkSpeed;

    public Player(double posX, double posY){
        this.posX = posX;
        this.posY = posY;
    }
    public void rotate(double radians){
        rotate = (rotate + radians * rotateSpeed) % (2 * Math.PI);
    }
    public void setRunSpeed(){
        speed = runSpeed;
    }
    public void setWalkSpeed(){
        speed = walkSpeed;
    }
    public double getPosX() {
        return posX;
    }
    public double getPosY() {
        return posY;
    }
    public double getVFOV_const() {
        return VFOV_const;
    }
    public double getRotate(){
        return rotate;
    }
    public void move(double x, double y, ArrayList<GameObject> objects){
        for (GameObject gameObject: objects) {
            if (!gameObject.isCollidable()) continue;
            switch (gameObject.getCollidableType()){
                case LINE -> {
                    if (SuperDuperMath.lineIntersect(
                            this.posX, this.posY,
                            this.posX + x * speed * 1.5, this.posY + y * speed * 1.5,
                            gameObject.getPosX1(), gameObject.getPosY1(),
                            gameObject.getPosX2(), gameObject.getPosY2()
                    ) != null)
                        return;
                }
                case RADIUS -> {
                    // TODO: для избежания багов искать пересечение отрезка с окружностью
                    if (SuperDuperMath.dist(
                            new SuperDuperMath.Point(this.posX + x * speed, this.posY + y * speed),
                            new SuperDuperMath.Point(gameObject.getPosX(), gameObject.getPosY())) < gameObject.getRadius())
                        return;
                }
            }
        }
        posX += x * speed;
        posY += y * speed;
    }
}
