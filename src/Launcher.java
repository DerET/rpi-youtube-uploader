import editor.PresetEditor;
import ytuploader.Uploader;

import java.awt.*;
import java.io.IOException;

public class Launcher {
  public static void main(String[] args) throws IOException, InterruptedException {
    if (GraphicsEnvironment.isHeadless() || (args.length > 0 && args[0].equals("nogui"))) {
      new Uploader();
    }
    else {
      new PresetEditor();
    }
  }
}
