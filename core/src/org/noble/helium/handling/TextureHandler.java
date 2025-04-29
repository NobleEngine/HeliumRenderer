package org.noble.helium.handling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import org.noble.helium.PrintUtils;

import java.util.HashMap;
import java.util.Map;

public class TextureHandler {
  private static TextureHandler m_instance;
  private final HashMap<String, Texture> m_textures;

  private TextureHandler() {
    m_textures = new HashMap<>();
    PrintUtils.println("Texture Handler","Texture handler initialized");
  }

  public static TextureHandler getInstance() {
    if (m_instance == null) {
      m_instance = new TextureHandler();
    }
    return m_instance;
  }

  private void loadTexture(String textureName) {
    if (m_textures.get(textureName) == null) {
      m_textures.put(textureName, new Texture(Gdx.files.internal(textureName)));
    }
  }

  public Texture getTexture(String textureName) {
    loadTexture(textureName);
    return m_textures.get(textureName);
  }

  public Texture getTexture(Color color) {
    Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
    pixmap.setColor(color);
    pixmap.fill();
    Texture texture = new Texture(pixmap);
    pixmap.dispose();
    return texture;
  }

  public void clear() {
    for (Map.Entry<String, Texture> entry : m_textures.entrySet()) {
      entry.getValue().dispose();
    }
    m_textures.clear();
  }
}
