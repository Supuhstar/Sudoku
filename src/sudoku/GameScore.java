package sudoku;

import java.io.*;
import java.util.*;
import javax.swing.*;
import bht.tools.misc.YesNoBox;

public class GameScore
{
  public static void tryScore(String fileName, String username, long win, long lose) throws IOException
  {//System.out.println("tryScore");
    if (addScore(fileName, username, win, lose).equalsIgnoreCase("Username \"" + username + "\" already exists."))
    {
      System.err.println("Changing score of " + username + ".");
      changeScore(fileName, username, win, lose);
    }

//    return "Score added/changed";
  }//END tryScore METHOD

  public static String changeScore(String fileName, String username, long win, long lose) throws IOException
  {//System.out.println("changeScore");
    Scanner scan;
    String[] usernames;
    long[] wins, losses;
    int fileLength = 0;

    username = fixName(username);

    try
    {
      scan = new Scanner(new File(fileName));
    }
    catch (FileNotFoundException ex)
    {
      System.err.println("File not found: " + fileName);
//      return "File not found: " + fileName;

      FileWriter writeToFile = new FileWriter(fileName);
      writeToFile.write("//BEGIN\r\n" + "//END");
      writeToFile.close();

      scan = new Scanner(new File(fileName));
    }

    String fileContents = "", fileLine = scan.nextLine();
    while (!fileLine.equals("//END")) //First sweep gets the length of the file
    {
      fileLine = scan.nextLine();
      if (!fileLine.equals("//END"))
        fileLength++;
    }

    usernames = new String[fileLength];
    wins = new long[fileLength];
    losses = new long[fileLength];

    scan = new Scanner(new File(fileName)); //RESET THE SCANNER FOR THE SECOND SWEEP
    String spare = scan.next();

    int i;
    for (i = 0; i < fileLength; i++) //Second sweep makes arrays of all values
    {
      try
      {
        usernames[i] = scan.next();
        //System.out.print(usernames[i]);
        wins[i] = scan.nextLong();
        //System.out.print(wins[i]);
        losses[i] = scan.nextLong();
        //System.out.println(losses[i]);
      }
      catch (InputMismatchException ex)
      {
        if (usernames[i].equals(username))
        {
          if (YesNoBox.bool(
          "Unexpected glitch when reading " + usernames[i] + "'s scores.\nThey may not be present. Would you like to reset " + usernames[i]
          + "'s scores in the file \"" + fileName + "\" to " + win + " wins and " + lose
          + " losses?\n\n(NOTE: The file is corrupt and the program you are trying to use may not work without resetting these scores. It is reccommended that you click Yes)",
                            "Error from GameScore", YesNoBox.ERROR))
          {
            wins[i] = win;
            losses[i] = lose;
          }
        }
        else if (YesNoBox.bool(
        "Unexpected glitch when reading " + usernames[i] + "'s scores.\nThey may not be present. Would you like to reset " + usernames[i]
        + "'s scores in the file \"" + fileName
        + "\" to 0 wins and 0 losses?\n\n(NOTE: The file is corrupt and the program you are trying to use may not work without resetting these scores. It is reccommended that you click Yes)",
                               "Error from GameScore", YesNoBox.ERROR))
        {
          wins[i] = 0;
          losses[i] = 0;
        }
        else
          JOptionPane.showMessageDialog(null, "CAUTION: This program may not work if this file is corrupt. You are warned.", "WARNING from GameScore", 0);
      }
    }

    for (i = 0; i < fileLength; i++)
    {
      if (usernames[i].equalsIgnoreCase(username))
      {
        wins[i] = win;
        losses[i] = lose;
        break;
      }
    }

    for (i = 0; i < fileLength; i++)
    {
      fileContents += usernames[i] + "\t" + wins[i] + " " + losses[i] + "\r\n";
    }


    //System.out.println(fileContents);
    FileWriter writeToFile = new FileWriter(fileName);
    FileWriter addToFile = new FileWriter(fileName, true);
    writeToFile.write("//BEGIN\r\n" + fileContents + "//END");
    writeToFile.close();
    return "Scores successfully changed.";
  }//END changeScore METHOD

  public static String addScore(String fileName, String username, long win, long lose) throws IOException
  {//System.out.println("addScore");
    Scanner scan;
    String[] usernames;
    long[] wins, losses;
    int fileLength = 0;

    username = fixName(username);

    try
    {
      scan = new Scanner(new File(fileName));
    }
    catch (FileNotFoundException ex)
    {
      System.err.println("File not found: " + fileName);
//      return "File not found: " + fileName;

      FileWriter writeToFile = new FileWriter(fileName);
      writeToFile.write("//BEGIN\r\n" + "//END");
      writeToFile.close();

      scan = new Scanner(new File(fileName));
    }

    String fileContents = "", fileLine = scan.nextLine();
    while (!fileLine.equals("//END")) //First sweep gets the length of the file
    {
      fileLine = scan.nextLine();
      if (!fileLine.equals("//END"))
        fileLength++;
    }

    usernames = new String[fileLength];
    wins = new long[fileLength];
    losses = new long[fileLength];

    scan = new Scanner(new File(fileName)); //RESET THE SCANNER FOR THE SECOND SWEEP
    String spare = scan.next();

    int i;
    for (i = 0; i < fileLength; i++) //Second sweep makes arrays of all values
    {
      try
      {
        usernames[i] = scan.next();
        //System.out.print(usernames[i]);
        wins[i] = scan.nextLong();
        //System.out.print(wins[i]);
        losses[i] = scan.nextLong();
        //System.out.println(losses[i]);
      }
      catch (InputMismatchException ex)
      {
        if (YesNoBox.bool(
        "Unexpected glitch when reading " + usernames[i] + "'s scores\nThey may not be present.\n"
        + "Would you like to reset " + usernames[i] + "'s scores in the file \"" + fileName + "\" to 0 wins and 0 losses?\n\n"
        + "(NOTE: The file is corrupt and the program you are trying to use may not work without resetting these scores. It is reccommended that you click Yes)",
                          "Error from GameScore", YesNoBox.ERROR))
        {
          wins[i] = 0;
          losses[i] = 0;
        }
        else
          JOptionPane.showMessageDialog(null, "CAUTION: This program may not work if this file is corrupt. You are warned.", "WARNING from GameScore", 0);
      }
    }

    for (i = 0; i < fileLength; i++)
    {
      if (usernames[i].equalsIgnoreCase(username))
      {
        System.err.println("Username \"" + username + "\" already exists.");
        return ("Username \"" + username + "\" already exists.");
      }
    }


    for (i = 0; i < fileLength; i++)
    {
      fileContents += usernames[i] + "\t" + wins[i] + " " + losses[i] + "\r\n";
    }

    fileContents += username + "\t" + win + " " + lose + "\r\n";

    //System.out.println(fileContents);
    FileWriter writeToFile = new FileWriter(fileName);
    FileWriter addToFile = new FileWriter(fileName, true);
    writeToFile.write("//BEGIN\r\n" + fileContents + "//END");
    writeToFile.close();
    return ("Score successfully added.");
  }//END addScore METHOD

  public static String getScore(String fileName, String username) throws IOException
  {
    Scanner scan;
    String[] usernames;
    long wins[], losses[], win = -1, lose = -1;
    int fileLength = 0;

    username = fixName(username);

    try
    {
      scan = new Scanner(new File(fileName));
    }
    catch (FileNotFoundException ex)
    {
      System.err.println("File not found: " + fileName);
//      return "File not found: " + fileName;

      FileWriter writeToFile = new FileWriter(fileName);
      writeToFile.write("//BEGIN\r\n" + "//END");
      writeToFile.close();

      scan = new Scanner(new File(fileName));
    }

    String fileContents = "", fileLine = scan.nextLine();
    while (!fileLine.equals("//END")) //First sweep gets the length of the file
    {
      fileLine = scan.nextLine();
      if (!fileLine.equals("//END"))
        fileLength++;
    }

    usernames = new String[fileLength];
    wins = new long[fileLength];
    losses = new long[fileLength];

    scan = new Scanner(new File(fileName)); //RESET THE SCANNER FOR THE SECOND SWEEP
    String spare = scan.next();

    int i;
    for (i = 0; i < fileLength; i++) //Second sweep makes arrays of all values
    {
      try
      {
        usernames[i] = scan.next();
        //System.out.print(usernames[i]);
        wins[i] = scan.nextLong();
        //System.out.print(wins[i]);
        losses[i] = scan.nextLong();
        //System.out.println(losses[i]);
      }
      catch (InputMismatchException ex)
      {
        changeScore(fileName, usernames[i], 0, 0);
      }
    }

    for (i = 0; i < fileLength; i++)
    {
      if (usernames[i].equalsIgnoreCase(username))
      {
        win = wins[i];
        lose = losses[i];
        break;
      }
    }

    for (i = 0; i < fileLength; i++)
    {
      if (usernames[i].equalsIgnoreCase(username))
        break;
    }
    if (i == fileLength)
    {
      System.err.println("No such username!");
      return ("No such username!");
    }
    return (win + " " + lose);
  }//END getScore METHOD

  public static long getWins(String fileName, String username) throws IOException, NameNotFoundException, ValueGlitch
  {
    Scanner scan;
    String[] usernames;
    long wins[], losses[], win = -1, lose = -1;
    int fileLength = 0;

    username = fixName(username);

    try
    {
      scan = new Scanner(new File(fileName));
    }
    catch (FileNotFoundException ex)
    {
      System.err.println("File not found: " + fileName);
//      return "File not found: " + fileName;

      FileWriter writeToFile = new FileWriter(fileName);
      writeToFile.write("//BEGIN\r\n" + "//END");
      writeToFile.close();

      scan = new Scanner(new File(fileName));
    }

    String fileContents = "", fileLine = scan.nextLine();
    while (!fileLine.equals("//END")) //First sweep gets the length of the file
    {
      fileLine = scan.nextLine();
      if (!fileLine.equals("//END"))
        fileLength++;
    }

    usernames = new String[fileLength];
    wins = new long[fileLength];
    losses = new long[fileLength];

    scan = new Scanner(new File(fileName)); //RESET THE SCANNER FOR THE SECOND SWEEP
    String spare = scan.next();

    int i;

    for (i = 0; i < fileLength; i++) //Second sweep makes arrays of all values
    {
      try
      {
        usernames[i] = scan.next();
        //System.out.print(usernames[i]);
        wins[i] = scan.nextLong();
        //System.out.print(wins[i]);
        losses[i] = scan.nextLong();
        //System.out.println(losses[i]);
      }
      catch (InputMismatchException ex)
      {
        changeScore(fileName, usernames[i], 0, 0);
      }
    }

    for (i = 0; i < fileLength; i++)
    {
      if (usernames[i].equalsIgnoreCase(username))
      {
        win = wins[i];
        lose = losses[i];
        break;
      }
    }

    for (i = 0; i < fileLength; i++)
    {
      if (usernames[i].equalsIgnoreCase(username))
        break;
    }
    if (i == fileLength)
    {
      throw new NameNotFoundException(username);
    }
    return (win);
  }//END getLosses METHOD

  public static long getLosses(String fileName, String username) throws IOException, NameNotFoundException
  {
    Scanner scan;
    String[] usernames;
    long wins[], losses[], win = -1, lose = -1;
    int fileLength = 0;

    username = fixName(username);

    try
    {
      scan = new Scanner(new File(fileName));
    }
    catch (FileNotFoundException ex)
    {
      System.err.println("File not found: " + fileName);
//      return "File not found: " + fileName;

      FileWriter writeToFile = new FileWriter(fileName);
      writeToFile.write("//BEGIN\r\n" + "//END");
      writeToFile.close();

      scan = new Scanner(new File(fileName));
    }

    String fileContents = "", fileLine = scan.nextLine();
    while (!fileLine.equals("//END")) //First sweep gets the length of the file
    {
      fileLine = scan.nextLine();
      if (!fileLine.equals("//END"))
        fileLength++;
    }

    usernames = new String[fileLength];
    wins = new long[fileLength];
    losses = new long[fileLength];

    scan = new Scanner(new File(fileName)); //RESET THE SCANNER FOR THE SECOND SWEEP
    String spare = scan.next();

    int i;
    for (i = 0; i < fileLength; i++) //Second sweep makes arrays of all values
    {
      try
      {
        usernames[i] = scan.next();
        //System.out.print(usernames[i]);
        wins[i] = scan.nextLong();
        //System.out.print(wins[i]);
        losses[i] = scan.nextLong();
        //System.out.println(losses[i]);
      }
      catch (InputMismatchException ex)
      {
        changeScore(fileName, usernames[i], 0, 0);
      }
    }


    for (i = 0; i < fileLength; i++)
    {
      if (usernames[i].equalsIgnoreCase(username))
      {
        win = wins[i];
        lose = losses[i];
        break;
      }
    }

    for (i = 0; i < fileLength; i++)
    {
      if (usernames[i].equalsIgnoreCase(username))
        break;
    }
    if (i == fileLength)
    {
      throw new NameNotFoundException(username);
    }
    return (lose);
  }//END getLosses METHOD

  public static String allScores(String fileName) throws IOException
  {//System.out.println("allScores");
    Scanner scan;
    String[] usernames;
    long[] wins, losses;
    int fileLength = 0, win = -1, lose = -1;
    try
    {
      scan = new Scanner(new File(fileName));
    }
    catch (FileNotFoundException ex)
    {
      System.err.println("File not found: " + fileName);
//      return "File not found: " + fileName;

      FileWriter writeToFile = new FileWriter(fileName);
      writeToFile.write("//BEGIN\r\n" + "//END");
      writeToFile.close();

      scan = new Scanner(new File(fileName));
    }

    String fileContents = "", fileLine = scan.nextLine();
    while (!fileLine.equals("//END")) //First sweep gets the length of the file
    {
      fileLine = scan.nextLine();
      if (!fileLine.equals("//END"))
        fileLength++;
    }
    if (fileLength == 0)
      return "There are no scores in the scoreboard!\nSave yours and get 1st place ;3";

    usernames = new String[fileLength];
    wins = new long[fileLength];
    losses = new long[fileLength];

    scan = new Scanner(new File(fileName)); //RESET THE SCANNER FOR THE SECOND SWEEP
    String spare = scan.next();

    int i;
    for (i = 0; i < fileLength; i++) //Second sweep makes arrays of all values
    {
      try
      {
        usernames[i] = scan.next();
        //System.out.print(usernames[i]);
        wins[i] = scan.nextLong();
        //System.out.print(wins[i]);
        losses[i] = scan.nextLong();
        //System.out.println(losses[i]);
      }
      catch (InputMismatchException ex)
      {
        changeScore(fileName, usernames[i], 0, 0);
      }
    }

    usernames = readNames(usernames);

    fileContents = "Username\t Wins\t Losses\n";
    for (i = 0; i < fileLength; i++)
    {
      fileContents += (i + 1 + ": " + usernames[i] + (usernames[i].length() < 5 ? "\t\t" : "\t ") + wins[i] + "\t " + losses[i] + "\n");
    }

    return (fileContents);
  }//END allScores METHOD

  public static String sortScores(String fileName) throws IOException
  {//System.out.println("sortScores");
    Scanner scan;
    String[] usernames, oUser;
    long[] wins, losses, order, oWin, oLose;
    int fileLength = 0, highScore = 0;
    try
    {
      scan = new Scanner(new File(fileName));
    }
    catch (FileNotFoundException ex)
    {
      System.err.println("File not found: " + fileName);
//      return "File not found: " + fileName;

      FileWriter writeToFile = new FileWriter(fileName);
      writeToFile.write("//BEGIN\r\n" + "//END");
      writeToFile.close();

      scan = new Scanner(new File(fileName));
    }

    String fileContents = "", fileLine = scan.nextLine();
    while (!fileLine.equals("//END")) //First sweep gets the length of the file
    {
      fileLine = scan.nextLine();
      if (!fileLine.equals("//END"))
        fileLength++;
    }

    usernames = new String[fileLength];
    wins = new long[fileLength];
    losses = new long[fileLength];
    order = new long[fileLength];
    oUser = new String[fileLength];
    oWin = new long[fileLength];
    oLose = new long[fileLength];

    scan = new Scanner(new File(fileName)); //RESET THE SCANNER FOR THE SECOND SWEEP
    String spare = scan.next();

    int i;
    for (i = 0; i < fileLength; i++) //Second sweep makes arrays of all values
    {
      try
      {
        usernames[i] = scan.next();
        oUser[i] = usernames[i];
        //System.out.print(usernames[i]);
        wins[i] = scan.nextLong();
        oWin[i] = wins[i];
        //System.out.print(wins[i]);
        losses[i] = scan.nextLong();
        oLose[i] = losses[i];
        //System.out.println(losses[i]);
      }
      catch (InputMismatchException ex)
      {
        //System.err.println(wins[i] + " " + losses[i]);
        changeScore(fileName, usernames[i], (wins[i] != 0 ? wins[i] : 0), (losses[i] != 0 ? losses[i] : 0));
      }
    }

    for (i = 0; i < fileLength; i++)
    {
      for (int j = 0; j < fileLength - 1; j++)
      {
        if (oWin[j] < oWin[j + 1])
        {
          long spareLong = oWin[j];
          oWin[j] = oWin[j + 1];
          oWin[j + 1] = spareLong;
          spareLong = oLose[j];
          oLose[j] = oLose[j + 1];
          oLose[j + 1] = spareLong;
          String spareStr = oUser[j];
          oUser[j] = oUser[j + 1];
          oUser[j + 1] = spareStr;
        }
      }
    }

    fileContents = "";
    for (i = 0; i < fileLength; i++)
    {
      fileContents += oUser[i] + "\t" + oWin[i] + " " + oLose[i] + "\r\n";
    }

    //fileContents += username + "\t" + win + " " + lose + "\r\n";

    //System.out.println(fileContents);
    FileWriter writeToFile = new FileWriter(fileName);
    FileWriter addToFile = new FileWriter(fileName, true);
    writeToFile.write("//BEGIN\r\n" + fileContents + "//END");
    writeToFile.close();
    return ("Scores successfully sorted.");
  }//END sortScores METHOD

  /**
   * 
   * @param fileName
   * @param scores
   * @return 
   * @throws IOException
   */
  public static Object[][] topScores(String fileName, int scores) throws IOException
  {//System.out.println("topScores");
    sortScores(fileName);

    Scanner scan;
    String[] usernames;
    long[] wins, losses;
    int fileLength = 0, win = -1, lose = -1;
    try
    {
      scan = new Scanner(new File(fileName));
    }
    catch (FileNotFoundException ex)
    {
      System.err.println("File not found: " + fileName);
//      return "File not found: " + fileName;

      FileWriter writeToFile = new FileWriter(fileName);
      writeToFile.write("//BEGIN\r\n" + "//END");
      writeToFile.close();

      scan = new Scanner(new File(fileName));
    }

    Object fileContents[][], fileLine = scan.nextLine();
    while (!fileLine.equals("//END")) //First sweep gets the length of the file
    {
      fileLine = scan.nextLine();
      if (!fileLine.equals("//END"))
        fileLength++;
    }

    if (fileLength == 0)
      return new String[][]
      {
        {
          "There are no scores in the scoreboard!","Save yours and get 1st place ;3", null
        }
      };
    usernames = new String[fileLength];
    wins = new long[fileLength];
    losses = new long[fileLength];

    scan = new Scanner(new File(fileName)); //RESET THE SCANNER FOR THE SECOND SWEEP
    /*String spare = */scan.next();

    int i;
    for (i = 0; i < fileLength; i++) //Second sweep makes arrays of all values
    {
      try
      {
        usernames[i] = scan.next();
        //System.out.print(usernames[i]);
        wins[i] = scan.nextLong();
        //System.out.print(wins[i]);
        losses[i] = scan.nextLong();
        //System.out.println(losses[i]);
      }
      catch (InputMismatchException ex)
      {
        System.err.println(wins[i] + " " + losses[i]);
        changeScore(fileName, usernames[i], (wins[i] != 0 ? wins[i] : 0), (losses[i] != 0 ? losses[i] : 0));
      }
    }

    fileContents = new Object[scores][3];

    for (i = 0; i < fileLength && i < scores; i++)
    {
      fileContents[i][0] = usernames[i];
      fileContents[i][1] = wins[i];
      fileContents[i][2] = losses[i];
    }

    return (fileContents);
  }//END topScores METHOD

  public static String fixName(String user)
  {//System.out.println("fixName");
    String username = user;

    for (int i = 0; i < username.length(); i++)
    {//System.out.println("fixName username: " + username + "; username.charAt(" + i + "): " + username.charAt(i));
      if (username.charAt(i) == ' ' || username.charAt(i) == '\t' || username.charAt(i) == '\n' || username.charAt(i) == '\r' || username.charAt(i) == '\b')
        username = username.substring(0, i) + "_" + username.substring(i + 1, username.length());
    }
    return username;
  }

  public static String[] readNames(String[] users)
  {//System.out.println("readNames");
    String[] usernames = users;

    for (int i = 0; i < usernames.length; i++)
    {
      for (int j = 0; j < usernames[i].length(); j++)
      {//System.out.println("readNames usernames[" + i + "]: " + usernames[i] + "; usernames[" + i + "].charAt(" + j + "): " + usernames[i].charAt(j));
        if (usernames[i].charAt(j) == '_')
          usernames[i] = usernames[i].substring(0, j) + " " + usernames[i].substring(j + 1, usernames[i].length());
      }
    }
    return usernames;
  }
}
