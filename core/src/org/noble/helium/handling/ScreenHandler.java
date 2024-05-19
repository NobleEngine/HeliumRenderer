package org.noble.helium.handling;

import com.badlogic.gdx.Screen;
import org.noble.helium.Helium;
import org.noble.helium.screens.SimpleModels;
import org.noble.helium.subsystems.Physics;
import org.noble.helium.subsystems.UserInterface;

public class ScreenHandler {
  private static ScreenHandler m_instance;
  private Screen m_currentScreen;
  private final SimpleModelHandler m_simpleModelHandler;
  private final Helium m_helium;
  private final Physics m_physics;
  private final ObjectHandler m_objectHandler;
  private final UserInterface m_userInterface;


  private ScreenHandler() {
    m_helium = Helium.getInstance();
    m_simpleModelHandler = SimpleModelHandler.getInstance();
    m_physics = Physics.getInstance();
    m_objectHandler = ObjectHandler.getInstance();
    m_userInterface = UserInterface.getInstance();
    changeScreen(new SimpleModels());
  }

  public Screen getCurrentScreen() {
    return m_currentScreen;
  }

  public static ScreenHandler getInstance() {
    if (m_instance == null) {
      m_instance = new ScreenHandler();
    }
    return m_instance;
  }

  public void dispose() {
    m_currentScreen.dispose();
  }

  public void changeScreen(Screen screen) {
    if (m_currentScreen != null) {
      System.out.println("Changing screen to " + screen.getClass().getSimpleName());
    } else {
      System.out.println("Starting game on " + screen.getClass().getSimpleName());
    }
    m_objectHandler.clear();
    m_physics.reset();
    m_simpleModelHandler.clear();
    m_userInterface.clear();
    m_helium.setScreen(screen);
    if (m_currentScreen != null) {
      m_currentScreen.dispose();
    }
    m_currentScreen = m_helium.getScreen();
  }
}