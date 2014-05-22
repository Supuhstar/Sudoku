/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bht.resources;

import bht.tools.misc.YesNoBox;
import javax.swing.ImageIcon;

/**
 * Manages the icons stored within this package. All are static for greatest usability.
 * @author Supuhstar
 * @since 1.6.0_23
 * @version 1.0.0
 */
public class Icons
{
//  public static ImageIcon
//          warningIcon32 = getIcon("dialog-warning 32.png"),
//          errorIcon32 = getIcon("dialog-error 32.png"),
//          infoIcon32 = getIcon("dialog-information 32.png"),
//          questionIcon32 = getIcon("dialog-question 32.png"),
//          warningIcon16 = getIcon("dialog-warning 16.png"),
//          errorIcon16 = getIcon("dialog-error 16.png"),
//          infoIcon16 = getIcon("dialog-information 16.png"),
//          questionIcon16 = getIcon("dialog-question 16.png"),
//          blank =  getIcon("blank.png");
  public static final byte BLANK = YesNoBox.PLAIN;
  public static final byte INFO_32 = YesNoBox.INFO;
  public static final byte INFO_16 = (byte)(INFO_32 - 10);
  public static final byte QUESTION_32 = YesNoBox.QUESTION;
  public static final byte QUESTION_16 = (byte)(QUESTION_32 - 10);
  public static final byte WARNING_32 = YesNoBox.WARNING;
  public static final byte WARNING_16 = (byte)(WARNING_32 - 10);
  public static final byte ERROR_32 = YesNoBox.ERROR;
  public static final byte ERROR_16 = (byte)(ERROR_32 - 10);
  
  private static ImageIcon getIcon(String name)
  {
    return new javax.swing.ImageIcon(Icons.class.getResource("/bht/resources/" + name));
  }
  
  /**
   * Gets the icon represented by the given <tt>byte</tt> mask. You may use a <tt>byte</tt> mask from another class in the
   * toolbox; it will result in the 32x32-pixel version of the respective icon.
   * @param mask the <tt>byte</tt> representing the icon you want.
   * @return the icon you want, as an <tt>javax.swing.ImageIcon</tt>
   * @throws IllegalArgumentException if the mask you provide is not recognized
   */
  public static ImageIcon getIcon(byte mask)
  {
    switch(mask)
    {
      default:
        throw new IllegalArgumentException("Icon mask not recognized: " + mask);
      case BLANK:
        return getIcon("blank.png");
      case INFO_16:
          return getIcon("dialog-information 16.png");
      case INFO_32:
        return getIcon("dialog-information 32.png");
      case QUESTION_16:
        return getIcon("dialog-question 16.png");
      case QUESTION_32:
          return getIcon("dialog-question 32.png");
      case WARNING_16:
          return getIcon("dialog-warning 16.png");
      case WARNING_32:
        return getIcon("dialog-warning 32.png");
      case ERROR_16:
          return getIcon("dialog-error 16.png");
      case ERROR_32:
        return getIcon("dialog-error 32.png");
    }
  }
}
