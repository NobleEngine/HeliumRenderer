package org.noble.helium.logic;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import org.noble.helium.math.Dimensions3;
import org.noble.helium.math.EulerAngles;

public class OrientedBoundingBox {
  private final Vector3 center; // Floating-point center
  private final Vector3 halfSizes; // Half-width, half-height, half-depth
  private final Matrix4 rotationMatrix; // 4x4 Rotation matrix
  private final Vector3[] corners; // Transformed corner points

  public OrientedBoundingBox(Vector3 position, Dimensions3 dimensions, EulerAngles angles) {
    this.center = position;
    this.halfSizes = new Vector3(dimensions.getWidth() / 2f, dimensions.getHeight() / 2f, dimensions.getDepth() / 2f);
    this.rotationMatrix = createRotationMatrix(angles.getYaw(), angles.getPitch(), angles.getRoll());
    this.corners = computeCorners();
  }

  private Matrix4 createRotationMatrix(float yaw, float pitch, float roll) {
    Matrix4 rotation = new Matrix4();
    rotation.idt(); // Identity matrix

    // Apply Yaw (Z-axis), Pitch (Y-axis), and Roll (X-axis) in order
    rotation.rotate(Vector3.Z, yaw);
    rotation.rotate(Vector3.Y, pitch);
    rotation.rotate(Vector3.X, roll);

    return rotation;
  }

  private Vector3[] computeCorners() {
    Vector3[] localCorners = {
        new Vector3(-halfSizes.x, -halfSizes.y, -halfSizes.z),
        new Vector3(halfSizes.x, -halfSizes.y, -halfSizes.z),
        new Vector3(halfSizes.x, halfSizes.y, -halfSizes.z),
        new Vector3(-halfSizes.x, halfSizes.y, -halfSizes.z),
        new Vector3(-halfSizes.x, -halfSizes.y, halfSizes.z),
        new Vector3(halfSizes.x, -halfSizes.y, halfSizes.z),
        new Vector3(halfSizes.x, halfSizes.y, halfSizes.z),
        new Vector3(-halfSizes.x, halfSizes.y, halfSizes.z)
    };

    // Transform corners using the rotation matrix and add center position
    for (Vector3 localCorner : localCorners) {
      localCorner.mul(rotationMatrix);
      localCorner.add(center); // Keep floating-point accuracy
    }
    return localCorners;
  }

  public boolean isColliding(OrientedBoundingBox other) {
    Vector3[] axes = getSeparatingAxes(other);

    for (Vector3 axis : axes) {
      if (!overlapsOnAxis(axis, other)) {
        return false; // Found a separating axis, so no collision
      }
    }
    return true; // No separating axis found → Collision
  }

  private Vector3[] getSeparatingAxes(OrientedBoundingBox other) {
    Vector3[] axesA = getLocalAxes();
    Vector3[] axesB = other.getLocalAxes();
    Vector3[] axes = new Vector3[15]; // Ensure all axes are assigned

    System.arraycopy(axesA, 0, axes, 0, 3);
    System.arraycopy(axesB, 0, axes, 3, 3);

    int index = 6;
    for (Vector3 axisA : axesA) {
      for (Vector3 axisB : axesB) {
        Vector3 cross = new Vector3(axisA).crs(axisB);
        if (cross.len2() > 0.0001f) { // Ensure it's not a near-zero vector
          axes[index++] = cross.nor();
        }
      }
    }

    // Ensure all 15 elements are non-null by filling missing ones with safe default axes
    while (index < 15) {
      axes[index++] = new Vector3(1, 0, 0); // Default to a valid axis (fallback)
    }

    return axes;
  }

  private boolean overlapsOnAxis(Vector3 axis, OrientedBoundingBox other) {
    float minA = Float.POSITIVE_INFINITY, maxA = Float.NEGATIVE_INFINITY;
    float minB = Float.POSITIVE_INFINITY, maxB = Float.NEGATIVE_INFINITY;

    for (Vector3 corner : this.corners) {
      float projection = corner.dot(axis);
      minA = Math.min(minA, projection);
      maxA = Math.max(maxA, projection);
    }

    for (Vector3 corner : other.corners) {
      float projection = corner.dot(axis);
      minB = Math.min(minB, projection);
      maxB = Math.max(maxB, projection);
    }

    return maxA >= minB && maxB >= minA; // If there's an overlap, return true
  }

  private Vector3[] getLocalAxes() {
    return new Vector3[]{
        new Vector3(1, 0, 0).mul(rotationMatrix), // Local X axis
        new Vector3(0, 1, 0).mul(rotationMatrix), // Local Y axis
        new Vector3(0, 0, 1).mul(rotationMatrix)  // Local Z axis
    };
  }
}
