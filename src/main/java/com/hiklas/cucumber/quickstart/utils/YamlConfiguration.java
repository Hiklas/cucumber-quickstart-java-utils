package com.hiklas.cucumber.quickstart.utils;


import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Load the configuration from Yaml files.
 * 
 * There are two files to load, one is common.yaml and the second is specific to 
 * the given environment.  The environment to test against is set in a system property.
 * The common filename can also be set using a property value.
 * 
 * Both of the files are loaded as resources from the classpath and are expected to be in the 
 * root/default package.  For this reason the path to them is constructed with a leading /.
 * Without this leading / the files don't get loaded.
 * 
 * The two properties that can be set to control which files are loaded are as follows
 * 
 *   common.yaml.config.file - defaults to 'common'
 *   test.environment.config.file - defaults to 'localhost'
 *   
 * The suffix .yaml is appended to the above filenames.
 * 
 * NOTE: Rather annoyingly the Yaml parser returns an object which can be Map, ArrayList
 * or String.  We only really care about Map but we can't use generics here as Java forgets 
 * about types at runtime.  Marking the whole class to suppress warnings for ease.  It's still 
 * yuck though.
 * 
 * @author Fiona Bianchi
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class YamlConfiguration {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(YamlConfiguration.class);
  
  public static final String COMMON_FILE_PROPERTY = "common.yaml.config.file";
  public static final String TEST_ENVIRONMENT_PROPERTY = "test.environment.config.file";
  
  public static final String DEFAULT_COMMON_FILE = "common";
  public static final String DEFAULT_ENVIRONMENT_FILE = "localhost";
  
  public static final String FILE_EXTENSION = ".yaml";
  
  //
  // Top-level keys 
  //
  public static final String WEBPAGE_CLIENT = "webpage_client";
  public static final String SCREENS = "screens";
  public static final String MESSAGES = "messages";
  public static final String ELEMENT_IDS = "element_ids";
  public static final String ELEMENT_GROUPS = "element_groups";
  
  //
  // Second-level keys
  //
  public static final String BASE_URL = "base_url";
  public static final String SELENIUM = "selenium";
  
  //
  // Third-level keys
  //
  public static final String BROWSER = "browser";
  public static final String URL = "url";
  public static final String TITLE = "title";
  public static final String CHECK_FOR_IDS = "check_for_ids";
  public static final String GET_HERE_BY = "get_here_by";
  public static final String FORM_DATA = "form_data";
  
  
  /**
   * Holds all the configuration values read from the Yaml configuration files
   */
  private Map yamlObjects; 
  
  public YamlConfiguration() {
    LOGGER.debug("Constructor called");
  }
  
  public Map webpage_client() {
    return readMapFromMap(yamlObjects, WEBPAGE_CLIENT);
  }
    
  public Map messages() {
    return readMapFromMap(yamlObjects, MESSAGES);
  }
  
  public Map screens() {
    return readMapFromMap(yamlObjects, SCREENS);
  }
  
  public Map screen_info(String screen_name) {
    return readMapFromMap(screens(), screen_name);
  }
  
  public String url_for(String screen_name) {
    return readStringFromMap(screen_info(screen_name), URL);
  }

  public String title_for(String screen_name) {
    return readStringFromMap(screen_info(screen_name), TITLE);
  }

  public List check_for_ids_for(String screen_name) {
    return readListFromMap(screen_info(screen_name), CHECK_FOR_IDS);
  }
  
  public String get_here_by_for(String screen_name) {
    return readStringFromMap(screen_info(screen_name), GET_HERE_BY);
  }
  
  public List form_data_for(String screen_name) {
    return readListFromMap(screen_info(screen_name), FORM_DATA);
  }

  public Map element_ids() {
    return readMapFromMap(yamlObjects, ELEMENT_IDS);
  }

  public String element_id(String element_key) {
    return readStringFromMap(element_ids(), element_key);
  }

  public Map element_groups() {
    return readMapFromMap(yamlObjects, ELEMENT_GROUPS);
  }
  
  public List element_group(String group_name) {
    return readListFromMap(element_groups(), group_name);
  }
  
  public String message(String message_key) {
    return readStringFromMap(messages(), message_key);
  }
    
  public String base_url() {
    return readStringFromMap(webpage_client(), BASE_URL);
  }
  
  public Map selenium() {
    return readMapFromMap(webpage_client(), SELENIUM);
  }
  
  public String browser() {
    return readStringFromMap(selenium(), BROWSER);
  }
  
  
  
  
  /**
   * Load the configuration in from the common.yaml and environment specific 
   * files.  
   * 
   * @return Map containing all of the loaded keys
   * @throws IOException 
   */
  public Map loadConfiguration() throws IOException {
    LOGGER.debug("Loading Yaml configuration files ...");
    yamlObjects = loadYaml(getCommonYamlFileResourceStream());
    yamlObjects.putAll(loadYaml(getEnvironmentYamlFileResourceStream()));
    LOGGER.debug("... loaded");
    return yamlObjects;
  }
  
  
  protected Map loadYaml(InputStream yamlInputStream) throws IOException {
    Map result = new HashMap<String, Object>();
    Object loadResult = null;
    try {
      loadResult = load(yamlInputStream);
      if (loadResult != null) { 
        result = (Map)loadResult;
      } else {
        LOGGER.debug("Yaml load returned null");
      }
    } catch (ClassCastException cce) {
      LOGGER.debug("Couldn't cast loaded data to Map type was: {}", loadResult.getClass().getName());
    }
    return result;
  }
  
  
  protected Object load(InputStream yamlInputStream) throws IOException {
    Object yamlResult = null;
    if (yamlInputStream != null) {
      yamlResult = readYamlFromInputStreamAndCloseStream(yamlInputStream);
    }
    return yamlResult;
  }
    
  
  protected InputStream getCommonYamlFileResourceStream() {
    String filename = getCommonYamlFilename();
    LOGGER.debug("Getting common input stream for file '{}'", filename);
    return this.getClass().getResourceAsStream(filename); 
  }
  
  protected InputStream getEnvironmentYamlFileResourceStream() {
    String filename = getEnvironmentYamlFilename();
    LOGGER.debug("Getting environment input stream for file '{} '", filename);
    return this.getClass().getResourceAsStream(filename); 
  }
  
  protected String getEnvironmentYamlFilename() {
    return getYamlFilenameFromSystemProperties(TEST_ENVIRONMENT_PROPERTY, DEFAULT_ENVIRONMENT_FILE);
  }
  
  protected String getCommonYamlFilename() {
    return getYamlFilenameFromSystemProperties(COMMON_FILE_PROPERTY, DEFAULT_COMMON_FILE);
  }

  
  protected Map readMapFromMap(Map mapToReadFrom, String keyToRead) {
    Map resultMap = new HashMap();
    Object resultObject = mapToReadFrom.get(keyToRead);
    if (resultObject instanceof Map) {
      resultMap = (Map)resultObject;
    } else if (resultObject !=null ){
      LOGGER.debug("Couldn't convert to map for key '{}', type was '{}'", 
          keyToRead, resultObject.getClass().getName());
    } else {
      LOGGER.debug("No value found for key '{}'", keyToRead);
    }
    return resultMap;
  }
  
  protected String readStringFromMap(Map mapToReadFrom, String keyToRead) {
    String resultString = "";
    Object resultObject = mapToReadFrom.get(keyToRead);
    if (resultObject instanceof String) {
      resultString = (String)resultObject;
    } else if (resultObject !=null ){
      LOGGER.debug("Couldn't convert to string for key '{}', type was '{}'", 
          keyToRead, resultObject.getClass().getName());
    } else {
      LOGGER.debug("No value found for key '{}'", keyToRead);
    }
    return resultString;
  }
  
  protected List readListFromMap(Map mapToReadFrom, String keyToRead) {
    List resultList = new ArrayList();
    Object resultObject = mapToReadFrom.get(keyToRead);
    if (resultObject instanceof List) {
      resultList = (List)resultObject;
    } else if (resultObject !=null ){
      LOGGER.debug("Couldn't convert to string for key '{}', type was '{}'", 
          keyToRead, resultObject.getClass().getName());
    } else {
      LOGGER.debug("No value found for key '{}'", keyToRead);
    }
    return resultList;
  }
  
  private Object readYamlFromInputStreamAndCloseStream(InputStream yamlInputStream) throws IOException {
    try {
      return readYamlFromInputStream(yamlInputStream);
    } catch (IOException ie) {
      LOGGER.debug("Failed to read from InputStream, exception: {}", ie);
      throw ie;
    } finally {
      closeStream(yamlInputStream);
    }
  }
  
  private Object readYamlFromInputStream(InputStream yamlInputStream) throws YamlException {
    YamlReader yamlReader = new YamlReader(new InputStreamReader(yamlInputStream));
    return yamlReader.read();
  }
  
  private void closeStream(InputStream inputStream) {
    try {
      inputStream.close();
    } catch (IOException ie) {
      LOGGER.error("Failed to close the inputstream");  
    } 
  }

  private String getYamlFilenameFromSystemProperties(String key, String defaultValue) {
    return "/" + System.getProperties().getOrDefault(key, defaultValue) + FILE_EXTENSION;
  }
}
