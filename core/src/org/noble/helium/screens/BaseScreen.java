package org.noble.helium.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import org.noble.helium.Helium;
import org.noble.helium.actors.PlayerController;
import org.noble.helium.handling.ObjectHandler;
import org.noble.helium.handling.SimpleModelHandler;
import org.noble.helium.rendering.HeliumModelBatch;

public class BaseScreen implements Screen {
  public final Helium m_game;
  public final HeliumModelBatch m_batch;
  public final PlayerController m_player;
  public final SimpleModelHandler m_simpleModelHandler;
  public final ObjectHandler m_objectHandler;
  public Environment m_environment;

  public interface gameStatus {
    int PLAY = 0;
    int PAUSE = 1;
  }

  public BaseScreen() {
    m_game = Helium.getInstance();
    m_batch = m_game.getModelBatch();
    m_player = PlayerController.getInstance();
    m_simpleModelHandler = SimpleModelHandler.getInstance();
    m_objectHandler = ObjectHandler.getInstance();
    m_game.setStatus(gameStatus.PLAY);

    m_environment = new Environment();
    m_environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.0f));
    m_environment.add(new DirectionalLight().set(0.8f,0.8f,0.8f,-1f,-0.8f,-0.2f));
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
    if(m_batch.isWorking()) {
      m_batch.end();
      System.err.println("Model batch was not ended last cycle");
    }

    if(m_game.getStatus() == gameStatus.PLAY) {
      m_player.update();
    }

    m_batch.begin(m_player.getCamera());
    m_simpleModelHandler.render(m_batch, m_environment);
  }

  @Override
  public void resize(int x, int y) {
  }

  @Override
  public void pause() {
    m_game.setStatus(gameStatus.PAUSE);
  }

  @Override
  public void resume() {
    m_game.setStatus(gameStatus.PLAY);
  }

  @Override
  public void hide() {

  }

  @Override
  public void dispose() {
    m_simpleModelHandler.clear();
    m_objectHandler.clear();
  }
}
