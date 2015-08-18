package data;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.List;

public class Presets {
  public List<Preset> list;

  public static class Preset {
    public String label;
    public String pattern;
    public String title;
    public String description;
    public String[] tags;
    public Integer category;

    @JsonProperty("tags")
    public void set(String tags) {
      if (tags == null) {
        this.tags = new String[0];
      }
      else {
        this.tags = tags.trim().split(" *, *");
      }
    }

    @JsonGetter("tags")
    public String getTags() {
      if (this.tags != null && this.tags.length > 0) {
        StringBuilder builder = new StringBuilder();

        for (String tag : this.tags) {
          builder.append(tag + ", ");
        }

        builder.delete(builder.length() - 2, builder.length());
        return builder.toString();
      }

      return "";
    }

    @JsonGetter("category")
    public Integer getCategory() {
      if (this.category == -1) {
        return null;
      }

      return this.category;
    }

    @Override
    public Preset clone() {
      Preset preset = new Preset();
      preset.pattern = this.pattern;
      preset.label = this.label;
      preset.title = this.title;
      preset.description = this.description;
      preset.tags = this.tags;
      preset.category = this.category;

      return preset;
    }

    @Override
    public String toString() {
      return this.label;
    }
  }

  public Presets() {
    this.list = new ArrayList<>();
  }
}
