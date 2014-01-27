package com.kargames.flumpgdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import com.kargames.flumpgdx.flump.FlumpLibrary;
import com.kargames.flumpgdx.flump.Movie;
import com.kargames.flumpgdx.gdx.GdxLibrary;


// https://aduros.com/flambe/demos/monster/?flambe=flash

public class FlumpGdx extends ApplicationAdapter {
	
	public static final String path = "assets/monster/";

	PanStage stage;
	Gui gui;
	Skin skin;

	public static void main(String[] args) {
		new LwjglApplication(new FlumpGdx(), "FlumpGdx", 1000, 800, true);
	}

	@Override
	public void create() {
		Json json = new Json();
		FlumpLibrary library = json.fromJson(FlumpLibrary.class, Gdx.files.internal(path + "library.json"));

		GdxLibrary gdxLib = new GdxLibrary(library);
		
		stage = new PanStage();
		gui = new Gui(gdxLib, stage);
		Gdx.input.setInputProcessor(new InputMultiplexer(gui, stage));
		
		String[] movies = new String[library.movies.size];
		
		int i = 0;
		for (Movie movie : library.movies) {
			movies[i++] = movie.id;
		}
		gui.setMovies(movies);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0.6f, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		stage.act();
		gui.act();
		
		stage.draw();
		gui.draw();

	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
		gui.setViewport(width, height, true);
	}
}

