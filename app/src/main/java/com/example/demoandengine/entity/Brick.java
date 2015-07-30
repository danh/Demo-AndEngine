package com.example.demoandengine.entity;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Brick extends Obstacle{

	public Brick(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isAblePush() {
		// TODO Auto-generated method stub
		return false;
	}

}
