package com.pandadecst.toy.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btConvexShape;
import com.badlogic.gdx.physics.bullet.collision.btPairCachingGhostObject;
import com.badlogic.gdx.physics.bullet.dynamics.btKinematicCharacterController;
import com.pandadecst.toy.ui.ObjectController;
import com.pandadecst.toy.utils.base.BaseBulletTest;

public class Player {
    BaseBulletTest bbt;
	public BulletEntity character;
//    btGhostPairCallback ghostPairCallback;
	public btPairCachingGhostObject ghostObject;
	public btConvexShape ghostShape;
    Model playerModel;
	public btKinematicCharacterController characterController;
    ObjectController objController;
	public Matrix4 characterTransform;
	public Vector3 characterDirection = new Vector3();
	public Vector3 camDirection = new Vector3();
	public Vector3 walkDirection = new Vector3();

	private final float xScalar = 0.2f;
	private final float yScalar = 0.1f;

	// Raycast事件
	ClosestRayResultCallback callback;
	Vector3 rayFrom = new Vector3();
	Vector3 rayTo = new Vector3();

	public Player(BaseBulletTest b, ObjectController objController) {
        this.bbt = b;
        this.objController = objController;
		final float playerRad = 2f;
		final float playerHeight = 5f;

//        ghostPairCallback = new btGhostPairCallback();
//		sweep.getOverlappingPairCache().setInternalGhostPairCallback(ghostPairCallback);
        
        final Texture texture = new Texture(Gdx.files.internal("et/STONE.png"));
        bbt.disposables.add(texture);
        final Material material = new Material(TextureAttribute.createDiffuse(texture), ColorAttribute.createSpecular(1,1,1,1), FloatAttribute.createShininess(8f));
        final long attributes = Usage.Position | Usage.Normal | Usage.TextureCoordinates;
        playerModel = bbt.modelBuilder.createBox(playerRad, playerHeight, playerRad, material, attributes);
		bbt.disposables.add(playerModel);
        
		bbt.world.addConstructor("player", new BulletConstructor(playerModel, null));
		character = bbt.world.add("player", 2.5f, 10f, 5f);
		character.invisible = false;
		characterTransform = character.transform;
        characterTransform.rotate(Vector3.X, 90);

		ghostObject = new btPairCachingGhostObject();
		ghostObject.setWorldTransform(characterTransform);
		ghostShape = new btBoxShape(new Vector3(playerRad, playerHeight, playerRad));
		ghostObject.setCollisionShape(ghostShape);
		ghostObject.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT);
		(characterController = new btKinematicCharacterController(ghostObject, ghostShape, .35f, Vector3.Y)).setJumpSpeed(12);
        
	}
    
    public Player
    (BaseBulletTest b,
    ObjectController objController,
    Model p,
    btConvexShape g) {
        this.bbt = b;
        this.objController = objController;
        
        playerModel = p;
        bbt.disposables.add(playerModel);

        bbt.world.addConstructor("player", new BulletConstructor(playerModel, null));
        character = bbt.world.add("player", 2.5f, 10f, 5f);
        character.invisible = false;
        characterTransform = character.transform;

        ghostObject = new btPairCachingGhostObject();
        ghostObject.setWorldTransform(characterTransform);
        ghostShape = g;
        ghostObject.setCollisionShape(ghostShape);
        ghostObject.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT);
        (characterController = new btKinematicCharacterController(ghostObject, ghostShape, .35f, Vector3.Y)).setJumpSpeed(12);

	}

	public void update() {

		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			characterController.jump();
		}

		characterTransform.rotate(0, 1, 0, -Gdx.input.getDeltaX() * xScalar);
		ghostObject.setWorldTransform(characterTransform);

		characterDirection.set(-1, 0, 0).rot(characterTransform).nor();
		
		walkDirection.set(0, 0, 0);
		if (Gdx.input.isKeyPressed(Keys.W) | objController.frontPressed)
			walk(FORWARDS);

		if (Gdx.input.isKeyPressed(Keys.S) | objController.backPressed)
			walk(BACKWARDS);

		if (Gdx.input.isKeyPressed(Keys.A) | objController.leftPressed)
			walk(LEFT);

		if (Gdx.input.isKeyPressed(Keys.D) | objController.rightPressed)
			walk(RIGHT);

        walkDirection.scl((Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) ? 16.7f : 13.2f) * Gdx.graphics.getDeltaTime());

		characterController.setWalkDirection(walkDirection);

		character.modelInstance.transform.getTranslation(bbt.camera.position);

	}

	public void walk(DIRECTION direction) {
		switch (direction) {
            case FORWARDS:
                walkDirection.add(characterDirection);
                break;
            case BACKWARDS:
                walkDirection.add(characterDirection.cpy().scl(-1));
                break;
            case LEFT:
                walkDirection.add(rotVec3(characterDirection.cpy()));
                break;
            case RIGHT:
                walkDirection.add(rotVec3(characterDirection.cpy()).scl(-1));
                break;
            default: break;
		}
	}

    static Vector3 rotVec3(Vector3 in) {
        return new Vector3(in.z, in.y, -in.x);
	}

	private enum DIRECTION {
		FORWARDS, BACKWARDS, LEFT, RIGHT
        }

	DIRECTION FORWARDS  = DIRECTION.FORWARDS;
	DIRECTION BACKWARDS = DIRECTION.BACKWARDS;
	DIRECTION LEFT      = DIRECTION.LEFT;
	DIRECTION RIGHT     = DIRECTION.RIGHT;
}
