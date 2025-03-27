package org.noble.helium.lda;

import com.badlogic.gdx.files.FileHandle;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.noble.helium.subsystems.telemetry.HeliumTelemetry;

import javax.tools.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LDAExtractor {
  public static Map<String, JsonElement> getLDAElements(FileHandle LDA) throws IOException {
    return unzipAndParseJson(LDA.readBytes());
  }

  public static Map<String, Class<?>> getScripts(FileHandle LDA) {
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
      HeliumTelemetry.getInstance().println("Found Element - " + entry.getKey() + ": " + entry.getValue().getAsJsonObject());
    }

    return jsonFiles;
  }

  public static Map<String, Class<?>> unzipAndLoadClasses(FileHandle zipFileHandle) {
    Map<String, Class<?>> loadedClasses = new HashMap<>();

    try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipFileHandle.readBytes()))) {
      ZipEntry entry;

      // Iterate through the zip file
      while ((entry = zipInputStream.getNextEntry()) != null) {
        String entryName = entry.getName();

        // If it's a .java file, compile it
        if (entryName.endsWith(".java")) {
          // Read the .java file into a String
          String sourceCode = readSourceCodeFromZipEntry(zipInputStream);

          String className = entryName.substring(entryName.lastIndexOf("/") + 1).replace(".java","");

          // Compile the .java file and load the class into memory
          Class<?> compiledClass = compileAndLoadClass(className, sourceCode);

          // Store the compiled class
          loadedClasses.put(className, compiledClass);
        }
      }
    } catch (IOException | ClassNotFoundException e) {
      HeliumTelemetry.getInstance().printErrorln("Exception while compiling LDA scripts: " + e.getMessage());
      for(StackTraceElement element : e.getStackTrace()) {
        HeliumTelemetry.getInstance().println(element.toString());
      }
    }

    return loadedClasses;
  }

  private static String readSourceCodeFromZipEntry(ZipInputStream zipInputStream) throws IOException {
    // Read the entry's content into a String
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int bytesRead;

    while ((bytesRead = zipInputStream.read(buffer)) != -1) {
      outputStream.write(buffer, 0, bytesRead);
    }

    return outputStream.toString();
  }

  private static Class<?> compileAndLoadClass(String className, String sourceCode) throws MalformedURLException, ClassNotFoundException {
    // Prepare the JavaCompiler
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

    // set compiler's classpath to be same as the runtime
    List<String> optionList = new ArrayList<>(Arrays.asList("-classpath", System.getProperty("java.class.path")));

    // Prepare in-memory file using a custom JavaFileObject
    JavaFileObject file = new JavaSourceFromString(className, sourceCode);

    // Compile the source code
    boolean success = compiler.getTask(null, fileManager, null, optionList, null, List.of(file)).call();

    if (!success) {
      throw new RuntimeException("Compilation failed for " + className);
    }

    // Load the compiled class into memory
    return loadClass(className);
  }

  private static Class<?> loadClass(String className) throws MalformedURLException, ClassNotFoundException {
    URL[] urls = { new URL("file:///") }; // Use an empty URL for the classloader
    URLClassLoader classLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());

    return classLoader.loadClass(className);
  }

  public static class JavaSourceFromString extends SimpleJavaFileObject {
    private final String code;

    // Constructor
    protected JavaSourceFromString(String name, String code) {
      super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
      this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
      return code;
    }
  }



}