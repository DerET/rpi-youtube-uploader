package ytuploader.json;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Presets {
  public List<Preset> list;

  public static class Preset {
    public String pattern;
    public String name;
    public String description;
    public String[] tags;
    public Integer category;

    @JsonProperty("tags")
    public void set(String tags) {
      this.tags = tags.split(" *, *");
    }
  }

  public Presets() {
    this.list = new ArrayList<>();
  }

  @JsonAnySetter
  public void set(String key, Preset value) {
    value.pattern = key;
    this.list.add(value);
  }
}
