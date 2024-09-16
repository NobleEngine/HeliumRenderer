package org.noble.helium.subsystems.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import org.noble.helium.handling.TextureHandler;
import org.noble.helium.math.Dimensions2;

public class UISprite {
  private final Vector2 m_position;
  private final Dimensions2 m_size;
  private final String m_textureName;

  public UISprite(String textureName, float x, float y, Dimensions2 dimensions) {
    m_textureName = textureName;
    m_position = new Vector2(x, y);
    m_size = dimensions;
  }

  public Texture getTexture() {
    return TextureHandler.getInstance().getTexture(m_textureName);
  }

  public float getX() {
    return m_position.x;
  }

  public float getY() {
    return m_position.y;
  }

  public float getWidth() {
    return m_size.getWidth();
  }

  public float getHeight() {
    return m_size.getHeight();
  }
}
