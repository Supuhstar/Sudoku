/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

/**
 *
 * @author Supuhstar
 */
class TriedAllNumbersException extends Exception
{
  private static final long serialVersionUID = 1L;
  private String detailMessage = "All the possible numbers for this square have been tried.";

  @Override
  public String getMessage()
  {
    return detailMessage;
  }
}
