package ytuploader.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import ytuploader.oauth.io.SimpleHTTP;
import ytuploader.oauth.json.Auth;
import ytuploader.oauth.json.Code;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OAuth2 {
  private String clientId;
  private String clientSecret;

  private String accessToken;
  private String refreshToken;
  private String tokenType;
  private long expires;

  private String deviceCode;


  public OAuth2(String clientId, String clientSecret) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }

  public OAuth2(String clientId, String clientSecret, String refreshToken) throws IOException {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.refreshToken = refreshToken;
    this.expires = 0;

    this.refresh();
  }


  private OAuth2 refresh() throws IOException {
    if (this.expires < System.currentTimeMillis()) {
      SimpleHTTP http = new SimpleHTTP();

      Map<String, String> post = new HashMap<>();
      post.put("client_id", this.clientId);
      post.put("client_secret", this.clientSecret);
      post.put("refresh_token", this.refreshToken);
      post.put("grant_type", "refresh_token");

      Auth auth = new ObjectMapper().readValue(
        http.post(
          "https://www.googleapis.com/oauth2/v3/token",
          post
        ),
        Auth.class
      );
      http.close();

      this.accessToken = auth.access_token;
      this.tokenType = auth.token_type;
      this.expires = System.currentTimeMillis() + (auth.expires_in - 60) * 1000;

      if (auth.refresh_token != null) {
        this.refreshToken = auth.refresh_token;
      }
    }

    return this;
  }


  public String getAccessToken() throws IOException {
    return this.refresh().accessToken;
  }

  public String getRefreshToken() {
    return this.refreshToken;
  }

  public String getTokenType() {
    return this.tokenType;
  }

  public String getHeader() throws IOException {
    return this.getTokenType() + " " + this.getAccessToken();
  }


  public String getCode() throws IOException {
    SimpleHTTP http = new SimpleHTTP();

    Map<String, String> post = new HashMap<>();
    post.put("client_id", this.clientId);
    post.put("scope", "https://www.googleapis.com/auth/youtube.upload");

    Code code = new ObjectMapper().readValue(
      http.post(
        "https://accounts.google.com/o/oauth2/device/code",
        post
      ),
      Code.class
    );
    http.close();

    this.deviceCode = code.device_code;
    return code.user_code;
  }

  public boolean check() throws IOException {
    Map<String, String> post = new HashMap<>();
    post.put("client_id", this.clientId);
    post.put("client_secret", this.clientSecret);
    post.put("code", this.deviceCode);
    post.put("grant_type", "http://oauth.net/grant_type/device/1.0");

    Auth auth = new ObjectMapper().readValue(
      new SimpleHTTP().post(
        "https://www.googleapis.com/oauth2/v3/token",
        post
      ),
      Auth.class
    );

    if (auth.error == null) {
      this.accessToken = auth.access_token;
      this.tokenType = auth.token_type;
      this.refreshToken = auth.refresh_token;
      this.expires = System.currentTimeMillis() + (auth.expires_in - 60) * 1000;

      return true;
    }
    else {
      return false;
    }
  }
}
