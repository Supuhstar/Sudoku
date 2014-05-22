/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

/**
 *
 * @author Supuhstar
 */
public class Sudoku
{
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    try
    {
      byte w = 9, s = 3;
      if (args.length > 0)
      {
        try
        {
          w = Byte.parseByte(args[0]);
          s = Byte.parseByte(args[1]);
        }
        catch (Throwable t)
        {
          w = 9;
          s = 3;
        }
      }
      System.out.println(java.util.Arrays.toString(args));
      SudokuFrame sf = new SudokuFrame(w, s);
      sf.setSize(512, 512);
      bht.tools.effects.CompAction.center(sf);
      new bht.tools.effects.CompAction().ghostIn(sf);
    }
    catch (Throwable t)
    {
      String s = t.getClass().getSimpleName();
      t.printStackTrace();
      if (bht.tools.misc.YesNoBox.bool("Program threw a" + (Character.toLowerCase(s.charAt(0)) == 'a' || Character.toLowerCase(s.charAt(0)) == 'e' || Character.
                                                            toLowerCase(s.charAt(0)) == 'i' || Character.toLowerCase(s.charAt(0)) == 'o' || Character.
                                                            toLowerCase(s.charAt(0)) == 'u' || Character.toLowerCase(s.charAt(0)) == 'y' ? "n " : " ") + s
                                       + " when starting. Quit for safety's sake?", "Error - " + SudokuFrame.TITLE,
                                       bht.tools.misc.YesNoBox.ERROR))
        System.exit(t.hashCode());
    }
  }
}
