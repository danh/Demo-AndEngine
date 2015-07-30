package com.example.demoandengine;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLayerProperty;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.TMXTiledMapProperty;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.IModifier;

import android.opengl.GLES20;

public class MainActivity extends SimpleBaseGameActivity {
	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 320;

	private Camera mCamera;

	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;

	private BitmapTextureAtlas mOnScreenControlTexture;
	private TiledTextureRegion mPrincessTextureRegion;
	private ITextureRegion mOnScreenControlBaseTextureRegion;
	private ITextureRegion mOnScreenControlKnobTextureRegion;

	private DigitalOnScreenControl mDigitalOnScreenControl;

	private TMXTiledMap mTMXTiledMap;

	private Boolean mAnimating = false;

	@Override
	public EngineOptions onCreateEngineOptions() {
		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				this.getTextureManager(), 512, 256, TextureOptions.BILINEAR);
		this.mPrincessTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.mBitmapTextureAtlas, this,
						"princess.png", 10, 1);
		try {
			this.mBitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 1));
			this.mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}

		this.mOnScreenControlTexture = new BitmapTextureAtlas(
				this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mOnScreenControlTexture, this,
						"onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mOnScreenControlTexture, this,
						"onscreen_control_knob.png", 128, 0);
		this.mOnScreenControlTexture.load();
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		try {
			final TMXLoader tmxLoader = new TMXLoader(this.getAssets(),
					this.mEngine.getTextureManager(),
					TextureOptions.BILINEAR_PREMULTIPLYALPHA,
					this.getVertexBufferObjectManager(),
					new ITMXTilePropertiesListener() {
						@Override
						public void onTMXTileWithPropertiesCreated(
								final TMXTiledMap pTMXTiledMap,
								final TMXLayer pTMXLayer,
								final TMXTile pTMXTile,
								final TMXProperties<TMXTileProperty> pTMXTileProperties) {
						}
					});
			this.mTMXTiledMap = tmxLoader.loadFromAsset("tmx/level1.tmx");
		} catch (final TMXLoadException e) {
			Debug.e(e);
		}

		final ArrayList<TMXLayer> tmxLayers = this.mTMXTiledMap.getTMXLayers();
		for (TMXLayer tmxLayer : tmxLayers) {
			scene.attachChild(tmxLayer);
		}

		final TMXTile [][]tile = tmxLayers.get(0).getTMXTiles();
		System.out.println("hhhhhhhhhhhhhhh: "+tmxLayers.get(1).getTMXTile(0, 0).getGlobalTileID());
		
		/* Snapdragon. */
		final AnimatedSprite princess = new AnimatedSprite(0, 0,
				this.mPrincessTextureRegion,
				this.getVertexBufferObjectManager());
		princess.animate(100, false);

		scene.attachChild(princess);

		final PhysicsHandler physicsHandler = new PhysicsHandler(princess);
		princess.registerUpdateHandler(physicsHandler);

		this.mDigitalOnScreenControl = new DigitalOnScreenControl(0,
				CAMERA_HEIGHT
						- this.mOnScreenControlBaseTextureRegion.getHeight(),
				this.mCamera, this.mOnScreenControlBaseTextureRegion,
				this.mOnScreenControlKnobTextureRegion, 0.1f,
				this.getVertexBufferObjectManager(),
				new IOnScreenControlListener() {
					@Override
					public void onControlChange(
							final BaseOnScreenControl pBaseOnScreenControl,
							final float pValueX, final float pValueY) {

						if (pValueX != 0 || pValueY != 0) {
							synchronized (mAnimating) {
								if (mAnimating) {
									return;
								}

								if (pValueX > 0) {

									final float pFromX = princess.getX();
									princess.registerEntityModifier(new MoveXModifier(
											1,
											pFromX,
											pFromX + 24,
											new IEntityModifier.IEntityModifierListener() {

												@Override
												public void onModifierStarted(
														IModifier<IEntity> pModifier,
														IEntity pItem) {
													// TODO Auto-generated
													// method stub
													princess.animate(
															new long[] { 300,
																	300 }, 0,
															1, true);
													mAnimating = true;
												}

												@Override
												public void onModifierFinished(
														IModifier<IEntity> pModifier,
														IEntity pItem) {
													// TODO Auto-generated
													// method stub
													princess.stopAnimation();
													mAnimating = false;
												}
											}));
								} else if (pValueX < 0) {
									final float pFromX = princess.getX();
									princess.registerEntityModifier(new MoveXModifier(
											1,
											pFromX,
											pFromX - 24,
											new IEntityModifier.IEntityModifierListener() {

												@Override
												public void onModifierStarted(
														IModifier<IEntity> pModifier,
														IEntity pItem) {
													// TODO Auto-generated
													// method stub
													princess.animate(
															new long[] { 300,
																	300 }, 8,
															9, true);
													mAnimating = true;
												}

												@Override
												public void onModifierFinished(
														IModifier<IEntity> pModifier,
														IEntity pItem) {
													// TODO Auto-generated
													// method stub
													princess.stopAnimation();
													mAnimating = false;
												}
											}));
								} else if (pValueY > 0) {
									final float pFromY = princess.getY();
									princess.registerEntityModifier(new MoveYModifier(
											1,
											pFromY,
											pFromY + 24,
											new IEntityModifier.IEntityModifierListener() {

												@Override
												public void onModifierStarted(
														IModifier<IEntity> pModifier,
														IEntity pItem) {
													// TODO Auto-generated
													// method stub
													princess.animate(
															new long[] { 200,
																	200, 200 },
															2, 4, true);
													mAnimating = true;
												}

												@Override
												public void onModifierFinished(
														IModifier<IEntity> pModifier,
														IEntity pItem) {
													// TODO Auto-generated
													// method stub
													princess.stopAnimation();
													mAnimating = false;
												}
											}));

								} else if (pValueY < 0) {
									final float pFromY = princess.getY();
									princess.registerEntityModifier(new MoveYModifier(
											1,
											pFromY,
											pFromY - 24,
											new IEntityModifier.IEntityModifierListener() {

												@Override
												public void onModifierStarted(
														IModifier<IEntity> pModifier,
														IEntity pItem) {
													// TODO Auto-generated
													// method stub
													princess.animate(
															new long[] { 200,
																	200, 200 },
															5, 7, true);
													mAnimating = true;
												}

												@Override
												public void onModifierFinished(
														IModifier<IEntity> pModifier,
														IEntity pItem) {
													// TODO Auto-generated
													// method stub
													princess.stopAnimation();
													mAnimating = false;
												}
											}));
								}
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

		scene.setChildScene(this.mDigitalOnScreenControl);

		return scene;
	}

}
