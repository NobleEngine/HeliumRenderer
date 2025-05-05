package org.noble.helium.lda;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.noble.helium.PrintUtils;
import org.noble.helium.handling.TextureHandler;
import org.noble.helium.math.Dimensions3;
import org.noble.helium.rendering.HeliumModelBuilder;
import org.noble.helium.world.WorldObject;

import java.util.HashMap;
import java.util.Map;

public class LDAParser {
  public static void addWorldObjects(Map<String, JsonElement> elements) {
    Map<String, JsonElement> worldElements = new HashMap<>();
    elements.forEach((key, value) -> {
      if (key.startsWith("world/")) {
        String fileName = key.substring(key.lastIndexOf('/') + 1)
            .replace(".json", ""); // Remove folder and .json from name
        worldElements.put(fileName, value);
      }
    });
    worldElements.forEach((key, value) -> {
      try {
        PrintUtils.println("Level Data Archive", "Adding world object: " + key);
        JsonObject object = value.getAsJsonObject();
        Texture texture;
        Model model = null;
        Dimensions3 dimensions;
        Vector3 position;

        if(object.has("color")) {
          texture = TextureHandler.getInstance().getTexture(toColor(object.get("color").getAsString()));
        } else if (object.has("texture")) {
          texture = TextureHandler.getInstance().getTexture(object.get("texture").getAsString());
        } else {
          texture = TextureHandler.getInstance().getTexture(Color.PURPLE);//TextureHandler.getInstance().getMissingTexture();
        }

        if(object.has("dimensions")) {
          dimensions = new Dimensions3(toVector3(object.get("dimensions").getAsJsonArray()));
        } else {
          PrintUtils.println("Level Data Archive", "World object " + key + " has no dimensions", PrintUtils.printType.ERROR);
          dimensions = null;
        }

        if(object.has("position")) {
          position = toVector3(object.get("position").getAsJsonArray());
        } else {
          PrintUtils.println("Level Data Archive", "World object " + key + " has no position", PrintUtils.printType.ERROR);
          position = null;
        }

        if (object.get("type").getAsString().equals("shape")) {
          model = HeliumModelBuilder.getInstance().create(toShapeType(object.get("shape").getAsString()), texture, dimensions);
        } else if (object.get("type").getAsString().equals("model")) {
//          model = HeliumModelBuilder.getInstance().create(object.get("model").getAsString(), HeliumModelBuilder.ModelType.OBJ); //TODO: wrong
        } else {
          model = null;
        }

//        if(model == null || position == null || dimensions == null ) {
//          PrintUtils.error("Level Data Archive", new IllegalArgumentException("World object " + key + " has missing required fields"), PrintUtils.ErrorType.FATAL, true);
//        }

        new WorldObject(model, position, WorldObject.CollisionType.STANDARD);
      } catch (Exception e) {
        PrintUtils.error("Level Data Archive", e, PrintUtils.ErrorType.NONFATAL, true);
        PrintUtils.println("Level Data Archive", "Failed to add world object: " + key, PrintUtils.printType.ERROR);
      }
    });
  }

  private static HeliumModelBuilder.Shape toShapeType(String type) {
    switch (type) {
      case "rectangle" -> {
        return HeliumModelBuilder.Shape.CUBE;
      }
      case "sphere" -> {
        return HeliumModelBuilder.Shape.SPHERE;
      }
      default -> {
        PrintUtils.println("Level Data Archive", "Unknown shape type: " + type, PrintUtils.printType.ERROR);
        return HeliumModelBuilder.Shape.CUBE;
      }
    }
  }

  private static Color toColor(String color) {
    if (color.startsWith("rgba(")) {
      return parseRGBA(color);
    } else if (color.startsWith("#")) {
      return Color.valueOf(color);
    }
    return null;
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
