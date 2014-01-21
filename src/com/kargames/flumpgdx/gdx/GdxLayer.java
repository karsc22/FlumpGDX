package com.kargames.flumpgdx.gdx;

import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.kargames.flumpgdx.Gui;
import com.kargames.flumpgdx.flump.FlumpKeyframe;
import com.kargames.flumpgdx.flump.FlumpLayer;

public class GdxLayer extends Image {
	
	public static GdxLayer selected;
	public Array<GdxKeyframe> keyframes;
	public String name;
	public int currentFrameNum;
	GdxKeyframe cf;
	GdxKeyframe nf;
	public int currentProgress;
	public int duration;
	
	public float secTimer;
	public boolean isHighlighted;
	
	Label label;
	
	private GdxMovie movie;

	Matrix4 projMatrix;
	Matrix4 tranMatrix;
	
	static ShapeRenderer sr = new ShapeRenderer();
	
	public GdxLayer(Map<String, GdxTexture> textures, FlumpLayer layer, GdxMovie movie) {
		name = layer.name;
		this.movie = movie;
		keyframes = new Array<GdxKeyframe>();
		duration = 0;
		
		for (FlumpKeyframe flumpKeyframe : layer.keyframes) {
			keyframes.add(new GdxKeyframe(textures, flumpKeyframe));
			duration += flumpKeyframe.duration;
		}
//		reset();
		secTimer = 1;
		
		label = new Label(name, Gui.skin);
		
		addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				select();
			}
			
		});
		projMatrix = new Matrix4();
		tranMatrix = new Matrix4();
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		Matrix4 proj = batch.getProjectionMatrix().cpy();
		Matrix4 tran = batch.getTransformMatrix().cpy();
		
		projMatrix.mul(proj);
		tranMatrix.mul(tran);
		batch.setProjectionMatrix(projMatrix);
//		batch.setTransformMatrix(tranMatrix);
		this.translate(-getOriginX()/2, -getOriginY()/2);
		super.draw(batch, (selected == this) ? 1 : 0.6f);
		batch.setProjectionMatrix(proj);
//		batch.setTransformMatrix(tran);
		this.translate(getOriginX()/2, getOriginY()/2);
		
		batch.end();
		sr.setProjectionMatrix(projMatrix);
//		sr.setTransformMatrix(tranMatrix);
		
		sr.setColor(Color.RED);
		sr.begin(ShapeType.Line);
		sr.rect(-getWidth()/2, -getHeight()/2, getWidth(), getHeight());
		sr.end();
		
		sr.begin(ShapeType.Filled);
		sr.circle(0, 0, 10);
		sr.end();
		
		batch.begin();
		label.draw(batch, parentAlpha);
	}

	@Override
	public void act(float delta) {
		updateFrame();
		super.act(delta);
		label.setPosition(getX()+getOriginX(), getY()+getOriginY());
	}
	
	private void updateFrame() {
		
        int finalFrame = keyframes.size-1;
        currentFrameNum = 0;

        while (currentFrameNum < finalFrame && 
        		keyframes.get(currentFrameNum+1).index <= movie.current) {
            ++currentFrameNum;
        }

        if (currentFrameNum > finalFrame) {
        	currentFrameNum = 0;
        }
        
        cf = keyframes.get(currentFrameNum);

		if (cf.texture != null) {
			TextureRegionDrawable trd = cf.texture.region;
			setDrawable(trd);
			setSize(trd.getRegion().getRegionWidth(), trd.getRegion()
					.getRegionHeight());
		}

        float locX = cf.loc.x;
        float locY = cf.loc.y;
        float scaleX = cf.scale.x;
        float scaleY = cf.scale.y;
        float skewX = cf.skew.x;
        float skewY = cf.skew.y;
        float alpha = cf.alpha;

        if (true && currentFrameNum < finalFrame) {
            nf = keyframes.get(currentFrameNum+1);
            if (nf.ref != null) {
            	
                float percent = (movie.current-cf.index) / cf.duration;
                locX += (nf.loc.x-locX) * percent;
                locY += (nf.loc.y-locY) * percent;
                scaleX += (nf.scale.x-scaleX) * percent;
                scaleY += (nf.scale.y-scaleY) * percent;
                skewX += (nf.skew.x-skewX) * percent;
                skewY += (nf.skew.y-skewY) * percent;
                alpha += (nf.alpha-alpha) * percent;
            }
        }

        float sinX = MathUtils.sin(skewX), cosX = MathUtils.cos(skewX);
        float sinY = MathUtils.sin(skewY), cosY = MathUtils.cos(skewY);

        float m00 = cosY * scaleX;
        float m01 = sinY * scaleX;
        float m10 = -sinX * scaleY;
        float m11 = cosX * scaleY;
        
//        float[] values = {m00, m01, 0, 0,
//			   	  m10, m11, 0, 0,
//				  0, 0, 1, 0,
//				  locX, locY, 0, 1};
        
        float[] values = {m00, m01, 0, 0,
			   	  m10, m11, 0, 0,
				  0, 0, 1, 0,
				  0, 0, 0, 1};
        projMatrix.set(values);

		float[] tranValues = {1, 0, 0, 0,
			   	  0, 1, 0, 0,
				  0, 0, 1, 0,
				  locX, locY, 0, 1};
		tranMatrix.set(tranValues);
		
		setScale(1);
		setRotation(0);
		setOrigin(cf.pivot.x, cf.pivot.y);
		
		label.setText(name + ":" + cf.ref);
	}
	
	public float inter(float v1, float v2, float percent) {
		return v1 + (v2-v1)*percent;
	}
	
	public GdxKeyframe getCurrentKeyframe() {
		return null;
	}
	
	public void select() {
		selected = this;
		toFront();
	}
	
	public String toString() {
		return name + "   rotation: " + getRotation() + "   position: " 
				+ getX() + ", " + getY();
	}
	
	
}
