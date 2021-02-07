package com.pandadecst.toy.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDefaultVehicleRaycaster;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRaycastVehicle;
import com.badlogic.gdx.physics.bullet.dynamics.btRaycastVehicle.btVehicleTuning;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btVehicleRaycaster;
import com.badlogic.gdx.physics.bullet.dynamics.btWheelInfo;
import com.pandadecst.toy.physics.base.BaseBulletTest;
import com.pandadecst.toy.tool.BulletConstructor;
import com.pandadecst.toy.tool.Logger;
import com.pandadecst.toy.world.BulletEntity;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

/** @author Xoppa */
public class VehicleTest extends BaseBulletTest {
	public btVehicleRaycaster raycaster;
	public btRaycastVehicle vehicle;
	public btVehicleTuning tuning;
	BulletEntity chassis;
	BulletEntity wheels[] = new BulletEntity[4];

	Vector3 tmpV = new Vector3();

	protected btVehicleRaycaster getRaycaster() {
		return new btDefaultVehicleRaycaster((btDynamicsWorld)world.collisionWorld);
	}

	@Override
	public void show() {
		super.show();

		final Model chassisModel = objLoader.loadModel(Gdx.files.absolute("./storage/emulated/0/AIDE/girl.obj"));
		disposables.add(chassisModel);
		chassisModel.materials.get(0).clear();
		chassisModel.materials.get(0).set(ColorAttribute.createDiffuse(Color.GRAY), ColorAttribute.createSpecular(Color.WHITE));
		final Model wheelModel = createCapsuleModel(0.2f, 0.2f, 0.2f);
		disposables.add(wheelModel);
		wheelModel.materials.get(0).clear();
		wheelModel.materials.get(0).set(ColorAttribute.createDiffuse(Color.BLACK), ColorAttribute.createSpecular(Color.WHITE),
                                        FloatAttribute.createShininess(128));
		Texture checkboard = new Texture(Gdx.files.internal("et/SAND.png"));
		final Model largeGroundModel = tb.build(tg);
//        modelBuilder.createBox(
//			200f,
//			4f,
//			200f,
//			new Material(TextureAttribute.createDiffuse(checkboard), ColorAttribute.createSpecular(Color.WHITE), FloatAttribute
//                         .createShininess(16f)), Usage.Position | Usage.Normal | Usage.TextureCoordinates);
		largeGroundModel.manageDisposable(checkboard);
		disposables.add(largeGroundModel);
		world.addConstructor("largeground", new BulletConstructor(largeGroundModel, 0f));

		BoundingBox bounds = new BoundingBox();
		Vector3 chassisHalfExtents = chassisModel.calculateBoundingBox(bounds).getDimensions(new Vector3()).scl(0.5f);
		Vector3 wheelHalfExtents = wheelModel.calculateBoundingBox(bounds).getDimensions(new Vector3()).scl(0.5f);

		world.addConstructor("chassis", new BulletConstructor(chassisModel, 5f, new btBoxShape(chassisHalfExtents)));
		world.addConstructor("wheel", new BulletConstructor(wheelModel, 0, null));

//		for (int i = 0; i < 40; i += 4) {
//            for (int j = 0; j < 40; j += 4) {
//                world.add("largeground", j, -1f, i);
//            }
//        }
        world.add("largeground", -20, -1f, -20);

		chassis = world.add("chassis", 0, 3f, 0);
		wheels[0] = world.add("wheel", 0, 0, 0);
		wheels[1] = world.add("wheel", 0, 0, 0);
		wheels[2] = world.add("wheel", 0, 0, 0);
		wheels[3] = world.add("wheel", 0, 0, 0);

		// Create the vehicle
		raycaster = getRaycaster();
		tuning = new btVehicleTuning();
		vehicle = new btRaycastVehicle(tuning, (btRigidBody)chassis.body, raycaster);
		chassis.body.setActivationState(Collision.DISABLE_DEACTIVATION);
		((btDynamicsWorld)world.collisionWorld).addVehicle(vehicle);

		vehicle.setCoordinateSystem(0, 1, 2);

		btWheelInfo wheelInfo;
		Vector3 point = new Vector3();
		Vector3 direction = new Vector3(0, -1, 0);
		Vector3 axis = new Vector3(-1, 0, 0);
		wheelInfo = vehicle.addWheel(point.set(chassisHalfExtents).scl(0.9f, -0.8f, 0.7f), direction, axis,
                                     wheelHalfExtents.z * 0.3f, wheelHalfExtents.z, tuning, true);
		wheelInfo = vehicle.addWheel(point.set(chassisHalfExtents).scl(-0.9f, -0.8f, 0.7f), direction, axis,
                                     wheelHalfExtents.z * 0.3f, wheelHalfExtents.z, tuning, true);
		wheelInfo = vehicle.addWheel(point.set(chassisHalfExtents).scl(0.9f, -0.8f, -0.5f), direction, axis,
                                     wheelHalfExtents.z * 0.3f, wheelHalfExtents.z, tuning, false);
		wheelInfo = vehicle.addWheel(point.set(chassisHalfExtents).scl(-0.9f, -0.8f, -0.5f), direction, axis,
                                     wheelHalfExtents.z * 0.3f, wheelHalfExtents.z, tuning, false);
	}

	float maxForce = 100f;
	float currentForce = 0f;
	float acceleration = 50f; // force/second
	float maxAngle = 60f;
	float currentAngle = 0f;
	float steerSpeed = 45f; // angle/second

	@Override
	public void update() {
		final float delta = Gdx.graphics.getDeltaTime();
		float angle = currentAngle;
		if (objController.rightPressed) {
			if (angle > 0f) angle = 0f;
			angle = MathUtils.clamp(angle - steerSpeed * delta, -maxAngle, 0f);
		} else if (objController.leftPressed) {
			if (angle < 0f) angle = 0f;
			angle = MathUtils.clamp(angle + steerSpeed * delta, 0f, maxAngle);
		} else
			angle = 0f;
		if (angle != currentAngle) {
			currentAngle = angle;
			vehicle.setSteeringValue(angle * MathUtils.degreesToRadians, 0);
			vehicle.setSteeringValue(angle * MathUtils.degreesToRadians, 1);
		}

		float force = currentForce;
		if (objController.frontPressed) {
			if (force < 0f) force = 0f;
			force = MathUtils.clamp(force + acceleration * delta, 0f, maxForce);
		} else if (objController.backPressed) {
			if (force > 0f) force = 0f;
			force = MathUtils.clamp(force - acceleration * delta, -maxForce, 0f);
		} else
			force = 0f;
		if (force != currentForce) {
			currentForce = force;
			vehicle.applyEngineForce(force, 0);
			vehicle.applyEngineForce(force, 1);
		}

		super.update();

		for (int i = 0; i < wheels.length; i++) {
			vehicle.updateWheelTransform(i, true);
			vehicle.getWheelInfo(i).getWorldTransform().getOpenGLMatrix(wheels[i].transform.val);
		}

		chassis.transform.getTranslation(camera.position);
		tmpV.set(camera.position).sub(5, 0, 5).y = 0f;
		camera.position.add(tmpV.nor().scl(-6f)).y = 4.f;
		chassis.transform.getTranslation(tmpV);
//		camera.lookAt(tmpV);
//		camera.up.set(Vector3.Y);
//		camera.update();
	}

    protected Model createCapsuleModel(float w, float h, float d) {
        final Model result = modelBuilder.createSphere(
            w, h, d, 16, 16,
            new Material(ColorAttribute.createDiffuse(Color.WHITE), ColorAttribute.createSpecular(Color.WHITE)), Usage.Position
            | Usage.Normal);
        disposables.add(result);
        return result;
    }

    protected Model createBoxModel(float w, float h, float d) {
        final Model result = modelBuilder.createBox(w, h, d, new Material(ColorAttribute.createDiffuse(Color.WHITE), ColorAttribute.createSpecular(Color.WHITE)), Usage.Position | Usage.Normal);
        disposables.add(result);
        return result;
	}

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        //shoot(x, y);
        return super.touchUp(x, y, pointer, button);
    }

	@Override
	public void dispose() {
		super.dispose();
		vehicle.dispose();
		vehicle = null;
		raycaster.dispose();
		raycaster = null;
		tuning.dispose();
		tuning = null;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
            case Keys.DOWN:
                objController.backPressed = true;
                break;
            case Keys.UP:
                objController.frontPressed = true;
                break;
            case Keys.LEFT:
                objController.leftPressed = true;
                break;
            case Keys.RIGHT:
                objController.rightPressed = true;
                break;
		}
		return super.keyDown(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
            case Keys.DOWN:
                objController.backPressed = false;
                break;
            case Keys.UP:
                objController.frontPressed = false;
                break;
            case Keys.LEFT:
                objController.leftPressed = false;
                break;
            case Keys.RIGHT:
                objController.rightPressed = false;
                break;
            case Keys.R:
                chassis.body.setWorldTransform(chassis.transform.setToTranslation(0, 5, 0));
                chassis.body.setInterpolationWorldTransform(chassis.transform);
                ((btRigidBody)(chassis.body)).setLinearVelocity(Vector3.Zero);
                ((btRigidBody)(chassis.body)).setAngularVelocity(Vector3.Zero);
                chassis.body.activate();
                break;
		}
		return super.keyUp(keycode);
	}
}
