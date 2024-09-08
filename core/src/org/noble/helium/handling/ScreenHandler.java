package org.noble.helium.handling;

import com.badlogic.gdx.Screen;
import org.noble.helium.Helium;
import org.noble.helium.screens.tests.PhysicsTest;
import org.noble.helium.subsystems.ui.UserInterface;

public class ScreenHandler {
  private static ScreenHandler m_instance;
  private Screen m_currentScreen;
  private final Helium m_helium;
  private final UserInterface m_userInterface;


  private ScreenHandler() {
    m_helium = Helium.getInstance();
    m_userInterface = UserInterface.getInstance();
    changeScreen(new PhysicsTest());
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
    m_userInterface.clear();
    m_helium.setScreen(screen);
    if (m_currentScreen != null) {
      m_currentScreen.dispose();
    }
    m_currentScreen = m_helium.getScreen();
  }
}