package com.pandadecst.toy.utils.geometry;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by macbury on 30.08.14.
 */
public class MeshVertexInfo implements Pool.Poolable {
  private static final float BASE_AO = 0.2f;
  public float specular;
  public float ao;
  public Vector3 position;
  public Vector3 normal;
  public Vector2 uv;
  public Color   color;
  public Material material;
  public short index;
  public float[] textureFullCords = new float[4];

  public static AttributeType[] voxelTypes() {
    return new AttributeType[] {
        AttributeType.Position,
        MeshVertexInfo.AttributeType.Normal,
        MeshVertexInfo.AttributeType.TextureCord,
        MeshVertexInfo.AttributeType.Material,
        MeshVertexInfo.AttributeType.TextureFullCords
    };
  }

  public static VertexAttribute[] voxelAttributes() {
    AttributeType[] types              = voxelTypes();
    VertexAttribute[] vertexAttributes = new VertexAttribute[types.length];
    for (int i = 0; i < types.length; i++) {
      vertexAttributes[i] = types[i].attribute;
    }
    return vertexAttributes;
  }

  public MeshVertexInfo emission(float emission) {
    this.material.setEmission(emission);
    return this;
  }

  public static enum AttributeType {
    Position(VertexAttributes.Usage.Position, 3,ShaderProgram.POSITION_ATTRIBUTE),
    Normal(VertexAttributes.Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE),
    Material(VertexAttributes.Usage.ColorPacked, 4, "a_material"),
    TextureCord(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0"),
    TextureFullCords(VertexAttributes.Usage.TextureCoordinates, 4, "a_textureFullCoords"),
    Color(VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE); // probably should be 4 not 1

    private final int attributeSize;
    private final VertexAttribute attribute;

    AttributeType(int usage, int attributeSize, String shaderAttributte) {
      this.attributeSize = attributeSize;
      this.attribute     = new VertexAttribute(usage, attributeSize, shaderAttributte);
    }

    public int size() {
      return attributeSize;
    }

    public VertexAttribute attribute() {
      return attribute;
    }
  }

  public MeshVertexInfo() {
    this.position = new Vector3();
    this.normal   = new Vector3();
    this.color    = new Color(Color.WHITE);
    this.ao       = 0.0f;
    this.specular = 0.0f;
    this.uv       = new Vector2();
    this.index    = 0;
    this.material = new Material();
    reset();
  }

  public MeshVertexInfo normal(float x, float y, float z) {
    this.normal.set(x,y,z);
    return this;
  }

  public MeshVertexInfo normal(Vector3 in) {
    this.normal.set(in);
    return this;
  }

  /**
   * Sets Normals to X-
   * @return
   */

  public MeshVertexInfo normalLeft() {
    this.normal.set(-1,0,0);
    return this;
  }

  /**
   * Sets Normals to X+
   * @return
   */

  public MeshVertexInfo normalRight() {
    this.normal.set(1,0,0);
    return this;
  }

  /**
   * Sets Normals to Y+
   * @return
   */
  public MeshVertexInfo normalUp() {
    this.normal.set(0,1,0);
    return this;
  }

  /**
   * Sets Normals to Y-
   * @return
   */
  public MeshVertexInfo normalBottom() {
    this.normal.set(0,-1,0);
    return this;
  }

  /**
   * Sets Normals to Z+
   * @return
   */
  public MeshVertexInfo normalFront() {
    this.normal.set(0,0,1);
    return this;
  }

  /**
   * Sets Normals to Z-
   * @return
   */
  public MeshVertexInfo normalBack() {
    this.normal.set(0,0,-1);
    return this;
  }

  public MeshVertexInfo uv(float u, float v) {
    this.uv.set(u,v);
    return this;
  }


  public MeshVertexInfo uv(TextureAtlas.AtlasRegion region) {
    uv(region.getU(), region.getV());
    return this;
  }


  public MeshVertexInfo u2v2(TextureAtlas.AtlasRegion region) {
    uv(region.getU2(), region.getV2());
    return this;
  }

  public MeshVertexInfo u2v(TextureAtlas.AtlasRegion region) {
    uv(region.getU2(), region.getV());
    return this;
  }

  public MeshVertexInfo uv2(TextureAtlas.AtlasRegion region) {
    uv(region.getU(), region.getV2());
    return this;
  }

  public MeshVertexInfo set(float x, float y, float z) {
    this.position.set(x,y,z);
    return this;
  }

  public MeshVertexInfo rgba(float r, float g, float b, float a) {
    this.color.set(r,g,b,a);
    return this;
  }

  public MeshVertexInfo rgb(float r, float g, float b) {
    rgba(r,g,b,1);
    return this;
  }

  public float color() {
    return Color.toFloatBits(color.r, color.g, color.b, color.a);
  }

  /**
   * Return material propeties as color
   * Red is ambient occulsion
   * Green is specular value
   * @return
   */
  public float material() {
    return Color.toFloatBits(material.r, material.g, material.b, material.a);
  }

  public MeshVertexInfo color(Color nc) {
    this.color.set(nc);
    return this;
  }

  public MeshVertexInfo applyAoShade() {
    this.ao += BASE_AO;
    this.ao = MathUtils.clamp(ao, 0.0f, 1.0f);
    material.setAO(ao);
    return this;
  }

  public MeshVertexInfo textureFullCords(float u, float v, float u2, float v2) {
    this.textureFullCords[0] = u;
    this.textureFullCords[1] = v;
    this.textureFullCords[2] = u2;
    this.textureFullCords[3] = v2;
    return this;
  }

  public MeshVertexInfo ao(float incrBy) {
    this.ao += incrBy;
    this.ao = MathUtils.clamp(ao, 0.0f, 1.0f);
    material.setAO(ao);
    return this;
  }

  public MeshVertexInfo transparent(boolean transparent) {
    material.setTransparent(transparent);
    return this;
  }

  @Override
  public void reset() {
    uv(0,0);
    set(0, 0, 0);
    normal(0, 0, 0);
    this.textureFullCords[0] = 0;
    this.textureFullCords[1] = 0;
    this.textureFullCords[2] = 0;
    this.textureFullCords[3] = 0;
    ao = 0;
    index = 0;
    material.reset();
  }
}
