package editor.ui.content;

import data.Presets;
import editor.ui.MainWindow;
import ytuploader.util.RegularExpression;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuBar extends JMenuBar {
  private String lastFilename;

  private JMenu file;
  private JMenuItem file_exit;

  private JMenu tools;
  private JMenuItem tools_test;

  private JMenu help;
  private JMenuItem help_report;

  public MenuBar(final MainWindow window, final List<Presets.Preset> list, final CategorySelector selector) {
    this.lastFilename = "";

    this.file = new JMenu("File");
    this.file_exit = new JMenuItem("Exit");
    this.file_exit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        window.dispose();
      }
    });

    this.file.add(this.file_exit);
    this.add(this.file);

    this.tools = new JMenu("Tools");
    this.tools_test = new JMenuItem("Test Filename");
    this.tools_test.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String filename = JOptionPane.showInputDialog(null, "Filename:", lastFilename);

        if (filename != null) {
          lastFilename = filename;

          for (int i = 0; i < list.size(); i++) {
            Presets.Preset preset = list.get(i);
            Matcher matcher = Pattern.compile(preset.pattern).matcher(filename);

            if (matcher.find()) {
              String title = filename;
              if (preset.title != null) {
                title = RegularExpression.replaceCallback(preset.title, matcher);
              }

              String category = "Default";
              if (preset.category != null) {
                category = selector.getCategoryNameById(preset.category);
              }

              String description = "";
              if (preset.description != null) {
                description = RegularExpression.replaceCallback(preset.description, matcher);
              }

              String tags = "";
              if (preset.tags != null) {
                tags = RegularExpression.replaceCallback(preset.getTags(), matcher);
              }

              JOptionPane.showMessageDialog(
                null,
                String.format(
                  "Title: %s\nCategory: %s\n\nDescription:\n%s\n\nTags:\n%s",
                  title,
                  category,
                  description,
                  tags
                ),
                "match found using " + preset.label,
                JOptionPane.INFORMATION_MESSAGE
              );

              return;
            }
          }

          JOptionPane.showMessageDialog(
            null,
            "no match found"
          );
        }
      }
    });

    this.tools.add(this.tools_test);
    this.add(this.tools);

    this.help = new JMenu("Help");
    this.help_report = new JMenuItem("Report Issue");

    this.help_report.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
          try {
            Desktop.getDesktop().browse(new URI("https://github.com/DerET/rpi-youtube-uploader"));
            return;
          }
          catch (Exception e1) {
            e1.printStackTrace();
          }
        }

        JOptionPane.showMessageDialog(null, "An error occurred", "", JOptionPane.ERROR_MESSAGE);
      }
    });

    this.help.add(this.help_report);
    this.add(this.help);
  }
}
