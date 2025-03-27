package org.noble.helium.lda;

import com.badlogic.gdx.files.FileHandle;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.noble.helium.subsystems.telemetry.HeliumTelemetry;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LDAExtractor {
  public static Map<String, JsonElement> getLDAElements(FileHandle LDA) throws IOException {
    return unzipAndParseJson(LDA.readBytes());
  }

  public static Map<String, Class<?>> getScripts(FileHandle LDA) throws IOException, ClassNotFoundException {
    return unzipAndLoadClasses(LDA);
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
      HeliumTelemetry.getInstance().println(entry.getKey() + ": " + entry.getValue().getAsJsonObject());
    }

    return jsonFiles;
  }

  public static Map<String, Class<?>> unzipAndLoadClasses(FileHandle zipFileHandle) throws IOException, ClassNotFoundException {
    Map<String, byte[]> classBytesMap = new HashMap<>();

    // Extract .class files from ZIP into memory
    try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipFileHandle.readBytes()))) {
      ZipEntry entry;
      while ((entry = zis.getNextEntry()) != null) {
        if (!entry.isDirectory() && entry.getName().startsWith("script/") && entry.getName().endsWith(".class")) {
          // Read .class file into memory
          byte[] bytes = zis.readAllBytes();
          String relativePath = entry.getName().substring("script/".length());
          classBytesMap.put(relativePath, bytes);
        }
      }
    }

    // Load classes dynamically into memory
    Map<String, Class<?>> scriptMap = new HashMap<>();
    InMemoryClassLoader classLoader = new InMemoryClassLoader(classBytesMap);

    for (String path : classBytesMap.keySet()) {
      String className = path.replace("/", ".").replace(".class", "");
      Class<?> clazz = classLoader.loadClass(className);
      scriptMap.put(path, clazz);
    }

    return scriptMap;
  }

  public static class InMemoryClassLoader extends ClassLoader {
    private final Map<String, byte[]> classes;

    public InMemoryClassLoader(Map<String, byte[]> classes) {
      this.classes = classes;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
      byte[] classBytes = classes.get(name.replace(".", "/") + ".class");
      if (classBytes != null) {
        return defineClass(name, classBytes, 0, classBytes.length);
      }
      return super.findClass(name);
    }
  }

}