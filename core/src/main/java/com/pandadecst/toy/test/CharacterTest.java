package com.pandadecst.toy.test;

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
import com.badlogic.gdx.physics.bullet.collision.btAxisSweep3;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseProxy;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btConvexShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btGhostPairCallback;
import com.badlogic.gdx.physics.bullet.collision.btPairCachingGhostObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btKinematicCharacterController;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.pandadecst.toy.*;
import com.pandadecst.toy.physics.base.*;
import com.pandadecst.toy.world.*;
import com.pandadecst.toy.tool.*;

public class CharacterTest extends BaseBulletTest {
	final int BOXCOUNT_X = 5;
	final int BOXCOUNT_Y = 5;
	final int BOXCOUNT_Z = 1;

	final float BOXOFFSET_X = -2.5f;
	final float BOXOFFSET_Y = 0.5f;
	final float BOXOFFSET_Z = 0f;

	BulletEntity ground;
    Player player;
    
    btGhostPairCallback ghostPairCallback;

	@Override
	public BulletWorld createWorld() {
		// We create the world using an axis sweep broadphase for this test
		btDefaultCollisionConfiguration collisionConfiguration = new btDefaultCollisionConfiguration();
		btCollisionDispatcher dispatcher = new btCollisionDispatcher(collisionConfiguration);
		btAxisSweep3 sweep = new btAxisSweep3(new Vector3(-1000, -1000, -1000), new Vector3(1000, 1000, 1000));
		btSequentialImpulseConstraintSolver solver = new btSequentialImpulseConstraintSolver();
		btDiscreteDynamicsWorld collisionWorld = new btDiscreteDynamicsWorld(dispatcher, sweep, solver, collisionConfiguration);
        ghostPairCallback = new btGhostPairCallback();
		sweep.getOverlappingPairCache().setInternalGhostPairCallback(ghostPairCallback);
		return new BulletWorld(collisionConfiguration, dispatcher, sweep, solver, collisionWorld);
	}

	@Override
	public void show() {
		super.show();
        //ModelFactory.cutmodel();
        player = new Player(this, objController);
        world.collisionWorld.addCollisionObject(player.ghostObject,
                                                (short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter,                                         (short) (btBroadphaseProxy.CollisionFilterGroups.StaticFilter
                                                | btBroadphaseProxy.CollisionFilterGroups.DefaultFilter));
        ((btDiscreteDynamicsWorld) (world.collisionWorld)).addAction(player.characterController);
		// Add the ground
		(ground = world.add("ground", 0f, -5f, 0f))
			.setColor(0.25f + 0.5f * (float)Math.random(), 0.25f + 0.5f * (float)Math.random(), 0.25f + 0.5f * (float)Math.random(), 1f);
		// Create some boxes to play with
		for (int x = 0; x < BOXCOUNT_X; x++) {
			for (int y = 0; y < BOXCOUNT_Y; y++) {
				for (int z = 0; z < BOXCOUNT_Z; z++) {
					world.add("box", BOXOFFSET_X + x, BOXOFFSET_Y + y, BOXOFFSET_Z + z)
						.setColor(0.5f + 0.5f * (float)Math.random(), 0.5f + 0.5f * (float)Math.random(), 0.5f + 0.5f * (float)Math.random(), 1f);
				}
			}
		}
	}

	@Override
	public void update() {
        player.update();
		super.update();
        player.ghostObject.getWorldTransform(player.characterTransform);
	}

	@Override
	protected void renderWorld() {
		// TODO Auto-generated method stub
		super.renderWorld();
	}

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        shootNew(x, y, 20f);
        return super.touchDown(x, y, pointer, button);
    }

	@Override
	public void dispose() {
		((btDiscreteDynamicsWorld)(world.collisionWorld)).removeAction(player.characterController);
		world.collisionWorld.removeCollisionObject(player.ghostObject);
		super.dispose();
		player.characterController.dispose();
        player.ghostObject.dispose();
		player.ghostShape.dispose();
		ground = null;
	}
}
