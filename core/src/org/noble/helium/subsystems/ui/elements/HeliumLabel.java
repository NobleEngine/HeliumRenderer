package org.noble.helium.subsystems.ui.elements;

import com.kotcrab.vis.ui.widget.VisLabel;
import org.noble.helium.subsystems.ui.UpdatableValue;

public class HeliumLabel extends VisLabel {
  UpdatableValue m_value = null;
  public HeliumLabel(UpdatableValue value) {
    super(Float.toString(value.getValue()));
    m_value = value;
  }

  public HeliumLabel(String text) {
    super(text);
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    if(m_value != null) {
      setText(Float.toString(m_value.getValue()));
    }
  }
}
