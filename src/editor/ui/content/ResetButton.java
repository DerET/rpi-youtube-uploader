package editor.ui.content;

import data.Presets;
import editor.ui.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResetButton extends JButton implements ActionListener {
  private MainWindow window;
  private Presets.Preset preset;

  public ResetButton(MainWindow window) {
    this.window = window;
    this.setText("Reset");
    this.addActionListener(this);
  }

  public void setPreset(Presets.Preset preset) {
    this.preset = preset.clone();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    this.window.draw(this.preset);
  }
}
