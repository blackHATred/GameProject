package com.bmstu.ppm.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.bmstu.ppm.PPMGame;
import com.bmstu.ppm.STAGE;
import com.bmstu.ppm.Settings;

import javax.swing.*;

public class LoadLevelScreen implements Screen {
    final PPMGame game;
    private Stage stage;
    private final JFileChooser fileChooser = new JFileChooser();

    public LoadLevelScreen(final PPMGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        TextButton button = new TextButton("Выбрать файл сохранения", game.skin);
        TextButton backButton = new TextButton("Назад", game.skin);

        Table rootTable = new Table(game.skin);
        rootTable.background("window");
        rootTable.setDebug(false);
        rootTable.setFillParent(true);
        stage.addActor(rootTable);
        rootTable.add(new Label("Выбери файл сохранения", game.skin));
        rootTable.row();
        rootTable.add(button);
        rootTable.row();
        rootTable.add(new Label("", game.skin));
        rootTable.row();
        rootTable.add(backButton);
        rootTable.row();
        button.addListener( new ClickListener(){public void clicked(InputEvent event, float x, float y){
            int r = fileChooser.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION){
                game.saveFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    game.setScreen(game.screens.get(Settings.load_save(game.saveFilePath)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }});
        backButton.addListener( new ClickListener(){public void clicked(InputEvent event, float x, float y){
            game.setScreen(game.screens.get(STAGE.MAIN_MENU));
        }});
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
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
        stage.dispose();
    }


}
