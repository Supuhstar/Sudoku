package sudoku;

public class ValueGlitch extends Exception
{
  static final long serialVersionUID = -384123952320643537L;
  
  ValueGlitch (String username)
  {
    super ("Unexpected glitch when reading " + username + "'s win score.\n(it may not be present. Try resetting the scores with a call to GameScore.addScore(java.lang.String, java.lang.String, int, int)");
  }
}