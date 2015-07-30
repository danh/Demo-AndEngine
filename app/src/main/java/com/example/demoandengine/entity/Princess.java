package com.example.demoandengine.entity;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;

import com.example.demoandengine.ResourceManager;
import com.example.demoandengine.scence.GameScene.PlayerDirection;
import com.example.demoandengine.utils.Constants;

public class Princess extends AnimatedSprite {
	private static final int INIT_TILE_INDEX = 1;

	public static enum State {
		LIVING, WILL_DIE, DIED
	}

	private State mState = State.LIVING;

	private volatile static Princess instance;

	private static VertexBufferObjectManager vbom;

	public interface WalkingListener {
		public void completeWalking();

		public void startWalking();
	}

	public static final String TYPE = "Player";

	private List<WalkingListener> listeners;

	public static Princess getInstance() {
		if (instance == null) {
			synchronized (Princess.class) {
				if (instance == null) {
					instance = new Princess(
							0,
							0,
							ResourceManager.getInstance().mPrincessTextureRegion,
							vbom);
				}
			}
		}

		return instance;
	}

	private Princess(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		setCurrentTileIndex(INIT_TILE_INDEX);
	}

	public void addWalkingListener(WalkingListener listener) {
		if (listeners == null) {
			listeners = new ArrayList<WalkingListener>();
		}

		listeners.add(listener);
	}

	public void removeWalkingListener(WalkingListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	public void walkTo(PlayerDirection playerDirection) {
		if (playerDirection != PlayerDirection.NONE) {
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

							for (WalkingListener listener : listeners) {
								listener.startWalking();
							}
						}

						@Override
						public void onModifierFinished(
								IModifier<IEntity> pModifier, IEntity pItem) {
							stopAnimation();
							setCurrentTileIndex(pStopTileIndex);

							for (WalkingListener listener : listeners) {
								listener.completeWalking();
							}

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

	public State getState() {
		return mState;
	}

	public void setState(State mState) {
		this.mState = mState;
	}
}
