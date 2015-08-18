package editor.ui;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import editor.ui.content.*;
import editor.ui.content.MenuBar;
import editor.ui.util.*;
import data.Presets;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainWindow extends JFrame {
  private File file;
  private Presets presets;

  private PresetSelector selector;
  private CreateButton cbutton;
  private DeleteButton dbutton;
  private UpButton mubutton;
  private DownButton mdbutton;
  private JTextField pfield;
  private JTextField nfield;
  private JTextPane darea;
  private JTextPane tarea;
  private SaveButton sbutton;
  private ResetButton rbutton;
  private CategorySelector cselector;

  public MainWindow(File file) throws IOException {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }

    try {
      this.file = file;
      this.presets = new ObjectMapper().readValue(this.file, Presets.class);
    }
    catch (FileNotFoundException e) {
      this.presets = new Presets();
    }

    this.setTitle("Preset Editor");
    this.setLocationByPlatform(true);
    this.setMinimumSize(new Dimension(832, 468));
    this.setLayout(new GridBagLayout());

    this.pfield = new JTextField();
    this.pfield.setBorder(
      BorderFactory.createTitledBorder("Pattern")
    );
    this.add(this.pfield, new Constraints().setX(0).setY(1).setWidth(2));

    this.nfield = new JTextField();
    this.nfield.setBorder(
      BorderFactory.createTitledBorder("Title")
    );
    this.add(this.nfield, new Constraints().setX(0).setY(2).setWidth(2));

    this.darea = new JTextPane();
    {
      JScrollPane jsp = new JScrollPane(this.darea);
      jsp.setBackground(this.pfield.getBackground());
      jsp.setBorder(
        BorderFactory.createTitledBorder("Description")
      );
      this.add(jsp, new Constraints().setX(0).setY(3).setWidth(2).setVerticalWeight(2.0f).fillBoth());
    }

    this.tarea = new JTextPane();
    {
      JScrollPane jsp = new JScrollPane(this.tarea);
      jsp.setBackground(this.pfield.getBackground());
      jsp.setBorder(
        BorderFactory.createTitledBorder("Tags")
      );
      this.add(jsp, new Constraints().setX(0).setY(4).setWidth(2).setVerticalWeight(1.0f).fillBoth());
    }

    this.cselector = new CategorySelector();
    this.cselector.setBackground(this.pfield.getBackground());
    this.add(this.cselector, new Constraints().setX(0).setY(5).setWidth(2));

    this.sbutton = new SaveButton(this);
    this.rbutton = new ResetButton(this);
    {
      JPanel panel = new JPanel();
      panel.add(this.sbutton);
      panel.add(this.rbutton);
      panel.setBackground(this.pfield.getBackground());
      this.add(panel, new Constraints().setX(0).setY(6).setWidth(2).setBottomMargin(8));
    }

    this.cbutton = new CreateButton(this);
    this.mubutton = new UpButton(this);
    this.mdbutton = new DownButton(this);
    this.dbutton = new DeleteButton(this);
    {
      JPanel panel = new JPanel();
      panel.add(this.cbutton);
      panel.add(this.mubutton);
      panel.add(this.mdbutton);
      panel.add(this.dbutton);
      panel.setBackground(this.pfield.getBackground());
      this.add(panel, new Constraints().setX(1).setY(0).setLeftMargin(0));
    }

    this.selector = new PresetSelector(this, this.presets.list);
    this.selector.setBackground(this.pfield.getBackground());
    this.add(this.selector, new Constraints().setX(0).setY(0).setHorizontalWeight(1).setRightMargin(6));

    if (this.selector.getSelectedItem() != null) this.rbutton.setPreset((Presets.Preset) this.selector.getSelectedItem());
    this.pfield.addKeyListener(new PresetPatternListener(this.selector, this.rbutton, this.sbutton));
    this.nfield.addKeyListener(new PresetTitleListener(this.selector, this.rbutton, this.sbutton));
    this.darea.addKeyListener(new PresetDescriptionListener(this.selector, this.rbutton, this.sbutton));
    this.tarea.addKeyListener(new PresetTagsListener(this.selector, this.rbutton, this.sbutton));
    this.cselector.addActionListener(new PresetCategoryListener(this.selector, this.rbutton, this.sbutton));

    this.sbutton.setEnabled(false);
    this.setJMenuBar(new MenuBar(this, this.presets.list, this.cselector));

    this.getContentPane().setBackground(this.pfield.getBackground());
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setVisible(true);
  }


  public void draw(Presets.Preset preset) {
    if (preset != null) {
      this.rbutton.setEnabled(false);

      this.pfield.setText(preset.pattern);
      this.nfield.setText(preset.title);
      this.darea.setText(preset.description);
      this.cselector.select(preset.category == null ? -1 : preset.category);
      this.tarea.setText(preset.getTags());

      if (this.selector != null) {
        this.rbutton.setPreset((Presets.Preset) this.selector.getSelectedItem());
      }
    }

    if (this.presets.list.size() == 0 || preset == this.presets.list.get(0)) {
      this.mubutton.setEnabled(false);
    }
    else {
      this.mubutton.setEnabled(true);
    }

    if (this.presets.list.size() == 0 || preset == this.presets.list.get(this.presets.list.size() - 1)) {
      this.mdbutton.setEnabled(false);
    }
    else {
      this.mdbutton.setEnabled(true);
    }
  }

  public void addPreset(String label) {
    Presets.Preset preset = new Presets.Preset();
    preset.label = label;
    preset.pattern = this.pfield.getText();
    preset.title = this.nfield.getText();
    preset.description = this.darea.getText();
    preset.tags = this.tarea.getText().trim().split(" *, *");
    preset.category = ((CategorySelector.Category) this.cselector.getSelectedItem()).id;

    this.sbutton.setEnabled(true);
    this.rbutton.setEnabled(false);
    this.dbutton.setEnabled(true);

    this.presets.list.add(preset);
    this.selector.add(preset);
  }

  public void deletePreset() {
    this.sbutton.setEnabled(true);
    this.rbutton.setEnabled(false);

    if (this.presets.list.size() == 1) {
      this.dbutton.setEnabled(false);
    }

    this.presets.list.remove(this.selector.remove());
  }

  public void save() {
    try {
      new ObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        .enable(SerializationFeature.INDENT_OUTPUT)
        .writeValue(this.file, this.presets);

      this.rbutton.setEnabled(false);
      this.sbutton.setEnabled(false);
    }
    catch (IOException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, e.getMessage(), "An error occurred", JOptionPane.ERROR_MESSAGE);
    }

    if (this.selector.getSelectedItem() != null) {
      this.rbutton.setPreset((Presets.Preset) this.selector.getSelectedItem());
    }
  }

  public void swapNext() {
    int index = this.selector.getSelectedIndex();

    if (index < this.presets.list.size() - 1) {
      Presets.Preset preset = this.presets.list.get(index + 1);
      this.presets.list.set(index + 1, this.presets.list.get(index));
      this.presets.list.set(index, preset);

      this.selector.removeItemAt(index + 1);
      this.selector.insertItemAt(preset, index);
    }

    this.sbutton.setEnabled(true);

    if (index == this.presets.list.size()) {
      this.mubutton.setEnabled(false);
    }
    else {
      this.mubutton.setEnabled(true);
    }

    if (index == 0) {
      this.mdbutton.setEnabled(false);
    }
    else {
      this.mdbutton.setEnabled(true);
    }
  }

  public void swapPrevious() {
    int index = this.selector.getSelectedIndex();

    if (index > 0) {
      Presets.Preset preset = this.presets.list.get(index - 1);
      this.presets.list.set(index - 1, this.presets.list.get(index));
      this.presets.list.set(index, preset);

      this.selector.removeItemAt(index - 1);
      this.selector.insertItemAt(preset, index);
    }

    this.sbutton.setEnabled(true);

    if (index == 1) {
      this.mubutton.setEnabled(false);
    }
    else {
      this.mubutton.setEnabled(true);
    }

    if (index == this.presets.list.size()) {
      this.mdbutton.setEnabled(false);
    }
    else {
      this.mdbutton.setEnabled(true);
    }
  }


  @Override
  public void dispose() {
    if (!this.sbutton.isEnabled() || JOptionPane.showConfirmDialog(null, "There are unsaved changes. Do you really want to close the application?", null, JOptionPane.YES_NO_OPTION) == 0) {
      super.dispose();

      synchronized (this) {
        this.notifyAll();
      }
    }
  }
}
