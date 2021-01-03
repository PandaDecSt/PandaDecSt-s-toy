package com.pandadecst.toy.physics.base;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.FloatCounter;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.pandadecst.toy.tool.FPSCameraController;

public class BulletTest extends BaseInput implements Screen {

	public StringBuilder performance = new StringBuilder();
	public String instructions = "test";
	public PerformanceCounter performanceCounter = new PerformanceCounter(this.getClass().getSimpleName());
	public FloatCounter fpsCounter = new FloatCounter(5);
    public FPSCameraController cameraController;
	public PerspectiveCamera camera;

	@Override
	public void show() {  
	}

	@Override
	public void resize(int width, int height) {
	}

    @Override
    public void render(float d) {
        
    }

    @Override
    public void hide() {
    }

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
