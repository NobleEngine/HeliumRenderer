package org.noble.helium.lda;

import com.badlogic.gdx.files.FileHandle;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.noble.helium.subsystems.HeliumTelemetry;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LDAExtractor {
  public static Map<String, JsonElement> getLDAElements(FileHandle LDA) throws IOException {
    return unzipAndParseJson(LDA.readBytes());
  }

  public static List<Class<?>> getScripts(FileHandle LDA) {
    try {
      return InMemoryClassCompiler.unzipAndCompile(LDA);
    } catch (Exception e) {
      HeliumTelemetry.getInstance().printErrorln("Level Data Archive", e.getMessage());
      return Collections.emptyList();
    }
  }

  // Unzip and parse JSON files using Gson
  private static Map<String, JsonElement> unzipAndParseJson(byte[] zipBytes) throws IOException {
    Map<String, JsonElement> jsonFiles = new HashMap<>();

    try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
      ZipEntry entry;

      while ((entry = zis.getNextEntry()) != null) {
        if (!entry.isDirectory() && entry.getName().endsWith(".json")) {
          // Parse JSON using Gson
          byte[] fileBytes = zis.readAllBytes();
          String jsonString = new String(fileBytes);

          JsonElement jsonElement = JsonParser.parseString(jsonString);
          jsonFiles.put(entry.getName(), jsonElement);
        }
        zis.closeEntry();
      }
    }

    for (Map.Entry<String, JsonElement> entry : jsonFiles.entrySet()) {
      HeliumTelemetry.getInstance().println("Level Data Archive", "Found Element - " + entry.getKey() + ": " + entry.getValue().getAsJsonObject());
    }

    return jsonFiles;
  }



}