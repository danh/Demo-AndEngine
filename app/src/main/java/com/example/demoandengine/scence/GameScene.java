package com.example.demoandengine.scence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.util.modifier.IModifier;

import android.opengl.GLES20;

import com.example.demoandengine.DataManager;
import com.example.demoandengine.GameActivity;
import com.example.demoandengine.ResourceManager;
import com.example.demoandengine.SceneManager;
import com.example.demoandengine.entity.Glass;
import com.example.demoandengine.entity.Item;
import com.example.demoandengine.entity.Obstacle;
import com.example.demoandengine.entity.Princess;
import com.example.demoandengine.entity.Spider;
import com.example.demoandengine.entity.StarveFlower;
import com.example.demoandengine.entity.Stone;
import com.example.demoandengine.entity.Princess.State;
import com.example.demoandengine.factory.GlassFactory;
import com.example.demoandengine.factory.ObstacleFactory;
import com.example.demoandengine.factory.PrincessFactory;
import com.example.demoandengine.factory.StoneFactory;
import com.example.demoandengine.utils.Constants;

public class GameScene extends AbstractScene implements
		Princess.WalkingListener {
	public static enum PlayerDirection {
		NONE, UP, DOWN, LEFT, RIGHT
	}

	private PlayerDirection mPlayerDirection = PlayerDirection.RIGHT;

	private AnimatedSprite stepedGrass;
	private Princess princess;
	private Spider mSpider;
	private Boolean mWalking = false;
	private DigitalOnScreenControl mDigitalOnScreenControl;

	private int mRow = 0;
	private int mCol = 0;

	int mColGoingTo = 0;
	int mRowGoingTo = 0;

	public GameScene() {
		super();
	}

	@Override
	public void populate() {
		attachTileMap();
		attachObstacles();
		attachItems();
		attachStepGrass();
		attachSpiders();
		createPlayer();
	}

	private void attachObstacles() {
		for (Obstacle ob : res.getObstacles()) {
			attachChild(ob);
		}
	}

	private void attachItems() {
		for (Item item : res.getItems()) {
			attachChild(item);
		}
	}

	private void attachSpiders(){
		mSpider = Spider.createSpider();
		mSpider.setX(Constants.WIDTH_CELL * 7);
		mSpider.setY(Constants.HEIGHT_CELL * 1);
		attachChild(mSpider);
	}

	private void createPlayer() {
		princess = Princess.getInstance();
		princess.setX(Constants.WIDTH_CELL);
		princess.setY(Constants.HEIGHT_CELL);
		princess.addWalkingListener(this);
		res.updateData(1, 1, Constants.STEPED_FIELD);
		attachChild(princess);
		mCol = princess.getX() % 24 > 0 ? (int) (princess.getX() / 24) + 1
				: (int) princess.getX() / 24;
		mRow = princess.getY() % 24 > 0 ? (int) (princess.getY() / 24) + 1
				: (int) princess.getY() / 24;
		this.mDigitalOnScreenControl = new DigitalOnScreenControl(0,
				GameActivity.CAMERA_HEIGHT
						- res.mOnScreenControlBaseTextureRegion.getHeight(),
				camera, res.mOnScreenControlBaseTextureRegion,
				res.mOnScreenControlKnobTextureRegion, 0.1f, vbom,
				new IOnScreenControlListener() {
					@Override
					public void onControlChange(
							final BaseOnScreenControl pBaseOnScreenControl,
							final float pValueX, final float pValueY) {
						synchronized (mWalking) {
							if (mWalking || princess.getState() != State.LIVING) {
								return;
							}

							if (pValueX != 0 || pValueY != 0) {
								mCol = princess.getX() % 24 > 0 ? (int) (princess
										.getX() / 24) + 1 : (int) princess
										.getX() / 24;
								mRow = princess.getY() % 24 > 0 ? (int) (princess
										.getY() / 24) + 1 : (int) princess
										.getY() / 24;
								mColGoingTo = mCol;
								mRowGoingTo = mRow;
							} else {
								return;
							}
							// Set the correct walking animation
							if (pValueY > 0) {
								mPlayerDirection = PlayerDirection.DOWN;
								mRowGoingTo++;
							} else if (pValueY < 0) {
								mPlayerDirection = PlayerDirection.UP;
								mRowGoingTo--;
							} else if (pValueX > 0) {
								mPlayerDirection = PlayerDirection.RIGHT;
								mColGoingTo++;
							} else if (pValueX < 0) {
								mPlayerDirection = PlayerDirection.LEFT;
								mColGoingTo--;
							} else {
								mPlayerDirection = PlayerDirection.NONE;
							}

							final int globalId = DataManager.getInstance()
									.getTileId(mRowGoingTo, mColGoingTo);
							stepedGrass.setVisible(true);
							switch (globalId) {
							case Constants.STEPED_FIELD:
								stepedGrass.setVisible(false);
							case Constants.BLANK:
							case Constants.FIELD:
							case Constants.GATE:
								princess.walkTo(mPlayerDirection);
								break;
							case Constants.PURPLE_MURSHROOM:
								princess.setState(State.DIED);
							case Constants.ORANGE:
							case Constants.RED_MURSHROOM:
								princess.walkTo(mPlayerDirection);
								Item item = res.getItem(mRowGoingTo,
										mColGoingTo);
								item.setVisible(false);

								break;
							case Constants.WATER_MELON:
							case Constants.GLASS:
								final int rowPushing = mRowGoingTo
										+ mRowGoingTo - mRow;
								final int colPushing = mColGoingTo
										+ mColGoingTo - mCol;
								if (DataManager.getInstance().getTileId(
										rowPushing, colPushing) == Constants.BLANK
										|| DataManager.getInstance().getTileId(
												rowPushing, colPushing) == Constants.FIELD
										|| DataManager.getInstance().getTileId(
												rowPushing, colPushing) == Constants.STEPED_FIELD) {
									princess.walkTo(mPlayerDirection);
									Obstacle obstacle = res.getObstacle(
											mRowGoingTo, mColGoingTo);
									obstacle.move(mPlayerDirection, globalId);
									// res.updateData(mRowGoingTo, mColGoingTo,
									// Constants.STEPED_FIELD);
									// res.updateData(rowPushing, colPushing,
									// globalId);
								}

								break;
							default:
								break;
							}

						}
					}
				});
		this.mDigitalOnScreenControl.getControlBase().setBlendFunction(
				GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.mDigitalOnScreenControl.getControlBase().setAlpha(0.5f);
		this.mDigitalOnScreenControl.getControlBase().setScaleCenter(0, 128);
		this.mDigitalOnScreenControl.getControlBase().setScale(1.25f);
		this.mDigitalOnScreenControl.getControlKnob().setScale(1.25f);
		this.mDigitalOnScreenControl.refreshControlKnobPosition();

		setChildScene(this.mDigitalOnScreenControl);
	}

	private void attachTileMap() {
		final ArrayList<TMXLayer> tmxLayers = res.getTMXTiledMap()
				.getTMXLayers();
		for (TMXLayer tmxLayer : tmxLayers) {
			attachChild(tmxLayer);
		}
	}

	private void attachStepGrass() {
		stepedGrass = new AnimatedSprite(Constants.WIDTH_CELL,
				Constants.HEIGHT_CELL,
				ResourceManager.getInstance().mStepedGrass, vbom);
		attachChild(stepedGrass);
	}

	private Obstacle getStone(int row, int col) {
		Obstacle stone = null;
		for (int i = row - 2; i <= row; i++) {
			for (int j = col - 1; j <= col + 1; j++) {
				if (DataManager.getInstance().getTileId(i, j) == Constants.STONE
						&& DataManager.getInstance().getTileId(i + 1, j) == Constants.STEPED_FIELD) {
					if (i + 1 == row && j == col) {
						continue;
					} else {
						stone = res.getObstacle(i, j);
					}
					break;
				}

			}
		}
		return stone;
	}

	private Obstacle getFlower(int row, int col) {
		Obstacle flower = null;
		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = col - 1; j <= col + 1; j++) {
				if (DataManager.getInstance().getTileId(i, j) == Constants.STARVE_FLOWER) {
					flower = res.getObstacle(i, j);
					break;
				}

			}
		}
		return flower;
	}

	private PlayerDirection findFood(StarveFlower flower) {
		final int col = flower.getX() % 24 > 0 ? (int) (flower.getX() / 24) + 1
				: (int) flower.getX() / 24;
		final int row = flower.getY() % 24 > 0 ? (int) (flower.getY() / 24) + 1
				: (int) flower.getY() / 24;

		if (DataManager.getInstance().getTileId(row + 1, col) == Constants.WATER_MELON) {
			return PlayerDirection.DOWN;
		} else if (DataManager.getInstance().getTileId(row - 1, col) == Constants.WATER_MELON) {
			return PlayerDirection.UP;
		} else if (DataManager.getInstance().getTileId(row, col + 1) == Constants.WATER_MELON) {
			return PlayerDirection.RIGHT;
		} else if (DataManager.getInstance().getTileId(row, col - 1) == Constants.WATER_MELON) {
			return PlayerDirection.LEFT;
		}

		return PlayerDirection.NONE;
	}

	private int getHeight(int row, int col) {
		int height = 0;
		while (true) {
			row = row + 1;
			if (DataManager.getInstance().getTileId(row, col) == Constants.STEPED_FIELD) {
				height = height + 1;
			} else {
				break;
			}
		}

		return height;
	}

	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		if (princess != null) {
			stepedGrass.setPosition(princess.getX(), princess.getY());
		}
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().showMenuScene();
	}

	@Override
	public void onPause() {

	}

	@Override
	public void onResume() {

	}

	@Override
	public void completeWalking() {
		mWalking = false;

		res.updateData(mRowGoingTo, mColGoingTo, Constants.STEPED_FIELD);

		// Obstacle obstacle = getStone(mRowGoingTo, mColGoingTo);
		// if (obstacle != null) {
		// int height = getHeight(obstacle.getRow(), obstacle.getCol());
		// if (height != 0) {
		// obstacle.registerEntityModifier(new MoveYModifier(1, obstacle
		// .getY(), obstacle.getY() + height
		// * Constants.HEIGHT_CELL));
		// res.updateData(obstacle.getRow(), obstacle.getCol(),
		// Constants.STEPED_FIELD);
		// res.updateData(obstacle.getRow() + height, obstacle.getCol(),
		// Constants.STONE);
		// }
		// }

		// Obstacle flower = getFlower(mRowGoingTo, mColGoingTo);
		// if (flower != null) {
		// ((StarveFlower) flower).eat(findFood(((StarveFlower) flower)));
		// }

	}

	@Override
	public void startWalking() {
		mWalking = true;
		// res.updateData(mRow, mCol, Constants.STEPED_FIELD);
	}

}
