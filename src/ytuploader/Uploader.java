package ytuploader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ytuploader.json.OAuth;
import ytuploader.json.Settings;
import ytuploader.oauth.OAuth2;
import ytuploader.ui.DefaultUploadEvent;
import ytuploader.youtube.YouTube;
import ytuploader.youtube.data.Upload;
import ytuploader.youtube.io.UploadEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Uploader {
  public static final String FILE_SETTINGS = "settings.json";
  public static final String FILE_OAUTH = "oauth.json";
  public static final String FILE_UPLOAD = "upload.json";

  public static void main(String[] args) throws IOException, InterruptedException {
    // Disable commons-logging
    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

    // Settings
    Settings settings = Settings.read(FILE_SETTINGS);

    // OAuth
    OAuth2 o2;

    if (!new File("oauth.json").exists()) {
      o2 = new OAuth2(settings.getClientId(), settings.getClientSecret());
      System.out.printf("Browse google.com/device and enter the following code: %s\n", o2.getCode());

      do {
        Thread.sleep(5050);
      }
      while (!o2.check());

      new OAuth(
        o2.getRefreshToken()
      ).write(FILE_OAUTH);
    }
    else {
      o2 = new OAuth2(
        settings.getClientId(),
        settings.getClientSecret(),
        OAuth.read(FILE_OAUTH).refreshToken
      );
    }

    // File Upload
    YouTube yt = new YouTube(o2);
    UploadEvent event = new DefaultUploadEvent();

    while (true) {
      try {
        if (new File(FILE_UPLOAD).exists()) {
          File fu = new File(FILE_UPLOAD);
          Upload upload = new ObjectMapper().readValue(fu, ytuploader.youtube.data.Upload.class);

          try {
            System.out.printf("Resuming %s...", upload.file.getName());
            yt.resumeUpload(upload, event, settings.getSpeedLimit());

            upload.file.renameTo(new File(upload.file.getPath() + ".up"));
            System.out.println(" done");
          }
          catch (FileNotFoundException e) {
            System.out.println(" aborted (file not found)");
          }

          fu.delete();
        }

        for (File f : new File(settings.getPath()).listFiles()) {
          if (
            f.isFile()
              && f.getName().lastIndexOf(".") != -1
              && settings.contains(f.getName().substring(f.getName().lastIndexOf(".") + 1))
              && f.lastModified() < System.currentTimeMillis() - 120000
            ) {
            long size = f.length();
            Thread.sleep(5050);

            if (f.length() == size) {
              File file = new File(FILE_UPLOAD);

              Upload upload = yt.prepareUpload(f);
              new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValue(file, upload);
              System.out.printf("Uploading %s...", f.getName());

              yt.upload(upload, event, settings.getSpeedLimit());
              new File("upload.json").delete();
              f.renameTo(new File(f.getPath() + ".up"));
              System.out.println(" done");
            }
          }
        }
      }
      catch (IOException e) {
        System.out.println(" aborted (connection error)");
      }

      Thread.sleep(60000);
    }
  }
}
