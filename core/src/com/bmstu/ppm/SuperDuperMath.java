package com.bmstu.ppm;

import com.bmstu.ppm.gameobjects.GameObject;
import com.bmstu.ppm.gameobjects.Player;

public class SuperDuperMath {
    public static class Point {
        double x;
        double y;
        public Point(double x, double y){
            this.x = x;
            this.y = y;
        }
    }
    public static Point lineIntersect(Point p1, Point p2, Point p3, Point p4){
        double denominator = (p4.y - p3.y) * (p2.x - p1.x) - (p4.x - p3.x) * (p2.y - p1.y);
        // Если линии параллельны
        if (denominator == 0.0)
            return null;
        double ua = ((p4.x - p3.x) * (p1.y - p3.y) - (p4.y - p3.y) * (p1.x - p3.x)) / denominator;
        double ub = ((p2.x - p1.x) * (p1.y - p3.y) - (p2.y - p1.y) * (p1.x - p3.x)) / denominator;
        if (ua >= 0.0 && ua <= 1.0 && ub >= 0.0 && ub <= 1.0) {
            // Найдена точка пересечения
            return new Point(p1.x + ua * (p2.x - p1.x), p1.y + ua * (p2.y - p1.y));
        }
        return null;
    }
    public static Point lineIntersect(Player player, GameObject object, double angle){
        return lineIntersect(
                new Point(player.getPosX(), player.getPosY()),
                new Point(player.getPosX() + 10000 * Math.cos(angle), player.getPosY() + 10000 * Math.sin(angle)),
                new Point(object.getPosX1(), object.getPosY1()),
                new Point(object.getPosX2(), object.getPosY2())
        );
    }
    public static double dist(Point p1, Point p2){
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
    public static double dist(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
    public static double dist(Player player, Point p2){
        return Math.sqrt(Math.pow(player.getPosX() - p2.x, 2) + Math.pow(player.getPosY()- p2.y, 2));
    }
}


