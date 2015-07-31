package ytuploader.youtube;

import com.fasterxml.jackson.databind.ObjectMapper;
import ytuploader.oauth.OAuth2;
import ytuploader.youtube.data.Upload;
import ytuploader.youtube.data.Video;
import ytuploader.youtube.io.SimpleHTTP;
import ytuploader.youtube.io.UploadEvent;
import ytuploader.youtube.io.UploadStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class YouTube {
  private OAuth2 oAuth2;

  public YouTube(OAuth2 oAuth2) {
    this.oAuth2 = oAuth2;
  }

  public Upload prepareUpload(File file) throws IOException {
    SimpleHTTP http = new SimpleHTTP();

    String body = new ObjectMapper().writeValueAsString(
      new Video(
        file.getName()
      )
    );

    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", this.oAuth2.getHeader());
    headers.put("Content-Type", "application/json; charset=UTF-8");
    headers.put("X-Upload-Content-Length", String.valueOf(file.length()));
    headers.put("X-Upload-Content-Type", "video/*");

    Upload url = new Upload(
      http.post(
        "https://www.googleapis.com//upload/youtube/v3/videos?uploadType=resumable&part=snippet,status",
        headers,
        body
      ),
      file
    );

    http.close();
    return url;
  }

  public void upload(Upload upload, UploadEvent event, long limit) throws IOException {
    SimpleHTTP http = new SimpleHTTP();

    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", this.oAuth2.getHeader());
    headers.put("Content-Type", "video/*");

    UploadStream stream = new UploadStream(upload.file, event);
    stream.setSpeedLimit(limit);
    http.put(upload.url, headers, stream);

    stream.close();
    http.close();
  }

  public void resumeUpload(Upload upload, UploadEvent event, long limit) throws IOException {
    SimpleHTTP http = new SimpleHTTP();

    long uploaded;
    {
      Map<String, String> headers = new HashMap<>();
      headers.put("Authorization", this.oAuth2.getHeader());
      headers.put("Content-Range", "bytes */" + String.valueOf(upload.file.length()));

      uploaded = http.put(upload.url, headers);
    }

    if (uploaded == 0) {
      this.upload(upload, event, limit);
    }
    else {
      long length = upload.file.length();

      Map<String, String> headers = new HashMap<>();
      headers.put("Authorization", this.oAuth2.getHeader());
      headers.put("Content-Range", String.format("bytes %d-%d/%d", uploaded, length - 1, length));

      UploadStream stream = new UploadStream(upload.file, event, uploaded);
      stream.setSpeedLimit(limit);
      http.put(upload.url, headers, stream);
      stream.close();
    }

    http.close();
  }
}
