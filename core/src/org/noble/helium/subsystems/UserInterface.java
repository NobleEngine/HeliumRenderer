package org.noble.helium.subsystems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;

import java.util.HashMap;

public class UserInterface extends Subsystem {
  private static UserInterface m_instance = null;
  private final HashMap<String, VisLabel> m_labels;
  private final Stage m_2dStage;

  private UserInterface() {
    VisUI.load();
    m_labels = new HashMap<>();
    m_2dStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
  }

  public static UserInterface getInstance() {
    if (m_instance == null) {
      m_instance = new UserInterface();
    }
    return m_instance;
  }

  public void addLabel(String labelName, String text, float x, float y, float width, float height) {
    VisLabel label = new VisLabel();
    label.setText(text);
    label.setPosition(x, y);
    label.setSize(width, height);

    m_labels.put(labelName, label);
    m_2dStage.addActor(label);
  }

  public void setLabel(String labelName, String text) {
    m_labels.get(labelName).setText(text);
  }

  public void clear() {
    m_2dStage.clear();
    for(VisLabel label : m_labels.values()) {
      label.clear();
    }
  }

  @Override
  public void update() {
    m_2dStage.act();
    m_2dStage.draw();
  }

  @Override
  public void dispose() {
    m_2dStage.dispose();
  }
}
