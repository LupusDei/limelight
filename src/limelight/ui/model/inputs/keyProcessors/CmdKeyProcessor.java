package limelight.ui.model.inputs.keyProcessors;

import limelight.ui.model.inputs.KeyProcessor;
import limelight.ui.model.inputs.TextModel;

import java.awt.event.KeyEvent;

public class CmdKeyProcessor extends KeyProcessor
{
  public CmdKeyProcessor(TextModel boxInfo)
  {
    super(boxInfo);
  }

  public void processKey(KeyEvent event)
  {
    switch (event.getKeyCode())
    {
      case KeyEvent.VK_A:
        modelInfo.selectAll();
        break;

      case KeyEvent.VK_V:
        modelInfo.pasteClipboard();
        break;
      case KeyEvent.VK_RIGHT:
        modelInfo.sendCursorToEndOfLine();
        break;
      case KeyEvent.VK_LEFT:
        modelInfo.sendCursorToStartOfLine();
        break;
      case KeyEvent.VK_UP:
        modelInfo.setCursorIndex(0);
        break;
      case KeyEvent.VK_DOWN:
        modelInfo.setCursorIndex(modelInfo.getText().length());
        break;


    }
  }

}
