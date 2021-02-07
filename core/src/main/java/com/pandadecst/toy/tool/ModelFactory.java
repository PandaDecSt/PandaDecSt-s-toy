package com.pandadecst.toy.tool;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ModelFactory {
    
    float[] v = {
        5,0,5
        ,-5,0,5
        ,-5,0,-5
        ,5,0,-5
        };

//    public void cutmodel() {
//        Model r = createModelbyVertexs(v, new Material(ColorAttribute.createDiffuse(Color.WHITE), ColorAttribute.createSpecular(Color.WHITE)), Usage.Position | Usage.Normal);
//    }
    
    public Model mesh2model(Mesh m1,Material m) {
        begin();
        part("m2m", m1, m);
        return end();
    }
    
    public Model meshes2model(Material m, Mesh... ms) {
        begin();
        for (Mesh m0: ms){
            part("m2m", m0, m);
        }
        return end();
    }

    Model model;
    Node node;
    Array<MeshBuilder> builders = new Array<MeshBuilder>();

    private Matrix4 tmpTransform = new Matrix4();

    private MeshBuilder getBuilder(final VertexAttributes attributes) {
        for (final MeshBuilder mb : builders)
            if (mb.getAttributes().equals(attributes) && mb.lastIndex() < Short.MAX_VALUE / 2) return mb;
        final MeshBuilder result = new MeshBuilder();
        result.begin(attributes);
        builders.add(result);
        return result;
    }

    //预处理
    public void begin() {
        if (model != null) throw new GdxRuntimeException("Call end() first");
        node = null;
        model = new Model();
        builders.clear();
    }

    //结束模型创建return创建好的模型
    public Model end() {
        if (model == null) throw new GdxRuntimeException("Call begin() first");
        final Model result = model;
        endnode();
        model = null;

        for (final MeshBuilder mb : builders)
            mb.end();
        builders.clear();

        rebuildReferences(result);
        return result;
    }

    private void endnode() {
        if (node != null) {
            node = null;
        }
    }

    //添加node
    protected Node node(final Node node) {
        if (model == null) throw new GdxRuntimeException("Call begin() first");

        endnode();

        model.nodes.add(node);
        this.node = node;

        return node;
    }

    public Node node() {
        final Node node = new Node();
        node(node);
        node.id = "node" + model.nodes.size;
        return node;
    }

    public Node node(final String id, final Model model) {
        final Node node = new Node();
        node.id = id;
        node.addChildren(model.nodes);
        node(node);
        for (final Disposable disposable : model.getManagedDisposables())
            manage(disposable);
        return node;
    }

    public void manage(final Disposable disposable) {
        if (model == null) throw new GdxRuntimeException("Call begin() first");
        model.manageDisposable(disposable);
    }

    //添加mesh到当前node
    public void part(final MeshPart meshpart, final Material material) {
        if (node == null) node();
        node.parts.add(new NodePart(meshpart, material));
    }

    public MeshPart part(final String id, final Mesh mesh, int primitiveType, int offset, int size, final Material material) {
        final MeshPart meshPart = new MeshPart();
        meshPart.id = id;
        meshPart.primitiveType = primitiveType;
        meshPart.mesh = mesh;
        meshPart.offset = offset;
        meshPart.size = size;
        part(meshPart, material);
        return meshPart;
    }

    public MeshPart part(final String id, final Mesh mesh, int primitiveType, final Material material) {
        return part(id, mesh, primitiveType, 0, mesh.getNumIndices(), material);
    }
    
    public MeshPart part(final String id, final Mesh mesh, final Material material) {
        return part(id, mesh, GL20.GL_TRIANGLES, 0, mesh.getNumIndices(), material);
    }

    //创建一个新mesh到当前node
    public MeshPartBuilder part(final String id, int primitiveType, final VertexAttributes attributes, final Material material) {
        final MeshBuilder builder = getBuilder(attributes);
        part(builder.part(id, primitiveType), material);
        return builder;
    }
    
    public MeshPartBuilder part(final String id, final VertexAttributes attributes, final Material material) {
        final MeshBuilder builder = getBuilder(attributes);
        part(builder.part(id, GL20.GL_TRIANGLES), material);
        return builder;
    }

    public MeshPartBuilder part(final String id, int primitiveType, final long attributes, final Material material) {
        return part(id, primitiveType, MeshBuilder.createAttributes(attributes), material);
    }
    
    public Model createModelbyVertexs(final float... values, int primitiveType, final Material material,
                           final long attributes) {
        begin();
        part("vertexs", primitiveType, attributes, material).vertex(values);
        return end();
    }
    
    public Model createModelbyVertexs(final float... values, final Material material,
                                      final long attributes) {
        return createModelbyVertexs(values, GL20.GL_TRIANGLES, material, attributes);
    }

    public Model createBox(float width, float height, float depth, final Material material, final long attributes) {
        return createBox(width, height, depth, GL20.GL_TRIANGLES, material, attributes);
    }

    public Model createBox(float width, float height, float depth, int primitiveType, final Material material,
                           final long attributes) {
        begin();
        part("box", primitiveType, attributes, material).box(width, height, depth);
        return end();
    }

    public Model createRect(float x00, float y00, float z00, float x10, float y10, float z10, float x11, float y11, float z11,
                            float x01, float y01, float z01, float normalX, float normalY, float normalZ, final Material material, final long attributes) {
        return createRect(x00, y00, z00, x10, y10, z10, x11, y11, z11, x01, y01, z01, normalX, normalY, normalZ, GL20.GL_TRIANGLES,
                          material, attributes);
    }

    public Model createRect(float x00, float y00, float z00, float x10, float y10, float z10, float x11, float y11, float z11,
                            float x01, float y01, float z01, float normalX, float normalY, float normalZ, int primitiveType, final Material material,
                            final long attributes) {
        begin();
        part("rect", primitiveType, attributes, material).rect(x00, y00, z00, x10, y10, z10, x11, y11, z11, x01, y01, z01, normalX,
                                                               normalY, normalZ);
        return end();
    }

    //对模型中mesh meshpart的引用重置为*它是node,这将使模型负责处理所有引用的mesh
    public static void rebuildReferences(final Model model) {
        model.materials.clear();
        model.meshes.clear();
        model.meshParts.clear();
        for (final Node node : model.nodes)
            rebuildReferences(model, node);
    }

    private static void rebuildReferences(final Model model, final Node node) {
        for (final NodePart mpm : node.parts) {
            if (!model.materials.contains(mpm.material, true)) model.materials.add(mpm.material);
            if (!model.meshParts.contains(mpm.meshPart, true)) {
                model.meshParts.add(mpm.meshPart);
                if (!model.meshes.contains(mpm.meshPart.mesh, true)) model.meshes.add(mpm.meshPart.mesh);
                model.manageDisposable(mpm.meshPart.mesh);
            }
        }
        for (final Node child : node.getChildren())
            rebuildReferences(model, child);
    }

    //创建一个为xyz三个正交向量的形状的model
    public Model createXYZCoordinates(float axisLength, float capLength, float stemThickness, int divisions, int primitiveType,
                                      Material material, long attributes) {
        begin();
        MeshPartBuilder partBuilder;
        Node node = node();

        partBuilder = part("xyz", primitiveType, attributes, material);
        partBuilder.setColor(Color.RED);
        partBuilder.arrow(0, 0, 0, axisLength, 0, 0, capLength, stemThickness, divisions);
        partBuilder.setColor(Color.GREEN);
        partBuilder.arrow(0, 0, 0, 0, axisLength, 0, capLength, stemThickness, divisions);
        partBuilder.setColor(Color.BLUE);
        partBuilder.arrow(0, 0, 0, 0, 0, axisLength, capLength, stemThickness, divisions);

        return end();
    }

    public Model createXYZCoordinates(float axisLength, Material material, long attributes) {
        return createXYZCoordinates(axisLength, 0.1f, 0.1f, 5, GL20.GL_TRIANGLES, material, attributes);
    }

    //创建arrow模型
    public Model createArrow(float x1, float y1, float z1, float x2, float y2, float z2, float capLength, float stemThickness,
                             int divisions, int primitiveType, Material material, long attributes) {
        begin();
        part("arrow", primitiveType, attributes, material).arrow(x1, y1, z1, x2, y2, z2, capLength, stemThickness, divisions);
        return end();
    }

    public Model createArrow(Vector3 from, Vector3 to, Material material, long attributes) {
        return createArrow(from.x, from.y, from.z, to.x, to.y, to.z, 0.1f, 0.1f, 5, GL20.GL_TRIANGLES, material, attributes);
    }

    /** Convenience method to create a model which represents a grid of lines on the XZ plane. The resources the Material might
     * contain are not managed, use {@link Model#manageDisposable(Disposable)} to add those to the model.
     * @param xDivisions row count along x axis.
     * @param zDivisions row count along z axis.
     * @param xSize Length of a single row on x.
     * @param zSize Length of a single row on z. */
    public Model createLineGrid(int xDivisions, int zDivisions, float xSize, float zSize, Material material, long attributes) {
        begin();
        MeshPartBuilder partBuilder = part("lines", GL20.GL_LINES, attributes, material);
        float xlength = xDivisions * xSize, zlength = zDivisions * zSize, hxlength = xlength / 2, hzlength = zlength / 2;
        float x1 = -hxlength, y1 = 0, z1 = hzlength, x2 = -hxlength, y2 = 0, z2 = -hzlength;
        for (int i = 0; i <= xDivisions; ++i) {
            partBuilder.line(x1, y1, z1, x2, y2, z2);
            x1 += xSize;
            x2 += xSize;
        }

        x1 = -hxlength;
        y1 = 0;
        z1 = -hzlength;
        x2 = hxlength;
        y2 = 0;
        z2 = -hzlength;
        for (int j = 0; j <= zDivisions; ++j) {
            partBuilder.line(x1, y1, z1, x2, y2, z2);
            z1 += zSize;
            z2 += zSize;
        }

        return end();
	}

}
