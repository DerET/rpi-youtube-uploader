package editor.ui.content;

import data.Presets;
import editor.ui.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PresetSelector extends JComboBox<Presets.Preset> implements ActionListener {
  private MainWindow window;
  private DefaultComboBoxModel<Presets.Preset> model;

  public PresetSelector(MainWindow window, List<Presets.Preset> presets) {
    this.window = window;
    this.model = new DefaultComboBoxModel<>();

    this.setModel(this.model);
    this.addActionListener(this);

    for (Presets.Preset preset : presets) {
      this.model.addElement(preset);
    }
  }

  public void add(Presets.Preset preset) {
    this.model.addElement(preset);
    this.model.setSelectedItem(preset);
  }

  public Presets.Preset remove() {
    Presets.Preset preset = (Presets.Preset) this.model.getSelectedItem();
    this.model.removeElement(this.model.getSelectedItem());

    return preset;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (this.model.getSelectedItem() != null) {
      this.window.draw((Presets.Preset) this.model.getSelectedItem());
    }
  }
}
