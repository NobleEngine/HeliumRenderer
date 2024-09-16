package org.noble.helium.logic;

import com.badlogic.gdx.math.Vector3;
import org.noble.helium.math.Dimensions3;
import org.noble.helium.math.EulerAngles;

public class OrientedBoundingBox {
  private final Vector3 m_position;
  private final Dimensions3 m_dimensions;
  private final EulerAngles m_angles; // Rotation in radians for each axis

  public OrientedBoundingBox(Vector3 position, Dimensions3 dimensions, EulerAngles angles) {
    m_position = position;
    m_dimensions = dimensions;
    m_angles = angles;
  }

  // Get the rotation matrix based on yaw, pitch, and roll
  private double[][] getRotationMatrix() {
    double cosYaw = Math.cos(m_angles.getYaw());
    double sinYaw = Math.sin(m_angles.getYaw());
    double cosPitch = Math.cos(m_angles.getPitch());
    double sinPitch = Math.sin(m_angles.getPitch());
    double cosRoll = Math.cos(m_angles.getRoll());
    double sinRoll = Math.sin(m_angles.getRoll());

    // Yaw, Pitch, Roll combined into a rotation matrix
    return new double[][]{
        { cosYaw * cosRoll, cosYaw * sinRoll * sinPitch - sinYaw * cosPitch, cosYaw * sinRoll * cosPitch + sinYaw * sinPitch },
        { sinYaw * cosRoll, sinYaw * sinRoll * sinPitch + cosYaw * cosPitch, sinYaw * sinRoll * cosPitch - cosYaw * sinPitch },
        { -sinRoll, cosRoll * sinPitch, cosRoll * cosPitch }
    };
  }

  public Vector3 getPosition() {
    return m_position;
  }

  public Dimensions3 getDimensions() {
    return m_dimensions;
  }

  public EulerAngles getAngles() {
    return m_angles;
  }

  // Full SAT intersection test with all 15 potential separating axes
  public boolean intersects(OrientedBoundingBox other) {
    double[][] rotationA = this.getRotationMatrix();
    double[][] rotationB = other.getRotationMatrix();

    // These are the face normals for both boxes
    double[][] axesToTest = new double[15][3];

    // First, the face normals of both boxes
    for (int i = 0; i < 3; i++) {
      axesToTest[i] = rotationA[i];  // Face normals of box A
      axesToTest[i + 3] = rotationB[i];  // Face normals of box B
    }

    // Now, the cross products of the edge vectors of both boxes
    int index = 6;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        axesToTest[index] = crossProduct(rotationA[i], rotationB[j]);
        index++;
      }
    }

    // Now, for each axis, test for overlap
    for (double[] axis : axesToTest) {
      if (!overlapOnAxis(this, other, axis)) {
        // If we find a separating axis, there is no collision
        return false;
      }
    }

    // No separating axis found, the boxes are intersecting
    return true;
  }

  // Calculate the cross product of two vectors
  private double[] crossProduct(double[] a, double[] b) {
    return new double[] {
        a[1] * b[2] - a[2] * b[1],
        a[2] * b[0] - a[0] * b[2],
        a[0] * b[1] - a[1] * b[0]
    };
  }

  // Project an OBB onto a given axis
  private double projectOntoAxis(OrientedBoundingBox box, double[] axis) {
    double[] extents = {
        box.getDimensions().getWidth() / 2.0, box.getDimensions().getHeight() / 2.0, box.getDimensions().getDepth() / 2.0
    };

    double projection = 0.0;

    // Project each axis of the box onto the given axis
    for (int i = 0; i < 3; i++) {
      projection += extents[i] * Math.abs(dotProduct(box.getRotationMatrix()[i], axis));
    }

    return projection;
  }

  // Check for overlap along a given axis using SAT
  private boolean overlapOnAxis(OrientedBoundingBox boxA, OrientedBoundingBox boxB, double[] axis) {
    double projectA = projectOntoAxis(boxA, axis);
    double projectB = projectOntoAxis(boxB, axis);

    // Calculate the distance between box centers projected onto the axis
    double centerDistance = Math.abs(dotProduct(new double[] {
        boxA.getPosition().x - boxB.getPosition().x,
        boxA.getPosition().y - boxB.getPosition().y,
        boxA.getPosition().z - boxB.getPosition().z }, axis));

    // If the center distance is greater than the combined projection extents, they are separated
    return centerDistance <= (projectA + projectB);
  }

  // Dot product helper function
  private double dotProduct(double[] a, double[] b) {
    return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
  }

  // Resolve the collision (smallest penetration axis)
  public void resolveCollision(OrientedBoundingBox other) {
    double[][] rotationA = this.getRotationMatrix();
    double[][] rotationB = other.getRotationMatrix();

    // Combine all axes that were tested for overlap
    double[][] axesToTest = new double[15][3];
    for (int i = 0; i < 3; i++) {
      axesToTest[i] = rotationA[i];
      axesToTest[i + 3] = rotationB[i];
    }
    int index = 6;
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        axesToTest[index] = crossProduct(rotationA[i], rotationB[j]);
        index++;
      }
    }

    // Track the smallest penetration axis and depth
    double minPenetrationDepth = Double.MAX_VALUE;
    double[] minPenetrationAxis = null;

    // Find the axis with the smallest penetration
    for (double[] axis : axesToTest) {
      double projectionA = projectOntoAxis(this, axis);
      double projectionB = projectOntoAxis(other, axis);

      double centerDistance = Math.abs(dotProduct(new double[] {
          this.getPosition().x - other.getPosition().x,
          this.getPosition().y - other.getPosition().y,
          this.getPosition().z - other.getPosition().z }, axis));

      double penetrationDepth = (projectionA + projectionB) - centerDistance;

      if (penetrationDepth < minPenetrationDepth) {
        minPenetrationDepth = penetrationDepth;
        minPenetrationAxis = axis;
      }
    }

    // Move this box along the smallest penetration axis to resolve the collision
    double moveDistance = minPenetrationDepth / 2.0;
    assert minPenetrationAxis != null;
    this.getPosition().x += (float) (minPenetrationAxis[0] * moveDistance);
    this.getPosition().y += (float) (minPenetrationAxis[1] * moveDistance);
    this.getPosition().z += (float) (minPenetrationAxis[2] * moveDistance);
  }
}
