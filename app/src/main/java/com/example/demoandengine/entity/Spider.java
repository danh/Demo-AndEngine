package com.example.demoandengine.entity;

import com.example.demoandengine.ResourceManager;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by danhdong on 8/1/2015.
 */
public class Spider extends AnimatedSprite {
    private static final int INIT_TILE_INDEX = 1;

    private static VertexBufferObjectManager vbom;

    public Spider(float pX, float pY,
                     ITiledTextureRegion pTiledTextureRegion,
                     VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        setCurrentTileIndex(INIT_TILE_INDEX);
    }

    public static Spider createSpider(){
        return new Spider(
                0,
                0,
                ResourceManager.getInstance().mPrincessTextureRegion,
                vbom);
    }
}
