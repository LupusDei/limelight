package limelight.ui.model.inputs;

import limelight.ui.model.TextAccessor;
import limelight.styles.Style;

import java.awt.*;

public class RadioButtonPanel extends InputPanel
{
  private RadioButton radioButton;

  protected Component createComponent()
  {
    return radioButton = new RadioButton(this);
  }

  protected TextAccessor createTextAccessor()
  {
    return new RadioButtonTextAccessor(radioButton);
  }

  protected void setDefaultStyles(Style style)
  {
    style.setDefault(Style.WIDTH, "" + radioButton.getPreferredSize().width);
    style.setDefault(Style.HEIGHT, "" + radioButton.getPreferredSize().height);
  }

  public RadioButton getRadioButton()
  {
    return radioButton;
  }

  private static class RadioButtonTextAccessor implements TextAccessor
  {
    private RadioButton button;

    public RadioButtonTextAccessor(RadioButton button)
    {
      this.button = button;
    }

    public void setText(String text)
    {
      button.setText(text);
    }

    public String getText()
    {
      return button.getText();
    }
  }
}