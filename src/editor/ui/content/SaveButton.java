package editor.ui.content;

import editor.ui.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveButton extends JButton implements ActionListener {
  private MainWindow window;

  public SaveButton(MainWindow window) {
    this.window = window;
    this.setText("Save");
    this.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    this.window.save();
  }
}
