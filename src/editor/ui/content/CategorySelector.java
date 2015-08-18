package editor.ui.content;

import javax.swing.*;

public class CategorySelector extends JComboBox<CategorySelector.Category> {
  public class Category {
    public final int id;
    public final String name;

    public Category(int id, String name) {
      this.id = id;
      this.name = name;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }


  private DefaultComboBoxModel<CategorySelector.Category> model;

  public CategorySelector() {
    this.model = new DefaultComboBoxModel<>();
    this.setModel(model);

    model.addElement(new CategorySelector.Category(-1, "Default"));
    model.addElement(new CategorySelector.Category(2, "Autos & Vehicles"));
    model.addElement(new CategorySelector.Category(23, "Comedy"));
    model.addElement(new CategorySelector.Category(27, "Education"));
    model.addElement(new CategorySelector.Category(24, "Entertainment"));
    model.addElement(new CategorySelector.Category(1, "Film & Animation"));
    model.addElement(new CategorySelector.Category(20, "Gaming"));
    model.addElement(new CategorySelector.Category(26, "Howto & Style"));
    model.addElement(new CategorySelector.Category(10, "Music"));
    model.addElement(new CategorySelector.Category(25, "News & Politics"));
    model.addElement(new CategorySelector.Category(29, "Nonprofits & Activism"));
    model.addElement(new CategorySelector.Category(22, "People & Blogs"));
    model.addElement(new CategorySelector.Category(15, "Pets & Animals"));
    model.addElement(new CategorySelector.Category(28, "Science & Technology"));
    model.addElement(new CategorySelector.Category(17, "Sports"));
    model.addElement(new CategorySelector.Category(19, "Travel & Events"));
  }

  public void select(int id) {
    for (int i = 0; i < this.model.getSize(); i++) {
      if (this.model.getElementAt(i).id == id) {
        this.model.setSelectedItem(this.model.getElementAt(i));
        return;
      }
    }
  }

  public String getCategoryNameById(int category) {
    for (int i = 0; i < this.model.getSize(); i++) {
      if (this.model.getElementAt(i).id == category) {
        return this.model.getElementAt(i).name;
      }
    }

    return null;
  }
}
