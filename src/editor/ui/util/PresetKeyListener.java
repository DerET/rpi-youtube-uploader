package editor.ui.util;

import data.Presets;
import editor.ui.content.PresetSelector;

import javax.swing.*;

public abstract class PresetKeyListener  {
  private PresetSelector selector;
  private JButton r, s;

  public PresetKeyListener(PresetSelector selector, JButton r, JButton s) {
    this.selector = selector;
    this.r = r;
    this.s = s;
  }

  protected Presets.Preset getPreset() {
    this.r.setEnabled(true);
    this.s.setEnabled(true);
    return (Presets.Preset) this.selector.getSelectedItem();
  }
}
