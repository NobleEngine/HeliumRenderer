package org.noble.helium;

import com.badlogic.gdx.Gdx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.OperatingSystemMXBean;

public class SystemInformation {
  private static SystemInformation m_instance;
  private final String m_osName;
  private final String m_osVersion;
  private final String m_cpuName;
  private final String m_cpuArch;
  private final String m_gpuName;
  private final String m_gpuVendor;
  private final String m_javaVersion;
  private final int m_cpuCores;
  private final int m_ramMB;
  private final int m_glVersionMajor;
  private final int m_glVersionMinor;

  private SystemInformation() {
    //TODO: Figure out OSHI to get GPU memory and better CPU stats
    OperatingSystemMXBean osBean = java.lang.management.ManagementFactory.getOperatingSystemMXBean();
    m_osName = osBean.getName();
    m_osVersion = osBean.getVersion();
    m_cpuName = fetchCPUname();
    m_cpuArch = osBean.getArch();
    m_cpuCores = Runtime.getRuntime().availableProcessors();
    m_gpuName = Gdx.graphics.getGLVersion().getRendererString();
    m_gpuVendor = Gdx.graphics.getGLVersion().getVendorString();
    m_javaVersion = System.getProperty("java.version");
    m_ramMB = (int) (Runtime.getRuntime().maxMemory() / 1024 / 1024);
    m_glVersionMajor = Gdx.graphics.getGLVersion().getMajorVersion();
    m_glVersionMinor = Gdx.graphics.getGLVersion().getMinorVersion();
  }

  public static SystemInformation getInstance() {
    if (m_instance == null) {
      m_instance = new SystemInformation();
    }
    return m_instance;
  }

  private String fetchCPUname() {
    String name = "Unknown";
    try {
      if (m_osName.contains("Windows")) {
        Process process = Runtime.getRuntime().exec("wmic cpu get Name");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        reader.readLine(); // Skip header
        if ((line = reader.readLine()) != null) {
          name = line.trim();
        }
      } else {
        Process process = Runtime.getRuntime().exec("lscpu");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
          if (line.contains("Model name:")) {
            name = line.split(":")[1].trim();
            break;
          }
        }
      }
    } catch (Exception e) {
      HeliumIO.error("System Information", e, HeliumIO.ErrorType.NONFATAL, true);
    }
    return name;
  }

  public String getOSName() {
    return m_osName;
  }

  public String getCPUName() {
    return m_cpuName;
  }

  public String getCPUArch() {
    return m_cpuArch;
  }

  public String getGPUName() {
    return m_gpuName;
  }

  public String getGPUVendor() {
    return m_gpuVendor;
  }

  public int getCPUCores() {
    return m_cpuCores;
  }

  public int getRAMMB() {
    return m_ramMB;
  }

  public int getGLVersionMajor() {
    return m_glVersionMajor;
  }

  public int getGLVersionMinor() {
    return m_glVersionMinor;
  }

  public String getJavaVersion() {
    return m_javaVersion;
  }

  public String getOSVersion() {
    return m_osVersion;
  }
}
