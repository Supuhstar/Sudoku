package bht.tools.utilities;



import java.io.*;
import java.util.logging.*;
import bht.tools.misc.YesNoBox;

/**
 * A simple class that creates an logging file for your program. To use this, you must either create a <code>ProgLog</code>
 * object and call <code>objectName.logger</code>, call <code>new ProgLog().logger</code> each time (recommend only using for
 * logging one error), or create a new PrintStream object (e.g. <code>java.io.PrintStream log = new ProgLog().logger</code>).
 * <br /><br /><i><b>Note:</b> Formerly <tt>ErrorPrinter</tt>, this class does everything its predecessor did and more. Name was
 * changed to reflect the fact that this does much more, now, than print errors.
 * @author Blue Husky Programming
 * @version 1.1.0
 * @author Supuhstar
 */
public class ProgLog
{
  /** A <code>PrintStream</code> object which prints everything to a logging file in the package */
  public PrintStream logger;
  private String title, version, fileName, ext;
  private File logFile;

  /**
   * Creates a new <code>ProgLog</code> object and a new logging file in the package.
   * @param title a <code>String</code> containing the title of the program for which you are logging happenings. <br />Example:
   * <code>"Blue Husky's Program"</code>
   * @param version a <code>String</code> containing the version number of the program for which you are making this
   * logging file. <br />Example: <code>"1.2.10"</code>
   * @param fileName a <code>String</code> that represents the name of the logging file. <br />Example: <code>"Program
   * log"</code>
   * @param extension a <code>String</code> representing the extension of the logging file. <br />Example:
   * <code>"log"</code>
   */
  @SuppressWarnings("OverridableMethodCallInConstructor")
  public ProgLog(String title, String version, String fileName, String extension)
  {
    this.title = title;
    this.version = version;
    this.fileName = fileName;
    this.ext = extension;
//    javax.swing.JOptionPane.showMessageDialog(null, "This is a test", "Serious error - " + TITLE, javax.swing.JOptionPane.ERROR_MESSAGE, (getClass().getResource("/no.png") == null ? null : new javax.swing.ImageIcon(getClass().getResource("/no.png"))));
    ext = ext.substring(ext.indexOf('.') + 1);
    logFile = new File("");
    logFile = new File(System.getProperty("user.dir") + "\\" + fileName + "." + ext);
    logFile.setWritable(true);
//    file = correctExtension(file);
    try
    {
      logger = new java.io.PrintStream(logFile);
//      throw new FileNotFoundException();
    }
    catch (FileNotFoundException ex)
    {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
      FileWriter writeToFile;
      try
      {
        writeToFile = new FileWriter(logFile);
        writeToFile.write("");
        writeToFile.close();
//        throw new IOException();
      }
      catch (IOException ex1)
      {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex1);
//        javax.swing.JOptionPane.showMessageDialog(null, "A serious error has occurred while trying to make the logging file."
//                + " (.... dear lord, the irony)\n Tell Blue Husky Programming. that this occurred at line 102: \"" + ex1.toString() +
//                "\"", "Serious error - " + title, javax.swing.JOptionPane.ERROR_MESSAGE, (getClass().getResource("/no.png") ==
//                null ? null : new javax.swing.ImageIcon(getClass().getResource("/no.png"))));
        javax.swing.JOptionPane.showMessageDialog(null, "You can not run this program from another program (such as opening it "
                + "from an IM client or internet browser after downloading). Please go to where you saved the program and open "
                + "it directly from your file browser.", "Error when making program log - " + title, YesNoBox.ERROR);
        System.exit(1);
      }
    }
    initErrorFile();
    Runtime.getRuntime().addShutdownHook(
    new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        closeFile(0);
      }
    }));
  }

  /**
   * Creates a new <code>ProgLog</code> object and a new error-logging file in the package. The same as calling
   * <code>ProgLog(title, version, fileName, "log");</code>
   * @param title a <code>String</code> containing the title of the program for which you are logging happenings.
   * <br />Example: <code>"Blue Husky's Program"</code>
   * @param version a <code>String</code> containing the version number of the program for which you are making this logging
   * file. <br />Example: <code>"1.2.10"</code>
   * @param fileName a <code>String</code> that represents the name of the logging file. <br />Example: <code>"Program
   * log"</code>
   */
  @SuppressWarnings("OverridableMethodCallInConstructor")
  public ProgLog(String title, String version, String fileName)
  {
    this(title, version, fileName, "log");
  }

  /**
   * Creates a new <code>ProgLog</code> object and a new logging file in the package. The same as calling
   * <code>ProgLog(title, version, title + "-log(" + System.currentTimeMillis() + ")");</code>
   * @param title a <code>String</code> containing the title of the program for which you are logging happenings.
   * <br />Example: <code>"Blue Husky's Program"</code>
   * @param version a <code>String</code> containing the version number of the program for which you are making this logging
   * file. <br />Example: <code>"1.2.10"</code>
   */
  @SuppressWarnings("OverridableMethodCallInConstructor")
  public ProgLog(String title, String version)
  {
    this(title, version, title + "-log(" + System.currentTimeMillis() + ")");
  }

  /**
   * Creates a new <code>ProgLog</code> object and a new logging file in the package. The same as calling
   * <code>ProgLog("a program", "unknown");</code>
   */
  @SuppressWarnings("OverridableMethodCallInConstructor")
  public ProgLog()
  {
    this("a program", "unknown");
  }

  /**
   * Creates a log file and fills in information about the user's computer. This is automatically called upon creation of a
   * ProgLog object.
   */
  public void initErrorFile()
  {
    File errorFile = new File("");
    errorFile = new File(System.getProperty("user.dir") + "\\" + fileName + ".log");
    try
    {
      logger = new java.io.PrintStream(errorFile);
    }
    catch (FileNotFoundException ex)
    {
      Logger.getLogger(getClass().getName()).log(Level.WARNING, null, ex);
      FileWriter writeToFile;
      try
      {
        writeToFile = new FileWriter(errorFile);
        writeToFile.write("");
        writeToFile.close();
      }
      catch (IOException ex1)
      {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex1);
        javax.swing.JOptionPane.showMessageDialog(null,"A serious error has occurred while trying to make the logging file."
                + " (.... dear lord, the irony)\n Tell Blue Husky, etc. that this occurred: \"" + ex1.getStackTrace()[0] + "\n"
                + ex1.getStackTrace()[1] + "\n" + ex1.getStackTrace()[2] + "\n" + ex1.getStackTrace()[3] + "\n" + ex1.getStackTrace()[4]
                + ex1.getStackTrace()[5] + "\n" + ex1.getStackTrace()[6] + "\n" + ex1.getStackTrace()[7] + "\n"
                + "\n" + "\"", "Serious error - " + title, javax.swing.JOptionPane.ERROR_MESSAGE,
                (getClass().getResource("/no.png") == null ? null : new javax.swing.ImageIcon(getClass().getResource("/no.png"))));
        System.exit(1);
      }
    }

    logger.println("This is the error-reporting file for " + title + " (version " + version + ")");
    logger.println();
    logger.println();
    logger.println("              time of file init: " + java.util.Calendar.getInstance().getTime());
    logger.println("               program run from: " + System.getProperty("user.dir"));
    logger.println("program used to run the program: " + System.getProperty("java.class.path"));
    logger.println("                        OS name: " + System.getProperty("os.name") + " (version " + System.getProperty(
            "os.version") + ")");
    logger.println("                OS Architecture: " + System.getProperty("os.arch"));
    logger.println("           Available Processors: " + Runtime.getRuntime().availableProcessors());
    logger.println("               Available Memory: " + bytes(Runtime.getRuntime().freeMemory()) + " / " + bytes(Runtime.getRuntime().totalMemory()) + " / " + bytes(Runtime.getRuntime().maxMemory()));
    logger.println("  Java Virtural Machine version: " + System.getProperty("java.vm.version"));
    logger.println("           Java runtime version: " + System.getProperty("java.runtime.version"));
    logger.println("                        Country: " + System.getProperty("user.country"));
    logger.println("                      Time Zone: " + System.getProperty("user.timezone"));
    logger.println("                       Language: " + System.getProperty("user.language"));
    logger.println("                       Username: " + System.getProperty("user.name"));
    for (int i=0; i < 0x8F; i++)
      logger.print("-");
    logger.println();
  }

  /**
   * Sets the title of the program, shown in error dialogs and the log file.
   * @param title a <code>String</code> containing the title of the program for which you are logging happenings.
   * <br />Example: <code>"Blue Husky's Program"</code>
   */
  public void setTitle(String title)
  {
    this.title = title;
  }

  /**
   * Sets the version of the program, shown in error dialogs and the log file.
   * @param version a <code>String</code> containing the version number of the program for which you are making this logging
   * file. <br />Example: <code>"1.2.10"</code>
   */
  public void setVersion(String version)
  {
    this.version = version;
  }

  /**
   * Sets the extension of the log file. Default is <code>.log</code>
   * @param extension a <code>String</code> representing the extension of the logging file. <br />Example: <code>.log</code>
   */
  public void setExtension(String extension)
  {
    ext = extension;
  }

  /**
   * Sets the name of the log file. Default is the title of the program followed by <code>" log"</code>
   * @param fileName a <code>String</code> representing the name of the logging file. <br />Example: <code>"Program log"</code>
   */
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }

  /**
   * Returns the title of the program, shown in error dialogs and the log file.
   * @return a <code>String</code> containing the title of the program for which you are logging happenings.
   * <br />Example: <code>"Blue Husky's Program"</code>
   */
  public String getTitle()
  {
    return title;
  }

  /**
   * Returns the version of the program, shown in error dialogs and the log file.
   * @return a <code>String</code> containing the version number of the program for which you are making this log file.
   * <br />Example: <code>"1.2.10"</code>
   */
  public String getVersion()
  {
    return version;
  }

  /**
   * Returns the extension of the log file.
   * @return a <code>String</code> representing the extension of the log file. <br />Example: <code>".log"</code>
   */
  public String getExtension()
  {
    return ext;
  }

  /**
   * Returns the name of the log file.
   * @return a <code>String</code> representing the name of the log file. <br />Example: <code>"Program log"</code>
   */
  public String getFileName()
  {
    return fileName;
  }

  /**
   * Returns the name of the log file.
   * @return a <code>String</code> representing the name of the log file. <br />Example: <code>"Program log"</code>
   */
  public java.io.File getFile()
  {
    return logFile;
  }

  /**
   * Logs a throwable in the selected <code>java.io.PrintStream</code>
   * @param t the <code>Throwable</code> that represents the error, exception, etc. that happened
   * @param stream the <code>java.io.PrintStream</code> into which the error will be logged.<br />
   * <strong>Please note that this is meant to be the included <code>logger</code> <tt>java.io.PrintStream</tt> object.
   * Please utilize that in here.</strong>
   * @param method The <code>String</code> containing the name of the method in which this is called. Leaving this empty will
   * simply leave less information to be reviewed in the file
   * @deprecated Please use <tt>logThrowable(Throwable t, String method);</tt>
   */
  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  public void logThrowable(Throwable t, java.io.PrintStream stream, String method)
  {
    stream.println();
    stream.println("<<<<< " + t + (method == null || method.isEmpty() ? "" : " found in " + method) + " on " + 
            java.util.Calendar.getInstance().getTime());
    t.printStackTrace(stream);
    stream.println();
    stream.println(">>>>> end log of " + t);
    System.err.println(t + " logged in error file");
  }

  /**
   * Logs a throwable in the selected <code>java.io.PrintStream</code>
   * @param t the <code>Throwable</code> that represents the error, exception, etc. that happened
   * @param stream the <code>java.io.PrintStream</code> into which the error will be logged.<br />
   * <strong>Please note that this is meant to be the <code>logger</code> object. Please utilize that in here.</strong>
   * @deprecated Please use <tt>logThrowable(Throwable t);</tt>
   */
  public void logThrowable(Throwable t, java.io.PrintStream stream)
  {
    logThrowable(t, stream, t.getStackTrace()[0].getMethodName());
  }

  /**
   * Logs a throwable in the selected <code>java.io.PrintStream</code>
   * @param t the <code>Throwable</code> that represents the error that happened
   * @param method The <code>String</code> containing the name of the method in which this is called. Leaving this empty will
   * simply leave less information to be reviewed in the file
   */
  public void logThrowable(Throwable t, String method)
  {
    logThrowable(t, logger, method);
  }

  /**
   * Logs a throwable in the selected <code>java.io.PrintStream</code>
   * @param t the <code>Throwable</code> that represents the error that happened
   */
  public void logThrowable(Throwable t)
  {
    logThrowable(t, logger, t.getStackTrace()[0].getMethodName());
  }
  
  /**
   * Finishes and closes <code>logger</code> and sets the file it wrote to to no longer be writable
   * @param status the status of the closing of the file and/or program.<br />
   * By convention, a nonzero status code indicates abnormal termination.
   */
  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  public void closeFile(int status)
  {
    logger.println();
    logger.println();
    logger.print("<<<<<Program exited ");
    if (status == 0)
      logger.print("properly");
    else
      logger.print("with error code " + status);
    logger.println(" on " + java.util.Calendar.getInstance().getTime() + ">>>>>");

    byte good = 0xf;

    try
    {
      if (!logFile.setWritable(false))
      {
        good = (byte)(good ^ 0x1);
      }
    }
    catch (Throwable t)
    {
      good = (byte)(good ^ 0x1);
    }

    try
    {
      if (!logFile.setExecutable(false))
      {
        good = (byte)(good ^ 0x2);
      }
    }
    catch (Throwable t)
    {
      good = (byte)(good ^ 0x2);
    }

    try
    {
      if (!logFile.setReadOnly())
      {
        good = (byte)(good ^ 0x4);
      }
    }
    catch (Throwable t)
    {
      good = (byte)(good ^ 0x4);
    }

    try
    {
      if (!logFile.setReadable(true))
      {
        good = (byte)(good ^ 0x8);
      }
    }
    catch (Throwable t)
    {
      good = (byte)(good ^ 0x8);
    }

    if (good != 0xf)
    {
      System.err.println("Error file improperly closed: (error code " + Integer.toHexString(good) + ")");
      logger.println("NOTE: Error file improperly closed: (error code " + Integer.toHexString(good) + ")");
    }
    logger.close();
  }

  private String bytes(long bytes)
  {
    if ((bytes / 1024) >= 1)
    {
      if (((bytes / 1024) / 1024) >= 1)
      {
        if ((((bytes / 1024) / 1024) / 1024) >= 1)
        {
          return Double.toString((double)bytes / (1024 * 1024 * 1024)).substring(0, Double.toString((double)bytes / (1024 * 1024 * 1024)).indexOf(".") + 2) + " GB";
        }
        return Double.toString((double)bytes / (1024 * 1024)).substring(0, Double.toString((double)bytes / (1024 * 1024)).indexOf(".") + 2) + " MB";
      }
      return ((double)bytes / 1024) + " KB";
    }
    return bytes + " B";
  }

  /**
   * Logs an event in the logging file
   * @param evt The event to be logged (such as an <tt>java.awt.event.ActionEvent</tt>)
   * @param info information about the event <br /><b>Example:</b> <tt>"Button 2 pressed"</tt>
   * @param method the method used to invoke the event
   * <br /><b>Example:</b> <tt>"jButtonActionPerformed(java.awt.event.ActionEvent evt)"</tt>
   */
  public void logAction(java.util.EventObject evt, String info, String method)
  {
    logger.println();
    logger.println("<@>" + (info != null && !info.isEmpty() ? info : "") + (method != null && !method.isEmpty() ?
      (info != null && !info.isEmpty() ? " inside " + method : method + " invoked ") : "") + " => " + (evt != null ? evt.getClass().getName() + " caused by " + evt.getSource() + ". Other info: " + evt : " NO GIVEN EVENT!"));
  }


  /**
   * Logs an event in the logging file<br />
   * <i>Same as calling <tt>logAction(evt, null, null);</tt></i>
   * @param evt The event to be logged (such as an <tt>java.awt.event.ActionEvent</tt>)
   */
  public void logAction(java.util.EventObject evt)
  {
    logAction(evt, null, null);
  }


  /**
   * Logs an event in the logging file<br />
   * <i>Same as calling <tt>logAction(null, info, method);</tt></i>
   * @param info information about the event <br /><b>Example:</b> <tt>"Button 2 pressed"</tt>
   * @param method the method used to invoke the event
   * <br /><b>Example:</b> <tt>"button2ActionPerformed(java.awt.event.ActionEvent evt)</tt>
   */
  public void logAction(String info, String method)
  {
    logAction(null, info, method);
  }


  /**
   * Logs an event in the logging file<br />
   * <i>Same as calling <tt>logAction(evt, info, null);</tt></i>
   * @param evt The event to be logged (such as an <tt>java.awt.event.ActionEvent</tt>)
   * @param info information about the event <br /><b>Example:</b> <tt>"Button 2 pressed"</tt>
   */
  public void logAction(java.util.EventObject evt, String info)
  {
    logAction(evt, info, null);
  }

  /**
   * Logs an event in the logging file<br />
   * <i>Same as calling <tt>logAction(null, info, null);</tt></i>
   * @param info information about the event <br /><b>Example:</b> <tt>"Button 2 pressed"</tt>
   */
  public void logAction(String info)
  {
    logAction(null, info, null);
  }

  /**
   * <i>Same as calling <tt>logAction(null, null, null);</tt></i><br />
   * Results in the following being logged:
   * <blockquote><tt>&lt;@&gt; =&gt; NO GIVEN EVENT!</tt></blockquote>
   * @deprecated PLEASE USE ANY <tt>logAction</tt> METHOD BUT THIS
   */
  public void logAction()
  {
    logAction(null, null, null);
  }
}
