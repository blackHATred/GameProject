package com.bmstu.ppm.gameobjects;

abstract public class GameObject {
    private boolean collidable;
    private boolean renderable;
    private float posX1;
    private float posY1;
    private float posX2;
    private float posY2;
    private float posX;
    private float posY;
    private float radius;
    private CollidableType collidableType;

    public boolean isCollidable() {
        return collidable;
    }

    public CollidableType getCollidableType() {
        return collidableType;
    }

    public boolean isRenderable() {
        return renderable;
    }

    public enum CollidableType {
        RADIUS,
        LINE
    }
    GameObject(boolean collidable, boolean renderable, float posX, float posY, float radius){
        this.collidable = collidable;
        this.renderable = renderable;
        this.posX = posX;
        this.posY = posY;
        this.radius = radius;
        this.collidableType = CollidableType.RADIUS;
    }
    GameObject(boolean collidable, boolean renderable, float posX1, float posY1, float posX2, float posY2){
        this.collidable = collidable;
        this.renderable = renderable;
        this.posX1 = posX1;
        this.posY1 = posY1;
        this.posX2 = posX2;
        this.posY2 = posY2;
        this.collidableType = CollidableType.LINE;
    }

    public float getPosX1() {
        return posX1;
    }
    public float getPosY1() {
        return posY1;
    }
    public float getPosX2() {
        return posX2;
    }
    public float getPosY2() {
        return posY2;
    }
    public float getPosX() {
        return posX;
    }
    public float getPosY() {
        return posY;
    }
    public float getRadius() {
        return radius;
    }
}
