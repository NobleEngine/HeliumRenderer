package org.noble.helium.handling;

import games.rednblack.miniaudio.MASound;
import games.rednblack.miniaudio.MiniAudio;
import org.noble.helium.HeliumIO;

import java.util.ArrayList;
import java.util.HashMap;

public class AudioHandler {
  private static AudioHandler m_instance;
  private final MiniAudio miniAudio;
  private final HashMap<String, MASound> m_sounds;
  private final ArrayList<MASound> m_queuedSounds;

  private AudioHandler() {
    miniAudio = new MiniAudio();
    m_sounds = new HashMap<>();
    m_queuedSounds = new ArrayList<>();
  }

  public static AudioHandler getInstance() {
    if(m_instance == null) {
      m_instance = new AudioHandler();
    }
    return m_instance;
  }

  private void loadSound(String soundName) {
    if (m_sounds.get(soundName) == null) {
      HeliumIO.println("Sound Handler","Loading sound: " + soundName);
      m_sounds.put(soundName, miniAudio.createSound(soundName));
    }
  }

  public MASound getSound(String soundName) {
    loadSound(soundName);
    return m_sounds.get(soundName);
  }

  public void queueSound(String soundName) {
    m_queuedSounds.add(getSound(soundName));
  }

  public void update() {
    if(!m_queuedSounds.isEmpty()) {
      MASound sound = m_queuedSounds.get(0);
      if(!sound.isPlaying()) {
        sound.play();
      }
      if(sound.isEnd()) {
        sound.stop();
        m_queuedSounds.remove(0);
      }
    }
  }
}
