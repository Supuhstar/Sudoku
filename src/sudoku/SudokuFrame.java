/*
 * SudokuFrame.java
 *
 * Created on Dec 17, 2010, 4:23:21 PM
 */
package sudoku;

import bht.resources.Icons;
import bht.tools.effects.Colors;
import bht.tools.effects.CompAction;
import bht.tools.misc.YesNoBox;
import bht.tools.utilities.ProgLog;
import java.io.FileInputStream;

/**
 * The frame and inner workings of the Sudoku game by Blue Husky Studios
 * @author Supuhstar
 * @version 1.0.0
 */
public class SudokuFrame extends javax.swing.JFrame
{
  private static final long serialVersionUID = 1L;

  /** Creates new form SudokuFrame */
  public SudokuFrame()
  {
    this((byte)9, (byte)3);
  }

  public SudokuFrame(byte width, byte segments)
  {
    // <editor-fold defaultstate="collapsed" desc="Fonts">
    log = new bht.tools.utilities.ProgLog(TITLE, VERSION);
    try
    {
//      java.net.URI uri = SudokuFrame.class.getResource("/resources/segoescb.ttf").toURI();
      java.io.InputStream i = new FileInputStream("resources/segoescb.ttf");
//      System.out.println(uri);
      font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, i);
      font = font.deriveFont((float)bht.tools.effects.Colors.DEFAULT_SIZE);
    }
    catch (Throwable t)
    {
//      System.out.println(t + " when trying to set font");
      log.logThrowable(t, "SudokuFrame(" + width + ", " + segments + ")");
      java.awt.Font fonts[] = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
      font = new java.awt.Font("Tahoma", java.awt.Font.BOLD, 12);
      boolean b = false;
      
      for (int i = 0; i < fonts.length; i++)
      {
        if (fonts[i].getName().equalsIgnoreCase("Segoe Script"))
        {
          b = true;
          font = fonts[i].deriveFont(java.awt.Font.BOLD).deriveFont(11F);
          break;
        }
      }
      if (!b)
        for (int i = 0; i < fonts.length; i++)
        {
          if (fonts[i].getName().equalsIgnoreCase("Segoe Print"))
          {
            b = true;
            font = fonts[i].deriveFont(java.awt.Font.BOLD).deriveFont(11F);
            break;
          }
        }
      if (!b)
        for (int i = 0; i < fonts.length; i++)
        {
          if (fonts[i].getName().equalsIgnoreCase("Segoe Print"))
          {
            b = true;
            font = fonts[i].deriveFont(java.awt.Font.BOLD).deriveFont(11F);
            break;
          }
        }
      if (!b)
        for (int i = 0; i < fonts.length; i++)
        {
          if (fonts[i].getName().equalsIgnoreCase("Comic Sans MS"))
          {
            b = true;
            font = fonts[i].deriveFont(java.awt.Font.BOLD).deriveFont(11F);
            break;
          }
        }
      if (!b)
        for (int i = 0; i < fonts.length; i++)
        {
          if (fonts[i].getName().equalsIgnoreCase("Script"))
          {
            b = true;
            font = fonts[i].deriveFont(java.awt.Font.BOLD).deriveFont(11F);
            break;
          }
        }
    }
    java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
    // </editor-fold>
    
    boardWidth = width;
    boardSegs = segments;
    
    // <editor-fold defaultstate="collapsed" desc="Components">
    initComponents();
    CompAction.center(aboutDialog);
    CompAction.center(helpFrame);
    CompAction.center(topScoresDialog);
    CompAction.center(winDialog);
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Board fields">
    sudokuTextFields = new javax.swing.JTextField[width][width];
    java.awt.GridBagConstraints gbc;
    for (int x=0; x < width; x++)
    {
      for (int y=0; y < width; y++)
      {
        final int Y = y, X = x;
        sudokuTextFields[y][x] = new javax.swing.JTextField(1);
        sudokuTextFields[y][x].getAccessibleContext().setAccessibleDescription(Colors.SIZE_OVERRIDE + (Colors.DEFAULT_SIZE + 3) + Colors.COMMAND_SEP);
        sudokuTextFields[y][x].setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuTextFields[y][x].addActionListener(new java.awt.event.ActionListener()
        {
          public void actionPerformed(java.awt.event.ActionEvent e)
          {
            checkBoard();
          }
        });
        sudokuTextFields[y][x].addKeyListener(new java.awt.event.KeyAdapter() 
        {
          @Override
          public void keyReleased(java.awt.event.KeyEvent evt)
          {
            boardVals[Y][X] = sudokuTextFields[Y][X].getText().isEmpty() ? NULL_VAL : Byte.parseByte(sudokuTextFields[Y][X].getText());
            checkBoard();
          }
        });
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = y/* + xOff*/;
        gbc.gridy = x/* + yOff*/;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.insets = new java.awt.Insets(x % segments == 0 ? 8 : 2, y % segments == 0 ? 8 : 2, x % segments == 2 ? 8 : 2, y % segments == 2 ? 8 : 2);
        gameBoardPanel.add(sudokuTextFields[y][x], gbc);
      }
    }
    
    resetBoard();
    // </editor-fold>
    
    @SuppressWarnings("static-access")
    boolean loading = SAVE_FILE.exists();
    if (loading)
    {
      int i = YesNoBox.integer("Would you like to continue your game from last time?", "Continue? - " + TITLE, QUESTION_MES);
      if (i == YesNoBox.CLOSED)
        System.exit(0);
      loading = i == YesNoBox.YES;
    }
    // <editor-fold defaultstate="collapsed" desc="Times and Timers">
    if (!loading)
      gameBeginTime = System.currentTimeMillis();
    gameTimer = new javax.swing.Timer(1, new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent e)
      {
        currentTime = System.currentTimeMillis();
        if (paused)
          gameBeginTime = currentTime - gameCurrentTime;
        else
        {
          gameCurrentTime = currentTime - gameBeginTime;
          long mil = gameCurrentTime % 1000,
               sec = (gameCurrentTime / 1000) % 60,
               min = ((gameCurrentTime / 1000) / 60) % 60,
               hr = (((gameCurrentTime / 1000) / 60) / 60) % 60;
          gameTimerLabel.setText(format(hr, 2) + ":" + format(min, 2) + ":" + format(sec, 2) + "." + format(mil, 3));

//          if (countUpLimitTimerCheckBox.isSelected())
//          {
//            countUpProgressBar.setValue((int)(gameCurrentTime));
//            countUpProgressBar.setString((byte)(countUpProgressBar.getPercentComplete() * 100) + "%");
//            if (sec >= countUpLimitSecs() && min >= countUpLimitMins() && hr >= countUpLimitHrs())
//            {
//              countUpStopButton.doClick();
//              countUpDisplayLabel.setText(format(countUpLimitHrs(), 2) + ":" + format(countUpLimitMins(), 2) + ":" + format(countUpLimitSecs(), 2)
//                                          + ".000");
//              countUpDialogLabel.setText("<html>Your count-up timer has reached its limit!");
//              setOffCountUp();
//            }
//          }
        }
      }
    });
    gameTimer.start();
    messageScrollTimer = new javax.swing.Timer(75, new java.awt.event.ActionListener()
    {
      boolean b = true;
      int v, vPrev;
      public void actionPerformed(java.awt.event.ActionEvent e)
      {
        vPrev = v;
        v = messageScrollPane.getHorizontalScrollBar().getValue();
        if (v == vPrev)
        {
          b = (v == 0);
        }
        messageScrollPane.getHorizontalScrollBar().setValue(b ? v + 1 : v - 1);
      }
    });
    messageScrollTimer.start();
    
//    helpLabelTimer = new javax.swing.Timer(5000, new java.awt.event.ActionListener()
//    
//    {
//      boolean starting = true, disappearing = false;
//      ;
//      CompAction ca = new CompAction();
//      javax.swing.JLabel l = new javax.swing.JLabel("← Click here for help");
//
//      public void actionPerformed(ActionEvent e)
//      {
//        if (starting)
//        {
//          System.out.println("Starting");
//
//          mainMenuBar.add(l);
////          ca.fadeIn(l);
//          fixUIIn(mainMenuBar);
//          starting = false;
//        }
//        else
//        {
//          System.out.println("Not Starting");
//          if (disappearing)
//          {
//            System.out.println("Disappearing");
////            ca.fadeAway(l);
//            helpLabelTimer.stop();
//          }
//          else
//          {
//            System.out.println("Not Disappearing");
//            disappearing = true;
//          }
//        }
//      }
//    });
//    helpLabelTimer.start();
    // </editor-fold>
    
    fixUI();
    
    if (loading)
      load();
    else
      newGameMenuItem.doClick();
    starting = false;
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    visualStylesButtonGroup = new javax.swing.ButtonGroup();
    resumeButton = new javax.swing.JButton();
    aboutDialog = new javax.swing.JDialog();
    aboutTitleVerLabel = new javax.swing.JLabel();
    aboutLogoPanel = new javax.swing.JPanel();
    aboutLogoLabel = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    topScoresDialog = new javax.swing.JDialog();
    jScrollPane1 = new javax.swing.JScrollPane();
    jTable1 = new javax.swing.JTable();
    jButton2 = new javax.swing.JButton();
    winDialog = new javax.swing.JDialog();
    jLabel9 = new javax.swing.JLabel();
    usernameTextField = new javax.swing.JTextField();
    jButton1 = new javax.swing.JButton();
    jLabel10 = new javax.swing.JLabel();
    helpFrame = new javax.swing.JFrame();
    jTabbedPane1 = new javax.swing.JTabbedPane();
    helpAboutSudokuTabbedPane = new javax.swing.JTabbedPane();
    jPanel1 = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jPanel2 = new javax.swing.JPanel();
    jLabel2 = new javax.swing.JLabel();
    jPanel3 = new javax.swing.JPanel();
    jLabel3 = new javax.swing.JLabel();
    helpPlayingHereTabbedPane = new javax.swing.JTabbedPane();
    helpHowPlayTabbedPane = new javax.swing.JTabbedPane();
    helpHowNewGamePanel = new javax.swing.JPanel();
    jLabel5 = new javax.swing.JLabel();
    helpHowPlayPanel = new javax.swing.JPanel();
    jLabel7 = new javax.swing.JLabel();
    helpHowPausePanel = new javax.swing.JPanel();
    jLabel6 = new javax.swing.JLabel();
    helpHowWinPanel = new javax.swing.JPanel();
    jLabel8 = new javax.swing.JLabel();
    titleLabel = new javax.swing.JLabel();
    gameBoardPanel = new javax.swing.JPanel();
    messagePanel = new javax.swing.JPanel();
    messageSeparator = new javax.swing.JSeparator();
    gameTimerLabel = new javax.swing.JLabel();
    messageScrollPane = new javax.swing.JScrollPane();
    messageLabel = new javax.swing.JLabel();
    messageIconLabel = new javax.swing.JLabel();
    mainMenuBar = new javax.swing.JMenuBar();
    gameMenu = new javax.swing.JMenu();
    newGameMenuItem = new javax.swing.JMenuItem();
    pauseGameMenuItem = new javax.swing.JMenuItem();
    jSeparator1 = new javax.swing.JPopupMenu.Separator();
    topScoresMenuItem = new javax.swing.JMenuItem();
    gameMenuSeparator = new javax.swing.JPopupMenu.Separator();
    quitMenuItem = new javax.swing.JMenuItem();
    viewMenu = new javax.swing.JMenu();
    visualStylesMenu = new javax.swing.JMenu();
    mochaRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
    huskyRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
    astroRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
    visualStylesSeparator = new javax.swing.JPopupMenu.Separator();
    metalRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
    helpMenu = new javax.swing.JMenu();
    checkAnswersMenuItem = new javax.swing.JMenuItem();
    helpMenuSeparator = new javax.swing.JPopupMenu.Separator();
    helpMenuItem = new javax.swing.JMenuItem();
    aboutMenuItem = new javax.swing.JMenuItem();

    resumeButton.setText("Resume game");
    resumeButton.setDoubleBuffered(true);
    resumeButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        resumeButtonActionPerformed(evt);
      }
    });

    aboutDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    aboutDialog.setTitle("About " + TITLE);
    aboutDialog.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(SudokuFrame.class.getResource("/bht/resources/dialog-information 16.png")));
    aboutDialog.setMinimumSize(new java.awt.Dimension(256, 384));
    aboutDialog.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        aboutDialogWindowClosing(evt);
      }
    });
    aboutDialog.getContentPane().setLayout(new java.awt.GridBagLayout());

    aboutTitleVerLabel.setText(TITLE + " version " + VERSION);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
    aboutDialog.getContentPane().add(aboutTitleVerLabel, gridBagConstraints);
    aboutTitleVerLabel.getAccessibleContext().setAccessibleDescription("<html>" + Colors.SIZE_OVERRIDE + "12" + Colors.COMMAND_SEP);

    aboutLogoPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
    aboutLogoPanel.setLayout(new java.awt.GridBagLayout());

    aboutLogoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    aboutLogoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/Logo.png"))); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    aboutLogoPanel.add(aboutLogoLabel, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(8, 8, 4, 8);
    aboutDialog.getContentPane().add(aboutLogoPanel, gridBagConstraints);

    jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel4.setText("<html><a href=\"http://supuh.wikia.com/wiki/Legal\">Copyright Blue Husky Programming ©2011</a>");
    jLabel4.setToolTipText("See the legal page on SupuhWiki");
    jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabel4MouseClicked(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(4, 8, 8, 8);
    aboutDialog.getContentPane().add(jLabel4, gridBagConstraints);

    topScoresDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    topScoresDialog.setTitle("The Top Scores of " + TITLE);
    topScoresDialog.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(SudokuFrame.class.getResource("/resources/Icon.png")));
    topScoresDialog.setMinimumSize(new java.awt.Dimension(384, 256));
    topScoresDialog.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        topScoresDialogWindowClosing(evt);
      }
    });

    jTable1.setAutoCreateRowSorter(true);
    jTable1.setColumnSelectionAllowed(true);
    jTable1.setDragEnabled(true);
    jTable1.setFillsViewportHeight(true);
    jScrollPane1.setViewportView(jTable1);

    topScoresDialog.getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

    jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/view-refresh 16.png"))); // NOI18N
    jButton2.setText("Refresh Scores");
    jButton2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton2ActionPerformed(evt);
      }
    });
    topScoresDialog.getContentPane().add(jButton2, java.awt.BorderLayout.PAGE_END);

    winDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    winDialog.setTitle("CONGRATULATIONS! - " + TITLE);
    winDialog.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(SudokuFrame.class.getResource("/resources/Icon.png")));
    winDialog.setMinimumSize(new java.awt.Dimension(384, 256));
    winDialog.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        winDialogWindowClosing(evt);
      }
    });
    winDialog.getContentPane().setLayout(new java.awt.GridBagLayout());

    jLabel9.setText("<html><b>Congratulations!");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
    winDialog.getContentPane().add(jLabel9, gridBagConstraints);
    jLabel9.getAccessibleContext().setAccessibleDescription(Colors.SIZE_OVERRIDE + "24" + Colors.COMMAND_SEP);

    usernameTextField.setText("Name");
    usernameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusGained(java.awt.event.FocusEvent evt) {
        usernameTextFieldFocusGained(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
    winDialog.getContentPane().add(usernameTextField, gridBagConstraints);

    jButton1.setText("Save my score!");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 8, 8, 8);
    winDialog.getContentPane().add(jButton1, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
    winDialog.getContentPane().add(jLabel10, gridBagConstraints);

    helpFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    helpFrame.setTitle("Help - " + TITLE);
    helpFrame.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(SudokuFrame.class.getResource("/bht/resources/dialog-question 16.png")));
    helpFrame.setMinimumSize(new java.awt.Dimension(512, 384));
    helpFrame.addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        helpFrameWindowClosing(evt);
      }
    });
    helpFrame.getContentPane().setLayout(new java.awt.GridBagLayout());

    jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);

    jPanel1.setLayout(new java.awt.GridBagLayout());

    jLabel1.setText("<html>Sudoku (数独 in Japanese, pronounced soo-DOH-koo) is a logic-based number puzzle first seen in Japan in the paper <i>Monthly Nikolist</i> in April 1984. Then, it was called <i><b>Sū</b>ji wa <b>doku</b>shin ni kagiru</i> (数字は独身に限る in Japanese), which can be translated as either  \"the digits must be single\" or \"the digits are limited to one occurrence.\"");
    jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
    jPanel1.add(jLabel1, gridBagConstraints);

    helpAboutSudokuTabbedPane.addTab("What is Sudoku?", jPanel1);

    jPanel2.setLayout(new java.awt.GridBagLayout());

    jLabel2.setText("<html>The goal of Sudoku is to fill in the entire board with numbers from 1 to the number of squares the board is wide. For a 9x9 board divided into 3 sections (a standard Sudoku), you must fill it so that no number you use is greater than 9 nor is it less than 1. Furthermore, you may only use a number once per row, column, and block. That is, if you read the numbers in a row, column, or block out loud, you will say each number exactly one time.");
    jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
    jPanel2.add(jLabel2, gridBagConstraints);

    helpAboutSudokuTabbedPane.addTab("How do I play Sudoku?", jPanel2);

    jPanel3.setLayout(new java.awt.GridBagLayout());

    jLabel3.setText("<html>You have won a Sudoku only if every square in the grid is filled AND no number repeats itself within any given row, column, or block.");
    jLabel3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
    jPanel3.add(jLabel3, gridBagConstraints);

    helpAboutSudokuTabbedPane.addTab("How do I know if I have won?", jPanel3);

    jTabbedPane1.addTab("About Sudoku", helpAboutSudokuTabbedPane);

    helpHowNewGamePanel.setLayout(new java.awt.GridBagLayout());

    jLabel5.setText("<html>Each time you start a new game with this program (every time you start the program and every time you select \"New game\" from the \"Game\" menu), a brand new Sudoku puzzle is created and you are asked to select the difficulty by sliding a knob to a certain number of hints to be given once the game starts. The moment you click the button to start your game, the Sudoku board is presented to you, and a timer in the bottom-right corner starts counting, with millisecond resolution, how long it has been since you started the game.");
    jLabel5.setVerticalAlignment(javax.swing.SwingConstants.TOP);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
    helpHowNewGamePanel.add(jLabel5, gridBagConstraints);

    helpHowPlayTabbedPane.addTab("How do I start a new game?", helpHowNewGamePanel);

    helpHowPlayPanel.setLayout(new java.awt.GridBagLayout());

    jLabel7.setText("<html>To play the game, simply click on a square and type in a number.If it is not a proper number (see the \"How do I play Sudoku?\" tab in the \"About Sudoku\" help section), the program will tell you so with a ding and a message at the bottom of the window. To check to see if your answers are all good so far, you can select \"Check for errors\" from the \"Help\" menu or press the F5 key on your keyboard.");
    jLabel7.setVerticalAlignment(javax.swing.SwingConstants.TOP);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
    helpHowPlayPanel.add(jLabel7, gridBagConstraints);

    helpHowPlayTabbedPane.addTab("How do I play Sudoku with this program?", helpHowPlayPanel);

    helpHowPausePanel.setLayout(new java.awt.GridBagLayout());

    jLabel6.setText("<html>If at any time you wish to take a break, you can click the \"Game\" menu at the top and select \"Pause\" (or press the F3 key while playing). This will hide all the squares so you can't think about it while the timer is not counting up. To continue, simply click the button provided in the center of the window, select \"Resume\" from the \"Game\" menu, or press F3. Please note that the game automatically pauses if you leave the main window.");
    jLabel6.setVerticalAlignment(javax.swing.SwingConstants.TOP);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
    helpHowPausePanel.add(jLabel6, gridBagConstraints);

    helpHowPlayTabbedPane.addTab("What if I don't want to play for now?", helpHowPausePanel);

    helpHowWinPanel.setLayout(new java.awt.GridBagLayout());

    jLabel8.setText("<html>Once you have filled all the squares, the program will evaluate your answers to see if they are correct. If they are, you have completed a Sudoku and your time is recorded in the score board, which you can see by selecting \"Top Scores\" from the \"Game\" menu or by pressing F12.");
    jLabel8.setVerticalAlignment(javax.swing.SwingConstants.TOP);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(8, 8, 8, 8);
    helpHowWinPanel.add(jLabel8, gridBagConstraints);

    helpHowPlayTabbedPane.addTab("How do I know if I have won?", helpHowWinPanel);

    helpPlayingHereTabbedPane.addTab("How can I use this program to play Sudoku?", helpHowPlayTabbedPane);

    jTabbedPane1.addTab("Playing here", helpPlayingHereTabbedPane);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    helpFrame.getContentPane().add(jTabbedPane1, gridBagConstraints);

    titleLabel.setText(TITLE);
    titleLabel.getAccessibleContext().setAccessibleName(Colors.SIZE_OVERRIDE + "24" + Colors.COMMAND_SEP);

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    setTitle(TITLE);
    setIconImage(appImage16);
    setMinimumSize(new java.awt.Dimension(256, 384));
    addWindowFocusListener(new java.awt.event.WindowFocusListener() {
      public void windowGainedFocus(java.awt.event.WindowEvent evt) {
      }
      public void windowLostFocus(java.awt.event.WindowEvent evt) {
        formWindowLostFocus(evt);
      }
    });
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        formWindowClosing(evt);
      }
    });
    getContentPane().setLayout(new java.awt.GridBagLayout());

    gameBoardPanel.setDoubleBuffered(true);
    gameBoardPanel.setLayout(new java.awt.GridBagLayout());
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    getContentPane().add(gameBoardPanel, gridBagConstraints);

    messagePanel.setMinimumSize(new java.awt.Dimension(0, 32));
    messagePanel.setLayout(new java.awt.GridBagLayout());
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    messagePanel.add(messageSeparator, gridBagConstraints);

    gameTimerLabel.setText("00:00:00.000");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 8);
    messagePanel.add(gameTimerLabel, gridBagConstraints);

    messageScrollPane.setBorder(null);
    messageScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    messageScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    messageLabel.setText("Welcome!");
    messageLabel.setIconTextGap(8);
    messageScrollPane.setViewportView(messageLabel);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
    messagePanel.add(messageScrollPane, gridBagConstraints);

    messageIconLabel.setIcon(Icons.getIcon(Icons.INFO_16));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
    messagePanel.add(messageIconLabel, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
    getContentPane().add(messagePanel, gridBagConstraints);

    gameMenu.setMnemonic('G');
    gameMenu.setText("Game");

    newGameMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
    newGameMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/game-new 16.png"))); // NOI18N
    newGameMenuItem.setText("New Game");
    newGameMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        newGameMenuItemActionPerformed(evt);
      }
    });
    gameMenu.add(newGameMenuItem);

    pauseGameMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
    pauseGameMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/player_pause 16.png"))); // NOI18N
    pauseGameMenuItem.setText("Pause");
    pauseGameMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        pauseGameMenuItemActionPerformed(evt);
      }
    });
    gameMenu.add(pauseGameMenuItem);
    gameMenu.add(jSeparator1);

    topScoresMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12, 0));
    topScoresMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/index 16.png"))); // NOI18N
    topScoresMenuItem.setText("Top Scores");
    topScoresMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        topScoresMenuItemActionPerformed(evt);
      }
    });
    gameMenu.add(topScoresMenuItem);
    gameMenu.add(gameMenuSeparator);

    quitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
    quitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/application-exit 16.png"))); // NOI18N
    quitMenuItem.setText("Quit");
    quitMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        quitMenuItemActionPerformed(evt);
      }
    });
    gameMenu.add(quitMenuItem);

    mainMenuBar.add(gameMenu);

    viewMenu.setMnemonic('V');
    viewMenu.setText("View");

    visualStylesMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/applications-graphics 16.png"))); // NOI18N
    visualStylesMenu.setText("Visual Styles");

    mochaRadioButtonMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
    visualStylesButtonGroup.add(mochaRadioButtonMenuItem);
    mochaRadioButtonMenuItem.setSelected(true);
    mochaRadioButtonMenuItem.setText("Mocha (SupuhWiki main theme)");
    mochaRadioButtonMenuItem.setBorder(javax.swing.BorderFactory.createLineBorder(c.getColor(Colors.MOCHA, Colors.BORDER), 2));
    mochaRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        mochaRadioButtonMenuItemActionPerformed(evt);
      }
    });
    visualStylesMenu.add(mochaRadioButtonMenuItem);
    mochaRadioButtonMenuItem.getAccessibleContext().setAccessibleDescription("Colors.COLOR_STYLE_OVERRIDE + Colors.MOCHA + Colors.COMMAND_SEP");

    huskyRadioButtonMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
    visualStylesButtonGroup.add(huskyRadioButtonMenuItem);
    huskyRadioButtonMenuItem.setText("Husky (SupuhWiki secondary theme)");
    huskyRadioButtonMenuItem.setBorder(javax.swing.BorderFactory.createLineBorder(c.getColor(Colors.HUSKY, Colors.BORDER), 2));
    huskyRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        huskyRadioButtonMenuItemActionPerformed(evt);
      }
    });
    visualStylesMenu.add(huskyRadioButtonMenuItem);
    huskyRadioButtonMenuItem.getAccessibleContext().setAccessibleDescription("Colors.COLOR_STYLE_OVERRIDE + Colors.HUSKY + Colors.COMMAND_SEP");

    astroRadioButtonMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
    visualStylesButtonGroup.add(astroRadioButtonMenuItem);
    astroRadioButtonMenuItem.setText("Astro (good for night time)");
    astroRadioButtonMenuItem.setBorder(javax.swing.BorderFactory.createLineBorder(c.getColor(Colors.ASTRO, Colors.BORDER), 2));
    astroRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        astroRadioButtonMenuItemActionPerformed(evt);
      }
    });
    visualStylesMenu.add(astroRadioButtonMenuItem);
    astroRadioButtonMenuItem.getAccessibleContext().setAccessibleDescription("Colors.COLOR_STYLE_OVERRIDE + Colors.ASTRO + Colors.COMMAND_SEP");

    visualStylesMenu.add(visualStylesSeparator);

    metalRadioButtonMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK));
    visualStylesButtonGroup.add(metalRadioButtonMenuItem);
    metalRadioButtonMenuItem.setText("Metal (Java default)");
    metalRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        metalRadioButtonMenuItemActionPerformed(evt);
      }
    });
    visualStylesMenu.add(metalRadioButtonMenuItem);

    viewMenu.add(visualStylesMenu);

    mainMenuBar.add(viewMenu);

    helpMenu.setMnemonic('H');
    helpMenu.setText("Help");

    checkAnswersMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
    checkAnswersMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/edit-find-replace 16.png"))); // NOI18N
    checkAnswersMenuItem.setText("Check for errors");
    checkAnswersMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        checkAnswersMenuItemActionPerformed(evt);
      }
    });
    helpMenu.add(checkAnswersMenuItem);
    helpMenu.add(helpMenuSeparator);

    helpMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
    helpMenuItem.setIcon(Icons.getIcon(Icons.QUESTION_16));
    helpMenuItem.setText("How to Play");
    helpMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        helpMenuItemActionPerformed(evt);
      }
    });
    helpMenu.add(helpMenuItem);

    aboutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.ALT_MASK));
    aboutMenuItem.setIcon(Icons.getIcon(Icons.INFO_16));
    aboutMenuItem.setText("About");
    aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        aboutMenuItemActionPerformed(evt);
      }
    });
    helpMenu.add(aboutMenuItem);

    mainMenuBar.add(helpMenu);

    setJMenuBar(mainMenuBar);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void quitMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_quitMenuItemActionPerformed
  {//GEN-HEADEREND:event_quitMenuItemActionPerformed
    exit(0);
  }//GEN-LAST:event_quitMenuItemActionPerformed

  private void pauseGameMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_pauseGameMenuItemActionPerformed
  {//GEN-HEADEREND:event_pauseGameMenuItemActionPerformed
    paused = !paused;
    pauseGameMenuItem.setText(paused ? "Resume" : "Pause");
    pauseGameMenuItem.setToolTipText((paused ? "Resum" : "Paus") + "e the game and " + (paused ? "show" : "hide") + " the Sudoku square");
    pauseGameMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/player_p" + (paused ? "lay" : "ause") + " 16.png")));
    checkAnswersMenuItem.setEnabled(!paused);
    resumeButton.setText(pauseGameMenuItem.getText());
    resumeButton.setToolTipText(pauseGameMenuItem.getToolTipText());
    resumeButton.setEnabled(true);
    gameTimer.setDelay(paused ? 250 : 1);
    for (int y=0; y < sudokuTextFields.length; y++)
    {
      for (int x=0; x < sudokuTextFields[y].length; x++)
      {
        sudokuTextFields[y][x].setVisible(!paused && !creating);
      }
    }
    if (paused)
    {
      java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
      gbc.ipadx = 64;
      gbc.ipady = 16;
      gameBoardPanel.add(resumeButton, gbc);
      fixUIIn(resumeButton);
    }
    else
    {
      gameBoardPanel.remove(resumeButton);
    }
    alertOf("Game " + (paused ? "paused" : "resumed!"), INFO_MES);
  }//GEN-LAST:event_pauseGameMenuItemActionPerformed

  @SuppressWarnings("static-access")
  private void newGameMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_newGameMenuItemActionPerformed
  {//GEN-HEADEREND:event_newGameMenuItemActionPerformed
    if (starting || ynb.bool("Are you sure you want to clear the board and create a new puzzle?", "Create new game? - " + TITLE, QUESTION_MES))
    {
      creating = true;
      for (int y=0; y < sudokuTextFields.length; y++)
      {
        for (int x=0; x < sudokuTextFields[y].length; x++)
        {
          sudokuTextFields[y][x].setVisible(false);
        }
      }
      gameTimerLabel.setVisible(false);
      newGameMenuItem.setEnabled(false);
      checkAnswersMenuItem.setEnabled(false);
      pauseGameMenuItem.doClick();
      try
      {
        gameBoardPanel.remove(resumeButton);
      }
      catch (Throwable t)
      {
        t.printStackTrace();
      }
      pauseGameMenuItem.setToolTipText("You can't pause while loading a new game.");
      pauseGameMenuItem.setEnabled(false);
      alertOf("Now inventing new puzzle. This usually takes 1-5 seconds, but may take up to half a minute.", INFO_MES);
      final long START = System.currentTimeMillis();
      final javax.swing.JProgressBar p = new javax.swing.JProgressBar(0, boardWidth * boardWidth);
      p.setMaximum(boardWidth * boardWidth);
      p.setStringPainted(true);
      p.setString("Beginning creator...");
      fixUIIn(p);

      java.awt.GridBagConstraints gbc;
      gbc = new java.awt.GridBagConstraints();
      gbc.weightx = 1;
      gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
      gameBoardPanel.add(p, gbc);
      resetBoard();
      new Thread(new Runnable()
      {
        java.util.Random rand = new java.util.Random();

        public void run()
        {
          // <editor-fold defaultstate="collapsed" desc="Create all values">
          for (int x = 0; x < boardWidth; x++)
          {
            for (int y = 0; y < boardWidth; y++)
            {
              p.setValue(((x + 1) * 9) + (y + 1));
              p.setString("Attempting to create a number at (" + (x + 1) + ", " + (y + 1) + ")...");
              do
              {
                try
                {
                  boardVals[y][x] = getNextVal(x, y);
                }
                catch (TriedAllNumbersException ex)
                {
                  p.setString("No number could be found for (" + (x + 1) + ", " + (y + 1) + "). Now backtracking...");
                  destroyVals(x, y);
                  if (y == 0)
                  {
                    x--;
                    y = boardWidth - 2;
                  }
                  else
                    y -= 2;
                  continue;
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                  resetBoard();
                }
              } while (!boardIsOK());
            }
          }
          // </editor-fold>
          
          printBoard();
          gameBoardPanel.remove(p);
          paused = true;
          pauseGameMenuItem.setToolTipText("Click the Start button to start");
          pauseGameMenuItem.setEnabled(false);
          gameTimer.setDelay(250);
          numHints = (boardSegs * boardSegs) * 2/*boardWidth * boardWidth - boardSegs*/;
          final javax.swing.JLabel l = new javax.swing.JLabel("Difficulty by number of hints:");
          final javax.swing.JSlider s = new javax.swing.JSlider(boardSegs * boardSegs, (int)((double)boardWidth * (Math.sqrt((double)boardWidth) + ((Math.round((double)boardWidth / 2.0)) / (double)boardWidth))), (boardSegs * boardSegs) * 2);
          final javax.swing.JButton b = new javax.swing.JButton("Start a " + getDifficultyName() + " puzzle!");
          s.addChangeListener(new javax.swing.event.ChangeListener() 
          {
            public void stateChanged(javax.swing.event.ChangeEvent e)
            {
              numHints = s.getValue();
              b.setText("Start a " + getDifficultyName() + " puzzle!");
            }
          });
          s.setInverted(true);
          s.setMajorTickSpacing(boardSegs);
          s.setMinorTickSpacing(1);
          s.setPaintTicks(true);
          s.setPaintLabels(true);
          s.setSnapToTicks(true);
          s.setToolTipText("Set the number of hints you get. The farther right the slider, the harder the puzzle.");
          b.addActionListener(new java.awt.event.ActionListener()
          {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
              // <editor-fold defaultstate="collapsed" desc="Add hints">
              new Thread(new Runnable() {

                public void run()
                {
                  alertOf("Adding hints...", INFO_MES);
                }
              }).start();
              int x = 0, y = 0;
              isHint = new boolean[boardWidth][boardWidth];
              for (int i = 0; i < numHints; i++)
              {
                do
                {
                  x/*++*/=rand.nextInt(boardWidth);
//                  if (x == boardWidth)
//                  {
//                    x = 0;
//                    y++;
//                  }
                  y=rand.nextInt(boardWidth);
                }while (sudokuTextFields[y][x].getText() != null && !sudokuTextFields[y][x].getText().isEmpty());
                sudokuTextFields[y][x].setText(boardVals[y][x] + "");
                isHint[y][x] = true;
              }
              for (x=0; x < boardWidth; x++)
              {
                for (y=0; y < boardWidth; y++)
                {
                  if (sudokuTextFields[y][x].getText() == null || sudokuTextFields[y][x].getText().isEmpty())
                  {
                    boardVals[y][x] = NULL_VAL;
                  }
                  else
                  {
                    sudokuTextFields[y][x].setEditable(false);
                    sudokuTextFields[y][x].getAccessibleContext().setAccessibleDescription(Colors.FONT_OVERRIDE + "Segoe UI" + Colors.COMMAND_SEP);
                  }
                }
              }
              // </editor-fold>

              gameBoardPanel.remove(l);
              gameBoardPanel.remove(s);
              gameBoardPanel.remove(b);

              // <editor-fold defaultstate="collapsed" desc="Unpause">
              paused = false;
              pauseGameMenuItem.setText("Pause");
              pauseGameMenuItem.setToolTipText((paused ? "Resum" : "Paus") + "e the game and " + (paused ? "show" : "hide") + " the Sudoku square");
              pauseGameMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/player_p" + (paused ? "lay" : "ause") + " 16.png"))); // NOI18N
              gameTimer.setDelay(1);
              for (y = 0; y < sudokuTextFields.length; y++)
              {
                for (x = 0; x < sudokuTextFields[y].length; x++)
                {
                  sudokuTextFields[y][x].setVisible(true);
                }
              }
              fixUI();
              alertOf("New game started! Time taken so far: ", INFO_MES);
              gameTimerLabel.setVisible(true);
              pauseGameMenuItem.setEnabled(true);
              newGameMenuItem.setEnabled(true);
              checkAnswersMenuItem.setEnabled(true);
              // </editor-fold>
              
              creating = false;
            }
          });
          java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
          gbc.gridy = 2;
          gbc.ipadx = 64;
          gbc.ipady = 16;
          gameBoardPanel.add(b, gbc);

          gbc = new java.awt.GridBagConstraints();
          gameBoardPanel.add(l, gbc);

          gbc = new java.awt.GridBagConstraints();
          gbc.gridy = 1;
          gbc.weightx = 1;
          gbc.fill = java.awt.GridBagConstraints.BOTH;
          gameBoardPanel.add(s, gbc);
          fixUI();
          alertOf("New puzzle created in " + ((double)System.currentTimeMillis() - (double)START) / 1000 + " seconds! Please select the number of hints you want and start your game!", INFO_MES);
          gameBeginTime = System.currentTimeMillis();
          gameCurrentTime = 0;
        }
      }).start();
    }
  }//GEN-LAST:event_newGameMenuItemActionPerformed

  private void mochaRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mochaRadioButtonMenuItemActionPerformed
  {//GEN-HEADEREND:event_mochaRadioButtonMenuItemActionPerformed
    long nT = System.nanoTime();
    fixUI();
    log.logAction(evt, "Updated UI (mocha), took " + commaFmt(System.nanoTime() - nT) + " nanoseconds", "mochaRadioButtonMenuItemActionPerformed");
}//GEN-LAST:event_mochaRadioButtonMenuItemActionPerformed

  private void huskyRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_huskyRadioButtonMenuItemActionPerformed
  {//GEN-HEADEREND:event_huskyRadioButtonMenuItemActionPerformed
    long nT = System.nanoTime();
    fixUI();
    log.logAction(evt, "Updated UI (mocha), took " + commaFmt(System.nanoTime() - nT) + " nanoseconds", "huskyRadioButtonMenuItemActionPerformed");
}//GEN-LAST:event_huskyRadioButtonMenuItemActionPerformed

  private void astroRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_astroRadioButtonMenuItemActionPerformed
  {//GEN-HEADEREND:event_astroRadioButtonMenuItemActionPerformed
    long nT = System.nanoTime();
    fixUI();
    log.logAction(evt, "Updated UI (astro), took " + commaFmt(System.nanoTime() - nT) + " nanoseconds", "astroRadioButtonMenuItemActionPerformed");
}//GEN-LAST:event_astroRadioButtonMenuItemActionPerformed

  private void resumeButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_resumeButtonActionPerformed
  {//GEN-HEADEREND:event_resumeButtonActionPerformed
    pauseGameMenuItem.doClick();
  }//GEN-LAST:event_resumeButtonActionPerformed

  private void checkAnswersMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_checkAnswersMenuItemActionPerformed
  {//GEN-HEADEREND:event_checkAnswersMenuItemActionPerformed
    alertOf("Checking answers...", INFO_MES);
    new Thread(new Runnable() 
    {
      public void run()
      {
        boolean b = boardIsOK();
        if (checkBoard())
          alertOf(b ? "Your answers are all correct, so far." : "You have written incorrect answers.", b ? INFO_MES : WARN_MES);
      }
    }).start();
  }//GEN-LAST:event_checkAnswersMenuItemActionPerformed

  private void helpMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_helpMenuItemActionPerformed
  {//GEN-HEADEREND:event_helpMenuItemActionPerformed
    helpFrame.setSize(768, 512);
    CompAction.center(helpFrame);
    hCA.ghostIn(helpFrame);
  }//GEN-LAST:event_helpMenuItemActionPerformed

  private void helpFrameWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_helpFrameWindowClosing
  {//GEN-HEADEREND:event_helpFrameWindowClosing
    hCA.ghostOut(helpFrame);
  }//GEN-LAST:event_helpFrameWindowClosing

  private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
  {//GEN-HEADEREND:event_formWindowClosing
    exit(0);
  }//GEN-LAST:event_formWindowClosing

  private void formWindowLostFocus(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowLostFocus
  {//GEN-HEADEREND:event_formWindowLostFocus
    if (!paused)
      pauseGameMenuItem.doClick();
  }//GEN-LAST:event_formWindowLostFocus

  private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_aboutMenuItemActionPerformed
  {//GEN-HEADEREND:event_aboutMenuItemActionPerformed
    int h = aboutDialog.getPreferredSize().height, w = aboutDialog.getPreferredSize().width;
    aboutDialog.setSize(w + 16, h + 16);
    CompAction.center(aboutDialog);
    aCA.ghostIn(aboutDialog);
  }//GEN-LAST:event_aboutMenuItemActionPerformed

  private void aboutDialogWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_aboutDialogWindowClosing
  {//GEN-HEADEREND:event_aboutDialogWindowClosing
    aCA.ghostOut(aboutDialog);
  }//GEN-LAST:event_aboutDialogWindowClosing

  private void jLabel4MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jLabel4MouseClicked
  {//GEN-HEADEREND:event_jLabel4MouseClicked
    try
    {
      String s = jLabel4.getText();
      java.awt.Desktop.getDesktop().browse(new java.net.URI(s.substring(s.indexOf("<a href=\"") + 9, s.indexOf("\">"))));
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }

  }//GEN-LAST:event_jLabel4MouseClicked

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
  {//GEN-HEADEREND:event_jButton1ActionPerformed
    starting = true;
    saveScoreFor(usernameTextField.getText());
    newGameMenuItem.doClick();
    new CompAction().ghostOut(winDialog);
    starting = false;
  }//GEN-LAST:event_jButton1ActionPerformed

  private void usernameTextFieldFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_usernameTextFieldFocusGained
  {//GEN-HEADEREND:event_usernameTextFieldFocusGained
    usernameTextField.requestFocus();
    usernameTextField.setSelectionStart(0);
    usernameTextField.setSelectionEnd(usernameTextField.getText().length());
  }//GEN-LAST:event_usernameTextFieldFocusGained

  private void topScoresMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_topScoresMenuItemActionPerformed
  {//GEN-HEADEREND:event_topScoresMenuItemActionPerformed
    fillScoreTable(true);
  }//GEN-LAST:event_topScoresMenuItemActionPerformed

  private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
  {//GEN-HEADEREND:event_jButton2ActionPerformed
    fillScoreTable(false);
  }//GEN-LAST:event_jButton2ActionPerformed

  private void topScoresDialogWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_topScoresDialogWindowClosing
  {//GEN-HEADEREND:event_topScoresDialogWindowClosing
    new CompAction().ghostOut(topScoresDialog);
  }//GEN-LAST:event_topScoresDialogWindowClosing

  private void winDialogWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_winDialogWindowClosing
  {//GEN-HEADEREND:event_winDialogWindowClosing
    new CompAction().ghostOut(winDialog);
  }//GEN-LAST:event_winDialogWindowClosing

  private void metalRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_metalRadioButtonMenuItemActionPerformed
  {//GEN-HEADEREND:event_metalRadioButtonMenuItemActionPerformed
    long nT = System.nanoTime();
    fixUI();
    log.logAction(evt, "Updated UI (default), took " + commaFmt(System.nanoTime() - nT) + " nanoseconds", "mochaRadioButtonMenuItemActionPerformed");
  }//GEN-LAST:event_metalRadioButtonMenuItemActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  javax.swing.JDialog aboutDialog;
  javax.swing.JLabel aboutLogoLabel;
  javax.swing.JPanel aboutLogoPanel;
  javax.swing.JMenuItem aboutMenuItem;
  javax.swing.JLabel aboutTitleVerLabel;
  private static javax.swing.JRadioButtonMenuItem astroRadioButtonMenuItem;
  javax.swing.JMenuItem checkAnswersMenuItem;
  javax.swing.JPanel gameBoardPanel;
  javax.swing.JMenu gameMenu;
  javax.swing.JPopupMenu.Separator gameMenuSeparator;
  javax.swing.JLabel gameTimerLabel;
  javax.swing.JTabbedPane helpAboutSudokuTabbedPane;
  javax.swing.JFrame helpFrame;
  javax.swing.JPanel helpHowNewGamePanel;
  javax.swing.JPanel helpHowPausePanel;
  javax.swing.JPanel helpHowPlayPanel;
  javax.swing.JTabbedPane helpHowPlayTabbedPane;
  javax.swing.JPanel helpHowWinPanel;
  javax.swing.JMenu helpMenu;
  javax.swing.JMenuItem helpMenuItem;
  javax.swing.JPopupMenu.Separator helpMenuSeparator;
  javax.swing.JTabbedPane helpPlayingHereTabbedPane;
  private static javax.swing.JRadioButtonMenuItem huskyRadioButtonMenuItem;
  javax.swing.JButton jButton1;
  javax.swing.JButton jButton2;
  javax.swing.JLabel jLabel1;
  javax.swing.JLabel jLabel10;
  javax.swing.JLabel jLabel2;
  javax.swing.JLabel jLabel3;
  javax.swing.JLabel jLabel4;
  javax.swing.JLabel jLabel5;
  javax.swing.JLabel jLabel6;
  javax.swing.JLabel jLabel7;
  javax.swing.JLabel jLabel8;
  javax.swing.JLabel jLabel9;
  javax.swing.JPanel jPanel1;
  javax.swing.JPanel jPanel2;
  javax.swing.JPanel jPanel3;
  javax.swing.JScrollPane jScrollPane1;
  javax.swing.JPopupMenu.Separator jSeparator1;
  javax.swing.JTabbedPane jTabbedPane1;
  javax.swing.JTable jTable1;
  javax.swing.JMenuBar mainMenuBar;
  javax.swing.JLabel messageIconLabel;
  javax.swing.JLabel messageLabel;
  javax.swing.JPanel messagePanel;
  javax.swing.JScrollPane messageScrollPane;
  javax.swing.JSeparator messageSeparator;
  javax.swing.JRadioButtonMenuItem metalRadioButtonMenuItem;
  private static javax.swing.JRadioButtonMenuItem mochaRadioButtonMenuItem;
  javax.swing.JMenuItem newGameMenuItem;
  javax.swing.JMenuItem pauseGameMenuItem;
  javax.swing.JMenuItem quitMenuItem;
  javax.swing.JButton resumeButton;
  javax.swing.JLabel titleLabel;
  javax.swing.JDialog topScoresDialog;
  javax.swing.JMenuItem topScoresMenuItem;
  javax.swing.JTextField usernameTextField;
  javax.swing.JMenu viewMenu;
  javax.swing.ButtonGroup visualStylesButtonGroup;
  javax.swing.JMenu visualStylesMenu;
  javax.swing.JPopupMenu.Separator visualStylesSeparator;
  javax.swing.JDialog winDialog;
  // End of variables declaration//GEN-END:variables
  private boolean paused = false, isHint[][], starting = true, creating = false;
  public static final byte PLAIN_MES = YesNoBox.PLAIN, INFO_MES = YesNoBox.INFO, QUESTION_MES = YesNoBox.QUESTION, WARN_MES = YesNoBox.WARNING,
  CRIT_MES = YesNoBox.ERROR, NULL_VAL = -1;
  private byte boardVals[][], triedBoardVals[][][], boardWidth, boardSegs;
  public byte style = Colors.MOCHA;
  private Colors c = new Colors();
  private CompAction mCA = new CompAction(), hCA = new CompAction(), aCA = new CompAction();
  public java.awt.Font font /*= new java.awt.Font("Segoe Script", java.awt.Font.BOLD, 11)*/;
  public final java.io.File SAVE_FILE = new java.io.File("Autosave.sav");
  public GameScore gs = new GameScore();
  public static final java.awt.Image
          appImage16 = java.awt.Toolkit.getDefaultToolkit().getImage(SudokuFrame.class.getResource("/resources/Icon.png"));
  static javax.swing.ImageIcon
          warningIcon16 =Icons.getIcon(Icons.WARNING_16),
          errorIcon16 = Icons.getIcon(Icons.ERROR_16),
          infoIcon16 = Icons.getIcon(Icons.INFO_16),
          questionIcon16 = Icons.getIcon(Icons.QUESTION_16);
  private int fontMod = 4, numHints;
//  private javax.swing.JSeparator sudokuSeparators[][];
  private javax.swing.JTextField sudokuTextFields[][];
  private long gameBeginTime, gameCurrentTime, currentTime, winTime;
  private ProgLog log;
  public static final String TITLE = "Blue Husky's Sudoku", VERSION = "1.0.1", SCORE_FILE_NAME = "Scores.sav";
  private javax.swing.Timer gameTimer, messageScrollTimer/*, helpLabelTimer*/;
  public static final YesNoBox ynb = new YesNoBox();
  
  private void exit(int status)
  {
    if (!starting && !creating)
      save();
    
    for (int i=0; i < getWindows().length; i++)
      new CompAction().ghostOut(getWindows()[i], true, status);
  }
  
  /**
   * Alerts the user (via the message pane) of a given message
   * @param message a <tt>String</tt> representing the message
   * @param type a <tt>byte</tt> representing what type of message this is.
   * Please use <tt>PLAIN</tt>, <tt>INFO</tt>, <tt>QUESTION</tt>, <tt>WARNING</tt>, <tt>ERROR</tt>, 
   */
  private void alertOf(String message, byte type)
  {
    messageLabel.setText(message);
    messageScrollPane.getHorizontalScrollBar().setValue(0);
    switch (type)
    {
      default:
      case PLAIN_MES:
        messageIconLabel.setIcon(null);
        break;
      case INFO_MES:
        messageIconLabel.setIcon(infoIcon16);
        break;
      case QUESTION_MES:
        messageIconLabel.setIcon(questionIcon16);
        break;
      case WARN_MES:
        messageIconLabel.setIcon(warningIcon16);
        break;
      case CRIT_MES:
        messageIconLabel.setIcon(errorIcon16);
        CompAction.flash(this, true);
        break;
    }
  }

  private String format(Object num, int fmt)
  {
    return format(Integer.parseInt(num.toString()), fmt);
  }

  private String format(String num, int fmt)
  {
    return format(Integer.parseInt(num), fmt);
  }

  private String format(long num, int fmt)
  {
    String numStr = Long.toString(num), ret = "";
    for (int i = 0; i < fmt; i++)
    {
      try
      {
        ret += numStr.charAt(i);
      }
      catch (java.lang.StringIndexOutOfBoundsException ex)
      {
        ret = '0' + ret;
      }
    }
    return ret;
  }

  private byte getNextVal(int y, int x) throws TriedAllNumbersException
  {
    int i = 0;
    for (; i < triedBoardVals[y][x].length; i++)
    {
      if (triedBoardVals[y][x][i] == NULL_VAL)
        break;
    }
    if (i >= boardWidth)
      throw new TriedAllNumbersException();
    byte c = 0, triedVals[] = new byte[triedBoardVals[y][x].length];
    for (int j = 0; j < triedVals.length; j++)
    {
      triedVals[j] = NULL_VAL;
    }

    java.util.Random r = new java.util.Random();

    try
    {
      do
      {
        do
        {
          triedVals[c] = (byte)(r.nextInt(boardWidth) + 1);
        } while (!setIsOK(triedVals));

        triedBoardVals[y][x][i] = triedVals[c];

        c++;
      } while (!setIsOK(triedBoardVals[y][x]));
      return triedBoardVals[y][x][i];
    }
    catch (ArrayIndexOutOfBoundsException ex)
    {
      throw new TriedAllNumbersException();
    }
  }

  private void destroyVals(int x, int y)
  {
    boardVals[y][x] = NULL_VAL;
    for(int i=0; i < triedBoardVals[y][x].length; i++)
      triedBoardVals[y][x][i] = NULL_VAL;
  }

  private boolean boardIsOK()
  {
//    printBoard();
    for (int i=0; i < boardWidth; i++)
    {
//      System.out.println("Looking at row " + i);
      if (!setIsOK(getRow(i)))
      {
//        System.out.println("Row is not OK");
        return false;
      }
//      System.out.println("Looking at col " + i);
      if (!setIsOK(getCol(i)))
      {
//        System.out.println("Col is not OK");
        return false;
      }
    }
    for (int x=0; x < boardSegs; x++)
    {
      for (int y=0; y < boardSegs; y++)
      {
//        System.out.println("Looking at block (" + x + ", " + y + ")");
        if (!areaIsOK(getSquare(x,y)))
        {
//          System.out.println("Area is not OK");
          return false;
        }
      }
    }
    return true;
  }

  private byte[] getRow(int index)
  {
    return boardVals[index];
  }

  private byte[] getCol(int index)
  {
    byte[] b = new byte[boardWidth];
    for (int i=0; i < boardWidth; i++)
      b[i] = boardVals[i][index];
    return b;
  }

  private byte[][] getSquare(int xIndex, int yIndex)
  {
    byte w = (byte)(boardWidth / boardSegs), b[][] = new byte[w][w];
    for (int x=0; x < b.length; x++)
    {
      for (int y=0; y < b[x].length; y++)
      {
        b[y][x] = boardVals[y + (yIndex * w)][x + (xIndex * w)];
      }
    }
    return b;
  }

  private boolean setIsOK(byte[] set)
  {
//    byte starting[] = new byte[row.length];
//    System.out.println("Looking at set " + java.util.Arrays.toString(set));
    for (int i=0; i < set.length - 1; i++)
    {
      for (int j=i + 1; j < set.length; j++)
      {
//        System.out.println("Comparing set[" + i + "](" + set[i] + ") and set[" + j + "](" + set[j] + ")");
        if (set[i] == set[j] && set[i] != NULL_VAL && set[j] != NULL_VAL)
        {
//          System.out.println("Set is not OK.");
          return false;
        }
      }
    }
//    System.out.println("Set is OK");
    return true;
  }

  private boolean areaIsOK(byte[][] area)
  {
//    System.out.println("Looking at area...");
    int size = 0;
    for (int i=0; i < area.length; i++)
    {
      size += area[i].length;
    }
    byte[] b = new byte[size];
    for (int x=0, i=0; x < area.length; x++)
    {
      for (int y=0; y < area[x].length; y++, i++)
      {
//        System.out.println("Setting starting[" + i + "] to area[" + x + "][" + y + "](" + area[x][y]);
        b[i] = area[x][y];
      }
    }
    return setIsOK(b);
  }

  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  private void printBoard()
  {
    printBoard(System.out);
  }

  @SuppressWarnings("UseOfSystemOutOrSystemErr")
  private void printBoard(java.io.PrintStream p)
  {
    System.out.println(toString());
  }
  
  private void resetBoard()
  {
    isHint = new boolean[boardWidth][boardWidth];
    boardVals = new byte[boardWidth][boardWidth];
    triedBoardVals = new byte[boardWidth][boardWidth][boardWidth];
    for (int x = 0; x < boardVals.length; x++)
    {
      for (int y = 0; y < boardVals[x].length; y++)
      {
        boardVals[y][x] = NULL_VAL;
        isHint[y][x] = false;
        for (int z=0; z < triedBoardVals[y][x].length; z++)
          triedBoardVals[y][x][z] = NULL_VAL;
        sudokuTextFields[y][x].setText(null);
        sudokuTextFields[y][x].setEnabled(true);
        sudokuTextFields[y][x].setEditable(true);
        sudokuTextFields[y][x].getAccessibleContext().setAccessibleDescription(Colors.SIZE_OVERRIDE + (Colors.DEFAULT_SIZE + 3) + Colors.COMMAND_SEP);
      }
    }
  }

  private String commaFmt(long l)
  {
    if (l < 1000)
      return Long.toString(l);

    String ret = "", lS = Long.toString(l);
    for (int i=0, j=lS.length() - 1; i < lS.length(); i++, j--)
    {
      ret = lS.charAt(j) + (i % 3 == 0 && i != 0 ? "," : "") + ret;
    }
    return ret;
  }

  private void fixUI()
  {
    for (int i=0; i < getWindows().length; i++)
      fixUIIn(getWindows()[i]);
  }

  private void fixUIIn(java.awt.Component c)
  {
    style = getSelectedStyle();
    this.c.fixStyleIn(c, style, font.getName(), java.awt.Font.PLAIN, fontMod, false);
  }

  public byte getSelectedStyle()
  {
    if (huskyRadioButtonMenuItem.isSelected())
      return Colors.HUSKY;
    else if (astroRadioButtonMenuItem.isSelected())
      return Colors.ASTRO;
    else if (metalRadioButtonMenuItem.isSelected())
      return Colors.NULL;
    else
      return Colors.MOCHA;
  }

  private String getDifficultyName()
  {
    if (numHints < (boardSegs * boardSegs) + boardSegs)
      return "diablolical";
    if (numHints < boardWidth * 2)
      return "tough";
    if (numHints < boardWidth * boardSegs)
      return "moderate";
    return "gentle";
  }

  private boolean boardIsFull()
  {
    for (int x=0; x < boardWidth; x++)
    {
      for (int y=0; y < boardWidth; y++)
      {
        if (sudokuTextFields[y][x].getText() == null || sudokuTextFields[y][x].getText().isEmpty() || boardVals[y][x] == NULL_VAL)
          return false;
      }
    }
    return true;
  }

  @SuppressWarnings("static-access")
  private void saveScoreFor(String username)
  {
    try
    {
      gs.tryScore(SCORE_FILE_NAME, username, gameCurrentTime, winTime);
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }
  }

  private void logThrowable(Throwable t)
  {
    t.printStackTrace();
    alertOf(t.getMessage() + " (" + t.getClass().getSimpleName() + ")", CRIT_MES);
    log.logThrowable(t);
  }

  private void fillScoreTable(final boolean show)
  {
    alertOf("Filling top scores...", INFO_MES);
    new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          final Object[][] scores = GameScore.topScores(SCORE_FILE_NAME, 10);
          for (int i=0; i < scores.length; i++)
          {
            if (scores[i][1] != null && !scores[i][1].toString().equalsIgnoreCase("null"))
            {
              try
              {
                long l = Long.parseLong(scores[i][1].toString());
                scores[i][1] = (((l / 1000) / 60) / 60) % 60 + ":" +
                               format(((l / 1000) / 60) % 60, 2) + ":" +
                               format((l / 1000) % 60, 2) + "." +
                               format(l % 1000, 3);
              }
              catch (Throwable t){t.printStackTrace();}
            }
            if (scores[i][2] != null && !scores[i][2].toString().equalsIgnoreCase("null"))
            {
              try
              {
                long l = Long.parseLong(scores[i][2].toString());
                scores[i][2] = null;
                scores[i][2] = new java.util.Date(l);
              }
              catch (Throwable t){t.printStackTrace();}
            }
          }
          jTable1.setModel(new javax.swing.table.TableModel()
          {
            String headers[] = {"Username", "Completion time (H:M:S:ms)", "Date completed"};
            Class classes[] = {String.class, String.class, java.util.Date.class};
            public int getRowCount()
            {
              return scores.length;
            }

            public int getColumnCount()
            {
              int c = 0;
              for (int i=0; i < scores.length; i++)
              {
                c = Math.max(c, scores[i].length);
              }
              return c;
            }

            public String getColumnName(int columnIndex)
            {
              return headers[columnIndex];
            }

            public Class<?> getColumnClass(int columnIndex)
            {
              return classes[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
              return false;
            }

            public Object getValueAt(int rowIndex, int columnIndex)
            {
              return scores[rowIndex][columnIndex];
            }

            public void setValueAt(Object aValue, int rowIndex, int columnIndex)
            {
              scores[rowIndex][columnIndex] = aValue;
            }

            public void addTableModelListener(javax.swing.event.TableModelListener l)
            {
    //          throw new UnsupportedOperationException("Not supported yet.");
            }

            public void removeTableModelListener(javax.swing.event.TableModelListener l)
            {
    //          throw new UnsupportedOperationException("Not supported yet.");
            }
          });
          if (show)
          {
            new CompAction().ghostIn(topScoresDialog);
          }
          alertOf("Top Scores filled" + (show ? " and shown." : ""), INFO_MES);
        }
        catch (Throwable t)
        {
          logThrowable(t);
        }
      }
    }).start();
    
  }
  
  @Override
  public String toString()
  {
    String b = gameCurrentTime + "\n\n";
    for (int y=0; y < boardWidth; y++)
    {
      for (int x=0; x < boardWidth; x++)
        b += boardVals[y][x] + (x < boardWidth - 1 ? ", " : "\n");
    }
    b += "\n";
    for (int y=0; y < boardWidth; y++)
    {
      for (int x=0; x < boardWidth; x++)
        b += isHint[y][x] + (x < boardWidth - 1 ? ", " : "\n");
    }
    
    return b + "\n";
  }
  
  public void save()
  {
    java.io.FileWriter writeToFile = null;
    try
    {
      writeToFile = new java.io.FileWriter(SAVE_FILE);
      writeToFile.write(toString());
      writeToFile.close();
    }
    catch (Throwable t)
    {
      logThrowable(t);
    }
    finally
    {
      try
      {
        writeToFile.close();
      }
      catch (java.io.IOException ex)
      {
        ex.printStackTrace();
      }
    }
  }

  private void load()
  {
    try
    {
      gameTimer.stop();
      java.util.Scanner line = new java.util.Scanner(SAVE_FILE), character;
      String s = line.nextLine();
      
      gameCurrentTime = Long.parseLong(s);
//      System.out.println(gameCurrentTime);
      
      line.nextLine(); //Skip blank line
      
      byte[][] board = new byte[boardWidth][boardWidth];
      for (int y=0; y < board.length; y++)
      {
        character = new java.util.Scanner(line.nextLine());
        for (int x=0; x < board[y].length; x++)
        {
          s = character.next();
          try
          {
            board[y][x] = Byte.parseByte(s.replace(",", ""));
          }
          catch (Throwable t)
          {
            System.err.println("WHAT IS THIS I DON'T EVEN: " + s + " at [" + y + "][" + x + "]");
          }
        }
      }
      
      line.nextLine(); //Skip blank line
      
      isHint = new boolean[boardWidth][boardWidth];
      for (int y=0; y < board.length; y++)
      {
        character = new java.util.Scanner(line.nextLine());
        for (int x=0; x < board[y].length; x++)
        {
          s = character.next();
          try
          {
            isHint[y][x] = Boolean.parseBoolean(s.replace(",", "").toLowerCase());
          }
          catch (Throwable t)
          {
            System.err.println("WHAT IS THIS I DON'T EVEN: " + s + " at [" + y + "][" + x + "]");
          }
        }
      }
      
      boardVals = new byte[boardWidth][boardWidth];
      triedBoardVals = new byte[boardWidth][boardWidth][boardWidth];
      for (int x = 0; x < boardVals.length; x++)
      {
        for (int y = 0; y < boardVals[x].length; y++)
        {
          boardVals[y][x] = board[y][x];
          for (int z=0; z < triedBoardVals[y][x].length; z++)
            triedBoardVals[y][x][z] = NULL_VAL;
          sudokuTextFields[y][x].setText(board[y][x] == NULL_VAL ? null : board[y][x] + "");
          sudokuTextFields[y][x].setEnabled(true);
          sudokuTextFields[y][x].setEditable(!isHint[y][x]);
          sudokuTextFields[y][x].getAccessibleContext().setAccessibleDescription(isHint[y][x] ? Colors.FONT_OVERRIDE + "Segoe UI" + Colors.COMMAND_SEP : Colors.SIZE_OVERRIDE + (Colors.DEFAULT_SIZE + 3) + Colors.COMMAND_SEP);
        }
      }
      fixUIIn(this);
      gameBeginTime = System.currentTimeMillis() - gameCurrentTime;
      pauseGameMenuItem.doClick();
      gameTimerLabel.setText(format((((gameCurrentTime / 1000) / 60) / 60) % 60, 2) + ":" + format(((gameCurrentTime / 1000) / 60) % 60, 2) + ":" + format((gameCurrentTime / 1000) % 60, 2) + "." + format(gameCurrentTime % 1000, 3));
      alertOf("Click \"Resume\" to continue your old game.", INFO_MES);
      gameTimer.start();
//      System.out.println(gameCurrentTime);
    }
    catch (Throwable t)
    {
      logThrowable(t);
    }
  }
  
  public boolean checkBoard()
  {
    for (int x=0; x < boardWidth; x++)
    {
      for (int y=0; y < boardWidth; y++)
      {
        try
        {
          System.out.println(y + ", " + x + ": " + boardVals[y][x]);
          if (sudokuTextFields[y][x].getText() != null && !sudokuTextFields[y][x].getText().isEmpty())
          boardVals[y][x] = Byte.parseByte(sudokuTextFields[y][x].getText());
          System.out.println("Checking to see if it's in range...");
          if ((boardVals[y][x] > boardWidth || boardVals[y][x] < 1) && boardVals[y][x] != NULL_VAL)
            throw new NumberFormatException();
          else if (boardIsFull())
          {
            if (boardIsOK())
            {
              winTime = System.currentTimeMillis();
              pauseGameMenuItem.doClick();
              pauseGameMenuItem.setEnabled(false);
              resumeButton.setEnabled(false);
              resumeButton.setText("You've won!");
              long mil = gameCurrentTime % 1000,
                   sec = (gameCurrentTime / 1000) % 60,
                   min = ((gameCurrentTime / 1000) / 60) % 60,
                   hr = (((gameCurrentTime / 1000) / 60) / 60) % 60;
              jLabel10.setText("<html>You have solved a Sudoku in exactly " +
              (hr > 0 ? "<b>" + hr + "</b> hour" + (hr == 1 ? "" : "s") : "") +
              (min > 0 ? (hr > 0 && (sec > 0 || mil > 0) ? ", " : "") + (hr > 0 && sec == 0 && mil == 0 ? " and " : "") + "<b>" + min + "</b> minute" + (min == 1 ? "" : "s") : "") +
              (sec > 0 ? (min > 0 && (hr > 0 || mil > 0) ? ", " : "") + ((min > 0 || hr > 0) && mil == 0 ? " and " : "") + "<b>" + sec + "</b> second" + (sec == 1 ? "" : "s") : "") +
              (mil > 0 ? (sec > 0 && (min > 0 || hr > 0) ? ", " : "") + (sec > 0 || min > 0 || hr > 0 ? " and " : "") + "<b>" + mil + "</b> millisecond" + (mil == 1 ? "" : "s") : "") +
              "! Please type your name in the field below and click the button to save your score!");
              usernameTextField.setText("Name");

              CompAction.center(winDialog);
              new CompAction().ghostIn(winDialog);
            }
          }
        }
        catch (NumberFormatException ex)
        {
          alertOf("The number at row " + (x + 1) + ", column " + (y + 1) + " is not between 1 and " + boardWidth, CRIT_MES);
          sudokuTextFields[y][x].grabFocus();
          sudokuTextFields[y][x].select(0, sudokuTextFields[y][x].getText().length());
          return false;
        }
      }
    }
    return true;
  }
}
