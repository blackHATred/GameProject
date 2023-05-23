package com.bmstu.ppm.gameobjects;

import com.bmstu.ppm.SuperDuperMath;

import java.util.ArrayList;

public class Player {
    private float hp = 100;
    private float posX;
    private float posY;
    private float rotate = 0;
    private final float VFOV_const = 30;
    private final float walkSpeed = 100f;
    private final float runSpeed = 200f;
    private float speed = walkSpeed;

    public Player(float posX, float posY){
        this.posX = posX;
        this.posY = posY;
    }
    public void rotate(float radians, float sensitivity){
        rotate = (rotate + radians * sensitivity) % (2 * (float) Math.PI);
    }
    public void setRunSpeed(){
        speed = runSpeed;
    }
    public void setWalkSpeed(){
        speed = walkSpeed;
    }
    public float getPosX() {
        return posX;
    }
    public float getPosY() {
        return posY;
    }
    public float getVFOV_const() {
        return VFOV_const;
    }
    public float getRotate(){
        return rotate;
    }
    public void move(float x, float y, ArrayList<GameObject> objects){
        for (GameObject gameObject: objects) {
            if (!gameObject.isCollidable()) continue;
            switch (gameObject.getCollidableType()){
                case LINE -> {
                    if (SuperDuperMath.lineIntersect(
                            this.posX, this.posY,
                            this.posX + x * speed * 1.5f, this.posY + y * speed * 1.5f,
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
