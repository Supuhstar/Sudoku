package bht.tools.effects;

//import bht.tools.utilities.Numbers;
import java.awt.Color;

/**
 * A class made to help assist with color management and regulating official Blue Husky Colors
 * @author Supuhstar of Blue Husky Programming
 * @version 0.1.5
 * @since 1.6.0_16
 */
public class Colors
{
  protected static final long serialVersionUID = 0L;
  private byte style, part;
  private static  byte spacer = 2;
  public static final byte NULL = -1;
  public static final byte MOCHA = 0;
  public static final byte HUSKY = 1;
  public static final byte ASTRO = 2;
  public final byte CUSTOM = 3;
  public static final byte BORDER = 10;
  public static final byte BOX = 11;
  public static final byte BACK = 12;
  public static final byte TEXT = 13;
  public static final byte TEXT_AREA = 14;
  private static final Color stdColors[] = 
                          {Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.GREEN,
                           Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.WHITE, Color.YELLOW};
  private static final String STD_HEX[] = 
                          {"000000", "0000FF", "00FFFF", "404040", "808080", "00FF00",
                           "C0C0C0", "FF00FF", "FFC800", "FFAFAF", "FF0000", "FFFFFF", "FFFF00"};
  private Color custBox = getColor(MOCHA, BOX), custBack = getColor(MOCHA, BACK), custText = getColor(MOCHA, TEXT),
          custTextArea = getColor(MOCHA, TEXT_AREA), custBord = getColor(MOCHA, BORDER);
  /** The default font size for the target component in the <tt>fixStyleIn()</tt> methods */
  public static final int DEFAULT_SIZE = 11;
  /** The name of the default font used in the <tt>fixStyleIn()</tt> methods */
  public static final String DEFAULT_FONT = "Segoe UI Semibold";
  /** A <tt>String</tt> that ends an override command */
  public static final String COMMAND_SEP = java.io.File.pathSeparator;
  /** A <tt>String</tt> override command that signifies that you want the size offset of the font in this component to be different */
  public static final String SIZE_OVERRIDE = "FONT_SIZE:";
  /** A <tt>String</tt> override command that signifies that you want the exact size of the font in this component to be different */
  public static final String SIZE_ONLY_OVERRIDE = "FONT_SIZE_MUST_BE:";
  /** A <tt>String</tt> override command that signifies that you want the icon in this component to be different
   * @deprecated Not yet implemented*/
  public static final String ICON_OVERRIDE = "ICON:";
  /** A <tt>String</tt> override command that signifies that you want the background color in this component to be different */
  public static final String BACK_OVERRIDE = "BOX_COLOR:";
  /** A <tt>String</tt> override command that signifies that you want the text (foreground) color in this component to be different */
  public static final String FORE_OVERRIDE = "TEXT_COLOR:";
  /** A <tt>String</tt> override command that signifies that you want the font style (bold, italic, etc.) in this component to
   * be different.<br/><b>Use the constants in <tt>java.awt.Font</tt></b> */
  public static final String FONT_STYLE_OVERRIDE = "FONT_STYLE:";
  /** A <tt>String</tt> override command that signifies that you want the font in this component to be different.<br/>
   * <b>Use the name of the font only</b> */
  public static final String FONT_OVERRIDE = "FONT:";
  /** A <tt>String</tt> override command that signifies that you want the <tt>style</tt> of this component to be different
   * (use <tt>Colors.MOCHA</tt>, <tt>Colors.HUSKY</tt>, etc.) */
  public static final String COLOR_STYLE_OVERRIDE = "COLOR_STYLE:";
  /** A <tt>String</tt> override command that signifies that you want this component to be treated as if it is a different
   * <tt>part</tt> (use <tt>Colors.BOX</tt>, <tt>Colors.BACK</tt>, etc.) */
  public static final String COLOR_PART_OVERRIDE = "COLOR_PART:";

  /**
   * Creates a new <tt>Colors</tt> object given a certain style and part. See documentation for the <tt>getColor(byte style,
   * byte part)</tt> method for further detail.
   * @param style the <tt>byte</tt> representing the visual style of the target color<br/>
   * <b>Example:</b><blockquote><tt>Colors.MOCHA</tt></blockquote>
   * @param part the <tt>byte</tt> representing the part for which the target color will be used<br/>
   * <b>Example:</b><blockquote><tt>Colors.BORDER</tt></blockquote>
   */
  public Colors(byte style, byte part)
  {
    this.style = style;
    this.part = part;
  }
  /**
   * Creates a new <tt>Colors</tt> object given a certain style and part. See documentation for the <tt>getColor(byte style,
   * byte part)</tt> method for further detail.
   * @param style the <tt>byte</tt> representing the visual style of the target color<br/>
   * <b>Example:</b><blockquote><tt>Colors.MOCHA</tt></blockquote>
   */
  public Colors(byte style)
  {
    this(style, Colors.MOCHA);
  }

  /**
   * Creates a new <tt>Colors</tt> object given a certain style and part. See documentation for the <tt>getColor(byte style,
   * byte part)</tt> method for further detail.
   * <b>Example:</b><blockquote><tt>Colors.BORDER</tt></blockquote>
   */
  public Colors()
  {
    this(Colors.MOCHA, Colors.BACK);
  }

  /**
   * Creates a new <tt>Color</tt> given a certain style and part. For example, if you wish to properly color a box (such
   * as a <tt>javax.swing.JButton</tt>) as it would be in the primary SupuhWiki theme, you would
   * set its border's color to be <tt>Colors.getColor(Colors.MOCHA, Colors.BORDER)</tt>, meaning that you want it to be a border
   * colored in the official way that SupuhWiki's borders are. For the filling, you would set the background to
   * <tt>Colors.getColor(Colors.MOCHA, COLORS.BOX)</tt>. Note that this does NOT use <tt>Colors.BACK</tt>. That is for the
   * farthest back backgrounds. For the text, you would use <tt>Colors.getColor(Colors.MOCHA, Colors.TEXT)</tt>.<br/>
   * <br/>
   * For custom colors, it will return the color set by you and/or the user in the <tt>setCustomColor(Color color, byte part)</tt>
   * @param part the <tt>byte</tt> representing the part for which the target color will be used.<br/>
   * <b>Example:</b><blockquote><tt>Colors.BORDER</tt></blockquote>
   * @return the official BH Color corresponding to the input style and part
   */
  public Color getColor(byte part)
  {
    return getColor(style, part);
  }
  /**
   * Creates a new <tt>Color</tt> given a certain style and part. For example, if you wish to properly color a box (such
   * as a <tt>javax.swing.JButton</tt>) as it would be in the primary SupuhWiki theme, you would
   * set its border's color to be <tt>Colors.getColor(Colors.MOCHA, Colors.BORDER)</tt>, meaning that you want it to be a border
   * colored in the official way that SupuhWiki's borders are. For the filling, you would set the background to
   * <tt>Colors.getColor(Colors.MOCHA, COLORS.BOX)</tt>. Note that this does NOT use <tt>Colors.BACK</tt>. That is for the
   * farthest back backgrounds. For the text, you would use <tt>Colors.getColor(Colors.MOCHA, Colors.TEXT)</tt>.<br/>
   * <br/>
   * For custom colors, it will return the color set by you and/or the user in the <tt>setCustomColor(Color color, byte part)</tt>
   * @param style the <tt>byte</tt> representing the visual style for which the target color will be used<br/>
   * <b>Example:</b><blockquote><tt>Colors.MOCHA</tt></blockquote>
   * @param part the <tt>byte</tt> representing the part of the target color<br/>
   * <b>Example:</b><blockquote><tt>Colors.BORDER</tt></blockquote>
   * @return the official BH Color corresponding to the input style and part
   */
  public Color getColor(byte style, byte part)
  {
    Color c = null;
    switch (part)
    {
      case BORDER:
        switch (style)
        {
          case MOCHA:
            c = getColor("#78411A");
            break;
          case HUSKY:
            c = getColor("#D8FF2E");
            break;
          case ASTRO:
            c = new java.awt.Color(.5F, 0F, 0F);
            break;
          case CUSTOM:
            c = custBord;
            break;
        }
        break;
      case TEXT:
        switch (style)
        {
          case MOCHA:
          case HUSKY:
          case ASTRO:
            c = getColor(style, Colors.BORDER);
            break;
          case CUSTOM:
            c = custText;
        }
        break;
      case TEXT_AREA:
        switch (style)
        {
          case MOCHA:
            c = new java.awt.Color(1F, 1F, 1F);
            break;
          case HUSKY:
            c = new java.awt.Color(0F, 0F, 0F);
            break;
          case ASTRO:
            c = new java.awt.Color(.1F, .1F, .1F);
            break;
          case CUSTOM:
            c = custTextArea;
            break;
        }
        break;
      case BOX:
        switch (style)
        {
          case MOCHA:
            c = getColor("#BD9F68");
            break;
          case HUSKY:
            c = getColor("#2B2B2B");
            break;
          case ASTRO:
            c = new java.awt.Color(.05F, 0F, 0F);
            break;
          case CUSTOM:
            c = custBox;
            break;
        }
        break;
      case BACK:
        switch (style)
        {
          case MOCHA:
            c = getColor("#EDE4BF");
            break;
          case HUSKY:
            c = getColor("#2B2B2B");
            break;
          case ASTRO:
            c = new java.awt.Color(0);
            break;
          case CUSTOM:
            c = custBack;
            break;
        }
        break;
    }
    return c;
  }

  /**
   * Gets the color you input when creating this object or wen you called <tt>setStyle(byte style)</tt>, <tt>setPart(byte part)</tt>,
   * or <tt>setColor(byte style, byte part)</tt>.
   * @return the <tt>java.awt.Color</tt> representing the color you want to get.
   */
  public Color getColor()
  {
    return getColor(style, part);
  }

  /**
   * Uses a hex color <tt>String</tt> (such as <tt>#BD9F68</tt>, <tt>0x78411A</tt>, or just <tt>EDE4BF</tt>) to create a <tt>java.awt.Color</tt>
   * @param hexString a <tt>String</tt> which represents the target color in hexadecimal, wherein the first two digits
   * (or first 16 bits) represent the amount of red in the color (<tt>00</tt> is no red, <tt>FF</tt> is pure red),
   * the second two digits represent the amount of green, and the last two represent the amount of blue.
   * @return a <tt>java.awt.Color</tt> representation of the hex string.<br />
   * <b>Examples:</b><br />
   * <blockquote>
   * an input of <tt>#FF0000</tt> returns the equivalent of <tt>java.awt.Color.RED</tt><br />
   * an input of <tt>#00FF00</tt> returns the equivalent of <tt>java.awt.Color.GREEN</tt><br />
   * an input of <tt>#0000FF</tt> returns the equivalent of <tt>java.awt.Color.BLUE</tt><br />
   * an input of <tt>#000000</tt> returns the equivalent of <tt>java.awt.Color.BLACK</tt><br />
   * an input of <tt>#FFFFFF</tt> returns the equivalent of <tt>java.awt.Color.WHITE</tt><br />
   * </blockquote>
   */
  public static Color getColor(String hexString)
  {
    hexString = hexString.substring(hexString.length() - 6).toUpperCase();
    for (int i=0; i < stdColors.length; i++)//Checks for basic colors
    {
      if (hexString.equals(STD_HEX[i]))
        return stdColors[i];
    }

    int intRed = 0, intGreen = 0, intBlue = 0;
    String hexRed = hexString.substring(0, 2),
           hexGreen = hexString.substring(2, 4),
           hexBlue = hexString.substring(4);
    boolean fR = false, fG = false, fB = false;
    for (int i=0; i < 3 && (!fR || !fG || !fB); i++)
    {
      for (int j=0; j <= 0xFF && (!fR || !fG || !fB); j++)
      {
        if (Integer.toHexString(j).equalsIgnoreCase(hexRed))
        {
          intRed = j;
          fR = true;
        }
        if (Integer.toHexString(j).equalsIgnoreCase(hexGreen))
        {
          intGreen = j;
          fG = true;
        }
        if (Integer.toHexString(j).equalsIgnoreCase(hexBlue))
        {
          intBlue = j;
          fB = true;
        }
      }
    }
    return new java.awt.Color(intRed, intGreen, intBlue);
  }

  /**
   * Calculates and returns a <tt>String</tt> representation of the specified Official BHColor in hexadecimal format
   * @param style the <tt>byte</tt> representing the visual style of the target color<br /><b>Example:</b><blockquote><tt>Colors.MOCHA</tt></blockquote>
   * @param part the <tt>byte</tt> representing the part of the target color<br /><b>Example:</b><blockquote><tt>Colors.BORDER</tt></blockquote>
   * @return <tt>String</tt> representation of the specified official BH Color in hexadecimal format
   */
  public String getHexString(byte style, byte part)
  {
    return getHexString(getColor(style, part));
  }

  /**
   * Calculates and returns a <tt>String</tt> representation of the specified Official BHColor in hexadecimal format
   * @return <tt>String</tt> representation of the Official BHColor (which you gave this <tt>Colors</tt> object) in hexadecimal format
   */
  public String getHexString()
  {
    return getHexString(style, part);
  }

  /**
   * Calculates and returns a <tt>String</tt> representation of the specified Official BHColor in hexadecimal format
   * @param c
   * @return <tt>String</tt> representation of the specified official BH Color in hexadecimal format
   */
  public static String getHexString(Color c)
  {
    return bht.tools.utilities.Numbers.lenFmt(Integer.toHexString(c.getRed()),2) +
           bht.tools.utilities.Numbers.lenFmt(Integer.toHexString(c.getGreen()),2) +
           bht.tools.utilities.Numbers.lenFmt(Integer.toHexString(c.getBlue()),2);
  }
  
  public void fixStyleIn(java.awt.Component comp, java.awt.Font f)
  {
    fixStyleIn(comp, style, f.getName(), f.getStyle(), f.getSize() - DEFAULT_SIZE, false);
  }
  
  public void fixStyleIn(java.awt.Component comp)
  {
    fixStyleIn(comp, style, DEFAULT_FONT, java.awt.Font.PLAIN, 0, false);
  }
  
  public void fixStyleIn(java.awt.Component comp, byte style, java.awt.Font f)
  {
    fixStyleIn(comp, style, f.getName(), f.getStyle(), f.getSize() - DEFAULT_SIZE, false);
  }
  
  public void fixStyleIn(java.awt.Component comp, byte style)
  {
    fixStyleIn(comp, style, DEFAULT_FONT, java.awt.Font.PLAIN, 0, false);
  }
  
  public void fixStyleIn(java.awt.Component comp, String fontName, int fontStyle, int fontMod)
  {
    fixStyleIn(comp, style, fontName, fontStyle, fontMod, false);
  }
  
  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  public void fixStyleIn(java.awt.Component comp, byte style, String fontName, int fontStyle, int fontMod, boolean print)
  {
    byte part = (comp instanceof javax.swing.AbstractButton || comp instanceof javax.swing.JSeparator) &&
            !(comp instanceof javax.swing.JMenuItem) ? Colors.BOX :
            (comp instanceof javax.swing.text.JTextComponent ? Colors.TEXT_AREA : Colors.BACK);
    String desc;

    if (comp != null)
    {
      for (int i = 0; print && i < spacer; i++)
      {
        System.out.print("  ");
      }
      desc = (comp.getName() != null && !comp.getName().isEmpty() ? "Name: \"" + comp.getName() + "\" " : "") + (comp.getAccessibleContext() != null ?
        (comp.getAccessibleContext().getAccessibleName() != null && !comp.getAccessibleContext().getAccessibleName().isEmpty() ?
          "Accessible Name: \"" + comp.getAccessibleContext().getAccessibleName() + "\" " : "") +
          (comp.getAccessibleContext().getAccessibleDescription() != null && !comp.getAccessibleContext().getAccessibleDescription().isEmpty() ?
            "Accessible Description: \"" + comp.getAccessibleContext().getAccessibleDescription() + "\" " : "") : "");
      if (print)
        System.out.println(comp.getClass().getName() + ": " + desc);
    }
    else
      throw new java.lang.NullPointerException("Cannot use a null component");
    try
    {
      String accDesc = comp.getAccessibleContext().getAccessibleDescription();
      if (accDesc.contains(COLOR_STYLE_OVERRIDE))
      {
        try
        {
          style = Byte.parseByte(accDesc.substring(accDesc.indexOf(COLOR_STYLE_OVERRIDE) + COLOR_STYLE_OVERRIDE.length(), accDesc.indexOf(COMMAND_SEP, accDesc.indexOf(
                  COLOR_STYLE_OVERRIDE) + COLOR_STYLE_OVERRIDE.length())));
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print("  ");
          }
          if (print)
            System.out.println("Style overrided to " + style);
        }
        catch (Throwable t)
        {
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print("  ");
          }
          if (print)
            System.out.println(t + " caught while overriding the style");
        }
      }
      if (accDesc.contains(COLOR_PART_OVERRIDE))
      {
        try
        {
          part = Byte.parseByte(accDesc.substring(accDesc.indexOf(COLOR_PART_OVERRIDE) + COLOR_PART_OVERRIDE.length(), accDesc.indexOf(COMMAND_SEP, accDesc.indexOf(
                  COLOR_PART_OVERRIDE) + COLOR_PART_OVERRIDE.length())));
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print("  ");
          }
          if (print)
            System.out.println("Part overrided to " + style);
        }
        catch (Throwable t)
        {
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print("  ");
          }
          if (print)
            System.out.println(t + " caught while overriding the style");
        }
      }
      if (accDesc.contains(SIZE_OVERRIDE))
      {
        try
        {
          fontMod += Integer.parseInt(accDesc.substring(accDesc.indexOf(SIZE_OVERRIDE) + SIZE_OVERRIDE.length(), accDesc.indexOf(COMMAND_SEP, accDesc.indexOf(
                  SIZE_OVERRIDE) + SIZE_OVERRIDE.length()))) - DEFAULT_SIZE;
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print("  ");
          }
          if (print)
            System.out.println("Font size overrided to " + (fontMod + DEFAULT_SIZE));
        }
        catch (Throwable t)
        {
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print("  ");
          }
          if (print)
            System.out.println(t + " caught while overriding the size");
        }
      }
      if (accDesc.contains(SIZE_ONLY_OVERRIDE))
      {
        try
        {
          fontMod = Integer.parseInt(accDesc.substring(accDesc.indexOf(SIZE_ONLY_OVERRIDE) + SIZE_ONLY_OVERRIDE.length(), accDesc.indexOf(COMMAND_SEP, accDesc.indexOf(
                  SIZE_ONLY_OVERRIDE) + SIZE_ONLY_OVERRIDE.length()))) - DEFAULT_SIZE;
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print("  ");
          }
          if (print)
            System.out.println("Font size overrided to " + (fontMod + DEFAULT_SIZE));
        }
        catch (Throwable t)
        {
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print("  ");
          }
          if (print)
            System.out.println(t + " caught while overriding the size");
        }
      }
      if (accDesc.contains(FONT_STYLE_OVERRIDE))
      {
        try
        {
          fontStyle = Integer.parseInt(accDesc.substring(accDesc.indexOf(FONT_STYLE_OVERRIDE) + FONT_STYLE_OVERRIDE.length(), accDesc.indexOf(COMMAND_SEP, accDesc.indexOf(
                  FONT_STYLE_OVERRIDE) + FONT_STYLE_OVERRIDE.length())));
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print("  ");
          }
          if (print)
            System.out.println("Font style overrided to " + fontStyle);
        }
        catch (Throwable t)
        {
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print("  ");
          }
          if (print)
            System.out.println(t + " caught while overriding the font style");
        }
      }
      if (accDesc.contains(FONT_OVERRIDE))
      {
        try
        {
          fontName = accDesc.substring(accDesc.indexOf(FONT_OVERRIDE) + FONT_OVERRIDE.length(), accDesc.indexOf(COMMAND_SEP, accDesc.indexOf(
                  FONT_OVERRIDE) + FONT_OVERRIDE.length()));
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print("  ");
          }
          if (print)
            System.out.println("Font overrided to " + fontName);
        }
        catch (Throwable t)
        {
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print("  ");
          }
          if (print)
            System.out.println(t + " caught while overriding the font");
        }
      }
      if (accDesc.contains(BACK_OVERRIDE))
      {
        try
        {
          String hexString = accDesc.substring(accDesc.indexOf(BACK_OVERRIDE) + BACK_OVERRIDE.length(),
                                               accDesc.indexOf(COMMAND_SEP, accDesc.indexOf(BACK_OVERRIDE) + BACK_OVERRIDE.length()));
          comp.setBackground(getColor(hexString));
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print("  ");
          }
          if (print)
            System.out.println("Background overrided to " + hexString);
        }
        catch (Throwable t)
        {
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print("  ");
          }
          if (print)
            System.out.println(t + " caught while overriding the background");
        }
      }
      if (accDesc.contains(FORE_OVERRIDE))
      {
        try
        {
          String hexString = accDesc.substring(accDesc.indexOf(FORE_OVERRIDE) + FORE_OVERRIDE.length(),
                                               accDesc.indexOf(COMMAND_SEP, accDesc.indexOf(FORE_OVERRIDE) + FORE_OVERRIDE.length()));
          comp.setForeground(getColor(hexString));
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print("  ");
          }
          if (print)
            System.out.println("Foreground overrided to " + hexString);
        }
        catch (Throwable t)
        {
          for (int i = 0; print && i < spacer; i++)
          {
            System.out.print("  ");
          }
          if (print)
            System.out.println(t + " caught while overriding the foreground");
        }
      }
    }
    catch (NullPointerException ex)
    {
//      ex.printStackTrace(System.out);
//      System.out.println("null");
//      for (int i = 0; print && i < spacer; i++)
//      {
//        System.out.print("  ");
//      }
//      if (print)
//        System.out.println("No More Components Found Herein");
    }
    catch (Throwable t)
    {
      if (print)
        t.printStackTrace(System.out);
    }

    spacer++;

    try
    {
      if (comp instanceof javax.swing.plaf.basic.BasicArrowButton)
      {
        int direction = ((javax.swing.plaf.basic.BasicArrowButton)comp).getDirection();

        ((javax.swing.plaf.basic.BasicArrowButton)comp).setBackground(style == NULL ? new javax.swing.plaf.basic.BasicArrowButton(direction).
                getBackground() : getColor(style, part));
        ((javax.swing.plaf.basic.BasicArrowButton)comp).setForeground(style == NULL ? new javax.swing.plaf.basic.BasicArrowButton(direction).
                getForeground() : getColor(style, TEXT));
        if (((javax.swing.plaf.basic.BasicArrowButton)comp) != null && ((javax.swing.plaf.basic.BasicArrowButton)comp).getName() != null)
          ((javax.swing.plaf.basic.BasicArrowButton)comp).setToolTipText((direction == javax.swing.plaf.basic.BasicArrowButton.NORTH
                                                                          ? "Increase this value (or scroll up" : "Decrease this value (or scroll down")
                                                                         + " with your mouse wheel)");
        ((javax.swing.plaf.basic.BasicArrowButton)comp).setBorder(style == NULL ? new javax.swing.plaf.basic.BasicArrowButton(direction).getBorder() : new javax.swing.border.LineBorder(
                getColor(style, BORDER), 1));
      }
      else if (comp instanceof javax.swing.JCheckBox)
      {
//        String type = (style == HUSKY ? "Husky" : (style == ASTRO ? "Astro" : "Mocha"));
//        System.out.println("/resources/" + type + "CheckBox ___.png");
//        ((javax.swing.JCheckBox)comp).setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/MochaCheckBox deselected.png")));
//        ((javax.swing.JCheckBox)comp).setSelectedIcon(style == NULL ? null : new javax.swing.ImageIcon(Main.class.
//        getResource("/resources/" + type + "CheckBox selected.png")));
//        ((javax.swing.JCheckBox)comp).setDisabledIcon(style == NULL ? null : new javax.swing.ImageIcon(Main.class.
//        getResource("/resources/" + type + "CheckBox disabled, deselected.png")));
//        ((javax.swing.JCheckBox)comp).setDisabledSelectedIcon(style == NULL ? null : new javax.swing.ImageIcon(Main.class.
//        getResource("/resources/" + type + "CheckBox disabled, selected.png")));
//        ((javax.swing.JCheckBox)comp).setPressedIcon(style == NULL ? null : new javax.swing.ImageIcon(Main.class.
//        getResource("/resources/" + type + "CheckBox deselected-hover.png")));
//        ((javax.swing.JCheckBox)comp).setRolloverIcon(style == NULL ? null : new javax.swing.ImageIcon(Main.class.
//        getResource("/resources/" + type + "CheckBox deselected-hover.png")));
//        ((javax.swing.JCheckBox)comp).setSelectedIcon(style == NULL ? null : new javax.swing.ImageIcon(Main.class.
//        getResource("/resources/" + type + "CheckBox selected.png")));

        ((javax.swing.JCheckBox)comp).setBackground(style == NULL ? new javax.swing.JCheckBox().getBackground() : getColor(
                style, part));
        ((javax.swing.JCheckBox)comp).setForeground(style == NULL ? new javax.swing.JCheckBox().getForeground() : getColor(
                style, TEXT));
      }
      else if (comp instanceof javax.swing.JTabbedPane)
      {
        ((javax.swing.JTabbedPane)comp).setBackground(style == NULL ? new javax.swing.JTabbedPane().getBackground() : getColor(
                style, part));
        ((javax.swing.JTabbedPane)comp).setForeground(style == NULL ? new javax.swing.JTabbedPane().getForeground() : getColor(
                style, TEXT));
      }
      else if (comp instanceof javax.swing.JSpinner)
      {
        ((javax.swing.JSpinner)comp).setBorder(style == NULL ? new javax.swing.JSpinner().getBorder() :
          (((javax.swing.JSpinner)comp).getBorder() == null ? null : new javax.swing.border.LineBorder(getColor(style, BORDER), 1)));
      }
      else if (comp instanceof javax.swing.text.JTextComponent)
      {
        ((javax.swing.text.JTextComponent)comp).setBackground(style == NULL ? new javax.swing.JTextField().getBackground() : getColor(
                style, comp.isEnabled() && ((javax.swing.text.JTextComponent)comp).isEditable() ? part : BACK));
        ((javax.swing.text.JTextComponent)comp).setForeground(style == NULL ? new javax.swing.JTextField().getForeground() : getColor(
                style, TEXT));
        ((javax.swing.text.JTextComponent)comp).setBorder(style == NULL ? new javax.swing.JTextField().getBorder() :
          new javax.swing.border.LineBorder(getColor(style, BORDER), 2));
//        ((javax.swing.JFormattedTextField)comp).getAccessibleContext().setAccessibleDescription(SIZE_OVERRIDE + "48" + COMMAND_SEP);
      }
      else if (comp instanceof javax.swing.JButton)
      {
        ((javax.swing.JButton)comp).setBackground(getColor(style, part));
        ((javax.swing.JButton)comp).setForeground(getColor(style, TEXT));
        ((javax.swing.JButton)comp).setBorder(style == NULL ? new javax.swing.JButton().getBorder() :
          new javax.swing.border.LineBorder(getColor(style, BORDER), 1));
      }
      else if (comp instanceof javax.swing.JToggleButton)
      {
        ((javax.swing.JToggleButton)comp).setBackground(getColor(style, part));
        ((javax.swing.JToggleButton)comp).setForeground(getColor(style, TEXT));
        ((javax.swing.JToggleButton)comp).setBorder(style == NULL ? new javax.swing.JToggleButton().getBorder() :
          new javax.swing.border.LineBorder(getColor(style, BORDER), 1));
      }
      else if (comp instanceof javax.swing.JProgressBar)
      {
        ((javax.swing.JProgressBar)comp).setBackground(style == NULL ? new javax.swing.JProgressBar().getBackground() :
          getColor(style, part));
        ((javax.swing.JProgressBar)comp).setForeground(style == NULL ? new javax.swing.JProgressBar().getForeground() :
          getColor(style, TEXT));
        ((javax.swing.JProgressBar)comp).setBorder(style == NULL ? new javax.swing.JProgressBar().getBorder() :
          new javax.swing.border.LineBorder(getColor(style, TEXT), 2));
      }
      else if (comp instanceof javax.swing.JPopupMenu)
      {
        ((javax.swing.JPopupMenu)comp).setBackground(getColor(style, part));
        ((javax.swing.JPopupMenu)comp).setForeground(getColor(style, TEXT));
        for (int i = 0; print && i < spacer; i++)
        {
          System.out.print("  ");
        }
//        if (print)
//          System.out.println("Searching for sub-components...");
//        for (int i = 0; i < ((javax.swing.JPopupMenu)comp).getComponentCount(); i++)
//        {
//          fixStyleIn(((javax.swing.JPopupMenu)comp).getComponent(i), style, fontName, fontStyle, fontMod, false);
//        }
      }
      else if (comp instanceof javax.swing.JMenu)
      {
        ((javax.swing.JMenuItem)comp).setBackground(getColor(style, part));
        ((javax.swing.JMenuItem)comp).setForeground(getColor(style, TEXT));
        for (int i = 0; print && i < spacer; i++)
        {
          System.out.print("  ");
        }
        if (print)
          System.out.println("Searching for sub-components...");
//        for (int i = 0; i < ((javax.swing.JMenu)comp).getMenuComponentCount(); i++)
//        {
          fixStyleIn(((javax.swing.JMenu)comp).getPopupMenu(), style, fontName, fontStyle, fontMod, print);
//        }
        fontMod += 1;
      }
      else if (comp instanceof javax.swing.JMenuItem)
      {
        fontMod += 1;
        ((javax.swing.JMenuItem)comp).setBackground(getColor(style, part));
        ((javax.swing.JMenuItem)comp).setForeground(getColor(style, TEXT));
      }
      else if (comp instanceof javax.swing.JPanel)
      {
        ((javax.swing.JPanel)comp).setBackground(style == NULL ? new javax.swing.JPanel().getBackground() :
          getColor(style, part));
        ((javax.swing.JPanel)comp).setForeground(style == NULL ? new javax.swing.JPanel().getForeground() :
          getColor(style, TEXT));
//        if (((javax.swing.JPanel)comp).getBorder() != null)
//        {
//          ((javax.swing.JPanel)comp).setBorder(new javax.swing.border.LineBorder(getColor(style, BORDER), 2));
//        }
      }
      else if (comp instanceof javax.swing.JMenuBar)
      {
        ((javax.swing.JMenuBar)comp).setBackground(style == NULL ? new javax.swing.JMenuBar().getBackground() :
          getColor(style, part));
        ((javax.swing.JMenuBar)comp).setForeground(style == NULL ? new javax.swing.JMenuBar().getForeground() :
          getColor(style, TEXT));
        ((javax.swing.JMenuBar)comp).setBorder(style == NULL ? new javax.swing.JMenuBar().getBorder() :
          javax.swing.BorderFactory.createLineBorder(getColor(style, part), 1));
//        java.awt.Graphics g = comp.getGraphics();
//        g.setColor(getColor(style, BORDER));
//        for (int i=0; i < comp.getHeight(); i++)
//          g.drawLine(0, i, comp.getWidth(), i);
////        ((javax.swing.JMenuBar)comp).update(g);
//        ((javax.swing.JMenuBar)comp).paint(g);
      }
      else if (comp instanceof javax.swing.JSeparator)
      {
        ((javax.swing.JSeparator)comp).setBackground(style == NULL ? new javax.swing.JSeparator().getBackground() :
          getColor(style, part));
        ((javax.swing.JSeparator)comp).setForeground(style == NULL ? new javax.swing.JSeparator().getForeground() :
          getColor(style, TEXT));
      }
      else if (comp instanceof javax.swing.JComponent)
      {
        comp.setBackground(style == NULL ? null : getColor(style, part));
        comp.setForeground(style == NULL ? null : getColor(style, TEXT));
//        if (((javax.swing.JComponent)comp).getBorder() != null)
//        {
//          ((javax.swing.JComponent)comp).setBorder(new javax.swing.border.LineBorder(getColor(style, BORDER), 2));
//        }
      }
      else
      {
        comp.setBackground(style == NULL ? null : getColor(style, part));
        comp.setForeground(style == NULL ? null : getColor(style, TEXT));
      }
    }
    catch (Throwable t)
    {
      if (print)
        t.printStackTrace(System.out);
    }

    try
    {
      if (((java.awt.Container)comp).getComponentCount() > 0)
      {
        for (int i = 0; print && i < spacer; i++)
        {
          System.out.print("  ");
        }
        if (print)
          System.out.println("Searching for sub-components...");
        for (int i = 0; i < ((java.awt.Container)comp).getComponentCount(); i++)
        {
          fixStyleIn(((java.awt.Container)comp).getComponent(i), style, fontName, fontStyle, fontMod, print);
        }
        
        for (int i = 0; print && i < spacer; i++)
        {
          System.out.print("  ");
        }
        if (print)
          System.out.println("No more components found herein...");
      }
    }
    catch (ClassCastException ex)
    {
      for (int i = 0; print && i < spacer; i++)
      {
        System.out.print("  ");
      }
      if (print)
        System.out.println("Component is not a container. Could not search therein.");
    }

    
    try
    {

      for (int i = 0; print && i < spacer; i++)
      {
        System.out.print("  ");
      }
      if (print)
        System.out.println("setting font to (" + fontName + ", " + fontStyle + ", " + (fontMod + DEFAULT_SIZE) + ")");
      comp.setFont(new java.awt.Font(fontName, fontStyle, fontMod + DEFAULT_SIZE));
    }
    catch (Throwable t)
    {
      for (int i = 0; print && i < spacer; i++)
      {
        System.out.print("  ");
      }
      if (print)
        System.out.println(t + " caught while setting the font");
    }
    spacer--;
  }

  public void setCustomColor(Color color, byte part)
  {
    switch (part)
    {
      case BORDER:
        custBord = color;
        break;
      case BOX:
        custBox = color;
        break;
      case BACK:
        custBack = color;
        break;
      case TEXT:
        custText = color;
        break;
      case TEXT_AREA:
        custTextArea = color;
        break;
        default:
          throw new IllegalArgumentException("I don't know the part number " + part);
    }
  }

  public Color getCustomColor(byte part)
  {
    switch (part)
    {
      case BORDER:
        return custBord;
      case BOX:
        return custBox;
      case BACK:
        return custBack;
      case TEXT:
        return custText;
    }
    throw new IllegalArgumentException("I don't know the part number " + part);
  }

  public byte getStyle()
  {
    return style;
  }

  public byte setStyle(byte style)
  {
    byte b = this.style;
    this.style = style;
    return b;
  }

  public void fixStyleInAllWindows()
  {
    for (int i=0; i < java.awt.Window.getWindows().length; i++)
    {
      fixStyleIn(java.awt.Window.getWindows()[i]);
    }
  }
}
