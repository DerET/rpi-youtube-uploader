package ytuploader.youtube.data;

public class Video {
  public final Snippet snippet;
  public final Status status;

  public static class Snippet {
    public final String title;
    public final String description;
    public final String[] tags;
    public final int categoryId;

    public Snippet(String title, String description, String[] tags, int categoryId) {
      this.title = title;
      this.description = description;
      this.tags = tags;
      this.categoryId = categoryId;
    }
  }

  public static class Status {
    public final String privacyStatus;
    public final boolean embeddable;
    public final String license;

    public Status(String privacyStatus, boolean embeddable, String license) {
      this.privacyStatus = privacyStatus;
      this.embeddable = embeddable;
      this.license = license;
    }
  }

  public Video(String title) {
    this.snippet = new Snippet(
      title,
      "automatically uploaded",
      new String[0],
      20
    );
    this.status = new Status(
      "private",
      true,
      "youtube"
    );
  }
}
