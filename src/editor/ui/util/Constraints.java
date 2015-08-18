package editor.ui.util;

import java.awt.*;

public class Constraints extends GridBagConstraints {
  public Constraints() {
    this.gridwidth = 1;
    this.gridheight = 1;
    this.weightx = 0;
    this.weighty = 0;
    this.fill = GridBagConstraints.HORIZONTAL;
    this.insets = new Insets(8, 8, 0, 8);
  }

  public Constraints setX(int x) {
    this.gridx = x;
    return this;
  }

  public Constraints setY(int y) {
    this.gridy = y;
    return this;
  }

  public Constraints setWidth(int width) {
    this.gridwidth = width;
    return this;
  }

  public Constraints setHeight(int height) {
    this.gridheight = height;
    return this;
  }

  public Constraints setHorizontalWeight(float weight) {
    this.weightx = weight;
    return this;
  }

  public Constraints setVerticalWeight(float weight) {
    this.weighty = weight;
    return this;
  }

  public Constraints setMargin(int top, int left, int bottom, int right) {
    this.insets = new Insets(top, left, bottom, right);
    return this;
  }

  public Constraints setTopMargin(int top) {
    this.insets.top = top;
    return this;
  }

  public Constraints setLeftMargin(int left) {
    this.insets.left = left;
    return this;
  }

  public Constraints setRightMargin(int right) {
    this.insets.right = right;
    return this;
  }

  public Constraints setBottomMargin(int bottom) {
    this.insets.bottom = bottom;
    return this;
  }

  public Constraints fillNone() {
    this.fill = GridBagConstraints.NONE;
    return this;
  }

  public Constraints fillHorizontal() {
    this.fill = GridBagConstraints.HORIZONTAL;
    return this;
  }

  public Constraints fillVertical() {
    this.fill = GridBagConstraints.VERTICAL;
    return this;
  }

  public Constraints fillBoth() {
    this.fill = GridBagConstraints.BOTH;
    return this;
  }
}
