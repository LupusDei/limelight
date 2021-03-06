package limelight.ui.model.inputs;

import limelight.ui.Panel;

public class InputPanelUtil
{

  public static InputPanel nextInputPanel(Panel start)
  {
    InputPanel next = null;
    InputPanel first = null;
    boolean foundMe = false;

    for(Panel panel : start.getRoot())
    {
      if(panel instanceof InputPanel)
      {
        if(foundMe)
        {
          next = (InputPanel)panel;
          break;
        }
        else if(panel == start)
          foundMe = true;
        if(first == null)
          first = (InputPanel)panel;
      }
    }

    if(next != null)
      return next;
    else
      return first;
  }

  public static InputPanel previousInputPanel(Panel start)
  {
    InputPanel previous = null;

    for(Panel panel : start.getRoot())
    {
      if(panel instanceof InputPanel)
      {
        if(panel == start && previous != null)
        {
          break;
        }
        else
        {
          previous = (InputPanel)panel;
        }
      }
    }

    return previous;
  }
}
