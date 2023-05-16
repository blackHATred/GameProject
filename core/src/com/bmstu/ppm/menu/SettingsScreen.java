package com.bmstu.ppm.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.bmstu.ppm.PPMGame;
import com.bmstu.ppm.STAGE;

import java.io.IOException;

public class SettingsScreen implements Screen {
    final PPMGame game;
    private Stage stage;
    private final TextButton back;
    private final Slider volume;
    private final Slider FOV;
    private final Slider graphic;
    private final CheckBox fishEye;
    private final Label volumeLabel;
    private final Label FOVLabel;
    private final Label graphicLabel;
    private final Label fishEyeLabel;
    public SettingsScreen(final PPMGame game) {
        this.game = game;
        back = new TextButton("Назад", game.skin);
        volume = new Slider(0, 100, 1, false, game.skin);
        volume.setValue(game.settings.volume);
        FOV = new Slider(60, 180, 2, false, game.skin);
        FOV.setValue(game.settings.FOV);
        graphic = new Slider(1, 8, 1, false, game.skin);
        graphic.setValue(game.settings.graphics);
        fishEye = new CheckBox("", game.skin);
        fishEye.setChecked(game.settings.fishEye);
        volumeLabel = new Label(String.valueOf(game.settings.volume), game.skin);
        FOVLabel = new Label(String.valueOf(game.settings.FOV), game.skin);
        graphicLabel = new Label(String.valueOf(game.settings.graphics), game.skin);
        fishEyeLabel = new Label(game.settings.fishEye ? "Включено" : "Выключено", game.skin);
    }

    @Override
    public void show() {
        // TODO: добавить регулировку чувствительности
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Table rootTable = new Table(game.skin);
        rootTable.background("window");
        rootTable.setDebug(false);
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        rootTable.add(new Label("Громкость", game.skin));
        rootTable.add(volume).pad(0, 10, 0, 10);
        rootTable.add(volumeLabel);
        rootTable.row();
        rootTable.add(new Label("Поле зрения", game.skin));
        rootTable.add(FOV).pad(0, 10, 0, 10);
        rootTable.add(FOVLabel);
        rootTable.row();
        rootTable.add(new Label("Качество графики", game.skin));
        rootTable.add(graphic).pad(0, 10, 0, 10);
        rootTable.add(graphicLabel);
        rootTable.row();
        rootTable.add(new Label("Эффект рыбьего глаза", game.skin));
        rootTable.add(fishEye).pad(0, 10, 0, 10);
        rootTable.add(fishEyeLabel);
        rootTable.row();
        rootTable.add(new Label("", game.skin));
        rootTable.row();
        rootTable.add(back).colspan(3);
        FOV.addListener(new ChangeListener(){public void changed(ChangeEvent event, Actor actor) {
            FOVLabel.setText(String.valueOf((int) FOV.getValue()));
            game.settings.FOV = (int) FOV.getValue();
            try {
                game.settings.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }});
        volume.addListener(new ChangeListener(){public void changed(ChangeEvent event, Actor actor) {
            volumeLabel.setText(String.valueOf((int) volume.getValue()));
            game.settings.volume = (int) volume.getValue();
            game.music.setVolume((float) volume.getValue() / 100);
            try {
                game.settings.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }});
        graphic.addListener(new ChangeListener(){public void changed(ChangeEvent event, Actor actor) {
            if(graphic.getValue() != 1) graphic.setValue(graphic.getValue() - graphic.getValue() % 2);
            graphicLabel.setText(String.valueOf((int) graphic.getValue()));
            game.settings.graphics = (int) graphic.getValue();
            try {
                game.settings.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }});
        fishEye.addListener(new ChangeListener(){public void changed(ChangeEvent event, Actor actor) {
            fishEyeLabel.setText(fishEye.isChecked() ? "Включено" : "Выключено");
            game.settings.fishEye = fishEye.isChecked();
            try {
                game.settings.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }});
        back.addListener( new ClickListener(){public void clicked(InputEvent event, float x, float y){
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
