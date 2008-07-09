package limelight.ui.model2;

import junit.framework.TestCase;

import limelight.util.Box;
import limelight.ui.*;
import limelight.ui.Panel;
import limelight.styles.FlatStyle;
import limelight.Context;
import limelight.caching.SimpleCache;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PaintJobTest extends TestCase
{
  private PaintJob job;
  private MockPropablePanel panel;
  private FlatStyle style;
  private MockGraphics graphics;
  private SimpleCache<Panel,BufferedImage> bufferedImageCache;

  public void setUp() throws Exception
  {
    bufferedImageCache = new SimpleCache<Panel, BufferedImage>();
    Context.instance().bufferedImageCache = bufferedImageCache;
    job = new PaintJob(new Box(100, 200, 300, 400));
    panel = new MockPropablePanel();
    style = panel.style;
    graphics = new MockGraphics();
  }

  public void testCreation() throws Exception
  {
    Box clip = new Box(1, 2, 3, 4);
    PaintJob job = new PaintJob(clip);

    assertSame(clip, job.getClip());

    BufferedImage buffer = job.getBuffer();
    assertEquals(3, buffer.getWidth());
    assertEquals(4, buffer.getHeight());

    Graphics2D graphics = job.getGraphics();
    assertNotNull(graphics);
    assertEquals(Color.white, graphics.getBackground());
  }

  public void testPaintRoot() throws Exception
  {
    panel.setLocation(1, 2);
    panel.setSize(1000, 1000);

    job.substituteGraphics(graphics);

    job.paint(panel);

    assertEquals(new Box(-99, -198, 1000, 1000), graphics.createdGraphicsBox);
  }

  public void testPanelIsInClip() throws Exception
  {
    panel.setLocation(0, 0);
    panel.setSize(100, 100);
    assertEquals(false, job.panelIsInClip(panel));

    panel.setLocation(1, 101);
    assertEquals(true, job.panelIsInClip(panel));

    panel.setSize(99, 99);
    assertEquals(false, job.panelIsInClip(panel));

    panel.setLocation(200, 300);
    assertTrue(job.panelIsInClip(panel));
  }

  public void testApplyComposite() throws Exception
  {
    Graphics2D graphics = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR).createGraphics();

    job.applyAlphaCompositeFor(panel, graphics);
    assertEquals(1.0, ((AlphaComposite) graphics.getComposite()).getAlpha(), 0.001);

    style.setTransparency("");
    job.applyAlphaCompositeFor(panel, graphics);
    assertEquals(1.0, ((AlphaComposite) graphics.getComposite()).getAlpha(), 0.001);

    style.setTransparency("0");
    job.applyAlphaCompositeFor(panel, graphics);
    assertEquals(1.0, ((AlphaComposite) graphics.getComposite()).getAlpha(), 0.001);

    style.setTransparency("50");
    job.applyAlphaCompositeFor(panel, graphics);
    assertEquals(0.5, ((AlphaComposite) graphics.getComposite()).getAlpha(), 0.001);

    job.restoreComposite(graphics);
    assertEquals(1.0, ((AlphaComposite) graphics.getComposite()).getAlpha(), 0.001);
  }

  public void testHandlingOfNonBufferedPanel() throws Exception
  {
    panel.canBeBuffered = false;

    job.paintClipFor(panel, graphics);

    assertEquals(true, panel.wasPainted);
    assertEquals(null, bufferedImageCache.retrieve(panel));
  }

  public void testBuilfBufferForPanel() throws Exception
  {
    assertEquals(null, bufferedImageCache.retrieve(panel));

    BufferedImage buffer = job.buildBufferFor(panel);

    assertEquals(true, panel.wasPainted);
    assertSame(buffer, bufferedImageCache.retrieve(panel));
    assertEquals(panel.getWidth(), buffer.getWidth());
    assertEquals(panel.getHeight(), buffer.getHeight());
    assertEquals(BufferedImage.TYPE_4BYTE_ABGR, buffer.getType());
  }

  public void testShouldBuildBufferWhenStyleChanges() throws Exception
  {
    assertEquals(true, job.shouldBuildBufferFor(panel, null));

    BufferedImage buffer = job.buildBufferFor(panel);
    assertEquals(false, job.shouldBuildBufferFor(panel, buffer));

    style.setWidth("101");
    assertEquals(true, job.shouldBuildBufferFor(panel, buffer));
    buffer = job.buildBufferFor(panel);
    assertEquals(false, job.shouldBuildBufferFor(panel, buffer));
  }

  public void testShouldNotBuildBufferWhenTransparencyChanges() throws Exception
  {
    BufferedImage buffer = job.buildBufferFor(panel);

    style.setTransparency("50");
    assertEquals(false, job.shouldBuildBufferFor(panel, buffer));
  }
  
  public void testShouldBuildBufferIfSizeChanges() throws Exception
  {
    style.setWidth("100%");
    style.setHeight("100%");
    BufferedImage buffer = job.buildBufferFor(panel);

    panel.setSize(123, 456);
    assertEquals(true, job.shouldBuildBufferFor(panel, buffer));
  }

  public void testApplyToGraphics() throws Exception
  {
    job.applyTo(graphics);

    assertSame(job.getBuffer(), graphics.drawnImage);
    assertEquals(100, graphics.drawnImageX);
    assertEquals(200, graphics.drawnImageY);
  }

  public void testPaintingChildren() throws Exception
  {
    MockPropablePanel child = new MockPropablePanel();
    panel.add(child);
    panel.childConsumableBox = new Box(12, 34, 56, 78);
    child.setLocation(123, 456);
    child.setSize(100, 200);
    limelight.alt_ui.MockGraphics graphics = new limelight.alt_ui.MockGraphics();

    job.paintChildren(panel, graphics);

    assertEquals(new Box(12, 34, 56, 78), graphics.clippedRectangle);
    assertEquals(new Box(123, 456, 100, 200), graphics.createdGraphicsRectangle);
  }

  public void testFloaterArePutAtTheBackOfTheLine() throws Exception
  {
    MockPropablePanel child1 = new MockPropablePanel();
    MockPropablePanel child2 = new MockPropablePanel();
    MockPropablePanel child3 = new MockPropablePanel();
    MockPropablePanel child4 = new MockPropablePanel();
    MockPropablePanel child5 = new MockPropablePanel();

    panel.add(child1);
    panel.add(child2);
    panel.add(child3);
    panel.add(child4);
    panel.add(child5);

    child1.floater = true;
    child3.floater = true;

    MockPanel.paintCount = 0;
    panel.setSize(100, 100);
    job = new PaintJob(new Box(0, 0, 100, 100));
    job.paintChildren(panel, graphics);

    assertEquals(0, child2.paintIndex);
    assertEquals(1, child4.paintIndex);
    assertEquals(2, child5.paintIndex);
    assertEquals(3, child1.paintIndex);
    assertEquals(4, child3.paintIndex);
  }
}
