package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class ShiftKeyProcessor extends KeyProcessor
{
  public ShiftKeyProcessor(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(KeyEvent event)
  {
    int keyCode = event.getKeyCode();
    
    if (isACharacter(keyCode))
      modelInfo.insertCharIntoTextBox(event.getKeyChar());
    else if(modelInfo.isMoveRightEvent(keyCode)){
      modelInfo.initSelection();
      modelInfo.setCursorIndex(modelInfo.getCursorIndex() + 1);
    }
    else if(modelInfo.isMoveLeftEvent(keyCode)){
      modelInfo.initSelection();
      modelInfo.setCursorIndex(modelInfo.getCursorIndex() - 1);
    }
  }
}
