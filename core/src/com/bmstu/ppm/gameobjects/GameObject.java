package com.bmstu.ppm.gameobjects;

abstract public class GameObject {
    private boolean collidable;
    private boolean renderable;
    private double posX1;
    private double posY1;
    private double posX2;
    private double posY2;
    private double posX;
    private double posY;
    private double radius;
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
    GameObject(boolean collidable, boolean renderable, double posX, double posY, double radius){
        this.collidable = collidable;
        this.renderable = renderable;
        this.posX = posX;
        this.posY = posY;
        this.radius = radius;
        this.collidableType = CollidableType.RADIUS;
    }
    GameObject(boolean collidable, boolean renderable, double posX1, double posY1, double posX2, double posY2){
        this.collidable = collidable;
        this.renderable = renderable;
        this.posX1 = posX1;
        this.posY1 = posY1;
        this.posX2 = posX2;
        this.posY2 = posY2;
        this.collidableType = CollidableType.LINE;
    }

    public double getPosX1() {
        return posX1;
    }
    public double getPosY1() {
        return posY1;
    }
    public double getPosX2() {
        return posX2;
    }
    public double getPosY2() {
        return posY2;
    }
    public double getPosX() {
        return posX;
    }
    public double getPosY() {
        return posY;
    }
    public double getRadius() {
        return radius;
    }
}
