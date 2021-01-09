package com.pandadecst.toy.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBvhTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.pandadecst.toy.physics.base.BaseBulletTest;
import com.pandadecst.toy.tool.BulletConstructor;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

/** @author xoppa */
public class MeshShapeTest extends BaseBulletTest {

    @Override
    public void show() {
        super.show();

        final Model sphereModel = modelBuilder.createSphere(0.5f, 0.5f, 0.5f, 8, 8,
                                                            new Material(ColorAttribute.createDiffuse(Color.WHITE), ColorAttribute.createSpecular(Color.WHITE)), Usage.Position
                                                            | Usage.Normal);
        disposables.add(sphereModel);
        final BulletConstructor sphereConstructor = new BulletConstructor(sphereModel, 0.25f, new btSphereShape(0.25f));
        sphereConstructor.bodyInfo.setRestitution(1f);
        world.addConstructor("sphere", sphereConstructor);

        final Model sceneModel = objLoader.loadModel(Gdx.files.internal("data/scene.obj"));
        disposables.add(sceneModel);
        final BulletConstructor sceneConstructor = new BulletConstructor(sceneModel, 0f, new btBvhTriangleMeshShape(
                                                                             sceneModel.meshParts));
        sceneConstructor.bodyInfo.setRestitution(0.25f);
        world.addConstructor("scene", sceneConstructor);

        world.add("scene", (new Matrix4()).setToTranslation(0f, 2f, 0f).rotate(Vector3.Y, -90)).setColor(
            0.25f + 0.5f * (float)Math.random(), 0.25f + 0.5f * (float)Math.random(), 0.25f + 0.5f * (float)Math.random(), 1f);

        world.add("ground", 0f, 0f, 0f).setColor(0.25f + 0.5f * (float)Math.random(), 0.25f + 0.5f * (float)Math.random(),
                                                 0.25f + 0.5f * (float)Math.random(), 1f);

        for (float x = -3; x < 7; x++) {
            for (float z = -5; z < 5; z++) {
                world.add("sphere", x, 10f + (float)Math.random() * 0.1f, z).setColor(0.5f + 0.5f * (float)Math.random(),
                                                                                      0.5f + 0.5f * (float)Math.random(), 0.5f + 0.5f * (float)Math.random(), 1f);
            }
        }
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        shootNew(x, y, 20f);
        return super.touchDown(x, y, pointer, button);
    }


}
