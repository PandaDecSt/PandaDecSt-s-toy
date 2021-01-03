package com.pandadecst.toy.tool;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntIntMap;
import com.pandadecst.toy.utils.Callback;

/**
 * Created by Kristian Rekstad on 07.03.2015.
 * Based on {@link com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController}, but it has private fields
 * so it can not be extended from.
 */
public class FPSCameraController extends InputAdapter {

    public static final String CALLBACK_FIRE = "fpsFire";
    public static final String CALLBACK_LIGHT = "fpsLigh";

    protected final Camera camera;
    protected final IntIntMap keys = new IntIntMap();
    protected int TOGGLE_CURSOR_LOCK = Input.Keys.ESCAPE;
    protected int STRAFE_LEFT = Input.Keys.A;
    protected int STRAFE_RIGHT = Input.Keys.D;
    protected int FORWARD = Input.Keys.W;
    protected int BACKWARD = Input.Keys.S;
    protected int SPRINT = Input.Keys.SHIFT_LEFT;

    protected boolean mLeftClicked = false;
    protected boolean mRightClicked = false;

    public float velocity = 5;
    public float sprintVelocity = 10;
    protected float currentVelocity = 0;
    protected boolean walking = false;

    public float degreesPerPixel = 0.3f;

    protected float yAxisDegrees = 0;
    protected float xAxisDegrees = 0;

    protected float headBob = 0;
    protected float cameraHeight = 1.8f;
    protected float cameraHeightOffset = 0f;

    protected final Vector3 tmp = new Vector3();
    protected final Vector3 tmp2 = new Vector3();
    protected final Quaternion rightAxisRotation = new Quaternion();

    protected boolean cursorLock = true;

    int cursorX;
    int cursorY;

    protected Callback playerFiredCallback;

    public FPSCameraController(Camera camera) {
        this.camera = camera;
        resized(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (Gdx.app.getType() == Application.ApplicationType.Android){
            setCursorLock(false);
        } else {
            setCursorLock(true);
        }
    }

    public void setPlayerFireCallback(Callback callback){
        playerFiredCallback = callback;
    }

    public void resized(int width, int height){
        cursorX = width/2;
        cursorY = height/2;
    }

    private int deltaX, deltaY;

    public void update(float deltaTime){

        if (cursorLock){
            deltaX = Gdx.input.getDeltaX();
            deltaY = Gdx.input.getDeltaY();
            Gdx.input.setCursorPosition(cursorX, cursorY);
            cameraMoved(deltaX, deltaY);
        }

        walking = false;
        tmp.set(0f,0f,0f);
        if (keys.containsKey(FORWARD)) {
            tmp2.set(camera.direction).y=0;
            tmp2.nor();
            tmp.add(tmp2);
            walking = true;
        }
        if (keys.containsKey(BACKWARD)) {
            tmp2.set(camera.direction).y=0;
            tmp2.nor();
            tmp.sub(tmp2);
            walking = true;
        }
        if (keys.containsKey(STRAFE_LEFT)) {
            tmp2.set(camera.direction).crs(camera.up).y=0;
            tmp2.nor();
            tmp.sub(tmp2);
            walking = true;
        }
        if (keys.containsKey(STRAFE_RIGHT)) {
            tmp2.set(camera.direction).crs(camera.up).y=0;
            tmp2.nor();
            tmp.add(tmp2);
            walking = true;
        }

        if (walking){
            currentVelocity = velocity;
            float sprintBob = 1f;
            if (keys.containsKey(SPRINT)){
                currentVelocity = sprintVelocity;
                headBob += deltaTime*16f;
                sprintBob = 0.8f;
            } else {
                headBob += deltaTime*13f;
            }
            cameraHeightOffset = (float)Math.sin(headBob)*0.05f*sprintBob;

        } else {
            headBob = 0f;
            cameraHeightOffset *= 30f*deltaTime;
            if (Math.abs(cameraHeightOffset) <= 0.0001){
                cameraHeightOffset = 0f;
            }
        }

        camera.position.y = cameraHeight+cameraHeightOffset;
        tmp.nor().scl(deltaTime * currentVelocity);
        camera.position.add(tmp);
        camera.update();

        if (mLeftClicked){
            mLeftClicked = false;
            if (playerFiredCallback != null){
                playerFiredCallback.onCallback(CALLBACK_FIRE);
            }
        }
        if (mRightClicked){
            mRightClicked = false;
            if (playerFiredCallback != null){
                playerFiredCallback.onCallback(CALLBACK_LIGHT);
            }
        }
    }

    public void setCursorLock(boolean locked){
        cursorLock = locked;
        Gdx.input.setCursorCatched(cursorLock);
    }

    public void toggleCursorLock(){
        setCursorLock(!cursorLock);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == TOGGLE_CURSOR_LOCK){
            toggleCursorLock();
        }

        keys.put(keycode, keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        keys.remove(keycode, 0);
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT){
            mLeftClicked = true;
        } else if (button == Input.Buttons.RIGHT){
            mRightClicked = true;
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return super.touchUp(screenX, screenY, pointer, button);
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (cursorLock) return false;

        cameraMoved(Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
        return true;
    }

    private void cameraMoved(int deltaX, int deltaY){
        float dx = -deltaX*degreesPerPixel;
        float dy = -deltaY*degreesPerPixel; // dy > 0 means drag up

        /*xAxisDegrees += dy;
        yAxisDegrees += dx;

        xAxisDegrees = MathUtils.clamp(xAxisDegrees, -89f, 89f);

        if (yAxisDegrees >= 360){
            yAxisDegrees -= 360;
        } else if (yAxisDegrees <= -360){
            yAxisDegrees += 360;
        }*/

        strat1(dx, dy);
    }

    private void strat1(float dx, float dy){
        tmp.set(camera.direction).crs(camera.up).nor();

        float currentY = camera.direction.y *90;
        if (Math.abs(currentY+dy) > 89){
            dy = Math.signum(currentY)*89-currentY;
        }

        camera.direction.rotate(tmp, dy).nor();
        camera.up.rotate(tmp, dy).nor();

        camera.direction.rotate(Vector3.Y, dx).nor();
        camera.up.rotate(Vector3.Y, dx).nor();

    }

    private void strat2(float dx, float dy){
        tmp.set(camera.direction).crs(Vector3.Y).y=0;

        if (tmp.len2() <= 0.01f && (camera.direction.y>0 && dy>0) || (camera.direction.y<0 && dy<0)){
            // parallell direction to up axis.
            return;
        }

        tmp.nor(); // right axis

        camera.direction.rotate(tmp, dy);
        camera.direction.rotate(Vector3.Y, dx);

        camera.up.set(camera.direction).crs(Vector3.Y).y=0;
        camera.up.crs(camera.direction).nor();
    }


    private void strat3(){
        // remember unit quaternions when rotating!!!!1!1! :pPpPP
        // q = q2 * q1 , q1 followed by q2

        /*
        Reset mouse coordinates to center-of-screen on each frame, so that mouse never gets caught on the screen borders
        Maintain the camera's "up" vector (disallow roll) and recompute the "sideways" vector
        Disallow looking up past the vertical +y axis, or down past the -y axis (too far up/down)
        Get the order of rotations correct (up/down first, then left/right)
        Renormalize the "up", "aim", and "sideways" vectors each frame
         */

        // NOT FULLY IMPLEMENTED.
        tmp.set(camera.direction).crs(camera.up);
        rightAxisRotation.setEulerAngles(yAxisDegrees, xAxisDegrees, 0);
        //camera.direction.rot
        //camera.direction.rotate(tmp, )
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return super.mouseMoved(screenX, screenY);
    }
}
