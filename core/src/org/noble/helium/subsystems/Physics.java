package org.noble.helium.subsystems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import org.noble.helium.Constants;
import org.noble.helium.handling.ModelHandler;
import org.noble.helium.handling.ObjectHandler;
import org.noble.helium.subsystems.telemetry.HeliumTelemetry;
import org.noble.helium.world.WorldObject;

import java.util.ArrayList;

public class Physics extends Subsystem {
  private static Physics m_instance;
  private final btCollisionConfiguration m_collisionConfig;
  private final btDispatcher m_dispatcher;
  private HeliumTelemetry m_telemetry;

  private Physics() {
    m_telemetry = HeliumTelemetry.getInstance();
    Bullet.init();
    m_collisionConfig = new btDefaultCollisionConfiguration();
    m_dispatcher = new btCollisionDispatcher(m_collisionConfig);
    m_telemetry.println("Physics subsystem initialized using Bullet version " + Bullet.VERSION);
  }

  public static Physics getInstance() {
    if(m_instance == null) {
      m_instance = new Physics();
    }
    return m_instance;
  }

  public boolean checkCollision(WorldObject object1, WorldObject object2) {
    CollisionObjectWrapper collisionObject1 = new CollisionObjectWrapper(object1.getBulletObject());
    CollisionObjectWrapper collisionObject2 = new CollisionObjectWrapper(object2.getBulletObject());

    btCollisionAlgorithmConstructionInfo constructionInfo = new btCollisionAlgorithmConstructionInfo();
    constructionInfo.setDispatcher1(m_dispatcher);
    btCollisionAlgorithm collisionAlgorithm = new btSphereBoxCollisionAlgorithm(null, constructionInfo, collisionObject1.wrapper, collisionObject2.wrapper, false);

    btDispatcherInfo dispatcherInfo = new btDispatcherInfo();
    btManifoldResult manifoldResult = new btManifoldResult(collisionObject1.wrapper, collisionObject2.wrapper);

    collisionAlgorithm.processCollision(collisionObject1.wrapper, collisionObject2.wrapper, dispatcherInfo, manifoldResult);

    boolean finalResult = manifoldResult.getPersistentManifold().getNumContacts() > 0;

    manifoldResult.dispose();
    dispatcherInfo.dispose();
    collisionAlgorithm.dispose();
    constructionInfo.dispose();
    collisionObject2.dispose();
    collisionObject1.dispose();

    return finalResult;
  }

  public ArrayList<WorldObject> getCollisions(WorldObject object1) {
    ArrayList<WorldObject> allObjects = new ArrayList<>(ObjectHandler.getInstance().getAllObjects().values());
    ArrayList<WorldObject> collidingObjects = new ArrayList<>();

    for(WorldObject object2 : allObjects) {
      if(object1 != object2) {
        if (checkCollision(object1, object2)) {
          if(Constants.Engine.Physics.k_debugDrawing) {
            ModelHandler.getInstance().setColor(ModelHandler.getInstance().getName(object2.getModelInstance()), Color.RED);
          }
          collidingObjects.add(object2);
        } else if(Constants.Engine.Physics.k_debugDrawing) {
          ModelHandler.getInstance().setColor(ModelHandler.getInstance().getName(object2.getModelInstance()), Color.GREEN);
        }
      }
    }

    return collidingObjects;
  }

  @Override
  public void update() {

  }

  @Override
  public void dispose() {
    m_dispatcher.dispose();
    m_collisionConfig.dispose();
  }
}
