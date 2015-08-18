package data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Settings {
  @JsonProperty("clientId")
  private String clientId;
  @JsonProperty("clientSecret")
  private String clientSecret;
  private String path;
  private Set<String> extensions;
  @JsonProperty("speedlimit")
  private long speedlimit;


  @JsonProperty("path")
  public void set(String value) {
    while (value.endsWith("\\") || value.endsWith("/")) {
      value = value.substring(0, value.length() - 1);
    }

    this.path = value;
  }

  @JsonProperty("extensions")
  public void set(String[] value) {
    this.extensions = new HashSet<>();

    for (String v : value) {
      this.extensions.add(v);
    }
  }


  public String getClientId() {
    return this.clientId;
  }

  public String getClientSecret() {
    return this.clientSecret;
  }

  public String getPath() {
    return this.path;
  }

  public long getSpeedLimit() {
    return this.speedlimit;
  }

  public boolean contains(String extension) {
    return this.extensions.contains(extension);
  }


  public static Settings read(String path) throws IOException {
    return new ObjectMapper().readValue(new File(path), Settings.class);
  }
}
