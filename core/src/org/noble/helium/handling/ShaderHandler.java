package org.noble.helium.handling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import org.noble.helium.subsystems.telemetry.HeliumTelemetry;

import java.util.HashMap;

public class ShaderHandler implements ShaderProvider {
  private static ShaderHandler m_instance;
  private final HashMap<String, ShaderProgram> m_shaders;
  private ShaderProgram m_activeShader;

  private ShaderHandler() {
    m_shaders = new HashMap<>();

    add("test", new ShaderProgram(Gdx.files.internal("assets/shaders/OilPainting/oilPainting.frag"), Gdx.files.internal("assets/shaders/OilPainting/oilPainting.vert")));
  }

  public static ShaderHandler getInstance() {
    if (m_instance == null) {
      m_instance = new ShaderHandler();
    }
    return m_instance;
  }

  public void add(String name, ShaderProgram shader) {
    m_shaders.put(name, shader);
    if(!shader.isCompiled()) {
      HeliumTelemetry.getInstance().printErrorln("Could not compile shader " + name);
      HeliumTelemetry.getInstance().printErrorln(shader.getLog());
      System.exit(1);
      return;
    }
    if(m_activeShader == null) {
      m_activeShader = shader;
    }
  }

  public void setActiveShader(String name) {
    m_activeShader = m_shaders.get(name);
  }

  @Override
  public Shader getShader(Renderable renderable) {
    if(m_activeShader == null) {
      return new DefaultShader(renderable);
    }
    return new DefaultShader(renderable, new DefaultShader.Config(), m_activeShader);
  }

  @Override
  public void dispose() {

  }
}
