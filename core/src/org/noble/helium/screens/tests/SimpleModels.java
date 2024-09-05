package org.noble.helium.screens.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import org.noble.helium.handling.ModelHandler;
import org.noble.helium.helpers.Dimensions;
import org.noble.helium.screens.BaseScreen;

public class SimpleModels extends BaseScreen {
  public SimpleModels() {
    super();
    m_modelHandler.addNewShape(
        "cube-01", ModelHandler.Shape.CUBE, new Texture(Gdx.files.internal("textures/dirt.png")),
        new Vector3(), new Dimensions(100f,10f,100f));
    m_modelHandler.addNewShape(
        "cube-physical", ModelHandler.Shape.CUBE, Color.BLUE,
        new Vector3(5f, 50f, 5f), new Dimensions(5f, 5f, 5f));
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    m_batch.end();
  }
}
