package org.noble.helium.logic;

import java.util.HashMap;
import java.util.Map;

public class CopperValues {
  private final Map<String, Map<String, Object>> m_objects;
  public CopperValues() {
    m_objects = new HashMap<>();
  }

  public void setObjectVariable(String objName, String varName, Object val) {
    if(m_objects.get(objName) == null) {
      Map<String, Object> varMap = m_objects.getOrDefault(objName, new HashMap<>());
      m_objects.put(objName, varMap);
    }
    m_objects.get(objName).put(varName, val);
  }

  public Object getObjectVariable(String objName, String varName) {
    Map<String, Object> varMap = m_objects.get(objName);
    if(varMap != null) {
      return varMap.get(varName);
    }
    return null;
  }
}
