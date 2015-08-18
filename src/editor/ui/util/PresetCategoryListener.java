package editor.ui.util;

import data.Presets;
import editor.ui.content.CategorySelector;
import editor.ui.content.PresetSelector;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PresetCategoryListener implements ActionListener {
  private PresetSelector selector;
  private JButton r, s;

  public PresetCategoryListener(PresetSelector selector, JButton r, JButton s) {
    this.selector = selector;
    this.r = r;
    this.s = s;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    this.r.setEnabled(true);
    this.s.setEnabled(true);

    Presets.Preset preset = (Presets.Preset) this.selector.getSelectedItem();
    CategorySelector selector = (CategorySelector) e.getSource();
    CategorySelector.Category category = (CategorySelector.Category) selector.getSelectedItem();

    preset.category = category.id;
  }
}
