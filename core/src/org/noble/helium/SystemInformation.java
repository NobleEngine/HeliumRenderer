package org.noble.helium;

import com.badlogic.gdx.Gdx;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.Display;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.OperatingSystemMXBean;

public class SystemInformation {
  private static SystemInformation m_instance;
  private final String m_osName;
  private final String m_osVersion;
  private final String m_cpuName;
  private final String m_gpuName;
  private final String m_gpuVendor;
  private final String m_javaVersion;
  private final String m_javaVendor;
  private final String m_manufacturer;
  private final int m_cpuCores;
  private final int m_ramMB;
  private final int m_glVersionMajor;
  private final int m_glVersionMinor;

  private SystemInformation() {
    SystemInfo si = new SystemInfo();
    HardwareAbstractionLayer hal = si.getHardware();
    OperatingSystem os = si.getOperatingSystem();
    CentralProcessor cpu = hal.getProcessor();
    ComputerSystem system = hal.getComputerSystem();

    m_osName = os.getFamily();
    m_manufacturer = system.getManufacturer();
    m_osVersion = os.getVersionInfo().getVersion();
    m_cpuName = cpu.getProcessorIdentifier().getName();
    m_cpuCores = cpu.getLogicalProcessorCount();
    m_gpuName = Gdx.graphics.getGLVersion().getRendererString();
    m_gpuVendor = Gdx.graphics.getGLVersion().getVendorString();
    m_javaVersion = System.getProperty("java.version");
    m_javaVendor = System.getProperty("java.vendor");
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

  public String getOSName() {
    return m_osName;
  }

  public String getCPUName() {
    return m_cpuName;
  }

  public String getGPUName() {
    return m_gpuName;
  }

  public String getGPUVendor() {
    return m_gpuVendor;
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

  public String getJavaVendor() {
    return m_javaVendor;
  }

  public String getOSVersion() {
    return m_osVersion;
  }

  public String getSystemManufacturer() {
    return m_manufacturer;
  }
}
