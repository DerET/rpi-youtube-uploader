package editor.ui.content;

import editor.ui.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DownButton extends JButton implements ActionListener {
  private MainWindow window;

  public DownButton(MainWindow window) {
    this.window = window;
    this.setText("Down");
    this.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    this.window.swapNext();
  }
}
