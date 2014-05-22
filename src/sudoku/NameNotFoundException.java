package sudoku;

public class NameNotFoundException extends Exception
{
  private static final long serialVersionUID = 1L;
  
  NameNotFoundException (String username)
  {
    super ("Cannot find username \"" + username + "\"");
  }
}