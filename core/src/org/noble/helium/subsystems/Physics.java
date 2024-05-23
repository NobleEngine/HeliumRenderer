package org.noble.helium.subsystems;

import com.badlogic.gdx.physics.bullet.*;
import com.badlogic.gdx.physics.bullet.collision.*;

public class Physics extends Subsystem {
  private static Physics m_instance;
  private final btCollisionConfiguration  m_collisionConfig;
  private final btDispatcher m_dispatcher;

  private Physics() {
    Bullet.init();
    m_collisionConfig = new btDefaultCollisionConfiguration();
    m_dispatcher = new btCollisionDispatcher(m_collisionConfig);
  }

  public static Physics getInstance() {
    if(m_instance == null) {
      m_instance = new Physics();
    }
    return m_instance;
  }

  public btDispatcher getDispatcher() {
    return m_dispatcher;
  }

  public boolean checkCollision(btCollisionObject obj1, btCollisionObject obj2) {
    boolean finalResult = false;

    CollisionObjectWrapper object0 = new CollisionObjectWrapper(obj1);
    CollisionObjectWrapper object1 = new CollisionObjectWrapper(obj2);

    // For each pair of shape types, Bullet will dispatch a certain collision algorithm, by using the dispatcher.
    // So we use the dispatcher here to find the algorithm needed for the two shape types being checked, ex. btSphereBoxCollisionAlgorithm
    btCollisionAlgorithm algorithm = getDispatcher().findAlgorithm(object0.wrapper, object1.wrapper, null, ebtDispatcherQueryType.BT_CONTACT_POINT_ALGORITHMS);

    btDispatcherInfo info = new btDispatcherInfo();
    btManifoldResult result = new btManifoldResult(object0.wrapper, object1.wrapper);

    // Execute the algorithm using processCollision, this stores the result (the contact points) in the btManifoldResult
    algorithm.processCollision(object0.wrapper, object1.wrapper, info, result);

    // Free the algorithm back to a pool for reuse later
    getDispatcher().freeCollisionAlgorithm(algorithm.getCPointer());

    // btPersistentManifold is a contact point cache to store contact points for a given pair of objects.
    btPersistentManifold man = result.getPersistentManifold();
    if (man != null) {
      // If the number of contact points is more than zero, then there is a collision.
      finalResult = man.getNumContacts() > 0;
    }

    result.dispose();
    info.dispose();
    object1.dispose();
    object0.dispose();

    return finalResult;
  }

  public void reset() {
//    m_objects.clear();
  }

  @Override
  public void update() {
  }

  @Override
  public void dispose() {
  }
}
