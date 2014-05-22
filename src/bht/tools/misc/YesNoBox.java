package bht.tools.misc;

import bht.resources.Icons;
import javax.swing.*;

/**
 * A utility that displays a dialog box with a yes-or-no question and answer buttons. Custom Nimbus icons are used if possible,
 * else the default are.
 * @version 1.3.0
 * @author Supuhstar of Blue Husky Programming, © 2010
 * @since 1.6_19
 */
public class YesNoBox
{
  /** Places an error icon in a message. */
  final public static byte ERROR = JOptionPane.ERROR_MESSAGE;
  /** Places an information icon in a message. */
  final public static byte INFO = JOptionPane.INFORMATION_MESSAGE;
  /** No icon is used in a message. */
  final public static byte PLAIN = JOptionPane.PLAIN_MESSAGE;
  /** Places a question icon in a message. */
  final public static byte QUESTION = JOptionPane.QUESTION_MESSAGE;
  /** Places a warning icon in a message. */
  final public static byte WARNING = JOptionPane.WARNING_MESSAGE;
  /** Return value from class method if YES is chosen. */
  final public static int YES = 0;
  /** Return value from class method if NO is chosen. */
  final public static int NO = 1;
  /** Return value from class method if user closes window without selecting anything.
   * More than likely this should be treated as either a <code>CANCEL</code> or <code>NO</code>. */
  final public static int CLOSED = -1;
  private static String dir = java.io.File.separator + "bht" + java.io.File.separator + "resources" + java.io.File.separator;
//  private static ImageIcon warningIcon = Icons.warningIcon32, errorIcon = Icons.errorIcon32, infoIcon = Icons.infoIcon32, questionIcon = Icons.questionIcon32;

  /**
   *
   * Displays a dialog box with a yes/no answer. Answer buttons are automatically translated. The title and icon type are
   * automatically filled in.
   * @param prompt The <code>String</code> containing the text that comprises the message shown in the dialog box
   * @return a <code>boolean</code> representing whether the user pressed yes or no. A return value of<code>true</code>
   * represents that the user pressed the "Yes" button. Any other action returns <code>false</code>
   */
  public static boolean bool(String prompt)
  {
    return integer(prompt) == YES;
  }

  /**
   * Displays a dialog box with a yes/no answer. Answer buttons are automatically translated.
   * @param prompt The <code>String</code> containing the text that comprises the message shown in the dialog box
   * @param title The <code>String</code> containing the text in the title bar
   * @param type The <code>int</code> representing the icon type. Recommended that you use the corresponding <code>int</code>s
   * provided in this class or those in the javax.swing.JOptionPane class
   * @return a <code>boolean</code> representing whether the user pressed yes or no. A return value of <code>true</code>
   * represents that the user pressed the "Yes" button. Any other action returns a <code>false</code>
   */
  public static boolean bool(String prompt, String title, byte type)
  {
    return integer(prompt, title, type) == YES;
  }

  /**
   * Displays a dialog box with a yes/no answer. Answer buttons are automatically translated. The title and icon type are
   * automatically filled in.
   * @param prompt The <code>String</code> containing the text that comprises the message shown in the dialog box
   * @return an <code>int</code> representing whether the user pressed yes or nor simply closed the box. Compare to the included
   * constants to determine the user's action.
   */
  public static int integer(String prompt)
  {
    return integer(prompt, "Yes or No?", QUESTION);
  }

  /**
   * Displays a dialog box with a yes/no answer. Answer buttons are automatically translated.
   * @param prompt The <code>String</code> containing the text that comprises the message shown in the dialog box
   * @param title The <code>String</code> containing the text in the title bar
   * @param type The <code>byte</code> representing the icon type.
   * It is recommended that you use the corresponding <code>byte</code>s provided in this class or those in the
   * javax.swing.JOptionPane class
   * @return an <code>int</code> representing whether the user pressed yes or nor simply closed the box. It is recommended that
   * you compare this to the included <code>int</code>s or those in javax.swing.JOptionPane to determine the user's action.
   */
  public static int integer(String prompt, String title, byte type)
  {
    try
    {
      return JOptionPane.showConfirmDialog(null, prompt, title, JOptionPane.YES_NO_OPTION, type, Icons.getIcon(type));
    }
    catch (Throwable t)
    {
      t.printStackTrace();
      return JOptionPane.showConfirmDialog(null, prompt, title, JOptionPane.YES_NO_OPTION, type);
    }
  }
}
