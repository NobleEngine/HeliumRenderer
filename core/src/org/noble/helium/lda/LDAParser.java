package org.noble.helium.lda;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.noble.helium.handling.ModelHandler;
import org.noble.helium.handling.ObjectHandler;
import org.noble.helium.math.Dimensions3;
import org.noble.helium.subsystems.telemetry.HeliumTelemetry;
import org.noble.helium.world.WorldObject;

import java.util.HashMap;
import java.util.Map;

public class LDAParser {
  public static void addWorldObjects(Map<String, JsonElement> elements) {
    Map<String, JsonElement> worldElements = new HashMap<>();
    Map<String, WorldObject> objects = new HashMap<>();
    elements.forEach((key, value) -> {
      if(key.startsWith("world/")) {
        String fileName = key.substring(key.lastIndexOf('/') + 1); // Remove folder prefix
        worldElements.put(fileName, value);
      }
    });
    worldElements.forEach((key, value) -> {
      JsonObject object = value.getAsJsonObject();
      ModelHandler modelHandler = ModelHandler.getInstance();
      if (object.get("type").getAsString().equals("shape")) {
          modelHandler.addNewShape(
              key + "-model",
              toShapeType(object.get("shape").getAsString()),
              toColor(object.get("color").getAsString()),
              toVector3(object.get("position").getAsJsonArray()),
              new Dimensions3(toVector3(object.get("dimensions").getAsJsonArray())));
          objects.put(key + "-model",new WorldObject(modelHandler.get(key + "-model"), WorldObject.CollisionType.STANDARD));
      }
    });
    objects.forEach((key, value) -> ObjectHandler.getInstance().add(key + "-object", value));
  }

  private static ModelHandler.Shape toShapeType(String type) {
    switch(type) {
      case "rectangle" -> {
        return ModelHandler.Shape.CUBE;
      }
      case "sphere" -> {
        return ModelHandler.Shape.SPHERE;
      }
      default -> {
        HeliumTelemetry.getInstance().printErrorln("Unknown shape type: " + type);
        return ModelHandler.Shape.CUBE;
      }
    }
  }

  private static Color toColor(String color) {
    if (color.startsWith("rgba(")) {
      return parseRGBA(color);  // Parse rgba() format
    } else if (color.startsWith("#")) {
      return Color.valueOf(color);  // Parse hex format
    }
    return null;  // Return null for invalid formats
  }

  private static Color parseRGBA(String rgbaColor) {
    rgbaColor = rgbaColor.replace("rgba(", "").replace(")", "");
    String[] rgba = rgbaColor.split(",");

    float r = Integer.parseInt(rgba[0]) / 255f;
    float g = Integer.parseInt(rgba[1]) / 255f;
    float b = Integer.parseInt(rgba[2]) / 255f;
    float a = Float.parseFloat(rgba[3]);

    return new Color(r, g, b, a);
  }

  private static Vector3 toVector3(JsonArray array) {
    return new Vector3(array.get(0).getAsFloat(), array.get(1).getAsFloat(), array.get(2).getAsFloat());
  }
}
