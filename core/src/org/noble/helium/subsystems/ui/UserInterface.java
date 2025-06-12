package org.noble.helium.subsystems.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import org.noble.helium.Constants;
import org.noble.helium.Helium;
import org.noble.helium.SystemInformation;
import org.noble.helium.subsystems.Subsystem;
import org.noble.helium.subsystems.ui.elements.HeliumLabel;

public class UserInterface extends Subsystem {
  private static UserInterface m_instance;
  private final Stage m_stage;
  private boolean m_showDebug;
  private UserInterface() {
    VisUI.load();
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
  }

  public void clearStage() {
    m_stage.clear();
  }

  private void addHUD() {
    HeliumLabel label = new HeliumLabel(new UpdatableValue(ValueType.PLAYER_HEALTH));
    label.setPosition(10, 10);
    label.setWidth(100);
    label.setHeight(100);
    addActor(label);
  }

  private void addDebug() {
    //Each of these are 25 Y units apart
    SystemInformation sysInfo = SystemInformation.getInstance();
    HeliumLabel engineLabel = new HeliumLabel(Constants.Engine.k_build + " " +sysInfo.getOSName() + " (" + sysInfo.getOSVersion() + ")");
    engineLabel.setPosition(0, Gdx.graphics.getHeight() - 70);
    engineLabel.setWidth(100);
    engineLabel.setHeight(100);
    addActor(engineLabel);

    HeliumLabel javaLabel = new HeliumLabel(sysInfo.getJavaVendor() + " " + sysInfo.getJavaVersion());
    javaLabel.setPosition(0, Gdx.graphics.getHeight() - 95);
    javaLabel.setWidth(100);
    javaLabel.setHeight(100);
    addActor(javaLabel);

    HeliumLabel cpuLabel = new HeliumLabel(sysInfo.getCPUName().stripTrailing() + " (" + sysInfo.getSystemManufacturer() + ")");
    cpuLabel.setPosition(0, Gdx.graphics.getHeight() - 120);
    cpuLabel.setWidth(100);
    cpuLabel.setHeight(100);
    addActor(cpuLabel);

    HeliumLabel gpuLabel = new HeliumLabel(sysInfo.getGPUName() + " (" + sysInfo.getGPUVendor() + ")");
    gpuLabel.setPosition(0, Gdx.graphics.getHeight() - 145);
    gpuLabel.setWidth(100);
    gpuLabel.setHeight(100);
    addActor(gpuLabel);

    HeliumLabel GLLabel = new HeliumLabel("OpenGL Version: " + sysInfo.getGLVersionMajor() + "." + sysInfo.getGLVersionMinor());
    GLLabel.setPosition(0, Gdx.graphics.getHeight() - 170);
    GLLabel.setWidth(100);
    GLLabel.setHeight(100);
    addActor(GLLabel);
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
