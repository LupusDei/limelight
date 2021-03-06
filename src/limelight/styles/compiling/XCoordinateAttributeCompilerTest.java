//- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.compiling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.InvalidStyleAttributeError;
import limelight.styles.HorizontalAlignment;
import limelight.styles.styling.StaticXCoordinateAttribute;
import limelight.styles.styling.PercentageXCoordinateAttribute;
import limelight.styles.styling.AlignedXCoordinateAttribute;

public class XCoordinateAttributeCompilerTest extends TestCase
{
  private XCoordinateAttributeCompiler compiler;

  public void setUp() throws Exception
  {
    compiler = new XCoordinateAttributeCompiler();
    compiler.setName("x-coordinate");
  }

  public void testValidValue() throws Exception
  {
    assertEquals(StaticXCoordinateAttribute.class, compiler.compile("123").getClass());
    assertEquals(StaticXCoordinateAttribute.class, compiler.compile("-123").getClass());
    assertEquals(StaticXCoordinateAttribute.class, compiler.compile("123.567").getClass());
    assertEquals(PercentageXCoordinateAttribute.class, compiler.compile("50%").getClass());
    assertEquals(PercentageXCoordinateAttribute.class, compiler.compile("3.14%").getClass());
    assertEquals(AlignedXCoordinateAttribute.class, compiler.compile("left").getClass());
    assertEquals(AlignedXCoordinateAttribute.class, compiler.compile("center").getClass());
    assertEquals(AlignedXCoordinateAttribute.class, compiler.compile("right").getClass());

    assertEquals(123, ((StaticXCoordinateAttribute) compiler.compile("123")).getValue());
    assertEquals(-123, ((StaticXCoordinateAttribute) compiler.compile("-123")).getValue());
    assertEquals(0, ((StaticXCoordinateAttribute) compiler.compile("0")).getValue());
    assertEquals(50.0, ((PercentageXCoordinateAttribute) compiler.compile("50%")).getPercentage(), 0.01);
    assertEquals(3.14, ((PercentageXCoordinateAttribute) compiler.compile("3.14%")).getPercentage(), 0.01);
    assertEquals(HorizontalAlignment.LEFT, ((AlignedXCoordinateAttribute)compiler.compile("left")).getAlignment());
    assertEquals(HorizontalAlignment.CENTER, ((AlignedXCoordinateAttribute)compiler.compile("center")).getAlignment());
    assertEquals(HorizontalAlignment.RIGHT, ((AlignedXCoordinateAttribute)compiler.compile("right")).getAlignment());
  }

  public void testInvalidValues() throws Exception
  {
    checkForError("200%");
    checkForError("blah");
    checkForError("top");
  }

  private void checkForError(String value)
  {
    try
    {
      compiler.compile(value);
      fail("should have throw error");
    }
    catch(InvalidStyleAttributeError e)
    {
      assertEquals("Invalid value '" + value + "' for x-coordinate style attribute.", e.getMessage());
    }
  }
}
