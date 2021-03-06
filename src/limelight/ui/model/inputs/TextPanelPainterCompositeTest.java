package limelight.ui.model.inputs;

import limelight.ui.MockGraphics;
import limelight.ui.model.inputs.painters.TextPanelBackgroundPainter;
import limelight.ui.model.inputs.painters.TextPanelCursorPainter;
import limelight.ui.model.inputs.painters.TextPanelSelectionPainter;
import limelight.ui.model.inputs.painters.TextPanelTextPainter;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TextPanelPainterCompositeTest
{
  TextPanelPainterComposite painterComposite;
  TextModel boxInfo;
  TextInputPanel panel;

  @Before
  public void setUp()
  {
    panel = new TextBox2Panel();
    boxInfo = panel.getModelInfo();
    painterComposite = new TextPanelPainterComposite(boxInfo);
  }

  class MockPainter extends TextPanelPainter
  {
    public MockPainter(TextModel boxInfo)
    {
      super(boxInfo);
      hasPainted = false;
    }

    @Override
    public void paint(Graphics2D graphics)
    {
      hasPainted = true;
    }
  }

  @Test
  public void itWillRunAllPaintersOnPaint()
  {
    setMockPainters();

    painterComposite.paint(new MockGraphics());

    assertAllPaintersHavePainted();
  }

  private void assertAllPaintersHavePainted()
  {
    assertTrue(painterComposite.getPanelBackgroundPainter().hasPainted);
    assertTrue(painterComposite.getTextPainter().hasPainted);
    assertTrue(painterComposite.getSelectionPainter().hasPainted);
    assertTrue(painterComposite.getCursorPainter().hasPainted);
  }

  private void setMockPainters()
  {
    painterComposite.setPanelBackgroundPainter(new MockPainter(boxInfo));
    painterComposite.setTextPainter(new MockPainter(boxInfo));
    painterComposite.setCursorPainter(new MockPainter(boxInfo));
    painterComposite.setSelectionPainter(new MockPainter(boxInfo));
  }

  @Test
  public void itShouldHaveACursorPainter()
  {
    assertEquals(TextPanelCursorPainter.class, painterComposite.getCursorPainter().getClass());
  }

  @Test
  public void itShouldHaveABoxPainter()
  {
    assertEquals(TextPanelBackgroundPainter.class, painterComposite.getPanelBackgroundPainter().getClass());
  }

  @Test
  public void itShouldHaveASelectionPainter()
  {
    assertEquals(TextPanelSelectionPainter.class, painterComposite.getSelectionPainter().getClass());
  }

  @Test
  public void itShouldHaveATextPainter()
  {
    assertEquals(TextPanelTextPainter.class, painterComposite.getTextPainter().getClass());
  }
}
