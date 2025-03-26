package org.noble.helium.handling;

import org.noble.helium.actors.Actor;

import java.util.ArrayList;

public class ActorHandler {
  private static ActorHandler m_instance;
  private final ArrayList<Actor> m_actors;

  private ActorHandler() {
    m_actors = new ArrayList<>();
  }

  public static ActorHandler getInstance() {
    if(m_instance == null) {
      m_instance = new ActorHandler();
    }
    return m_instance;
  }

  public void update() {
    for (Actor actor : m_actors) {
      actor.update();
    }
    m_actors.removeIf(Actor::isDead);
  }

  public void addActor(Actor actor) {
    m_actors.add(actor);
  }

  public void clear() {
    m_actors.clear();
  }
}
