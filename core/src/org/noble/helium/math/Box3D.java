package org.noble.helium.math;

import com.badlogic.gdx.math.Vector3;
import us.ihmc.euclid.geometry.Pose3D;
import us.ihmc.euclid.shape.collision.epa.ExpandingPolytopeAlgorithm;

public class Box3D extends us.ihmc.euclid.shape.primitives.Box3D {
  public Box3D(Vector3 coordinates, Dimensions3 dimensions) {
    super(new Pose3D(coordinates.x, coordinates.y, coordinates.z, 0, 0, 0), dimensions.getWidth(), dimensions.getHeight(), dimensions.getDepth());
  }

  public boolean intersects(Box3D box) {
    ExpandingPolytopeAlgorithm algorithm = new ExpandingPolytopeAlgorithm();
    return algorithm.evaluateCollision(this, box).areShapesColliding();
  }

  public void setPosition(Vector3 position) {
    Pose3D pose = new Pose3D(position.x, position.y, position.z, 0, 0, 0);
    set(pose, getSizeX(), getSizeY(), getSizeZ());
  }
}
