package com.example.demoandengine.entity;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class WaterMelon extends Obstacle {

	public WaterMelon(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
	}

	@Override
	public boolean isAblePush() {
		return true;
	}
}
