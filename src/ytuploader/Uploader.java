package ytuploader;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import data.OAuth;
import data.Presets;
import data.Settings;
import ytuploader.oauth.OAuth2;
import ytuploader.ui.DefaultUploadEvent;
import ytuploader.util.RegularExpression;
import ytuploader.youtube.YouTube;
import ytuploader.youtube.data.Upload;
import ytuploader.youtube.data.Video;
import ytuploader.youtube.exceptions.UploadException;
import ytuploader.youtube.io.UploadEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Uploader {
  public static final String FILE_SETTINGS = "settings.json";
  public static final String FILE_OAUTH = "oauth.json";
  public static final String FILE_PRESETS = "presets.json";
  public static final String FILE_UPLOAD = "upload.json";

  public Uploader() throws IOException, InterruptedException {
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

        for (File file : new File(settings.getPath()).listFiles()) {
          if (
            file.isFile()
              && file.getName().lastIndexOf(".") != -1
              && settings.contains(file.getName().substring(file.getName().lastIndexOf(".") + 1))
              && file.lastModified() < System.currentTimeMillis() - 120000
            ) {
            long size = file.length();
            Thread.sleep(5050);

            if (file.length() == size) {
              System.out.printf("Uploading %s...", file.getName());
              Video video = new Video();

              if (new File(FILE_PRESETS).exists()) {
                Presets presets = new ObjectMapper().readValue(new File(FILE_PRESETS), Presets.class);
                boolean run = true;

                for (int i = 0; i < presets.list.size() && run; i++) {
                  Presets.Preset preset = presets.list.get(i);
                  Matcher matcher = Pattern.compile(preset.pattern).matcher(file.getName());

                  if (matcher.find()) {
                    run = false;

                    if (preset.title != null) {
                      video.snippet.title = RegularExpression.replaceCallback(preset.title, matcher);
                    }
                    if (preset.description != null) {
                      video.snippet.description = RegularExpression.replaceCallback(preset.description, matcher);
                    }
                    if (preset.tags != null) {
                      for (int k = 0; k < preset.tags.length; k++) {
                        preset.tags[k] = RegularExpression.replaceCallback(preset.tags[k], matcher);
                      }

                      video.snippet.tags = preset.tags;
                    }
                    if (preset.category != null) {
                      video.snippet.categoryId = preset.category;
                    }
                  }
                }
              }

              try {
                Upload upload = yt.prepareUpload(file, video);
                new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValue(new File(FILE_UPLOAD), upload);

                yt.upload(upload, event, settings.getSpeedLimit());
                new File("upload.json").delete();
                file.renameTo(new File(file.getPath() + ".up"));
                System.out.println(" done");
              }
              catch (UploadException e) {
                file.renameTo(new File(file.getPath() + ".er"));

                System.out.println(" upload failed");
                for (UploadException.Error2 e2 : e.error.errors) {
                  System.out.println("  -> " + e2.message);
                }
              }
            }
          }
        }
      }
      catch (JsonMappingException e) {
        System.out.println(" error (" + e.getMessage() + ")");
      }
      catch (IOException e) {
        System.out.println(" aborted (connection error)");
      }

      Thread.sleep(60000);
    }
  }
}
