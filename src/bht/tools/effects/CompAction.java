package bht.tools.effects;

import com.sun.awt.AWTUtilities.Translucency;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * <b>ATTENTION:</b> Formerly the <tt>FrameAction</tt> class, now works more than just windows. Many methods, such as
 * <tt>center(Component comp)</tt> still work on windows, but will now move a component to a position relative to its parent.
 * A utility that places or morphs a component within its parent. <br /><br />
 * <b>A note:</b> Many internal things have changed since the previous version. You can still use any of the non-moving methods
 * in a static context, but you must make a new variable for such methods as <tt>slideOpen(Component comp)</tt> and
 * <tt>goToLocation(Component comp, java.awt.Rectangle location)</tt>. This doubles as both a safety precaution to avoid
 * problems and glitches such as twitchy components and so that you can combine effects. For instance, you can tell a component
 * to both go to a certain location and fade away (like in the new <tt>ghostIn</tt> and <tt>ghostOut</tt> methods), and you will
 * not get any strange or unpredicted effects.
 * @version 1.3.0λ
 * @author Blue Husky Programming, © 2011
 */
public class CompAction
{
  /**
   * Integer code to be used with the <tt>corner(Component comp, int corner)</tt> and <tt>goToCorner(final Component comp, final
   * int CORNER)</tt> methods to place the component in the corresponding corner of its parent
   */
  public final static byte TOP_LEFT = 0, TOP_RIGHT = 1, BOTTOM_RIGHT = 2, BOTTOM_LEFT = 3;
  public static final byte TOP = 4, RIGHT = 5, BOTTOM = 6, LEFT = 7;
  public final static byte DO_NOT_EXIT = -1, EXIT_OK = 0, EXIT_ERROR = 1;
  private static int screen = 0, offset;
  private static int count = 0;
  private static Window flashingWindow;
  private static Dialog flashingDialog;
  private javax.swing.Timer mover, slider, fader;
  private static Dimension dim = new Dimension(), min;
  private static boolean noOffset = false;
  private static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
  @SuppressWarnings("StaticNonFinalUsedInInitialization")
  private static Rectangle maxWinBounds = ge.getMaximumWindowBounds();
  @SuppressWarnings("StaticNonFinalUsedInInitialization")
  private static Rectangle screenBounds = new Rectangle(ge.getScreenDevices()[0].getDisplayMode().getWidth(), ge.
          getScreenDevices()[0].getDisplayMode().getHeight());

  public CompAction()
  {
    this(0);
  }
  
  public CompAction(int screen)
  {
    setScreen(screen);
  }
  /**
   * Centers a component in the selected monitor
   * @param comp
   * the component you wish to affect
   */
  public static void center(Component comp)
  {
    if (comp instanceof Window)
    {
      screenOffset();
      location(comp, new Rectangle((int) (((noOffset ? screenBounds : maxWinBounds).getWidth() / 2) - (comp.getSize().getWidth() / 2)) + offset,
                                            (int) (((noOffset ? screenBounds : maxWinBounds).getHeight() / 2) - (comp.getSize().getHeight() / 2)),
                                            (int) comp.getSize().getWidth(),
                                            (int) comp.getSize().getHeight()));
    }
    else
      location(comp, new Rectangle((int) ((comp.getParent().getWidth() / 2) - (comp.getSize().getWidth() / 2)) + offset,
                                            (int) ((comp.getParent().getHeight() / 2) - (comp.getSize().getHeight() / 2)),
                                            (int) comp.getSize().getWidth(),
                                            (int) comp.getSize().getHeight()));
  }

  /**
   * Places a component in the corner of the specified monitor
   * @param comp
   * the component you wish to place
   * @param CORNER
   * <t>byte</tt> indicating the corner of the display where you wish to place the component. Please use the provided constants
   * <tt>TOP_LEFT</tt>, <tt>TOP_RIGHT</tt>, <tt>BOTTOM_LEFT</tt>, and <tt>BOTTOM_RIGHT</tt> in this field.
   */
  public static void corner(Component comp, final byte CORNER)
  {
    screenOffset();
    Rectangle DEST;
    switch (CORNER)
    {
      default:
      case TOP_LEFT:
        DEST = new Rectangle(offset,
                                      0,
                                      (int) comp.getSize().getWidth(),
                                      (int) comp.getSize().getHeight());
        break;
      case TOP_RIGHT:
        DEST = new Rectangle((int) ((noOffset ? screenBounds : maxWinBounds).getWidth() - comp.getSize().getWidth())
                                      + offset,
                                      0,
                                      (int) comp.getSize().getWidth(),
                                      (int) comp.getSize().getHeight());
        break;
      case BOTTOM_RIGHT:
        DEST = new Rectangle((int) ((noOffset ? screenBounds : maxWinBounds).getWidth() - comp.getSize().getWidth())
                                      + offset,
                                      (int) ((noOffset ? screenBounds : maxWinBounds).getHeight() - comp.getSize().getHeight()),
                                      (int) comp.getSize().getWidth(),
                                      (int) comp.getSize().getHeight());
        break;
      case BOTTOM_LEFT:
        DEST = new Rectangle(offset,
                                      (int) ((noOffset ? screenBounds : maxWinBounds).getHeight() - comp.getSize().getHeight()),
                                      (int) comp.getSize().getWidth(),
                                      (int) comp.getSize().getHeight());
        break;
    }
    location(comp, DEST);
//    System.out.println("snapped to " + DEST + ".");
  }

  /**
   * Moves a component to the corner of the specified monitor.
   * @param comp
   * the component you wish to move
   * @param CORNER
   * <tt>byte</tt> indicating the corner of the display where you wish to move the component. Please use the provided constants
   * <tt>TOP_LEFT</tt>, <tt>TOP_RIGHT</tt>, <tt>BOTTOM_LEFT</tt>, and <tt>BOTTOM_RIGHT</tt> in this field.
   */
  public void goToCorner(final Component comp, final byte CORNER)
  {
    screenOffset();
    final Rectangle FROM, DEST;
    switch (CORNER)
    {
      default:
      case TOP_LEFT:
        DEST = new Rectangle(offset - 1,
                                      -1,
                                      (int) comp.getSize().getWidth(),
                                      (int) comp.getSize().getHeight());
        break;
      case TOP_RIGHT:
        DEST = new Rectangle((int) ((noOffset ? screenBounds : maxWinBounds).getWidth() - comp.getSize().getWidth())
                                      + offset + 1,
                                      -1,
                                      (int) comp.getSize().getWidth(),
                                      (int) comp.getSize().getHeight());
        break;
      case BOTTOM_RIGHT:
        DEST = new Rectangle((int) ((noOffset ? screenBounds : maxWinBounds).getWidth() - comp.getSize().getWidth())
                                      + offset + 1,
                                      (int) ((noOffset ? screenBounds : maxWinBounds).getHeight() - comp.getSize().getHeight())
                                      + 1,
                                      (int) comp.getSize().getWidth(),
                                      (int) comp.getSize().getHeight());
        break;
      case BOTTOM_LEFT:
        DEST = new Rectangle(offset - 1,
                                      (int) ((noOffset ? screenBounds : maxWinBounds).getHeight() - comp.getSize().getHeight())
                                      + 1,
                                      (int) comp.getSize().getWidth(),
                                      (int) comp.getSize().getHeight());
        break;
    }
    goToLocation(comp, DEST);
  }

  /**
   * Slides open a component for effect
   * @param comp
   * the component you wish to slide open
   * @param WIDTH
   * The final width of the component after sliding it open
   * @param HEIGHT
   * The final height of the component after sliding it open
   */
  public void slideOpen(final Component comp, final int WIDTH, final int HEIGHT)
  {
//    double w = 16;//WIDTH / 32;
//    double h = 16;//HEIGHT / 32;
    stopSliding();
    min = new Dimension(comp.getMinimumSize());
    comp.setMinimumSize(new Dimension(0, 0));
    comp.setSize(new Dimension(0, 0));
    dim = comp.getSize();
//    System.out.println(WIDTH + ", " + HEIGHT + "\t" + window.getSize().getWidth() + ", " + window.getSize().getWidth());
    slider = new javax.swing.Timer(50, new java.awt.event.ActionListener()
    {
      int w = WIDTH, h = HEIGHT;

      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
//        System.out.println("WIDTH - w, HEIGHT - h = " + WIDTH + " - " + w + ", " + HEIGHT + " - " + h);

        if (dim.getWidth() < WIDTH || dim.getHeight() < HEIGHT)
        {
          dim = new Dimension(WIDTH - w, HEIGHT - h);

          if (dim.getWidth() < WIDTH)
          {
            w = (int) (w / 1.25);
          }
          else
          {
            h = (int) (h / 1.25);
          }
          comp.setSize(dim);
//          comp.repaint();
          if (comp.getParent() != null)
            comp.getParent().repaint();
        }
        else
        {
          stopSliding();
          comp.setMinimumSize(new Dimension(min.width < WIDTH ? min.width : WIDTH, min.height < HEIGHT ? min.height : HEIGHT));
        }
//        System.out.println("WIDTH: " + WIDTH + ", HEIGHT: " + HEIGHT + ", w: " + w + ", h: " + h + ", window.getSize().toString(): " + window.getSize().toString() + ", dim:" + dim);
      }
    });
    comp.setVisible(true);
    slider.start();
  }

  /**
   * Slides open a component for effect, uses the preferred size as the target size.
   * @param comp
   * the component you wish to slide open
   */
  public void slideOpen(final Component comp)
  {
    slideOpen(comp, comp.getPreferredSize().width, comp.getPreferredSize().height);
  }

  /**
   * Sets the screen on which to display the target window when using the <tt>center(Component window)</tt>, <tt>corner(Component window, final int CORNER)</tt>, <tt>goToCorner(final Component window, final int CORNER)</tt>, <tt>slideOpenFromCenter(final Component window, final int WIDTH, final int HEIGHT)</tt>, <tt>slideOpenFromCenter(final Component window)</tt>, and <tt>slideClosedToCenter(final Component window, final boolean exit)</tt> methods.
   * @param preferredScreen
   * the number of the preferred screen
   */
  public final void setScreen(int preferredScreen)
  {
    screen = preferredScreen - 1;
    int w = ge.getScreenDevices()[0].getDisplayMode().getWidth(), h = ge.getScreenDevices()[0].getDisplayMode().getHeight();
//    System.out.println("Selected screen: " + preferredScreen + " [" + ge.getScreenDevices()[screen].getDisplayMode().getWidth() + ", " + ge.getScreenDevices()[screen].getDisplayMode().getHeight() + "]");
    for (int i = 1; i <= screen; i++)
    {
      w = ge.getScreenDevices()[i].getDisplayMode().getWidth();
      h = ge.getScreenDevices()[i].getDisplayMode().getHeight();
    }
//    System.out.println("w: " + w + "\th: " + h);
    screenBounds.setSize(w, h);
    maxWinBounds.setSize(w, (int) ge.getMaximumWindowBounds().getHeight());
//    System.out.println(maxWinBounds);
  }

  /**
   * Gets the Screen on which to display the target window when using the <tt>center(Component window)</tt>,
   * <tt>corner(Component window, final int CORNER)</tt>, <tt>goToCorner(final Component window, final int CORNER)</tt>,
   * <tt>slideOpenFromCenter(final Component window, final int WIDTH, final int HEIGHT)</tt>,
   * <tt>slideOpenFromCenter(final Component window)</tt>, and
   * <tt>slideClosedToCenter(final Component window, final boolean exit)</tt> methods.
   * @return the currently used screen
   */
  public static int getScreen()
  {
    return screen + 1;
  }

  /**
   * Stops the movement of all windows and other components currently being affected by CompAction
   * @deprecated use other <tt>stop</tt> methods to stop specific actions
   */
  public void stop()
  {
    stopSliding();
    stopMoving();
    stopFading();
    count = 0;
  }

  /**
   * Stops the movement of the window or other component currently being slid open or shut (<tt>slide</tt> methods)
   */
  public void stopSliding()
  {
    if (slider != null)
    {
      slider.restart();
      slider.stop();
    }
    count = 0;
  }

  /**
   * Stops the movement of the window or other component currently being moved (<tt>goTo</tt> methods)
   */
  public void stopMoving()
  {
    if (mover != null)
    {
      mover.restart();
      mover.stop();
    }
    count = 0;
  }

  /**
   * Stops the movement of the window currently being faded (<tt>fade</fade> methods)
   */
  public void stopFading()
  {
    if (fader != null)
    {
      fader.restart();
      fader.stop();
    }
    count = 0;
  }
  
  public boolean isSliding()
  {
    return slider != null && slider.isRunning();
  }
  
  public boolean isMoving()
  {
    return mover != null && mover.isRunning();
  }
  
  public boolean isFading()
  {
    return fader != null && fader.isRunning();
  }

  /**
   * sets whether the component is offset from the corner of the screen due to a taskbar, dock, or similar object when using the
   * <tt>corner(Component comp, final int CORNER)</tt> and <tt>goToCorner(final Component comp, final int CORNER)</tt> methods.
   * @param flag
   * the boolean flag as to whether there is an offset
   */
  public void setOffset(boolean flag)
  {
    noOffset = flag;
  }

  /**
   * @return a boolean that represents whether the component is offset from the corner of the screen due to a taskbar, dock, or
   * similar object when using the <tt>corner(Component comp, final int CORNER)</tt> and <tt>goToCorner(final Component comp, final int CORNER)</tt> methods.
   */
  public static boolean isOffset()
  {
    return noOffset;
  }

  /**
   * Slides open a component for effect, keeping it in the center of the selected display
   * @param comp
   * the component you wish to slide open
   * @param WIDTH
   * The final width of the component after sliding it open
   * @param HEIGHT
   * The final height of the component after sliding it open
   */
  public void slideOpenFromCenter(final Component comp, final int WIDTH, final int HEIGHT)
  {
    stopSliding();
    min = new Dimension(comp.getMinimumSize());
    comp.setMinimumSize(new Dimension(0, 0));
    comp.setSize(new Dimension(0, 0));
    dim = comp.getSize();
    slider = new javax.swing.Timer(50, new java.awt.event.ActionListener()
    {
      int w = WIDTH, h = HEIGHT;

      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        center(comp);
        if (dim.getWidth() < WIDTH || dim.getHeight() < HEIGHT)
        {
          dim = new Dimension(WIDTH - w, HEIGHT - h);

          if (dim.getWidth() < WIDTH)
          {
            w = (int) (w / 1.25);
          }
          else
          {
            h = (int) (h / 1.25);
          }
          comp.setSize(dim);
          comp.repaint();
        }
        else
        {
          stopSliding();
          comp.setMinimumSize(new Dimension(min.width < WIDTH ? min.width : WIDTH, min.height < HEIGHT ? min.height : HEIGHT));
        }
//        System.out.println("WIDTH: " + WIDTH + ", HEIGHT: " + HEIGHT + ", w: " + w + ", h: " + h + ", window.getSize().toString(): " + window.getSize().toString() + ", dim:" + dim + ", timer2.isRunning(): " + timer2.isRunning());
      }
    });
    center(comp);
    comp.setVisible(true);
    slider.start();
  }


  /**
   * Slides open a component for effect, keeping it in the center of the selected display
   * @param comp
   * the component you wish to slide open
   * @param d
   * The final dimensions of the component after sliding it open
   */
  public void slideOpenFromCenter(final Component comp, final Dimension d)
  {
    slideOpenFromCenter(comp, d.width, d.height);
  }

  /**
   * Slides open a component for effect, keeping it in the center of the selected display, uses the minimum size as the target size.<br />
   * Equivalent of calling <tt>slideOpenFromCenter(component, component.getMinimumSize().width, component.getMinimumSize().height);</tt>
   * @param comp
   * the component you wish to slide open
   */
  public void slideOpenFromCenter(final Component comp)
  {
    slideOpenFromCenter(comp, comp.getMinimumSize().width, comp.getMinimumSize().height);
  }

  /**
   * Slides a component closed for effect<br />
   * Equivalent of calling <tt>slideClosed(component, false);</tt>
   * @param comp
   * the component you wish to slide closed
   */
  public void slideClosed(final Component comp)
  {
    slideClosed(comp, false);
  }

  /**
   * Slides a component closed for effect
   * @param comp
   * the component you wish to slide closed
   * @param exit
   * quits the program at the end if true
   */
  public void slideClosed(final Component comp, final boolean exit)
  {
//    window.setResizable(true);
    stopSliding();
    min = new Dimension(comp.getMinimumSize());
    comp.setMinimumSize(new Dimension(0, 0));
    comp.setEnabled(false);
    dim = new Dimension(comp.getWidth(), comp.getHeight());
    slider = new javax.swing.Timer(50, new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        if (dim.getHeight() > 1)
        {
          dim = new Dimension((int) dim.getWidth(), (int) (dim.getHeight() / 1.25) - 1);
        }
        else
        {
          dim = new Dimension((int) (dim.getWidth() / 1.25) - 1, (int) dim.getHeight());
        }
        comp.setSize(dim);
        comp.repaint();

//          System.out.println(window.getSize() + "\t" + dim.getSize());
        if (dim.getHeight() <= 1)
        {
          comp.setVisible(false);
          stopSliding();
          if (exit)
          {
            System.exit(0);
          }
          comp.setMinimumSize(min);
        }
      }
    });
    slider.start();
  }

  /**
   * Slides a component closed for effect, keeping it in the center of the selected display<br />
   * Equivalent of calling <tt>slideClosedToCenter(component, DO_NOT_EXIT);</tt>
   * @param comp
   * the component you wish to slide closed
   */
  public void slideClosedToCenter(final Component comp)
  {
    slideClosedToCenter(comp, DO_NOT_EXIT);
  }

  /**
   * Slides a component closed for effect, keeping it in the center of the selected display
   * @param comp
   * the component you wish to slide closed
   * @param exit
   * quits the program at the end if the correct parameter is input. Please use the <tt>DO_NOT_EXIT</tt>, <tt>EXIT_OK</tt>, and
   * <tt>EXIT_ERROR</tt> constants
   */
  public void slideClosedToCenter(final Component comp, final int exit)
  {
//    window.setResizable(true);
    stopSliding();
    min = new Dimension(comp.getMinimumSize());
    comp.setMinimumSize(new Dimension(0, 0));
    comp.setEnabled(false);
    dim = new Dimension(comp.getWidth(), comp.getHeight());
    slider = new javax.swing.Timer(50, new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        center(comp);
        if (dim.getWidth() > 1)
        {
          dim = new Dimension((int) (dim.getWidth() / 1.25) - 1, (int) dim.getHeight());
        }
        else
        {
          dim = new Dimension((int) dim.getWidth(), (int) (dim.getHeight() / 1.25) - 1);
        }
        comp.setSize(dim);
        comp.repaint();

//          System.out.println(window.getSize() + "\t" + dim.getSize());
        if (dim.getHeight() <= 1)
        {
          comp.setVisible(false);
          stopSliding();
          if (exit != DO_NOT_EXIT)
          {
            System.exit(exit);
          }
          comp.setEnabled(true);
          comp.setMinimumSize(min);
        }
      }
    });
    slider.start();
  }
  
  /**
   * Slides the component in from the specified edge of the cmponent's parent to its current location
   * @param comp the <tt>java.awt.Component</tt> to emerge
   * @param location the edge from which the component will emerge
   */
  public void slideIn(Component comp, byte location)
  {
    Rectangle from = comp.getBounds();
    location(comp, location == TOP || location == BOTTOM ? comp.getX() : -comp.getWidth(),
                   location == LEFT || location == RIGHT ? comp.getY() : -comp.getHeight());
    comp.setVisible(true);
    goToLocation(comp, from);
  }
  
  /**
   * Slides the component out to the specified edge of the component's parent to its current location
   * @param comp the <tt>java.awt.Component</tt> to hide
   * @param location the edge to which the component will hide
   */
  public void slideOut(Component comp, byte location)
  {
    slideOut(comp, location, false, 0);
  }
  
  /**
   * Slides the component out to the specified edge of the component's parent to its current location
   * @param comp the <tt>java.awt.Component</tt> to hide
   * @param location the edge to which the component will hide
   * @param exit
   * @param status  
   */
  public void slideOut(Component comp, byte location, final boolean exit, final int status)
  {
    Rectangle from = comp.getBounds();
    goToLocation(comp, new Rectangle(location == TOP || location == BOTTOM ? comp.getX() : (location == LEFT ? -comp.getWidth() : (comp instanceof Window ? screenBounds.width : comp.getParent().getWidth()) + comp.getWidth()),
                   location == LEFT || location == RIGHT ? comp.getY() : (location == TOP ? -comp.getHeight() : (comp instanceof Window ? screenBounds.height : comp.getParent().getHeight()) + comp.getHeight()), comp.getWidth(), comp.getHeight()),
                   from, true, new java.awt.event.ActionListener()
                   {
                     public void actionPerformed(ActionEvent e)
                     {
                       if (exit)
                         System.exit(status);
                     }
                   });
  }

  /**
   * Moves the specified component to the specified location
   * @param comp
   * the component you wish to move
   * @param location
   * java.awt.Rectangle indicating the location where you wish to move the component.
   */
  public void goToLocation(final Component comp, final Rectangle location)
  {
    goToLocation(comp, location, location, false);
  }

  /**
   * Moves the specified component to the specified location
   * @param comp
   * the component you wish to move
   * @param X
   * <tt>int</tt> indicating the X location to where you wish to move the component.
   * @param Y
   * <tt>int</tt> indicating the Y location to where you wish to move the component.
   * @param ret a <tt>java.awt.Rectangle</tt> 
   */
  public void goToLocation(final Component comp, final int X, final int Y, final Rectangle ret)
  {
    goToLocation(comp, new Rectangle(X, Y), ret, false);
  }



  /**
   * Moves the specified component to the specified location
   * @param comp
   * the component you wish to move
   * @param location
   * <tt>java.awt.Rectangle</tt> indicating the location where you wish to move the component.
   * @param ret
   * <tt>java.awt.Rectangle</tt> indicating the location where you wish to place the component after moving.
   * @param hide true if you want the component's <tt>setVisible(boolean b)</tt> to false at the end
   */
  public void goToLocation(final Component comp, final Rectangle location, final Rectangle ret,
                           final boolean hide)
  {
    goToLocation(comp, location, ret, hide, null);
  }


  /**
   * Moves the specified component to the specified location
   * @param comp
   * the component you wish to move
   * @param location
   * <tt>java.awt.Rectangle</tt> indicating the location where you wish to move the component.
   * @param ret
   * <tt>java.awt.Rectangle</tt> indicating the location where you wish to place the component after moving.
   * @param hide true if you want the component's <tt>setVisible(boolean b)</tt> to false at the end
   * @param endAction The action to perform after the animation is complete 
   */
  public void goToLocation(final Component comp, final Rectangle location, final Rectangle ret,
                           final boolean hide, final java.awt.event.ActionListener endAction)
  {
//    System.out.println("goToLocation(" + comp + ", " + location + ")");
    stopMoving();
    final Rectangle FROM, DEST = new Rectangle(location);
    FROM = new Rectangle(comp.getBounds());
    final boolean goingUp = DEST.getY() > comp.getY(),
            goingDown = DEST.getY() < comp.getY(),
            goingRight = DEST.getX() > comp.getX(),
            goingLeft = DEST.getX() < comp.getX();

    mover = new javax.swing.Timer(100, new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        comp.setBounds((int) ((comp.getBounds().getX() + (DEST.getX() - 2)) / 2) + 1,
                         (int) ((comp.getBounds().getY() + (DEST.getY() - 2)) / 2) + 1,
                         (int) comp.getSize().getWidth(),
                         (int) comp.getSize().getHeight());
        count++;
//        System.out.println("(((" + (goingRight) + ") && " + (comp.getBounds().getX() >= DEST.getBounds().getX()) + ") && ((" + (goingDown) + ") && " + (comp.getBounds().getY() >= DEST.getBounds().getY()) + ")) || " +
//                           "(((" + (goingLeft) + ") && " + (comp.getBounds().getX() <= DEST.getBounds().getX()) + ") && ((" + (goingUp) + ") && " + (comp.getBounds().getY() <= DEST.getBounds().getY()) + ")) || " +
//                           (count > 20));
//        System.out.println((((goingRight) && comp.getBounds().getX() >= DEST.getBounds().getX()) && ((goingDown) && comp.getBounds().getY() >= DEST.getBounds().getY())) ||
//                           (((goingLeft) && comp.getBounds().getX() <= DEST.getBounds().getX()) && ((goingUp) && comp.getBounds().getY() <= DEST.getBounds().getY())) ||
//                           count > 20);
//        System.out.println(comp.getBounds());

        if ((((goingRight) && comp.getBounds().getX() >= DEST.getBounds().getX()) &&
                ((goingDown) && comp.getBounds().getY() >= DEST.getBounds().getY())) ||
                (((goingLeft) && comp.getBounds().getX() <= DEST.getBounds().getX()) &&
                ((goingUp) && comp.getBounds().getY() <= DEST.getBounds().getY())) ||
                count > 10)
        {
//          System.out.println(count);
          stopMoving();
          ((Component)comp).setVisible(!hide);
          location(comp, ret);
          comp.setEnabled(true);
          if (endAction != null)
            endAction.actionPerformed(evt);
        }
      }
    });
    mover.start();
    comp.setEnabled(false);
  }

  /**
   * Moves the specified component to the specified location.
   * Equivalent of calling <tt>goToLocation(comp, new java.awt.Rectangle(location));</tt>
   * @param comp
   * the component you wish to move
   * @param location
   * Dimension indicating the location where you wish to move the component.
   */
  public void goToLocation(final Component comp, final Dimension location)
  {
    goToLocation(comp, new Rectangle(location));
  }

  /**
   * Moves the specified component in the specified location.
   * Equivalent to calling <tt>goToocation(comp, new java.awt.Rectangle(x, y, comp.getWidth(), comp.getHeight()));</tt>
   * @param comp the <tt>java.awt.Component</tt> you wish to move
   * @param x <tt>int</tt> indicating the horizontal location where you wish to place the component.
   * @param y <tt>int</tt> indicating the vertical location where you wish to place the component.
   */
  public void goToLocation(Component comp, int x, int y)
  {
    goToLocation(comp, new Rectangle(x, y, comp.getWidth(), comp.getHeight()));
  }

  /**
   * Places the specified component in the specified location.
   * Equivalent of calling <tt>location(comp, new java.awt.Rectangle(x, y, comp.getWidth(), comp.getHeight()));</tt>
   * @param comp the <tt>java.awt.Component</tt> you wish to place
   * @param x <tt>int</tt> indicating the horizontal location where you wish to place the component.
   * @param y <tt>int</tt> indicating the vertical location where you wish to place the component.
   */
  public static void location(Component comp, int x, int y)
  {
    location(comp, new Rectangle(x, y, comp.getWidth(), comp.getHeight()));
  }

  /**
   * Places the specified component in the specified location.<br/>
   * <i>Equivalent to calling <tt>location(comp, new java.awt.Rectangle(location.width, location.height, comp.getWidth(),
   * comp.getHeight()));</tt></i>
   * @param comp the <tt>java.awt.Component</tt> you wish to place
   * @param location <tt>java.awt.Dimension</tt> indicating the location where you wish to place the component.
   */
  public static void location(Component comp, Dimension location)
  {
    location(comp, new Rectangle(location.width, location.height, comp.getWidth(), comp.getHeight()));
  }

  /**
   * Places the specified component in the specified location
   * @param comp the <tt>java.awt.Component</tt> you wish to place
   * @param location <tt>java.awt.Rectangle</tt> indicating the location where you wish to place the component.
   */
  public static void location(Component comp, Rectangle location)
  {
//    System.out.println("location(" + window + ", " + location + ")");
    comp.setBounds(location.x,
                     location.y,
                     location.width,
                     location.height);
  }

  /**
   * calculates the offset from the original position based on the screen selection
   */
  private static void screenOffset()
  {
    offset = 0;
    for (int i = 0; !noOffset && i < screen; i++)
    {
      offset += ge.getScreenDevices()[i].getDisplayMode().getWidth();
    }
//    System.out.println("offset set to " + offset);
  }

  /**
   * sets the transparency of a
   * @param comp The component to try to make transparent
   * @param t the transparency of the component (0.0F - 1.0F)
   */
  public static void setTransparency(Component comp, float t)
  {
//    System.out.println("Transparency: " + (t * 100) + "%");
    try
    {
      if (comp instanceof Window)
        com.sun.awt.AWTUtilities.setWindowOpacity((Window)comp, t);
      else
      {
        Graphics2D g2 = (Graphics2D) comp.getGraphics().create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        comp.paint(g2);
        g2.dispose();
      }
//    else
//      try
//      {
//        com.sun.awt.AWTUtilities.setWindowOpacity(window, 1);
//        com.sun.awt.AWTUtilities.setWindowOpaque(window, false);
    }
    catch (Throwable th)
    {
      th.printStackTrace();
    }
  }

  public static float getTransparency(Component comp)
  {
    if (comp instanceof Window)
      return com.sun.awt.AWTUtilities.getWindowOpacity((Window)comp);
    else
    {
      return 1;
    }
  }

  public void fadeIn(final Component comp)
  {
    fadeIn(comp, 1.5F);
  }

  public void fadeIn(final Component comp, final float speed)
  {
    stopFading();
//    if (com.sun.awt.AWTUtilities.isTranslucencySupported(Translucency.TRANSLUCENT))
//    {
      setTransparency(comp, 0);
      comp.setVisible(true);
      fader = new javax.swing.Timer(100, new java.awt.event.ActionListener()
      {
        float t = 0, t2 = 1;

        public void actionPerformed(java.awt.event.ActionEvent evt)
        {
          setTransparency(comp, t);
          t2 = t2 / speed;
          t = 1 - t2;

          if (t >= .999)
          {
            stopFading();
            comp.setVisible(true);
            setTransparency(comp, 1);
          }
        }
      });
      fader.start();
//    }
//    else
//    {
////      System.err.println("ERROR: transleucency not supported");
//      window.setVisible(true);
//      setTransparency(window, 1);
//    }
  }

  public void fadeAway(final Component comp)
  {
    fadeAway(comp, 1.5F, false, 0);
  }

  public void fadeAway(final Component comp, final float speed)
  {
    fadeAway(comp, speed, false, 0);
  }

  public void fadeAway(final Component comp, final boolean exit)
  {
    fadeAway(comp, 1.5F, exit, 0);
  }

  public void fadeAway(final Component comp, final boolean exit, final int status)
  {
    fadeAway(comp, 1.5F, exit, 0);
  }

  public void fadeAway(final Component comp, final float speed, final boolean exit)
  {
    fadeAway(comp, speed, exit, 0);
  }

  public void fadeAway(final Component comp, final float speed, final boolean exit, final int status)
  {
    stopFading();
//    if (com.sun.awt.AWTUtilities.isTranslucencySupported(Translucency.TRANSLUCENT))
//    {
      fader = new javax.swing.Timer(100, new java.awt.event.ActionListener()
      {
        float t = 1;

        public void actionPerformed(java.awt.event.ActionEvent evt)
        {
          setTransparency(comp, t);
          t = t / speed;

          if (t <= 0.01)
          {
            stopFading();
            try
            {
              comp.setVisible(false);
              setTransparency(comp, 1);
            }
            catch (Throwable t1)
            {
              t1.printStackTrace();
            }
            if (exit)
            {
              System.exit(status);
            }
          }
        }
      });
      fader.start();
//    }
//    else
//    {
//      System.err.println("ERROR: transleucency not supported");
//      window.setVisible(false);
//    }
  }

  /**
   * Utilizes all three types of window effects (moving, resizing, and fading) to morph one window into another
   * @param fromComp the original component, to be morphed away
   * @param toComp the component to which the original is being being morphed
   */
  public void morphInto(final Component fromComp, final Component toComp)
  {
    stop();
    final Rectangle FROM = fromComp.getBounds(), DEST = toComp.getBounds();
    final boolean GOING_UP = DEST.getY() < FROM.getY(),
                  GOING_RIGHT = DEST.getX() > FROM.getX(),
                  EXPANDING_H = DEST.getWidth() > FROM.getWidth(),
                  EXPANDING_V = DEST.getHeight() > FROM.getHeight(),
                  fromResize = (fromComp instanceof Frame && ((Frame)fromComp).isResizable()) ||
                  (fromComp instanceof Dialog && ((Dialog)fromComp).isResizable()),
                  toResize = (toComp instanceof Frame && ((Frame)toComp).isResizable()) ||
                  (toComp instanceof Dialog && ((Dialog)toComp).isResizable());

    int speed = 100;

    fromComp.setBounds(FROM);
    toComp.setBounds(FROM);
    mover = new javax.swing.Timer(speed, new java.awt.event.ActionListener()
    {
      double d = 0;
      int count = 0;
      Rectangle here = FROM;

      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
//        System.out.println("d: " + d);
//        System.out.println("FROM: " + FROM);
//        System.out.println("here: " + here);
//        System.out.println("DEST: " + DEST);

        here = new Rectangle((int) ((FROM.getX() * (1 - d)) + (DEST.getX() * d)), (int) ((FROM.getY() * (1 - d)) + (DEST.getY()
                                                                                                                    * d)),
                             (int) ((FROM.getWidth() * (1 - d)) + (DEST.getWidth() * d)), (int) ((FROM.getHeight() * (1 - d))
                                                                                                 + (DEST.getHeight() * d)));
        fromComp.setBounds(here);
        toComp.setBounds(here);
        fromComp.setSize((int) here.getWidth(), (int) here.getHeight());
        toComp.setSize((int) here.getWidth(), (int) here.getHeight());

//        System.out.println("((" + GOING_UP + " && " + here.getY() + " <= " + DEST.getY() + ") || (" + !GOING_UP + " && " + here.
//                getY() + " >= " + DEST.getY() + ")) &&\n" + "((" + GOING_RIGHT + " && " + here.getX() + " >= " + DEST.getX()
//                           + ") || (" + !GOING_RIGHT + " && " + here.getX() + " <= " + DEST.getX() + ")) &&\n" + "(("
//                           + EXPANDING_V + " && " + here.getHeight() + " >= " + DEST.getHeight() + ") || (" + !EXPANDING_V
//                           + " && " + here.getHeight() + " <= " + DEST.getHeight() + ")) &&\n" + "((" + EXPANDING_H + " && " + here.
//                getWidth() + " >= " + DEST.getWidth() + ") || (" + !EXPANDING_H + " && " + here.getWidth() + " <= " + DEST.
//                getWidth() + "))");

        if (d >= 1 ||
                (((GOING_UP && here.getY() <= DEST.getY()) || (!GOING_UP && here.getY() >= DEST.getY())) &&
                ((GOING_RIGHT && here.getX() >= DEST.getX()) || (!GOING_RIGHT && here.getX() <= DEST.getX())) &&
                ((EXPANDING_V && here.getHeight() >= DEST.getHeight()) || (!EXPANDING_V && here.getHeight() <= DEST.getHeight())) &&
                ((EXPANDING_H && here.getWidth() >= DEST.getWidth()) || (!EXPANDING_H && here.getWidth() <= DEST.getWidth()))))
        {
//          System.out.println("Done moving!");
          fromComp.setVisible(false);
          toComp.setVisible(true);
          fromComp.setBounds(FROM);
          toComp.setBounds(DEST);
          fromComp.setSize((int) FROM.getWidth(), (int) FROM.getHeight());
          toComp.setSize((int) DEST.getWidth(), (int) DEST.getHeight());

//          System.out.println("FROM: " + FROM);
//          System.out.println("DEST: " + DEST);

          if (fromComp instanceof Frame && !fromResize)
          {
            ((Frame) fromComp).setResizable(false);
          }
          else if (fromComp instanceof Dialog && !fromResize)
          {
            ((Dialog) fromComp).setResizable(false);
          }

          if (toComp instanceof Frame && !toResize)
          {
            ((Frame) toComp).setResizable(false);
          }
          else if (toComp instanceof Dialog && !toResize)
          {
            ((Dialog) toComp).setResizable(false);
          }
          stopMoving();
        }
        count++;
        d = (double) count / 10;
      }
    });
    try
    {
      if (com.sun.awt.AWTUtilities.isTranslucencySupported(Translucency.TRANSLUCENT))
      {
        setTransparency(toComp, 0F);
        toComp.setVisible(true);

        fader = new javax.swing.Timer(speed, new java.awt.event.ActionListener()
        {
          float t = 1;
          byte count = 0;

          public void actionPerformed(java.awt.event.ActionEvent evt)
          {
            t -= .1F;
            count++;

            if (t <= 0.01 || count >= 10)
            {
//              System.out.println("Done fading!");
              stopFading();
              fromComp.setVisible(false);
              setTransparency(fromComp, 1);
              setTransparency(toComp, 1);
            }
            else
            {
//              System.out.println("from: " + t + "\tto:" + (1 - t));
              setTransparency(fromComp, t);
              setTransparency(toComp, 1 - t);
            }
          }
        });
        fader.start();
      }
      else
      {
        System.err.println("ERROR: transleucency not supported");
        fromComp.setVisible(false);
      }
    }
    catch (Throwable t)
    {
    }
    mover.start();
    toComp.setVisible(true);
  }

  /**
   * Alerts the OS that a window wants attention, usually resulting in the corresponding taskbar icon "flashing"
   * @param window the <tt>Window</tt> that wants attention
   */
  public static void flash(Window window)
  {
    flash(window, false);
  }

  /**
   * Alerts the OS that a window wants attention, usually resulting in the corresponding taskbar icon "flashing". An optional alert sound can be played, as well
   * @param window the <tt>Window</tt> that wants attention
   * @param ding a <tt>boolean</tt> which signifies whether you want the window to make an alert sound along with the flash
   */
  public static void flash(Window window, final boolean ding)
  {
    flashingWindow = window;
    flashingDialog = new Dialog(flashingWindow);
    flashingDialog.setUndecorated(true);
    flashingDialog.setSize(0, 0);
    flashingDialog.setModal(false);
    flashingDialog.addWindowFocusListener(new java.awt.event.WindowAdapter()
    {
      @Override
      public void windowGainedFocus(java.awt.event.WindowEvent e)
      {
        flashingWindow.requestFocus();
        flashingDialog.setVisible(false);
        super.windowGainedFocus(e);
      }
    });
    flashingWindow.addWindowFocusListener(new java.awt.event.WindowAdapter()
    {
      @Override
      public void windowGainedFocus(java.awt.event.WindowEvent e)
      {
        flashingDialog.setVisible(false);
        super.windowGainedFocus(e);
      }
    });
    if (!flashingWindow.isFocused())
    {
      if (flashingDialog.isVisible())
      {
        flashingDialog.setVisible(false);
      }
      flashingDialog.setLocation(0, 0);
      flashingDialog.setLocationRelativeTo(flashingWindow);
      flashingDialog.setVisible(true);
    }
    flashingWindow.requestFocus();
    flashingWindow.requestFocusInWindow();
    if (ding)
      Toolkit.getDefaultToolkit().beep();
  }

  /**
   * Snaps a component to the edges of its parent when dragged near them. Only guaranteed to work on the primary monitor.<br />
   * <i><b>Note:</b> a glitch causes the window to snap too far from the respective lower or right edge when a taskbar, dock,
   * etc. is reserving the space on top or left. No glitches found when the item reserving the edge is on the bottom or right.<br/>
   * <br/>
   * <b>Note</b> A Java disability causes the window to be unable to maximize. Only use this for windows which you don't plan
   * to be maximized.</i>
   * @param comp the component that is to snap to its parent's edges
   * @param dist the distance that defines "near", as an <tt>int</tt>. A distance of zero deactivates the ability.
   */
  public void setSnapToEdges(final Component comp, final int dist)
  {
    java.awt.event.ComponentListener snap = new java.awt.event.ComponentAdapter()
    {
      @Override
      public void componentMoved(java.awt.event.ComponentEvent evt)
      {
        Rectangle b = comp.getBounds(), p = (comp.getParent() == null ? ge.getMaximumWindowBounds() : comp.getParent().getBounds());
        int bX = (int) b.getX(), bY = (int) b.getY(), bW = (int) b.getWidth(), bH = (int) b.getHeight(),
                pX = (int) p.getX(), pY = (int) p.getY(), pW = (int) p.getWidth(), pH = (int) p.getHeight();
//        System.out.println(b + "\n" + p);
        comp.setBounds((bX <= dist + pX && bX >= pX - dist) ? pX : (bX + bW >= pW - dist && bX + bW <= pW + dist ? pW - bW : bX),
                       (bY <= dist + pY && bY >= pY - dist) ? pY : (bY + bH >= pH - dist && bY + bH <= pH + dist ? pH - bH : bY),
                       bW, bH);
      }
    };

    if (dist != 0)
    {
      comp.addComponentListener(snap);
    }
    else
    {
      comp.removeComponentListener(snap);
    }
  }

  public void ghostIn(Component comp)
  {
    Rectangle b = comp.getBounds();
    location(comp, new Rectangle((int)b.getX(), (int)b.getY() + 64, (int)b.getWidth(), (int)b.getHeight()));
    fadeIn(comp, 2);
    goToLocation(comp, b);
  }
  public void ghostOut(Component comp)
  {
    ghostOut(comp, false);
  }

  public void ghostOut(Component comp, boolean exit)
  {
    ghostOut(comp, exit, 0);
  }

  public void ghostOut(Component comp, boolean exit, int status)
  {
    Rectangle b = comp.getBounds();
    fadeAway(comp, 2, exit, status);
    goToLocation(comp, new Rectangle((int)b.getX(), (int)b.getY() - 64, (int)b.getWidth(), (int)b.getHeight()), b, true);
  }

  public void morphTo(final Component fromComp, Rectangle to)
  {
    stop();
    final Rectangle FROM = fromComp.getBounds(), DEST = to;
    final boolean GOING_UP = DEST.getY() < FROM.getY(),
                  GOING_RIGHT = DEST.getX() > FROM.getX(),
                  EXPANDING_H = DEST.getWidth() > FROM.getWidth(),
                  EXPANDING_V = DEST.getHeight() > FROM.getHeight(),
                  fromResize = (fromComp instanceof Frame && ((Frame)fromComp).isResizable()) ||
                  (fromComp instanceof Dialog && ((Dialog)fromComp).isResizable());

    int delay = 25;

    fromComp.setBounds(FROM);
    mover = new javax.swing.Timer(delay, new java.awt.event.ActionListener()
    {
      double d = 0;
      int count = 0;
      Rectangle here = FROM;

      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        System.out.println("d: " + d);
        System.out.println("FROM: " + FROM);
        System.out.println("here: " + here);
        System.out.println("DEST: " + DEST);

        here = new Rectangle((int) ((FROM.getX() * (1 - d)) + (DEST.getX() * d)), (int) ((FROM.getY() * (1 - d)) + (DEST.getY()
                                                                                                                    * d)),
                             (int) ((FROM.getWidth() * (1 - d)) + (DEST.getWidth() * d)), (int) ((FROM.getHeight() * (1 - d))
                                                                                                 + (DEST.getHeight() * d)));
        fromComp.setBounds(here);
        fromComp.setSize((int) here.getWidth(), (int) here.getHeight());

//        System.out.println("((" + GOING_UP + " && " + here.getY() + " <= " + DEST.getY() + ") || (" + !GOING_UP + " && " + here.
//                getY() + " >= " + DEST.getY() + ")) &&\n" + "((" + GOING_RIGHT + " && " + here.getX() + " >= " + DEST.getX()
//                           + ") || (" + !GOING_RIGHT + " && " + here.getX() + " <= " + DEST.getX() + ")) &&\n" + "(("
//                           + EXPANDING_V + " && " + here.getHeight() + " >= " + DEST.getHeight() + ") || (" + !EXPANDING_V
//                           + " && " + here.getHeight() + " <= " + DEST.getHeight() + ")) &&\n" + "((" + EXPANDING_H + " && " + here.
//                getWidth() + " >= " + DEST.getWidth() + ") || (" + !EXPANDING_H + " && " + here.getWidth() + " <= " + DEST.
//                getWidth() + "))");

        if (d >= 1 ||
                (((GOING_UP && here.getY() <= DEST.getY()) || (!GOING_UP && here.getY() >= DEST.getY())) &&
                ((GOING_RIGHT && here.getX() >= DEST.getX()) || (!GOING_RIGHT && here.getX() <= DEST.getX())) &&
                ((EXPANDING_V && here.getHeight() >= DEST.getHeight()) || (!EXPANDING_V && here.getHeight() <= DEST.getHeight())) &&
                ((EXPANDING_H && here.getWidth() >= DEST.getWidth()) || (!EXPANDING_H && here.getWidth() <= DEST.getWidth()))))
        {
//          System.out.println("Done moving!");
          fromComp.setBounds(DEST);

//          System.out.println("FROM: " + FROM);
//          System.out.println("DEST: " + DEST);

          if (fromComp instanceof Frame && !fromResize)
          {
            ((Frame) fromComp).setResizable(false);
          }
          else if (fromComp instanceof Dialog && !fromResize)
          {
            ((Dialog) fromComp).setResizable(false);
          }
          stopMoving();
        }
        count++;
        d = (double) count / 10;
      }
    });
    mover.start();
  }
}
