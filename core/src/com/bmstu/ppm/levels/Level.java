package com.bmstu.ppm.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bmstu.ppm.PPMGame;
import com.bmstu.ppm.STAGE;
import com.bmstu.ppm.SuperDuperMath;
import com.bmstu.ppm.gameobjects.GameObject;
import com.bmstu.ppm.gameobjects.Player;
import com.bmstu.ppm.gameobjects.Wall;

import java.util.ArrayList;

public class Level implements Screen {
    protected final PPMGame game;
    protected final Stage stage;
    protected Player player;
    protected Texture skybox;
    protected SpriteBatch batch;
    protected Pixmap renderPixmap;
    protected ShapeRenderer shapeRenderer;
    protected Texture floor;
    protected float fogLevel = 200;
    protected final ArrayList<GameObject> objects = new ArrayList<>(10000);
    protected final int tileSize = 100;
    private float shakeCoefficient;
    /** UI */
    private final Label FPSCounter;

    public Level(final PPMGame game, int[][] map, Texture skybox, Texture floor) {
        this.game = game;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                switch (map[i][j]){
                    case 1 -> {
                        // Спавним игрока
                        player = new Player(j*tileSize+tileSize/2f, i*tileSize+tileSize/2f);
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
        //renderPixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        this.skybox = skybox;
        this.floor = floor;


        this.FPSCounter = new Label("0", game.skin);
    }
    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);
        Table rootTable = new Table(game.skin);
        rootTable.setFillParent(true);
        stage.addActor(rootTable);
        rootTable.add(FPSCounter).align(Align.topRight).expandX().expandY();
        rootTable.row();
    }

    @Override
    public void render(float dt) {
        // System.out.println(dt);
        // Очищаем экран
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //renderPixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);

        // Обрабатываем ввод с клавиатуры
        if (Gdx.input.isKeyPressed(Input.Keys.W)) player.move((float) (Math.cos(player.getRotate())*dt), (float) (Math.sin(player.getRotate())*dt), objects);
        if (Gdx.input.isKeyPressed(Input.Keys.A)) player.move((float) (Math.sin(player.getRotate())*dt), (float) (-Math.cos(player.getRotate())*dt), objects);
        if (Gdx.input.isKeyPressed(Input.Keys.S)) player.move((float) (-Math.cos(player.getRotate())*dt), (float) (-Math.sin(player.getRotate())*dt), objects);
        if (Gdx.input.isKeyPressed(Input.Keys.D)) player.move((float) (-Math.sin(player.getRotate())*dt), (float) (Math.cos(player.getRotate())*dt), objects);
        shakeCoefficient = (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.D)) ? Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 4 : 2 : .5f;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) player.rotate(-dt, game.settings.sensitivity);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) player.rotate(dt, game.settings.sensitivity);
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) player.setRunSpeed(); else player.setWalkSpeed();
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) game.setScreen(game.screens.get(STAGE.PAUSE));

        // TODO: сортировать объекты по мере отдалённости для оптимизации

        // Уровень рендера с учётом тряски экрана
        int renderLevel = (int) (Gdx.graphics.getHeight() / 2f + 10 * Math.sin(Math.PI*shakeCoefficient*System.currentTimeMillis()/1000d));

        // Рисуем скайбокс уровня
        batch.begin();
        batch.setColor(1,1,1,1);
        batch.draw(skybox, 0, Gdx.graphics.getHeight() - renderLevel, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        // Рисуем землю

        // Рисуем стены и энтити
        int pixelCounter = -8/game.settings.graphics; // Какой пиксель по горизонтали обрабатываем
        for (float angle = player.getRotate() - game.settings.FOV/2f * SuperDuperMath.rad;
             angle <= player.getRotate() + game.settings.FOV/2f * SuperDuperMath.rad;
             angle += game.settings.FOV * 8f / Gdx.graphics.getWidth() * SuperDuperMath.rad /game.settings.graphics
        ){
            // Обновлённый угол с учётом тряски экрана
            float ang = angle - .01f*(float)Math.sin(Math.PI*shakeCoefficient/2*System.currentTimeMillis()/1000d);
            pixelCounter+=8/game.settings.graphics;
            // Ищем ближайшую для рендера стену
            GameObject contextObj = null;
            SuperDuperMath.Point intersect;
            float dist = 1000000000;
            for (GameObject obj: objects){
                if (obj.getClass() != Wall.class || !obj.isRenderable()) continue;
                intersect = SuperDuperMath.lineIntersect(player, obj, ang);
                if (intersect != null && SuperDuperMath.dist(player, intersect) < dist){
                    dist = SuperDuperMath.dist(player, intersect);
                    contextObj = obj;
                }
            }
            // Устраняем артефакты изображения при необходимости
            if(!game.settings.fishEye)
                dist *= Math.cos(player.getRotate()-ang);
            // Рисуем ближайшую стену
            Wall wall = (Wall) contextObj;
            int h = (int) (player.getVFOV_const()*Gdx.graphics.getHeight()/dist);
            // System.out.println(h);
            intersect = SuperDuperMath.lineIntersect(player, contextObj, ang);
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
            // TODO: делать прикомпиляцию текстур под разный h и засовывать его в hashmap
            // TODO для TODO: добавить в настройки опцию для прекомпиляции текстур
            batch.begin();
            batch.setColor(dist > fogLevel ? fogLevel / dist : 1f, dist > fogLevel ? fogLevel / dist : 1f, dist > fogLevel ? fogLevel / dist : 1f, 1f);
            batch.draw(
                    new TextureRegion(
                            wall.texture,
                            (int) (wall.texture.getWidth() * SuperDuperMath.dist(wall.getPosX1(), wall.getPosY1(), intersect.getX(), intersect.getY()) / wall.getWidth()),
                            0,
                            1,
                            wall.texture.getHeight()
                    ),
                    pixelCounter,
                    Gdx.graphics.getHeight()-renderLevel-h,
                    8/game.settings.graphics,
                    2*h);
            batch.end();
        }
        FPSCounter.setText(String.valueOf(Math.floor(1/dt)));
        stage.act(dt);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("resize");
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
