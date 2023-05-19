package com.bmstu.ppm.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bmstu.ppm.PPMGame;
import com.bmstu.ppm.SuperDuperMath;
import com.bmstu.ppm.gameobjects.GameObject;
import com.bmstu.ppm.gameobjects.Player;
import com.bmstu.ppm.gameobjects.Wall;

import java.util.ArrayList;

public class Level implements Screen {
    protected final PPMGame game;
    // protected final FitViewport viewport;
    protected final Stage stage;
    protected Player player;
    protected Texture skybox;
    protected SpriteBatch batch;
    protected Pixmap renderPixmap;
    protected Texture floor;
    protected float fogLevel = 200;
    protected final ArrayList<GameObject> objects = new ArrayList<>(10000);
    protected final int tileSize = 100;

    public Level(final PPMGame game, int[][] map, Texture skybox, Texture floor) {
        this.game = game;
        // viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                switch (map[i][j]){
                    case 1 -> {
                        // Спавним игрока
                        player = new Player(j*tileSize+tileSize/2d, i*tileSize+tileSize/2d);
                    }
                    case 2 -> {
                        // рисуем кирпичную стенку 1
                        objects.add(new Wall(i*tileSize, j*tileSize, i*tileSize, (j+1)*tileSize, "Textures/256x256/256_Brick 01 Mud.png"));
                        objects.add(new Wall(i*tileSize, j*tileSize, (i+1)*tileSize, j*tileSize, "Textures/256x256/256_Brick 01 Mud.png"));
                        objects.add(new Wall((i+1)*tileSize, (j+1)*tileSize, (i+1)*tileSize, j*tileSize, "Textures/256x256/256_Brick 01 Mud.png"));
                        objects.add(new Wall(i*tileSize, (j+1)*tileSize, (i+1)*tileSize, (j+1)*tileSize, "Textures/256x256/256_Brick 01 Mud.png"));
                    }
                }
            }
        }
        batch = new SpriteBatch();
        renderPixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        this.skybox = skybox;
        this.floor = floor;
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        System.out.println(dt);
        // Очищаем экран
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderPixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);

        // Обрабатываем ввод с клавиатуры
        if (Gdx.input.isKeyPressed(Input.Keys.W)) player.move(Math.cos(player.getRotate())*dt, Math.sin(player.getRotate())*dt, objects);
        if (Gdx.input.isKeyPressed(Input.Keys.A)) player.move(Math.sin(player.getRotate())*dt, -Math.cos(player.getRotate())*dt, objects);
        if (Gdx.input.isKeyPressed(Input.Keys.S)) player.move(-Math.cos(player.getRotate())*dt, -Math.sin(player.getRotate())*dt, objects);
        if (Gdx.input.isKeyPressed(Input.Keys.D)) player.move(-Math.sin(player.getRotate())*dt, Math.cos(player.getRotate())*dt, objects);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) player.rotate(-dt);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) player.rotate(dt);
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) player.setRunSpeed(); else player.setWalkSpeed();

        // TODO: сортировать объекты по мере отдалённости для оптимизации
        // Рисуем скайбокс уровня
        batch.begin();
        batch.draw(skybox, 0, (float) Gdx.graphics.getHeight() / 2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Рисуем землю

        // Рисуем стены и энтити
        int pixelCounter = -1; // Какой пиксель по горизонтали обрабатываем
        for (double angle = player.getRotate() - game.settings.FOV/2d * SuperDuperMath.rad;
             angle <= player.getRotate() + game.settings.FOV/2d * SuperDuperMath.rad;
             angle += (double) game.settings.FOV / Gdx.graphics.getWidth() * SuperDuperMath.rad * 8/game.settings.graphics
        ){
            pixelCounter+=8/game.settings.graphics;
            // Ищем ближайшую для рендера стену
            GameObject contextObj = null;
            SuperDuperMath.Point intersect;
            double dist = 1000000000;
            for (GameObject obj: objects){
                if (obj.getClass() != Wall.class || !obj.isRenderable()) continue;
                intersect = SuperDuperMath.lineIntersect(player, obj, angle);
                if (intersect != null && SuperDuperMath.dist(player, intersect) < dist){
                    dist = SuperDuperMath.dist(player, intersect);
                    contextObj = obj;
                }
            }
            // Устраняем артифакты изображения при необходимости
            if(!game.settings.fishEye)
                dist *= Math.cos(player.getRotate()-angle);
            // Рисуем ближайшую стену
            Wall wall = (Wall) contextObj;
            double h = player.getVFOV_const()*Gdx.graphics.getHeight()/dist;
            System.out.println(h);
            intersect = SuperDuperMath.lineIntersect(player, contextObj, angle);
            // СТАРЫЙ АЛГОРИТМ
            // Универсален и гибок, подойдёт для самого низкоуровнего программирования, но не оптимален для libgdx
            /*for (int i = 0; i < h*2; i+=8/game.settings.graphics) {
                if(Gdx.graphics.getHeight()/2d-h+i < 0 || Gdx.graphics.getHeight()/2d-h+i > Gdx.graphics.getHeight()) continue;
                renderPixmap.setColor(wall.getPixmap().getPixel(
                        (int) ((intersect.getX() + intersect.getY()) % tileSize * wall.texture.getWidth() / tileSize ),
                        // универсальный вариант, если стенки заданы произвольными отрезками
                        // (int) (wall.texture.getWidth() * SuperDuperMath.dist(wall.getPosX1(), wall.getPosY1(), intersect.getX(), intersect.getY()) / wall.getWidth()),
                        (int) (i/h/2*wall.texture.getHeight())
                ));

                for (int j = 0; j < 8/game.settings.graphics; j++) {
                    for (int k = 0; k < 8/game.settings.graphics; k++) {
                        renderPixmap.drawPixel(
                                pixelCounter+k,
                                (int) (Gdx.graphics.getHeight()/2-h+i+j)
                        );
                    }
                }
            }*/
            // НОВЫЙ АЛГОРИТМ
            renderPixmap.drawPixmap(
                    wall.getPixmap(),
                    (int) (wall.texture.getWidth() * SuperDuperMath.dist(wall.getPosX1(), wall.getPosY1(), intersect.getX(), intersect.getY()) / wall.getWidth()),
                    0,
                    1,
                    wall.texture.getHeight(),
                    pixelCounter,
                    Gdx.graphics.getHeight()/2-(int)h,
                    8/game.settings.graphics,
                    2*(int)h
            );
            // Накладываем эффект тумана - #1D1D1D
            renderPixmap.setColor(29f/255,29f/255,29f/255, dist > fogLevel ? 1 - (float) (fogLevel / dist) : 0f);
            renderPixmap.drawRectangle(
                    pixelCounter,
                    Gdx.graphics.getHeight()/2-(int)h,
                    8/game.settings.graphics,
                    2*(int)h
            );

        }
        Texture toDraw = new Texture(renderPixmap);
        batch.draw(toDraw, 0, 0);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
