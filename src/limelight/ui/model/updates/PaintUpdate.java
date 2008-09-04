package limelight.ui.model.updates;

import limelight.ui.Panel;
import limelight.ui.model.PaintJob;
import limelight.ui.model.RootPanel;
import limelight.ui.model.Update;
import limelight.util.Box;

import java.awt.*;

public class PaintUpdate extends Update
{
  protected PaintUpdate(int severity)
  {
    super(severity);
  }

  public void performUpdate(Panel panel)
  {
    paintPanel(panel);
  }

  protected void paintPanel(Panel panel)
  {
    Box bounds = getAbsoluteBounds(panel);
    if(bounds.width == 0 || bounds.height == 0)
      return;
    PaintJob job = new PaintJob(bounds);
    RootPanel rootPanel = (RootPanel) panel.getRoot();
    job.paint(rootPanel.getPanel());
    Graphics2D rootGraphics = rootPanel.getGraphics();
    job.applyTo(rootGraphics);
    job.dispose();
  }

  protected Box getAbsoluteBounds(Panel panel)
  {
    return panel.getAbsoluteBounds();
  }
}