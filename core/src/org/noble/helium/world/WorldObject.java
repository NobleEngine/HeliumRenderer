package org.noble.helium.world;

import com.badlogic.gdx.physics.bullet.collision.*;
import org.noble.helium.HeliumModelInstance;
import org.noble.helium.subsystems.Physics;

public class WorldObject {
  private final Physics m_physics;
  private final btCollisionObject m_body;
  private final HeliumModelInstance m_modelInstance;
  private final ObjectType m_type;
  public WorldObject(HeliumModelInstance modelInstance, ShapeType shape, ObjectType type) {
    m_type = type;
    m_modelInstance = modelInstance;
    m_body = new btCollisionObject();
    m_body.setCollisionShape(getShape(shape));
    m_body.setWorldTransform(m_modelInstance.transform);
    m_physics = Physics.getInstance();
  }

  public HeliumModelInstance getModelInstance() {
    return m_modelInstance;
  }

  private btCollisionShape getShape(ShapeType shape) {
    switch(shape) {
      case ShapeType.BOX -> {
        return new btBoxShape(m_modelInstance.getDimensions());
      }
    }

    return null;
  }

  public btCollisionObject getBody() {
    return m_body;
  }

  public boolean isColliding(WorldObject object) {
    boolean finalResult = false;
    CollisionObjectWrapper co0 = new CollisionObjectWrapper(m_body);
    CollisionObjectWrapper co1 = new CollisionObjectWrapper(object.getBody());

    // For each pair of shape types, Bullet will dispatch a certain collision algorithm, by using the dispatcher.
    // So we use the dispatcher here to find the algorithm needed for the two shape types being checked, ex. btSphereBoxCollisionAlgorithm
    btCollisionAlgorithm algorithm = m_physics.getDispatcher().findAlgorithm(co0.wrapper, co1.wrapper, null, ebtDispatcherQueryType.BT_CONTACT_POINT_ALGORITHMS);

    btDispatcherInfo info = new btDispatcherInfo();
    btManifoldResult result = new btManifoldResult(co0.wrapper, co1.wrapper);

    // Execute the algorithm using processCollision, this stores the result (the contact points) in the btManifoldResult
    algorithm.processCollision(co0.wrapper, co1.wrapper, info, result);

    // Free the algorithm back to a pool for reuse later
    m_physics.getDispatcher().freeCollisionAlgorithm(algorithm.getCPointer());

    // btPersistentManifold is a contact point cache to store contact points for a given pair of objects.
    btPersistentManifold man = result.getPersistentManifold();
    if (man != null) {
      // If the number of contact points is more than zero, then there is a collision.
      finalResult = man.getNumContacts() > 0;
    }

    result.dispose();
    info.dispose();
    co1.dispose();
    co0.dispose();

    return finalResult;
  }

  public void setPosition(float x, float y, float z) {
    m_modelInstance.setPosition(x,y,z);
  }

  public float getX() {
    return m_modelInstance.getPosition().getX();
  }

  public float getY() {
    return m_modelInstance.getPosition().getY();
  }

  public float getZ() {
    return m_modelInstance.getPosition().getZ();
  }

  public void update() {
    if(m_type.equals(ObjectType.PHYSICS)) {

    }
    m_body.setWorldTransform(m_modelInstance.transform);
  }

  public void dispose() {
    m_body.dispose();
  }

  public enum ObjectType {
    STATIC, PHYSICS
  }

  public enum ShapeType { //TODO: Add more
    BOX
  }
}
