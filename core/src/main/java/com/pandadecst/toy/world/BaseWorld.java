/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.pandadecst.toy.world;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.pandadecst.toy.world.BulletEntity;

/** @author xoppa No physics, simple base class for rendering a bunch of entities. */
public class BaseWorld<T extends BaseEntity> implements Disposable {
	public static abstract class Constructor<T extends BaseEntity> implements Disposable {
		public Model model = null;

		public abstract T construct(final float x, final float y, final float z);

		public abstract T construct(final Matrix4 transform);
	}

	private final ObjectMap<String, Constructor<T>> constructors = new ObjectMap<String, Constructor<T>>();
	public final Array<T> entities = new Array<T>();
	private final Array<Model> models = new Array<Model>();

	public void addConstructor(final String name, final Constructor<T> constructor) {
        if (constructors.get(name) != null){
            constructors.remove(name);
        }
		constructors.put(name, constructor);
		if (constructor.model != null && !models.contains(constructor.model, true)) models.add(constructor.model);
	}

	public Constructor<T> getConstructor(final String name) {
		return constructors.get(name);
	}

	public void add(final T entity) {
		entities.add(entity);
	}

	public T add(final String type, float x, float y, float z) {
        if (constructors.get(type) != null) {
            final T entity = constructors.get(type).construct(x, y, z);
            add(entity);
            return entity;}
        return null;
	}

	public T add(final String type, final Matrix4 transform) {
        if (constructors.get(type) != null) {
            final T entity = constructors.get(type).construct(transform);
            add(entity);
            return entity;}
        return null;
	}

	public void render(final ModelBatch batch, final Environment lights) {
		render(batch, lights, entities);
	}

	public void render(final ModelBatch batch, final Environment lights, final Iterable<T> entities) {
        for (final T e : entities) {
            if (!e.invisible)
                batch.render(e.modelInstance, lights);
		}
	}

	public void render(final ModelBatch batch, final Environment lights, final T entity) {
		batch.render(entity.modelInstance, lights);
	}

	public void update() {
	}
    
    public void delete(T e){
        entities.removeValue(e, true);
    }

	@Override
	public void dispose() {
		for (int i = 0; i < entities.size; i++)
			entities.get(i).dispose();
		entities.clear();

		for (Constructor<T> constructor : constructors.values().toArray())
			constructor.dispose();
		constructors.clear();

		models.clear();
	}
}
