package editor.ui.util;

import editor.ui.content.PresetSelector;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PresetTitleListener extends PresetKeyListener implements KeyListener {
  public PresetTitleListener(PresetSelector selector, JButton r, JButton s) {
    super(selector, r, s);
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
    this.getPreset().title = ((JTextField) e.getSource()).getText();
  }
}
