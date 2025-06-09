package org.noble.helium.subsystems.ui;

import org.noble.helium.Helium;
import org.noble.helium.actors.PlayerController;

public class UpdatableValue {
  private final PlayerController m_player;
  private final Helium m_game;
  private final ValueType m_type;
  private float m_value;

  public UpdatableValue(ValueType type) {
    //TODO: Make a UpdateableValue that returns a String
    m_player = PlayerController.getInstance();
    m_game = Helium.getInstance();
    m_type = type;
    update();
  }

  private void update() {
    switch(m_type) {
      case GAME_FPS -> m_value = m_game.getFPS();
      case GAME_FRAMETIME -> m_value = m_game.getDelta();
      case GAME_STATE -> m_value = m_game.getStatus().ordinal();
      case PLAYER_HEALTH -> m_value = m_player.getHealth();
      case PLAYER_SPEED -> m_value = m_player.getSpeed();
      case PLAYER_POSITION_X -> m_value = m_player.getPosition().x;
      case PLAYER_POSITION_Y -> m_value = m_player.getPosition().y;
      case PLAYER_POSITION_Z -> m_value = m_player.getPosition().z;
    }
  }

  public float getValue() {
    update();
    return m_value;
  }
}
