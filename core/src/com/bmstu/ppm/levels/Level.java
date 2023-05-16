package com.bmstu.ppm.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
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
    protected final FitViewport viewport;
    protected final Stage stage;
    protected Player player;
    protected Texture skybox;
    protected SpriteBatch skyboxBatch;
    protected Texture floor;
    protected final ArrayList<GameObject> objects = new ArrayList<GameObject>(10000);
    protected final int tileSize = 50;

    public Level(final PPMGame game, int[][] map, Texture skybox, Texture floor) {
        this.game = game;
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage();
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
        skyboxBatch = new SpriteBatch();
        this.skybox = skybox;
        this.floor = floor;
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float dt) {
        // Очищаем экран
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Обрабатываем ввод с клавиатуры
        if (Gdx.input.isKeyPressed(Input.Keys.W)) player.move(Math.cos(player.getRotate())*dt, Math.sin(player.getRotate())*dt, objects);
        if (Gdx.input.isKeyPressed(Input.Keys.A)) player.move(Math.cos(player.getRotate())*dt, Math.sin(player.getRotate())*dt, objects);
        if (Gdx.input.isKeyPressed(Input.Keys.S)) player.move(Math.cos(player.getRotate())*dt, Math.sin(player.getRotate())*dt, objects);
        if (Gdx.input.isKeyPressed(Input.Keys.D)) player.move(Math.cos(player.getRotate())*dt, Math.sin(player.getRotate())*dt, objects);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) player.rotate(-dt);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) player.rotate(dt);
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) player.setRunSpeed(); else player.setWalkSpeed();

        // TODO: сортировать объекты по мере отдалённости для оптимизации
        // Рисуем скайбокс уровня
        skyboxBatch.begin();
        skyboxBatch.draw(skybox, 0, (float) Gdx.graphics.getHeight() /2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        skyboxBatch.end();
        // Рисуем землю

        // Рисуем стены
        int pixelCounter = -1; // Какой пиксель по горизонтали обрабатываем
        for (double angle = player.getRotate() - game.settings.FOV/2d;
             angle <= player.getRotate() + game.settings.FOV/2d;
             angle += (double) game.settings.FOV / Gdx.graphics.getWidth()
        ){
            pixelCounter++;
            // Ищем ближайшую для рендера стену
            GameObject contextObj = null;
            SuperDuperMath.Point intersect;
            double dist = 1000000000;
            for (GameObject obj: objects){
                if (obj.getClass() != Wall.class && obj.isRenderable()) continue;
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
            double h = player.getVFOV_const()*Gdx.graphics.getHeight()*dist;

            for (int i = 0; i < h*2; i++) {
                intersect = SuperDuperMath.lineIntersect(player, contextObj, angle);

            }
        }

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
