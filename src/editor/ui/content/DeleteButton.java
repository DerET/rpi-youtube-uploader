package editor.ui.content;

import editor.ui.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteButton extends JButton implements ActionListener {
  private MainWindow window;

  public DeleteButton(MainWindow window) {
    this.window = window;
    this.setText("Delete");
    this.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    this.window.deletePreset();
  }
}
