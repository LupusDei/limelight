package limelight;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BackgroundPainter extends Painter
{
  public BackgroundPainter(Panel panel)
  {
    super(panel);
  }

  public void paint(Graphics2D graphics)
  {
    Style style = getStyle();
    Rectangle r = panel.getRectangleInsideBorders();
    if (style.getSecondaryBackgroundColor() != null && style.getGradientAngle() != null && style.getGradientPenetration() != null)
      new GradientPainter(panel).paint(graphics);
    else if (style.getBackgroundColor() != null)
    {
      graphics.setColor(Colors.resolve(style.getBackgroundColor()));
      graphics.fill(r);
    }
    if (style.getBackgroundImage() != null)
    {
      try
      {
        Image image = ImageIO.read(new File(style.getBackgroundImage()));
        Graphics2D borderedGraphics = (Graphics2D) graphics.create(r.x, r.y, r.width, r.height);
        ImageFillStrategies.get(style.getBackgroundImageFillStrategy()).fill(borderedGraphics, image);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }
}