package editor.ui.content;

import editor.ui.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateButton extends JButton implements ActionListener {
  private MainWindow window;

  public CreateButton(MainWindow window) {
    this.window = window;
    this.setText("New");
    this.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String name = JOptionPane.showInputDialog("Preset Name");
    if (name != null) {
      this.window.addPreset(name);
    }
  }
}
