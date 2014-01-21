package com.kargames.flumpgdx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kargames.flumpgdx.gdx.GdxLayer;
import com.kargames.flumpgdx.gdx.GdxMovie;

public class PanStage extends Stage{
	Vector3 mouse = new Vector3();
	Vector3 mouseDown = new Vector3();
	
	public Actor current;
	
	ShapeRenderer sr;
	
	public PanStage() {
		current = new Actor();
		addActor(current);
		sr = new ShapeRenderer();
	}
	
	@Override
	public boolean scrolled(int amount) {
		((OrthographicCamera)getCamera()).zoom *= (1 + amount/5f);
		return super.scrolled(amount);
	}

	@Override
	public boolean touchDown(int x, int y, int pointer,	int button) {
		mouse.set(x, y, 0);
		getCamera().unproject(mouse);
		return super.touchDown(x, y, pointer, button);
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		mouseDown.set(x, y, 0);
		getCamera().unproject(mouseDown);
		getCamera().translate(mouse.x - mouseDown.x, mouse.y - mouseDown.y, 0);
		return super.touchDragged(x, y, pointer);
	}


	@Override
	public void draw() {
		sr.setProjectionMatrix(getSpriteBatch().getProjectionMatrix());
		sr.setTransformMatrix(getSpriteBatch().getTransformMatrix());
		sr.begin(ShapeType.Line);

		int size = 100;
		for (int x = 0; x < 1000; x += size) {
			for (int y = 0; y < 1000; y += size) {
				sr.line(x, 0, x, 1000);
				sr.line(0, y, 1000, y);
			}
		}

		sr.end();
		super.draw();
	}
	
	public void setActor(Actor actor) {
		this.clear();
		addActor(actor);
	}


	public void setMovie(GdxMovie movie) {
		if (movie == null) return;
		clear();
		addActor(movie);
	}
}
