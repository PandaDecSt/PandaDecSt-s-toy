package com.pandadecst.toy.test;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.bullet.collision.CustomCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.pandadecst.toy.physics.base.BaseBulletTest;
import com.pandadecst.toy.world.BulletWorld;
import com.badlogic.gdx.Gdx;
import com.pandadecst.toy.world.BulletEntity;

/** @author xoppa */
public class CollisionWorldTest extends BaseBulletTest {
    public class MyCollisionDispatcher extends CustomCollisionDispatcher {
        public MyCollisionDispatcher (btCollisionConfiguration collisionConfiguration) {
            super(collisionConfiguration);
        }

        @Override
        public boolean needsCollision (btCollisionObject body0, btCollisionObject body1) {
            if(body0.userData instanceof BulletEntity)
                if(!body0.isStaticObject())
                world.delete((BulletEntity)body0.userData);
            if(body1.userData instanceof BulletEntity)
                if(!body1.isStaticObject())
                world.delete((BulletEntity)body1.userData);
            if (body0.getUserValue() % 2 == 0 || body1.getUserValue() % 2 == 0) return super.needsCollision(body0, body1);
            return false;
        }

        @Override
        public boolean needsResponse (btCollisionObject body0, btCollisionObject body1) {
            if(body0.userData instanceof BulletEntity)
                if(!body0.isStaticObject())
                    world.delete((BulletEntity)body0.userData);
            if(body1.userData instanceof BulletEntity)
                if(!body1.isStaticObject())
                    world.delete((BulletEntity)body1.userData);
            if (body0.getUserValue() % 2 == 0 || body1.getUserValue() % 2 == 0) return super.needsCollision(body0, body1);
            return false;
        }
    }

    @Override
    public BulletWorld createWorld () {
        btDefaultCollisionConfiguration collisionConfiguration = new btDefaultCollisionConfiguration();
        MyCollisionDispatcher dispatcher = new MyCollisionDispatcher(collisionConfiguration);
        btDbvtBroadphase broadphase = new btDbvtBroadphase();
        btSequentialImpulseConstraintSolver solver = new btSequentialImpulseConstraintSolver();
        btDiscreteDynamicsWorld collisionWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
        return new BulletWorld(collisionConfiguration, dispatcher, broadphase, solver, collisionWorld);
    }

    @Override
    public void show () {
        super.show();

        // Create the entities
        world.add("ground", 0f, 0f, 0f).setColor(0.25f + 0.5f * (float)Math.random(), 0.25f + 0.5f * (float)Math.random(),
                                                 0.25f + 0.5f * (float)Math.random(), 1f);
        for (float x = -5f; x <= 5f; x += 2f) {
            for (float y = 5f; y <= 15f; y += 2f) {
                world.add("box", x + 0.1f * MathUtils.random(), y + 0.1f * MathUtils.random(), 0.1f * MathUtils.random()).body
                    .setUserValue((int)((x + 5f) / 2f + .5f));
            }
        }
	}
    
    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        shootNew(x, y, 20f);
        return super.touchDown(x, y, pointer, button);
    }
    
}

