package com.bmstu.ppm;

import com.bmstu.ppm.gameobjects.GameObject;
import com.bmstu.ppm.gameobjects.Player;

public class SuperDuperMath {
    public static final float rad = 0.01745f;
    public static class Point {
        float x;
        float y;
        public Point(float x, float y){
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }
        public float getY() {
            return y;
        }
    }
    public static Point lineIntersect(float x1, float y1, float x2, float y2,
                                      float x3, float y3, float x4, float y4) {
        float denominator = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        // Если линии параллельны
        if (denominator == 0.0)
            return null;
        float ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denominator;
        float ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denominator;
        if (ua >= 0.0 && ua <= 1.0 && ub >= 0.0 && ub <= 1.0) {
            // Найдена точка пересечения
            return new Point(x1 + ua * (x2 - x1), y1 + ua * (y2 - y1));
        }
        return null;
    }

    public static Point lineIntersect(Player player, GameObject object, float angle){
        return lineIntersect(
                player.getPosX(), player.getPosY(),
                player.getPosX() + 10000 * (float) Math.cos(angle), player.getPosY() + 10000 * (float) Math.sin(angle),
                object.getPosX1(), object.getPosY1(),
                object.getPosX2(), object.getPosY2()
        );
    }
    public static float dist(Point p1, Point p2){
        return (float) Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
    public static float dist(float x1, float y1, float x2, float y2){
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
    public static float dist(Player player, Point p2){
        return (float) Math.sqrt(Math.pow(player.getPosX() - p2.x, 2) + Math.pow(player.getPosY()- p2.y, 2));
    }
}


