package com.example.demoandengine.entity;

import com.example.demoandengine.ResourceManager;
import com.example.demoandengine.scence.GameScene;
import com.example.demoandengine.utils.Constants;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;

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

    public void walkTo(GameScene.PlayerDirection playerDirection) {
        if (playerDirection != GameScene.PlayerDirection.NONE) {
            final float pFromX = getX();
            final float pFromY = getY();
            float pToX = pFromX;
            float pToY = pFromY;

            final long[] pFrameDurations;
            final int pFirstTileIndex;
            final int pLastTileIndex;
            final int pStopTileIndex;

            switch (playerDirection) {
                case UP:
                    pToY = pFromY - Constants.HEIGHT_CELL;
                    pFrameDurations = new long[] { 200, 200, 200 };
                    pFirstTileIndex = 5;
                    pLastTileIndex = 7;
                    pStopTileIndex = 6;
                    break;
                case DOWN:
                    pToY = pFromY + Constants.HEIGHT_CELL;
                    pFrameDurations = new long[] { 200, 200, 200 };
                    pFirstTileIndex = 2;
                    pLastTileIndex = 4;
                    pStopTileIndex = 3;
                    break;
                case RIGHT:
                    pToX = pFromX + Constants.WIDTH_CELL;
                    pFrameDurations = new long[] { 300, 300 };
                    pFirstTileIndex = 0;
                    pLastTileIndex = 1;
                    pStopTileIndex = 1;
                    break;
                case LEFT:
                    pToX = pFromX - Constants.WIDTH_CELL;
                    pFrameDurations = new long[] { 300, 300 };
                    pFirstTileIndex = 8;
                    pLastTileIndex = 9;
                    pStopTileIndex = 9;
                    break;
                default:
                    pToX = pFromX;
                    pToY = pFromY;
                    pFrameDurations = new long[] { 300, 300 };
                    pFirstTileIndex = 0;
                    pLastTileIndex = 1;
                    pStopTileIndex = 1;
                    break;
            }

            registerEntityModifier(new MoveModifier(1, pFromX, pToX, pFromY,
                    pToY, new IEntityModifier.IEntityModifierListener() {

                @Override
                public void onModifierStarted(
                        IModifier<IEntity> pModifier, IEntity pItem) {
                    animate(pFrameDurations, pFirstTileIndex,
                            pLastTileIndex, true);
                }

                @Override
                public void onModifierFinished(
                        IModifier<IEntity> pModifier, IEntity pItem) {
                    stopAnimation();
                    setCurrentTileIndex(pStopTileIndex);
                }
            }));
        }
    }
}
