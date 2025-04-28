package org.noble.helium.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.noble.helium.Helium;
import org.noble.helium.actors.PlayerController;
import org.noble.helium.handling.ActorHandler;
import org.noble.helium.handling.ObjectHandler;
import org.noble.helium.handling.ModelHandler;
import org.noble.helium.rendering.HeliumModelBatch;
import org.noble.helium.subsystems.HeliumTelemetry;

public class BaseScreen implements Screen {
  public final Helium m_game;
  public final HeliumModelBatch m_batch;
  public final PlayerController m_player;
  public final ModelHandler m_modelHandler;
  public final ObjectHandler m_objectHandler;
  public final ActorHandler m_actorHandler;
  private final Viewport m_viewport;
  public final Environment m_environment;

  public BaseScreen() {
    m_game = Helium.getInstance();
    m_batch = m_game.getModelBatch();
    m_player = PlayerController.getInstance();
    m_modelHandler = ModelHandler.getInstance();
    m_objectHandler = ObjectHandler.getInstance();
    m_actorHandler = ActorHandler.getInstance();
    m_game.setState(Helium.State.PLAY);

    m_viewport = new ScreenViewport(m_player.getCamera());
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
      HeliumTelemetry.println("Renderer", "Model batch was not ended last cycle", HeliumTelemetry.printType.ERROR);
    }

    if(m_game.getStatus() == Helium.State.PLAY) {
      m_player.update();
      m_objectHandler.update();
      m_actorHandler.update();
    }

    m_batch.begin(m_player.getCamera());
    m_modelHandler.render(m_batch, m_environment);
  }

  @Override
  public void resize(int x, int y) {
    if(m_player.getWorldObject() == null) {
      return;
    }
    Vector3 playerPos = m_player.getPosition();
    m_viewport.update(x, y, true);
    m_player.setPosition(playerPos);
  }

  @Override
  public void pause() {
    m_game.setState(Helium.State.PAUSE);
  }

  @Override
  public void resume() {
    m_game.setState(Helium.State.PLAY);
  }

  @Override
  public void hide() {

  }

  @Override
  public void dispose() {
    m_modelHandler.clear();
    m_objectHandler.clear();
    m_actorHandler.clear();
  }
}
