package ytuploader.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public class OAuth {
  public final String refreshToken;


  public OAuth(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public OAuth() {
    this(null);
  }


  public static OAuth read(String path) throws IOException {
    return new ObjectMapper().readValue(new File(path), OAuth.class);
  }

  public void write(String path) throws IOException {
    new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValue(new File(path), this);
  }
}
