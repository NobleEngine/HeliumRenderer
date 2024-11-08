package org.noble.helium.handling;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import org.noble.helium.subsystems.telemetry.HeliumTelemetry;

import java.util.HashMap;

public class SoundHandler {
  private static SoundHandler m_instance;
  private final HashMap<String, Sound> m_soundList;

  private SoundHandler() {
    m_soundList = new HashMap<>();
    HeliumTelemetry.getInstance().println("Sound handler initialized");
  }

  public static SoundHandler getInstance() {
    if(m_instance == null) {
      m_instance = new SoundHandler();
    }
    return m_instance;
  }

  public void addSound(String name, String fileName) {
    if(m_soundList.get(name) == null) {
      m_soundList.put(name, Gdx.audio.newSound(Gdx.files.internal(fileName)));
    }
  }

  public Sound getSound(String name) {
    return m_soundList.get(name);
  }
}
