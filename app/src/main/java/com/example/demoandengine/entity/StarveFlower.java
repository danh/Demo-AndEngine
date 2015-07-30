package com.example.demoandengine.entity;

import java.util.Observable;
import java.util.Observer;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.example.demoandengine.DataManager;
import com.example.demoandengine.ResourceManager;
import com.example.demoandengine.entity.Princess.WalkingListener;
import com.example.demoandengine.model.Data;
import com.example.demoandengine.scence.GameScene.PlayerDirection;
import com.example.demoandengine.utils.Constants;

public class StarveFlower extends Obstacle implements WalkingListener {
	private final float initX;
	private final float initY;

	public static enum State {
		NONE, STARVING, EATING
	}

	private State mState = State.NONE;

	public StarveFlower(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, State state) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		initX = pX;
		initY = pY;
		setState(state);

		Princess.getInstance().addWalkingListener(this);
	}

	@Override
	public boolean isAblePush() {
		// TODO Auto-generated method stub
		return false;
	}

	public void eatLeft() {
		setCurrentTileIndex(2);
		setRotation(-90);
		setX(getX() - Constants.HEIGHT_CELL / 2);
		setY(getY() - Constants.WIDTH_CELL / 2);

	}

	public void eatRight() {
		setFlippedHorizontal(true);
		setCurrentTileIndex(2);
		setRotation(90);
		setX(getX() + Constants.HEIGHT_CELL / 2);
		setY(getY() - Constants.WIDTH_CELL / 2);

	}

	public void eatUp() {
		setCurrentTileIndex(3);
		setY(getY() - Constants.WIDTH_CELL);

	}

	public void eatDown() {
		setCurrentTileIndex(3);
		setRotation(180);

	}

	public void eat(PlayerDirection direction) {
		int row = getRow();
		int col = getCol();
		switch (direction) {
		case DOWN:
			setCurrentTileIndex(3);
			setRotation(180);
			row = row + 1;
			break;
		case UP:
			setCurrentTileIndex(3);
			setY(getY() - Constants.WIDTH_CELL);
			row = row - 1;
			break;
		case LEFT:
			setCurrentTileIndex(2);
			setRotation(-90);
			setX(getX() - Constants.HEIGHT_CELL / 2);
			setY(getY() - Constants.WIDTH_CELL / 2);
			col = col - 1;
			break;
		case RIGHT:
			setFlippedHorizontal(true);
			setCurrentTileIndex(2);
			setRotation(90);
			setX(getX() + Constants.HEIGHT_CELL / 2);
			setY(getY() - Constants.WIDTH_CELL / 2);
			col = col + 1;
			break;
		default:
			break;
		}
		final int ro = row;
		final int co = col;
		this.registerUpdateHandler(new TimerHandler(0.3f, false,
				new ITimerCallback() {
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler) {
						resetTile();
						setState(State.EATING);
						DataManager.getInstance().updateTileId(ro, co,
								Constants.BLANK);
						Obstacle waterMelon = ResourceManager.getInstance()
								.getObstacle(ro, co);
						if (waterMelon != null) {
							waterMelon.setVisible(false);
						}
					}
				}));
	}

	private void resetTile() {
		reset();
		setX(initX);
		setY(initY);
	}

	private PlayerDirection findFood() {
		final int col = this.getX() % 24 > 0 ? (int) (this.getX() / 24) + 1
				: (int) this.getX() / 24;
		final int row = this.getY() % 24 > 0 ? (int) (this.getY() / 24) + 1
				: (int) this.getY() / 24;

		if (DataManager.getInstance().getTileId(row + 1, col) == Constants.WATER_MELON
				|| (Princess.getInstance().getRow() == row + 1 && Princess
						.getInstance().getCol() == col)) {
			return PlayerDirection.DOWN;
		} else if (DataManager.getInstance().getTileId(row - 1, col) == Constants.WATER_MELON
				|| (Princess.getInstance().getRow() == row - 1 && Princess
						.getInstance().getCol() == col)) {
			return PlayerDirection.UP;
		} else if (DataManager.getInstance().getTileId(row, col + 1) == Constants.WATER_MELON
				|| (Princess.getInstance().getRow() == row && Princess
						.getInstance().getCol() == col + 1)) {
			return PlayerDirection.RIGHT;
		} else if (DataManager.getInstance().getTileId(row, col - 1) == Constants.WATER_MELON
				|| (Princess.getInstance().getRow() == row && Princess
						.getInstance().getCol() == col - 1)) {
			return PlayerDirection.LEFT;
		}

		return PlayerDirection.NONE;
	}

	public State getState() {
		return mState;
	}

	public void setState(State state) {
		this.mState = state;

		switch (mState) {
		case NONE:
			setCurrentTileIndex(0);
			break;
		case STARVING:
			setCurrentTileIndex(1);
			break;
		case EATING:
			setCurrentTileIndex(0);
			this.registerUpdateHandler(new TimerHandler(5f, false,
					new ITimerCallback() {
						@Override
						public void onTimePassed(
								final TimerHandler pTimerHandler) {
							setState(State.NONE);
						}
					}));
			break;
		default:
			break;
		}
	}

	@Override
	public void completeWalking() {
		// TODO Auto-generated method stub
		PlayerDirection direction = findFood();
		if (direction != PlayerDirection.NONE) {
			switch (getState()) {
			case NONE:
				setState(State.STARVING);
				break;
			case STARVING:
				eat(direction);
				break;
			case EATING:
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void startWalking() {
		// TODO Auto-generated method stub

	}

}
