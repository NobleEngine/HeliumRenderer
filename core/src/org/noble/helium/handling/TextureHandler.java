package org.noble.helium.handling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.noble.helium.subsystems.telemetry.HeliumTelemetry;

import java.util.HashMap;
import java.util.Map;

public class TextureHandler {
  private static TextureHandler m_instance;
  private final HashMap<String, Texture> m_textures;

  private TextureHandler() {
    m_textures = new HashMap<>();
    HeliumTelemetry.getInstance().println("Texture handler initialized");
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

  public void clear() {
    for (Map.Entry<String, Texture> entry : m_textures.entrySet()) {
      entry.getValue().dispose();
    }
    m_textures.clear();
  }
}
