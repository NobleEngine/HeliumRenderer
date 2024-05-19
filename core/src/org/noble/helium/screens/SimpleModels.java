package org.noble.helium.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import org.noble.helium.handling.SimpleModelHandler;
import org.noble.helium.helpers.Coordinates;
import org.noble.helium.helpers.Dimensions;

public class SimpleModels extends BaseScreen {
  public SimpleModels() {
    super();
    m_simpleModelHandler.addNewShape(
        "cube-01", SimpleModelHandler.Shape.CUBE, new Texture(Gdx.files.internal("textures/dirt.png")),
        new Coordinates(0f,0f,0f), new Dimensions(10f,100f,100f));
    m_simpleModelHandler.addNewShape(
        "cube-physical", SimpleModelHandler.Shape.CUBE, Color.BLUE,
        new Coordinates(5f, 50f, 5f), new Dimensions(5f, 5f, 5f));
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    m_batch.end();
  }
}
