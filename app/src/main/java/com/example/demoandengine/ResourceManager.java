package com.example.demoandengine;

import java.util.LinkedList;
import java.util.Observable;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import com.example.demoandengine.entity.Item;
import com.example.demoandengine.entity.Obstacle;
import com.example.demoandengine.entity.StarveFlower.State;
import com.example.demoandengine.factory.ItemFactory;
import com.example.demoandengine.factory.ObstacleFactory;
import com.example.demoandengine.utils.Constants;

import android.graphics.Typeface;

public class ResourceManager {

	// single instance is created only
	private static final ResourceManager INSTANCE = new ResourceManager();

	// common objects
	public GameActivity activity;
	public Engine engine;
	public Camera camera;
	public VertexBufferObjectManager vbom;

	// font
	public Font font;

	public ITextureRegion itr;

	private BuildableBitmapTextureAtlas gameTextureAtlas;

	public BitmapTextureAtlas mOnScreenControlTexture;
	public TiledTextureRegion mPrincessTextureRegion;
	public TiledTextureRegion mOrange;
	public TiledTextureRegion mRedMushroom;
	public TiledTextureRegion mPurpleMushroom;
	public TiledTextureRegion mGlass;
	public TiledTextureRegion mWaterMelon;
	public TiledTextureRegion mStone;
	public TiledTextureRegion mBrick;
	public TiledTextureRegion mStarveFlower;
	public TiledTextureRegion mStepedGrass;
	public ITextureRegion mOnScreenControlBaseTextureRegion;
	public ITextureRegion mOnScreenControlKnobTextureRegion;

	private TMXTiledMap mTMXTiledMap;
	private TMXLayer mTMXLayerForeGround;
	private TMXTile[][] mTmxTile;
	private LinkedList<Obstacle> obstacles = new LinkedList<Obstacle>();
	private LinkedList<Item> items = new LinkedList<Item>();
	int numberRow = 0;
	int numberCol = 0;

	// sounds
	public Sound soundFall;
	public Sound soundJump;

	// music
	public Music music;

	// splash graphics
	public ITextureRegion splashTextureRegion;
	private BitmapTextureAtlas splashTextureAtlas;

	// constructor is private to ensure nobody can call it from outside
	private ResourceManager() {
	}

	public static ResourceManager getInstance() {
		return INSTANCE;
	}

	public void create(GameActivity activity, Engine engine, Camera camera,
			VertexBufferObjectManager vbom) {
		this.activity = activity;
		this.engine = engine;
		this.camera = camera;
		this.vbom = vbom;
	}

	public void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		gameTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 512,
				BitmapTextureFormat.RGBA_8888,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.mPrincessTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.gameTextureAtlas, this.activity,
						"princess.png", 10, 1);
		this.mOrange = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(gameTextureAtlas, activity.getAssets(),
						"orange.png", 1, 1);
		this.mRedMushroom = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(gameTextureAtlas, activity.getAssets(),
						"red_mushroom.png", 1, 1);
		this.mPurpleMushroom = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(gameTextureAtlas, activity.getAssets(),
						"purple_mushroom.png", 1, 1);
		this.mGlass = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(gameTextureAtlas, activity.getAssets(),
						"glass.png", 1, 1);
		this.mWaterMelon = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(gameTextureAtlas, activity.getAssets(),
						"watermelon.png", 1, 1);
		this.mStone = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(gameTextureAtlas, activity.getAssets(),
						"stone.png", 1, 1);
		this.mBrick = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(gameTextureAtlas, activity.getAssets(),
						"brick.png", 1, 1);
		this.mStepedGrass = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(gameTextureAtlas, activity.getAssets(),
						"steped_grass.png", 1, 1);
		this.mStarveFlower = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(gameTextureAtlas, activity.getAssets(),
						"starve_flower.png", 4, 1);
		this.mOnScreenControlTexture = new BitmapTextureAtlas(
				this.activity.getTextureManager(), 256, 128,
				TextureOptions.BILINEAR);
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mOnScreenControlTexture, this.activity,
						"onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mOnScreenControlTexture, this.activity,
						"onscreen_control_knob.png", 128, 0);
		this.mOnScreenControlTexture.load();

		try {
			gameTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 1));
			gameTextureAtlas.load();

		} catch (final TextureAtlasBuilderException e) {
			throw new RuntimeException("Error while loading game textures", e);
		}

		try {
			final TMXLoader tmxLoader = new TMXLoader(
					this.activity.getAssets(),
					this.activity.getTextureManager(),
					TextureOptions.BILINEAR_PREMULTIPLYALPHA,
					this.activity.getVertexBufferObjectManager(),
					new ITMXTilePropertiesListener() {
						@Override
						public void onTMXTileWithPropertiesCreated(
								final TMXTiledMap pTMXTiledMap,
								final TMXLayer pTMXLayer,
								final TMXTile pTMXTile,
								final TMXProperties<TMXTileProperty> pTMXTileProperties) {
						}
					});
			mTMXTiledMap = tmxLoader.loadFromAsset("tmx/level1.tmx");
			mTMXLayerForeGround = getTMXTiledMap().getTMXLayers().get(1);
			numberRow = getTMXLayerForeGround().getTileRows();
			numberCol = getTMXLayerForeGround().getTileColumns();
			if (mTmxTile == null) {
				mTmxTile = getTMXLayerForeGround().getTMXTiles();
				DataManager.getInstance().setTile(mTmxTile, numberRow,
						numberCol);
				createObstacles(mTmxTile);
			}
		} catch (final TMXLoadException e) {
			Debug.e(e);
		}

	}

	public void loadGameAudio() {
		try {
			SoundFactory.setAssetBasePath("sfx/");
			soundJump = SoundFactory.createSoundFromAsset(
					activity.getSoundManager(), activity, "jump.ogg");
			soundFall = SoundFactory.createSoundFromAsset(
					activity.getSoundManager(), activity, "fall.ogg");

			MusicFactory.setAssetBasePath("mfx/");
			music = MusicFactory.createMusicFromAsset(
					activity.getMusicManager(), activity, "music.ogg");
		} catch (Exception e) {
			throw new RuntimeException("Error while loading audio", e);
		}
	}

	public void loadFont() {

		// starting from Android 4.4 the texture became too small for some
		// reason
		// changed this to 512x256.
		BitmapTextureAtlas mFontTexture = new BitmapTextureAtlas(
				activity.getTextureManager(), 512, 256, TextureOptions.BILINEAR);
		font = FontFactory.createStroke(activity.getFontManager(),
				mFontTexture,
				Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD), 50, true,
				Color.WHITE_ABGR_PACKED_INT, 2, Color.BLACK_ABGR_PACKED_INT);
		font.prepareLetters("01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ.,!?"
				.toCharArray());
		font.load();
	}

	public void loadSplashGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), 256, 256,
				BitmapTextureFormat.RGBA_8888,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		splashTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(splashTextureAtlas, activity.getAssets(),
						"badge.png", 0, 0);

		splashTextureAtlas.load();
	}

	public void unloadSplashGraphics() {
		splashTextureAtlas.unload();
	}

	public TMXTile[][] getTmxTile() {
		return mTmxTile;
	}

	public void updateData(int row, int col, int value) {
		DataManager.getInstance().updateTileId(row, col, value);
		if (value == Constants.STEPED_FIELD) {
			updateTile(row, col, value);
		}
	}

	private void updateTile(int row, int col, int value) {
		mTmxTile[row][col].setGlobalTileID(getTMXTiledMap(), value);

		getTMXLayerForeGround().updateTileTexture(col, row);
	}

	public TMXLayer getTMXLayerForeGround() {
		return mTMXLayerForeGround;
	}

	public TMXTiledMap getTMXTiledMap() {
		return mTMXTiledMap;
	}

	private void createObstacles(TMXTile[][] tmxTile) {
		if (tmxTile == null)
			return;

		int globalTileId = -1;
		for (int i = 0; i < numberRow; i++) {
			for (int j = 0; j < numberCol; j++) {
				globalTileId = tmxTile[i][j].getGlobalTileID();
				Obstacle obstacle = null;
				Item item = null;
				boolean replaceCell = true;
				switch (globalTileId) {
				case Constants.BRICK:
					obstacle = ObstacleFactory.getInstance()
							.createBrick(j * Constants.WIDTH_CELL,
									i * Constants.HEIGHT_CELL);
					break;
				case Constants.ORANGE:
					item = ItemFactory.getInstance()
							.createOrange(j * Constants.WIDTH_CELL,
									i * Constants.HEIGHT_CELL);
					break;
				case Constants.RED_MURSHROOM:
					item = ItemFactory.getInstance()
							.createRedMushroom(j * Constants.WIDTH_CELL,
									i * Constants.HEIGHT_CELL);
					break;
				case Constants.PURPLE_MURSHROOM:
					item = ItemFactory.getInstance()
							.createPurpleMushroom(j * Constants.WIDTH_CELL,
									i * Constants.HEIGHT_CELL);
					break;
				case Constants.GLASS:
					obstacle = ObstacleFactory.getInstance()
							.createGlass(j * Constants.WIDTH_CELL,
									i * Constants.HEIGHT_CELL);
					break;
				case Constants.WATER_MELON:
					obstacle = ObstacleFactory.getInstance()
							.createWaterMelon(j * Constants.WIDTH_CELL,
									i * Constants.HEIGHT_CELL);
					break;
				case Constants.STONE:
					obstacle = ObstacleFactory.getInstance()
							.createStone(j * Constants.WIDTH_CELL,
									i * Constants.HEIGHT_CELL);
					break;
				case Constants.FLOWER:
					obstacle = ObstacleFactory.getInstance()
							.createStarveFlower(j * Constants.WIDTH_CELL,
									i * Constants.HEIGHT_CELL, State.NONE);
					break;
				case Constants.STARVE_FLOWER:
					obstacle = ObstacleFactory.getInstance()
							.createStarveFlower(j * Constants.WIDTH_CELL,
									i * Constants.HEIGHT_CELL, State.STARVING);
					break;
				default:
					replaceCell = false;
					break;
				}

				if (obstacle != null) {
					getObstacles().add(obstacle);

				} else if (item != null) {
					getItems().add(item);
				}

				if (replaceCell) {
					getTMXLayerForeGround().getTMXTile(j, i).setGlobalTileID(
							getTMXTiledMap(), 1);

					getTMXLayerForeGround().updateTileTexture(j, i);
				}
			}
		}
	}

	public LinkedList<Obstacle> getObstacles() {
		return obstacles;
	}

	public Obstacle getObstacle(int row, int col) {
		Obstacle obstacle = null;
		if (obstacles != null) {
			for (Obstacle ob : obstacles) {
				if (ob.getX() == col * Constants.WIDTH_CELL
						&& ob.getY() == row * Constants.HEIGHT_CELL) {
					obstacle = ob;
					break;
				}
			}
		}

		return obstacle;
	}

	public LinkedList<Item> getItems() {
		return items;
	}

	public Item getItem(int row, int col) {
		Item item = null;
		if (obstacles != null) {
			for (Item i : items) {
				if (i.getX() == col * Constants.WIDTH_CELL
						&& i.getY() == row * Constants.HEIGHT_CELL) {
					item = i;
					break;
				}
			}
		}

		return item;
	}

}
