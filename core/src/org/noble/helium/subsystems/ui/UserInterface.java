package org.noble.helium.subsystems.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import org.noble.helium.Constants;
import org.noble.helium.Helium;
import org.noble.helium.HeliumIO;
import org.noble.helium.SystemInformation;
import org.noble.helium.actors.PlayerController;
import org.noble.helium.handling.ObjectHandler;
import org.noble.helium.math.Units;
import org.noble.helium.subsystems.Subsystem;

import java.util.HashMap;

public class UserInterface extends Subsystem {
  private static UserInterface m_instance;
  private final HashMap<String, VisLabel> m_hudLabels;
  private final Stage m_stage;
  private boolean m_showDebug;
  private UserInterface() {
    VisUI.load();
    m_hudLabels = new HashMap<>();
    m_stage = new Stage(new ScreenViewport());
    m_showDebug = false;
    Gdx.input.setInputProcessor(m_stage);
    reset();
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

    if(Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
      m_showDebug = !m_showDebug;
      reset();
    }

    updateHUD();
    if(m_showDebug) {
      updateDebug();
    }
  }

  public void clearStage() {
    m_stage.clear();
    m_hudLabels.clear();
  }

  private void updateHUD() {
    try {
      m_hudLabels.get("PLAYER_HEALTH").setText("Player Health: " + PlayerController.getInstance().getHealth());
    } catch (NullPointerException e) {
      HeliumIO.error("User Interface", e, HeliumIO.ErrorType.NONFATAL, false);
    }
  }

  private void updateDebug() {
    try {
      m_hudLabels.get("GAME_FPS").setText("Game FPS: " + Helium.getInstance().getFPS() + ", Frametime: " + Helium.getInstance().getDelta());
      m_hudLabels.get("PLAYER_POSITION").setText("Player Position: " + PlayerController.getInstance().getPosition());
      m_hudLabels.get("MEMORY").setText(
          "Allocated memory: " + Units.bytesToMB(Runtime.getRuntime().totalMemory()) + "MB, " +
          "Free memory: " + Units.bytesToMB(Runtime.getRuntime().freeMemory()) + "MB");
      m_hudLabels.get("WORLDOBJECT_COUNT").setText("Number of World Objects: " + ObjectHandler.getInstance().getAllObjects().size());
    } catch (NullPointerException e) {
      HeliumIO.error("User Interface", e, HeliumIO.ErrorType.NONFATAL, false);
    }
  }

  private void addHUD() {
    VisLabel playerHealth = new VisLabel();
    playerHealth.setPosition(10, 10);
    playerHealth.setWidth(100);
    playerHealth.setHeight(100);
    addActor(playerHealth);
    m_hudLabels.put("PLAYER_HEALTH", playerHealth);
  }

  private void addDebug() {
    //Each of these are 25 Y units apart
    SystemInformation sysInfo = SystemInformation.getInstance();
    VisLabel engineLabel = new VisLabel(Constants.Engine.k_build + " " +sysInfo.getOSName() + " (" + sysInfo.getOSVersion() + ")");
    engineLabel.setPosition(0, Gdx.graphics.getHeight() - 70);
    engineLabel.setWidth(100);
    engineLabel.setHeight(100);
    addActor(engineLabel);

    VisLabel javaLabel = new VisLabel(sysInfo.getJavaVendor() + " " + sysInfo.getJavaVersion());
    javaLabel.setPosition(0, Gdx.graphics.getHeight() - 95);
    javaLabel.setWidth(100);
    javaLabel.setHeight(100);
    addActor(javaLabel);

    VisLabel cpuLabel = new VisLabel(sysInfo.getCPUName().stripTrailing() + " (" + sysInfo.getSystemManufacturer() + ")");
    cpuLabel.setPosition(0, Gdx.graphics.getHeight() - 120);
    cpuLabel.setWidth(100);
    cpuLabel.setHeight(100);
    addActor(cpuLabel);

    VisLabel gpuLabel = new VisLabel(sysInfo.getGPUName() + " (" + sysInfo.getGPUVendor() + ")");
    gpuLabel.setPosition(0, Gdx.graphics.getHeight() - 145);
    gpuLabel.setWidth(100);
    gpuLabel.setHeight(100);
    addActor(gpuLabel);

    VisLabel GLLabel = new VisLabel("OpenGL Version: " + sysInfo.getGLVersionMajor() + "." + sysInfo.getGLVersionMinor());
    GLLabel.setPosition(0, Gdx.graphics.getHeight() - 170);
    GLLabel.setWidth(100);
    GLLabel.setHeight(100);
    addActor(GLLabel);

    VisLabel gameFPS = new VisLabel();
    gameFPS.setPosition(0, Gdx.graphics.getHeight() - 195);
    gameFPS.setWidth(100);
    gameFPS.setHeight(100);
    addActor(gameFPS);
    m_hudLabels.put("GAME_FPS", gameFPS);

    VisLabel playerPosition = new VisLabel();
    playerPosition.setPosition(0, Gdx.graphics.getHeight() - 220);
    playerPosition.setWidth(100);
    playerPosition.setHeight(100);
    addActor(playerPosition);
    m_hudLabels.put("PLAYER_POSITION", playerPosition);

    VisLabel memory = new VisLabel();
    memory.setPosition(0, Gdx.graphics.getHeight() - 245);
    memory.setWidth(100);
    memory.setHeight(100);
    addActor(memory);
    m_hudLabels.put("MEMORY", memory);

    VisLabel objects = new VisLabel();
    objects.setPosition(0, Gdx.graphics.getHeight() - 270);
    objects.setWidth(100);
    objects.setHeight(100);
    addActor(objects);
    m_hudLabels.put("WORLDOBJECT_COUNT", objects);
  }

  public void reset() {
    clearStage();
    if(m_showDebug) {
      addHUD();
      addDebug();
    } else {
      addHUD();
    }
  }

  @Override
  public void dispose() {
  }
}
