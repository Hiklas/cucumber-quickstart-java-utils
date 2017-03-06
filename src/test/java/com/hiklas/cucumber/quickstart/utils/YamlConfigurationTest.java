package com.hiklas.cucumber.quickstart.utils;

import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@SuppressWarnings({"rawtypes"})
public class YamlConfigurationTest {

  public static final String TEST_COMMON_FILE = "test-common";
  public static final String TEST_ENVIRONMENT = "test-discworld";
  public static final String TEST_STRING = "test-string";
  
  public static final String TEST_BASE_URL_FROM_COMMON = "localhost:8700";
  
  public static final String TEST_BASE_URL_FROM_DISCWORLD = "ankhmorpork:8700";
  
  public static final String TEST_ELEMENT_KEY = "User Details Header";
  public static final String TEST_MESSAGE_KEY = "not_registered";
  public static final String TEST_SCREEN_KEY = "User Details";
  
  public static final String TEST_MESSAGE_RESULT = "This customer is not yet registered";
  public static final String TEST_ELEMENT_RESULT = "user-details-header";
  
  public static final String TEST_SCREEN_RESULT = "/user";
  public static final String TEST_TITLE_RESULT = "User details";
  public static final String TEST_GET_HERE_RESULT = "POST";
  
  public static final String TEST_BROWSER_RESULT = "FIREFOX";
  
  public static final String TEST_STRING_KEY = "json_schema";
  public static final String TEST_MAP_KEY = "webpage_client";
  public static final String TEST_NULL_KEY = "CaptainAngua";
  
  public static final String TEST_ELEMENT_GROUP_KEY = "Discworld";
  
  
  private YamlConfiguration yamlConfigToTest;
  
  @Before
  public void setupObjectToTest() {
    yamlConfigToTest = new YamlConfiguration();
    setCommonProperty(null);
    setEnvironmentProperty(null);
  }
  
  @Test
  public void test_get_common_filename_default() {
    String commonFilename = yamlConfigToTest.getCommonYamlFilename();
    assertThat(commonFilename, 
        equalTo("/" + YamlConfiguration.DEFAULT_COMMON_FILE + YamlConfiguration.FILE_EXTENSION));
  }
  
  @Test
  public void test_get_environment_filename_default() {
    String environmentFilename = yamlConfigToTest.getEnvironmentYamlFilename();
    assertThat(environmentFilename, 
        equalTo("/" + YamlConfiguration.DEFAULT_ENVIRONMENT_FILE + YamlConfiguration.FILE_EXTENSION));
  }
  
  @Test
  public void test_get_common_inputstream() throws Exception {
    
    setCommonProperty(TEST_COMMON_FILE);
    
    InputStream stream = null;
    try {
      stream = yamlConfigToTest.getCommonYamlFileResourceStream();
      assertThat(stream, notNullValue());
    } finally {
      if (stream!=null) {
        stream.close();
      }
    }
  }
  

  @Test
  public void test_get_environment_inputstream() throws Exception {
    
    setEnvironmentProperty(TEST_ENVIRONMENT);
    
    InputStream stream = null;
    try {
      stream = yamlConfigToTest.getEnvironmentYamlFileResourceStream();
      assertThat(stream, notNullValue());
    } finally {
      if (stream!=null) {
        stream.close();
      }
    }
  } 
  
  @Test
  public void test_get_common_inputstream_default() throws Exception {
    
    InputStream stream = null;
    try {
      stream = yamlConfigToTest.getCommonYamlFileResourceStream();
      assertThat(stream, nullValue());
    } finally {
      if (stream!=null) {
        stream.close();
      }
    }
  }
  
  @Test
  public void test_get_environment_inputstream_default() throws Exception {
    
    InputStream stream = null;
    try {
      stream = yamlConfigToTest.getEnvironmentYamlFileResourceStream();
      assertThat(stream, nullValue());
    } finally {
      if (stream!=null) {
        stream.close();
      }
    }
  }
  
  @Test
  public void test_load_common_file() throws Exception {
    
    setCommonProperty(TEST_COMMON_FILE);
    
    InputStream stream = null;
    try {
      stream = yamlConfigToTest.getCommonYamlFileResourceStream();
      assertThat(stream, notNullValue());
      Object yamlObject = yamlConfigToTest.load(stream);
      assertThat(yamlObject, notNullValue());
      assertThat(yamlObject, instanceOf(Map.class));
    } finally {
      if (stream!=null) {
        stream.close();
      }
    }
  }
  
  
  @Test
  public void test_ask_for_map_but_string() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    
    Map configurationMap = yamlConfigToTest.loadConfiguration();
    
    Map mapResult = yamlConfigToTest.readMapFromMap(configurationMap, TEST_STRING_KEY);
    assertThat(mapResult, notNullValue());
    assertThat(mapResult.size(), equalTo(0));
  }

  @Test
  public void test_ask_for_map_but_null() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    
    Map configurationMap = yamlConfigToTest.loadConfiguration();
    
    Map mapResult = yamlConfigToTest.readMapFromMap(configurationMap, TEST_NULL_KEY);
    assertThat(mapResult, notNullValue());
    assertThat(mapResult.size(), equalTo(0));
  }
  
  @Test
  public void test_ask_for_list_but_string() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    
    Map configurationMap = yamlConfigToTest.loadConfiguration();
    
    List listResult = yamlConfigToTest.readListFromMap(configurationMap, TEST_STRING_KEY);
    assertThat(listResult, notNullValue());
    assertThat(listResult.size(), equalTo(0));
  }

  @Test
  public void test_ask_for_list_but_null() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    
    Map configurationMap = yamlConfigToTest.loadConfiguration();
    
    List listResult = yamlConfigToTest.readListFromMap(configurationMap, TEST_NULL_KEY);
    assertThat(listResult, notNullValue());
    assertThat(listResult.size(), equalTo(0));
  }
  
  @Test
  public void test_ask_for_string_but_map() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    
    Map configurationMap = yamlConfigToTest.loadConfiguration();
    
    String stringResult = yamlConfigToTest.readStringFromMap(configurationMap, TEST_MAP_KEY);
    assertThat(stringResult, notNullValue());
    assertThat(stringResult.length(), equalTo(0));
  }

  @Test
  public void test_ask_for_string_but_null() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    
    Map configurationMap = yamlConfigToTest.loadConfiguration();
    
    String stringResult = yamlConfigToTest.readStringFromMap(configurationMap, TEST_NULL_KEY);
    assertThat(stringResult, notNullValue());
    assertThat(stringResult.length(), equalTo(0));
  }
  
  
  @Test
  public void test_load_environment_file() throws Exception {
    
    setEnvironmentProperty(TEST_ENVIRONMENT);
    
    InputStream stream = null;
    try {
      stream = yamlConfigToTest.getEnvironmentYamlFileResourceStream();
      assertThat(stream, notNullValue());
      Object yamlObject = yamlConfigToTest.load(stream);
      assertThat(yamlObject, notNullValue());
      assertThat(yamlObject, instanceOf(Map.class));
    } finally {
      if (stream!=null) {
        stream.close();
      }
    }
  }
  
  
  @Test
  public void test_load_yaml_common_file() throws Exception {
    
    setCommonProperty(TEST_COMMON_FILE);
    
    InputStream stream = null;
    try {
      stream = yamlConfigToTest.getCommonYamlFileResourceStream();
      assertThat(stream, notNullValue());
      Map yamlObject = yamlConfigToTest.loadYaml(stream);
      assertThat(yamlObject, notNullValue());
      assertThat(yamlObject.size(), greaterThan(0));
    } finally {
      if (stream!=null) {
        stream.close();
      }
    }
  }  
  
  @Test
  public void test_load_yaml_common_file_with_just_string() throws Exception {
    
    setCommonProperty(TEST_STRING);
    
    InputStream stream = null;
    try {
      stream = yamlConfigToTest.getCommonYamlFileResourceStream();
      assertThat(stream, notNullValue());
      Map yamlObject = yamlConfigToTest.loadYaml(stream);
      assertThat(yamlObject, notNullValue());
      assertThat(yamlObject.size(), equalTo(0));
    } finally {
      if (stream!=null) {
        stream.close();
      }
    }
  }    
  
  
  @Test
  public void test_load_configuration_only_common() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    
    Map yamlObject = yamlConfigToTest.loadConfiguration();
    
    assertThat(yamlObject, notNullValue());
    assertThat(yamlObject.size(), greaterThan(0));
    
    Map webpageClientMap = (Map)yamlObject.get(YamlConfiguration.WEBPAGE_CLIENT);
    String baseUrl = (String)webpageClientMap.get(YamlConfiguration.BASE_URL);
    
    assertThat(baseUrl, equalTo(TEST_BASE_URL_FROM_COMMON));
  }
  
  
  @Test
  public void test_load_configuration_common_and_environment() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    setEnvironmentProperty(TEST_ENVIRONMENT);
    
    Map yamlObject = yamlConfigToTest.loadConfiguration();
    
    assertThat(yamlObject, notNullValue());
    assertThat(yamlObject.size(), greaterThan(0));
    
    Map webpageClientMap = (Map)yamlObject.get(YamlConfiguration.WEBPAGE_CLIENT);
    String baseUrl = (String)webpageClientMap.get(YamlConfiguration.BASE_URL);
    
    assertThat(baseUrl, equalTo(TEST_BASE_URL_FROM_DISCWORLD));
  }  
  

  @Test
  public void test_load_configuration_only_common_get_base_url() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    
    yamlConfigToTest.loadConfiguration();
    
    String baseUrl = yamlConfigToTest.base_url();
    assertThat(baseUrl, equalTo(TEST_BASE_URL_FROM_COMMON));
  }
  
  
  @Test
  public void test_load_configuration_common_and_environment_get_base_url() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    setEnvironmentProperty(TEST_ENVIRONMENT);

    yamlConfigToTest.loadConfiguration();
    
    String baseUrl = yamlConfigToTest.base_url();
    assertThat(baseUrl, equalTo(TEST_BASE_URL_FROM_DISCWORLD));
  }  


  @Test
  public void test_load_configuration_common_and_environment_get_webpage_client() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    setEnvironmentProperty(TEST_ENVIRONMENT);

    yamlConfigToTest.loadConfiguration();
    
    Map webpageClient = yamlConfigToTest.webpage_client();
    assertThat(webpageClient.get(YamlConfiguration.BASE_URL), equalTo(TEST_BASE_URL_FROM_DISCWORLD));
  }  


  @Test
  public void test_load_configuration_common_and_environment_get_screen_info() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    setEnvironmentProperty(TEST_ENVIRONMENT);

    yamlConfigToTest.loadConfiguration();
    
    Map screenInfo = yamlConfigToTest.screen_info(TEST_SCREEN_KEY);
    assertThat(screenInfo, notNullValue());
  }  
  
  @Test
  public void test_load_configuration_common_and_environment_get_messages() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    setEnvironmentProperty(TEST_ENVIRONMENT);

    yamlConfigToTest.loadConfiguration();
    
    Map messages = yamlConfigToTest.messages();
    assertThat(messages, notNullValue());
  }  
  
  @Test
  public void test_load_configuration_common_and_environment_get_element_ids() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    setEnvironmentProperty(TEST_ENVIRONMENT);

    yamlConfigToTest.loadConfiguration();
    
    Map element_ids = yamlConfigToTest.element_ids();
    assertThat(element_ids, notNullValue());
  }  
  
  @Test
  public void test_load_configuration_common_and_environment_get_element_id() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    setEnvironmentProperty(TEST_ENVIRONMENT);

    yamlConfigToTest.loadConfiguration();
    
    String element_id = yamlConfigToTest.element_id(TEST_ELEMENT_KEY);
    assertThat(element_id, notNullValue());
    assertThat(element_id, equalTo(TEST_ELEMENT_RESULT));
  }  

  @Test
  public void test_load_configuration_common_and_environment_get_element_groups() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    setEnvironmentProperty(TEST_ENVIRONMENT);

    yamlConfigToTest.loadConfiguration();
    
    Map element_groups = yamlConfigToTest.element_groups();
    assertThat(element_groups, notNullValue());
    assertThat(element_groups.size(), equalTo(1));
  }  
  
  @Test
  public void test_load_configuration_common_and_environment_get_element_group() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    setEnvironmentProperty(TEST_ENVIRONMENT);

    yamlConfigToTest.loadConfiguration();
    
    List element_group = yamlConfigToTest.element_group(TEST_ELEMENT_GROUP_KEY);
    assertThat(element_group, notNullValue());
    assertThat(element_group.size(), equalTo(3));
  }  
    
  @Test
  public void test_load_configuration_common_and_environment_get_screen_name() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    setEnvironmentProperty(TEST_ENVIRONMENT);

    yamlConfigToTest.loadConfiguration();
    
    String screenTitle = yamlConfigToTest.title_for(TEST_SCREEN_KEY);
    assertThat(screenTitle, equalTo(TEST_TITLE_RESULT));
  }  

  @Test
  public void test_load_configuration_common_and_environment_get_screen_url() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    setEnvironmentProperty(TEST_ENVIRONMENT);

    yamlConfigToTest.loadConfiguration();
    
    String screenUrl = yamlConfigToTest.url_for(TEST_SCREEN_KEY);
    assertThat(screenUrl, equalTo(TEST_SCREEN_RESULT));
  }  
  
  @Test
  public void test_load_configuration_common_and_environment_get_here_by() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    setEnvironmentProperty(TEST_ENVIRONMENT);

    yamlConfigToTest.loadConfiguration();
    
    String getHereBy = yamlConfigToTest.get_here_by_for(TEST_SCREEN_KEY);
    assertThat(getHereBy, equalTo(TEST_GET_HERE_RESULT));
  }  

  @Test
  public void test_load_configuration_common_and_environment_form_data() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    setEnvironmentProperty(TEST_ENVIRONMENT);

    yamlConfigToTest.loadConfiguration();
    
    List formData = yamlConfigToTest.form_data_for(TEST_SCREEN_KEY);
    assertThat(formData, notNullValue());
    assertThat(formData.size(), equalTo(6));
  }  
  
  @Test
  public void test_load_configuration_common_and_environment_get_message() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    setEnvironmentProperty(TEST_ENVIRONMENT);

    yamlConfigToTest.loadConfiguration();
    
    String message = yamlConfigToTest.message(TEST_MESSAGE_KEY);
    assertThat(message, notNullValue());
    assertThat(message, equalTo(TEST_MESSAGE_RESULT));
  }  
  
  @Test
  public void test_load_configuration_common_and_environment_get_selenium() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    setEnvironmentProperty(TEST_ENVIRONMENT);

    yamlConfigToTest.loadConfiguration();
    
    Map selenium = yamlConfigToTest.selenium();
    assertThat(selenium, notNullValue());
    assertThat(selenium.size(), greaterThan(0));
  }  
  
  @Test
  public void test_load_configuration_common_and_environment_get_browser() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    setEnvironmentProperty(TEST_ENVIRONMENT);

    yamlConfigToTest.loadConfiguration();
    
    String browser = yamlConfigToTest.browser();
    assertThat(browser, notNullValue());
    assertThat(browser, equalTo(TEST_BROWSER_RESULT));
  }  
 
  @Test
  public void test_load_configuration_common_and_environment_get_check_for_ids() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    setEnvironmentProperty(TEST_ENVIRONMENT);

    yamlConfigToTest.loadConfiguration();
    
    Map check_for_ids = yamlConfigToTest.element_ids();
    assertThat(check_for_ids, notNullValue());
  }  

  
  @Test
  public void test_load_configuration_common_and_environment_get_check_for_id() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    setEnvironmentProperty(TEST_ENVIRONMENT);

    yamlConfigToTest.loadConfiguration();
    
    List check_for_id = yamlConfigToTest.check_for_ids_for(TEST_SCREEN_KEY);
    assertThat(check_for_id, notNullValue());
    assertThat(check_for_id.size(), equalTo(0));
  }  
  
  @Test
  public void test_load_configuration_common_and_environment_get_titles() throws Exception {
    setCommonProperty(TEST_COMMON_FILE);
    setEnvironmentProperty(TEST_ENVIRONMENT);

    yamlConfigToTest.loadConfiguration();
    
    Map titles = yamlConfigToTest.element_ids();
    assertThat(titles, notNullValue());
  }  

    
  
  /* *************** */
  /* PRIVATE METHODS */
  /* *************** */
    
  private void setCommonProperty(String value) {
    String key = YamlConfiguration.COMMON_FILE_PROPERTY;
    setOrRemoveProperty(key, value);
  }
  
  private void setEnvironmentProperty(String value) {
    String key = YamlConfiguration.TEST_ENVIRONMENT_PROPERTY;
    setOrRemoveProperty(key, value);
  }

  private void setOrRemoveProperty(String key, String value) {
    Properties properties = getSystemProperties();
    if (value == null) {
      properties.remove(key);
    } else {
      properties.setProperty(key, value);
    }
  }
  
  private Properties getSystemProperties() {
    return System.getProperties();
  }
}
