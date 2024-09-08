package org.noble.helium.subsystems.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import org.noble.helium.helpers.Dimensions2;

public class UIRectangle extends Rectangle {
  private final ShapeRenderer.ShapeType m_type;
  private final Color m_color;

  public UIRectangle(float x, float y, Dimensions2 dimensions, Color color, ShapeRenderer.ShapeType type) {
    super(x, y, dimensions.getWidth(), dimensions.getHeight());
    m_color = color;
    m_type = type;
  }

  public ShapeRenderer.ShapeType getType() {
    return m_type;
  }

  public Color getColor() {
    return m_color;
  }
}
