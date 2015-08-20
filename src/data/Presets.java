package data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    public Preset() {
      this.label = "Unnamed Preset";
      this.pattern = "";
      this.category = -1;
    }

    @JsonProperty("pattern")
    public void setPattern(String pattern) {
      this.pattern = (pattern == null ? "" : pattern);
    }

    @JsonProperty("title")
    public void setTitle(String title) {
      this.title = (title == null || title.trim().equals("") ? null : title);
    }

    @JsonProperty("tags")
    public void setTags(String tags) {
      this.tags = (tags == null ? new String[0] : tags.trim().split(" *, *"));
    }

    @JsonProperty("category")
    public void setCategory(Integer category) {
      this.category = (category == null ? -1 : category);
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
      return (this.category == -1 ? null : this.category);
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
