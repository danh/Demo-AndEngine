package com.example.demoandengine.entity;

import android.graphics.Point;

import com.example.demoandengine.DataManager;
import com.example.demoandengine.ResourceManager;
import com.example.demoandengine.pathfinder.AStar;
import com.example.demoandengine.pathfinder.AreaMap;
import com.example.demoandengine.pathfinder.heuristics.AStarHeuristic;
import com.example.demoandengine.pathfinder.heuristics.ClosestHeuristic;
import com.example.demoandengine.scence.GameScene;
import com.example.demoandengine.utils.Constants;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;

import java.util.ArrayList;

/**
 * Created by danhdong on 8/1/2015.
 */
public class Spider extends AnimatedSprite implements Princess.WalkingListener {
    private static final int INIT_TILE_INDEX = 1;

    private static VertexBufferObjectManager vbom;

    @Override
    public void completeWalking() {
        GameScene.PlayerDirection direction = findFood();
        if (direction != GameScene.PlayerDirection.NONE) {
            switch (getState()) {
                case SLEEPING:
                    setState(State.NONE);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void startWalking() {

    }

    public static enum State {
        NONE, SLEEPING
    }

    private State mState = State.SLEEPING;

    public Spider(float pX, float pY,
                     ITiledTextureRegion pTiledTextureRegion,
                     VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        setCurrentTileIndex(INIT_TILE_INDEX);

        Princess.getInstance().addWalkingListener(this);
    }

    public static Spider createSpider(){
        return new Spider(
                0,
                0,
                ResourceManager.getInstance().mPrincessTextureRegion,
                vbom);
    }

    public State getState() {
        return mState;
    }

    public void setState(State state) {
        this.mState = state;

        switch (mState) {
            case NONE:
                this.registerUpdateHandler(new TimerHandler(1.0f, true,
                        new ITimerCallback() {
                            @Override
                            public void onTimePassed(final TimerHandler pTimerHandler) {
                                AreaMap map = new AreaMap(DataManager.getInstance().getRows(), DataManager.getInstance().getCols(), DataManager.getInstance().getTileIds());
                                AStarHeuristic heuristic = new ClosestHeuristic();
                                AStar aStar = new AStar(map, heuristic);
                                ArrayList<Point> shortestPath = aStar.calcShortestPath(getCol(), getRow(), Princess.getInstance().getCol(), Princess.getInstance().getRow());
                            }
                        }));
                break;
            default:
                break;
        }
    }

    private GameScene.PlayerDirection findFood() {
        final int col = this.getX() % 24 > 0 ? (int) (this.getX() / 24) + 1
                : (int) this.getX() / 24;
        final int row = this.getY() % 24 > 0 ? (int) (this.getY() / 24) + 1
                : (int) this.getY() / 24;

        if (DataManager.getInstance().getTileId(row + 1, col) == Constants.WATER_MELON
                || (Princess.getInstance().getRow() == row + 1 && Princess
                .getInstance().getCol() == col)) {
            return GameScene.PlayerDirection.DOWN;
        } else if (DataManager.getInstance().getTileId(row - 1, col) == Constants.WATER_MELON
                || (Princess.getInstance().getRow() == row - 1 && Princess
                .getInstance().getCol() == col)) {
            return GameScene.PlayerDirection.UP;
        } else if (DataManager.getInstance().getTileId(row, col + 1) == Constants.WATER_MELON
                || (Princess.getInstance().getRow() == row && Princess
                .getInstance().getCol() == col + 1)) {
            return GameScene.PlayerDirection.RIGHT;
        } else if (DataManager.getInstance().getTileId(row, col - 1) == Constants.WATER_MELON
                || (Princess.getInstance().getRow() == row && Princess
                .getInstance().getCol() == col - 1)) {
            return GameScene.PlayerDirection.LEFT;
        }

        return GameScene.PlayerDirection.NONE;
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

    public int getCol() {
        return getX() % 24 > 0 ? (int) (getX() / 24) + 1 : (int) getX() / 24;
    }

    public int getRow() {
        return getY() % 24 > 0 ? (int) (getY() / 24) + 1 : (int) getY() / 24;
    }
}
