//- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import limelight.styles.abstrstyling.HorizontalAlignmentAttribute;
import limelight.styles.HorizontalAlignment;
import limelight.LimelightError;

import java.awt.*;

public class SimpleHorizontalAlignmentAttribute implements HorizontalAlignmentAttribute
{
  private final HorizontalAlignment alignment;

  public SimpleHorizontalAlignmentAttribute(HorizontalAlignment alignment)
  {
    this.alignment = alignment;
  }

  public HorizontalAlignment getAlignment()
  {
    return alignment;
  }

  public String toString()
  {
    if(alignment == HorizontalAlignment.LEFT)
      return "left";
    else if(alignment == HorizontalAlignment.CENTER)
      return "center";
    else if(alignment == HorizontalAlignment.RIGHT)
      return "right";
    else
      throw new LimelightError("Unknown Horizontal Alignment: " + alignment);
  }

  public boolean equals(Object obj)
  {
    if(obj instanceof SimpleHorizontalAlignmentAttribute)
    {
      return alignment.equals(((SimpleHorizontalAlignmentAttribute)obj).alignment);
    }
    return false;
  }

  public int getX(int consumed, Rectangle area)
  {
    if(alignment == HorizontalAlignment.LEFT)
      return area.x;
    else if(alignment == HorizontalAlignment.CENTER)
      return area.x + (area.width - (int) (consumed + 0.5)) / 2;
    else
      return area.x + (area.width - (int) (consumed + 0.5));

  }
}
