package editor;

import editor.ui.MainWindow;

import java.io.File;
import java.io.IOException;

public class PresetEditor {
  public static final String FILE_PRESETS = "presets.json";

  public PresetEditor() throws InterruptedException, IOException {
    MainWindow window = new MainWindow(new File(FILE_PRESETS));

    synchronized (window) {
      window.wait();
    }
  }
}
