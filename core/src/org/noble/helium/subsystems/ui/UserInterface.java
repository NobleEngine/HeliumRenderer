package org.noble.helium.subsystems.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisWindow;
import org.noble.helium.Helium;
import org.noble.helium.subsystems.Subsystem;
import org.noble.helium.subsystems.ui.elements.HeliumLabel;

public class UserInterface extends Subsystem {
  private static UserInterface m_instance;
  private final Stage m_stage;
  private UserInterface() {
    VisUI.load();
    m_stage = new Stage(new ScreenViewport());
    Gdx.input.setInputProcessor(m_stage);

    HeliumLabel label = new HeliumLabel(new UpdatableValue(ValueType.PLAYER_HEALTH));
    label.setPosition(10, 10);
    label.setWidth(100);
    label.setHeight(100);
    addActor(label);
  }

  public static UserInterface getInstance() {
    if(m_instance == null) {
      m_instance = new UserInterface();
    }
    return m_instance;
  }

  public void addActor(Actor actor) {
    m_stage.addActor(actor);
  }

  @Override
  public void update() {
    m_stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    m_stage.act(Helium.getInstance().getDelta());
    m_stage.draw();
  }

  @Override
  public void dispose() {

  }
}
