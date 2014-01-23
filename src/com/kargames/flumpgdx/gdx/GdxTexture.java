package com.kargames.flumpgdx.gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.kargames.flumpgdx.flump.FlumpTexture;

public class GdxTexture {

//	public TextureRegionDrawable region;
	public SpriteDrawable region;
	public String name;
	public Vector2 origin;
	public float scaleFactor;
	
	public GdxTexture(Texture texture, FlumpTexture ft, float scaleFactor) {
//		region = new TextureRegionDrawable(new TextureRegion(
//				texture, ft.rect[0], ft.rect[1], ft.rect[2], ft.rect[3]));
		region = new SpriteDrawable(new Sprite(new TextureRegion(
				texture, ft.rect[0], ft.rect[1], ft.rect[2], ft.rect[3])));
		name = ft.symbol;
		origin = new Vector2(ft.origin[0], ft.origin[1]);
		this.scaleFactor = scaleFactor;
	}
}
