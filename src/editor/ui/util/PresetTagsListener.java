package editor.ui.util;

import editor.ui.content.PresetSelector;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PresetTagsListener extends PresetKeyListener implements KeyListener {
  public PresetTagsListener(PresetSelector selector, JButton r, JButton s) {
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
    this.getPreset().tags = ((JTextPane) e.getSource()).getText().trim().split(" *, *");
  }
}
