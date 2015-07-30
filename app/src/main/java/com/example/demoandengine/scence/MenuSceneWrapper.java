package com.example.demoandengine.scence;

import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationByModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.emitter.RectangleParticleEmitter;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.ease.EaseBounceOut;

import com.example.demoandengine.SceneManager;


public class MenuSceneWrapper extends AbstractScene implements IOnMenuItemClickListener {
	
	private IMenuItem playMenuItem;
	private MyTextMenuItemDecorator soundMenuItem;
	
	@Override
	public void populate() {

		MenuScene menuScene = new MenuScene(camera);
		menuScene.getBackground().setColor(0.82f, 0.96f, 0.97f);		
		
	    playMenuItem = new ColorMenuItemDecorator(new TextMenuItem(0, res.font, "PLAY", vbom),
	    		Color.CYAN, Color.WHITE);
	    
	    soundMenuItem = new MyTextMenuItemDecorator(new TextMenuItem(1, res.font, getSoundLabel(), vbom),
	    		Color.CYAN, Color.WHITE);
	    
	    menuScene.addMenuItem(playMenuItem);
	    menuScene.addMenuItem(soundMenuItem);
	    
	    menuScene.buildAnimations();
	    menuScene.setBackgroundEnabled(true);
	    
	    menuScene.setOnMenuItemClickListener(this);		
		
		Text hiscoreText = new Text(240, 1000, res.font, "HISCORE: " + activity.getHiScore(), vbom);
		menuScene.attachChild(hiscoreText);
		

		hiscoreText.registerEntityModifier(
				new SequenceEntityModifier(
						new ParallelEntityModifier(
								new MoveYModifier(2f, 1000, 600, 
										EaseBounceOut.getInstance()
										),
								new RotationByModifier(2f, 20f)
								),
						new RotationByModifier(0.2f, -20f)
						)
				);
		
    
	    setChildScene(menuScene);

	}

	private CharSequence getSoundLabel() {
		return activity.isSound() ? "SOUND ON" : "SOUND OFF";
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onResume() {
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
		case 0 : 
			SceneManager.getInstance().showGameScene();
			return true;
		case 1 :
			boolean soundState = activity.isSound();
			soundState = !soundState;
			activity.setSound(soundState);
			soundMenuItem.setText(getSoundLabel());
			return true;
		default :
			return false;
		} 
	}

	@Override
	public void onBackKeyPressed() {
		activity.finish();
	}
	
	

}
