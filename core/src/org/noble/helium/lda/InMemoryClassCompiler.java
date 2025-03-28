package org.noble.helium.lda;

import com.badlogic.gdx.files.FileHandle;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.*;

public class InMemoryClassCompiler {
  private static final Map<String, byte[]> compiledClasses = new HashMap<>();

  // In-memory class loader for loading compiled classes
  public static class InMemoryClassLoader extends ClassLoader {
    private final Map<String, byte[]> classes;

    public InMemoryClassLoader(Map<String, byte[]> classes) {
      this.classes = classes;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
      byte[] bytecode = classes.get(name);
      if (bytecode == null) {
        throw new ClassNotFoundException(name);
      }
      return defineClass(name, bytecode, 0, bytecode.length);
    }
  }

  // Represents compiled .class files in memory
  private static class InMemoryJavaFileObject extends SimpleJavaFileObject {
    private final String className;

    protected InMemoryJavaFileObject(String className) {
      super(URI.create("mem:///" + className.replace('.', '/') + ".class"), Kind.CLASS);
      this.className = className;
    }

    @Override
    public OutputStream openOutputStream() {
      return new ByteArrayOutputStream() {
        @Override
        public void close() throws IOException {
          compiledClasses.put(className, toByteArray());
        }
      };
    }
  }

  // In-memory file manager for the Java compiler
  private static class MemoryFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    public MemoryFileManager(JavaFileManager fileManager) {
      super(fileManager);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
      return new InMemoryJavaFileObject(className);
    }
  }

  /**
   * Unzips a .zip file from a LibGDX FileHandle into memory, compiles all .java files, and returns a List<Class<?>>.
   *
   * @param zipFile LibGDX FileHandle pointing to the zip archive
   * @return List of compiled classes
   * @throws Exception If compilation or loading fails
   */
  public static List<Class<?>> unzipAndCompile(FileHandle zipFile) throws Exception {
    if(!compiledClasses.isEmpty()) {
      compiledClasses.clear();
    }

    // Read the zip file into memory
    Map<String, String> sourceFiles = new HashMap<>();
    try (ZipInputStream zipInput = new ZipInputStream(zipFile.read())) {
      ZipEntry entry;
      while ((entry = zipInput.getNextEntry()) != null) {
        if (entry.isDirectory() || !entry.getName().endsWith(".java")) {
          continue;
        }

        // Extract the .java file content
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = zipInput.read(buffer)) > 0) {
          output.write(buffer, 0, len);
        }

        // Store the source code in memory
        String className = extractClassName(entry.getName());
        String sourceCode = output.toString(StandardCharsets.UTF_8);
        sourceFiles.put(className, sourceCode);
      }
    }

    if (sourceFiles.isEmpty()) {
      throw new RuntimeException("No .java files found in zip!");
    }

    // Compile the Java files entirely in memory
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    if (compiler == null) {
      throw new IllegalStateException("No Java compiler available. Are you running with a JRE instead of a JDK?");
    }

    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
    StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnostics, null, null);
    MemoryFileManager fileManager = new MemoryFileManager(standardFileManager);

    List<JavaFileObject> compilationUnits = new ArrayList<>();
    for (Map.Entry<String, String> entry : sourceFiles.entrySet()) {
      String className = entry.getKey();
      String sourceCode = entry.getValue();

      compilationUnits.add(new SimpleJavaFileObject(
          URI.create("string:///" + className.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension),
          JavaFileObject.Kind.SOURCE) {
        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
          return sourceCode;
        }
      });
    }

    // Compile the scripts
    JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
    if (!task.call()) {
      diagnostics.getDiagnostics().forEach(System.err::println);
      throw new RuntimeException("Compilation failed!");
    }

    // Load the compiled classes
    InMemoryClassLoader classLoader = new InMemoryClassLoader(compiledClasses);
    List<Class<?>> loadedClasses = new ArrayList<>();

    for (String className : compiledClasses.keySet()) {
      loadedClasses.add(classLoader.loadClass(className));
    }

    return loadedClasses;
  }

  /**
   * Extracts the class name from the file path in the zip.
   * Example: "scripts/MyScript.java" → "MyScript"
   */
  private static String extractClassName(String filePath) {
    return filePath.substring(filePath.lastIndexOf("/") + 1).replace(".java","");
  }
}
