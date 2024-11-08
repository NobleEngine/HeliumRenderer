package org.noble.helium.subsystems.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import org.noble.helium.handling.TextureHandler;
import org.noble.helium.math.Dimensions2;
import org.noble.helium.subsystems.Subsystem;
import org.noble.helium.subsystems.telemetry.HeliumTelemetry;

import java.util.HashMap;

public class UserInterface extends Subsystem {
  private static UserInterface m_instance = null;
  private final HashMap<String, VisLabel> m_labels;
  private final HashMap<String, UISprite> m_sprites;
  private final HashMap<String, UIRectangle> m_rectangles;
  private final ShapeRenderer m_shapeRenderer;
  private final Stage m_2dStage;
  private final TextureHandler m_textureHandler;

  private UserInterface() {
    VisUI.load();
    m_textureHandler = TextureHandler.getInstance();
    m_labels = new HashMap<>();
    m_rectangles = new HashMap<>();
    m_sprites = new HashMap<>();
    m_2dStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
    m_shapeRenderer = new ShapeRenderer();
    HeliumTelemetry.getInstance().println("User Interface subsystem initialized");
  }

  public static UserInterface getInstance() {
    if (m_instance == null) {
      m_instance = new UserInterface();
    }
    return m_instance;
  }

  public void addLabel(String labelName, String text, float x, float y, float width, float height, Color color) {
    VisLabel label = new VisLabel();
    label.setText(text);
    label.setPosition(x, y);
    label.setSize(width, height);
    label.setColor(color);

    m_labels.put(labelName, label);
    m_2dStage.addActor(label);
  }

  public void addRect(String rectName, UIRectangle rect) {
    m_rectangles.put(rectName, rect);
  }

  public void addSprite(String spriteName, UISprite sprite) {
    m_sprites.put(spriteName, sprite);
  }

  public UISprite getSprite(String spriteName) {
    return m_sprites.get(spriteName);
  }

  public void setSprite(String spriteName, UISprite sprite) {
    m_sprites.replace(spriteName, sprite);
  }

  public VisLabel getLabel(String labelName) {
    return m_labels.get(labelName);
  }

  public void setLabel(String labelName, String text, float x, float y, Dimensions2 dimensions, Color color) {
    VisLabel label = getLabel(labelName);
    label.setText(text);
    label.setPosition(x, y);
    label.setSize(dimensions.getWidth(), dimensions.getHeight());
    label.setColor(color);
  }

  public UIRectangle getRectangle(String rectName) {
    return m_rectangles.get(rectName);
  }

  public void setRectangle(String rectName, UIRectangle rect) {
    m_rectangles.replace(rectName, rect);
  }

  public void clear() {
    m_2dStage.clear();
    for(VisLabel label : m_labels.values()) {
      label.clear();
    }
  }

  @Override
  public void update() {
    for(UIRectangle rect : m_rectangles.values()) {
      m_shapeRenderer.setColor(rect.getColor());
      m_shapeRenderer.begin(rect.getType());
      m_shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
      m_shapeRenderer.end();
    }

    Batch batch = m_2dStage.getBatch();
    for(UISprite sprite : m_sprites.values()) {
      batch.begin();
      batch.draw(sprite.getTexture(), sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
      batch.end();
    }

    m_2dStage.act();
    m_2dStage.draw();
  }

  @Override
  public void dispose() {
    m_2dStage.dispose();
  }
}
