/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bht.tools.utilities;

/**
 *
 * @author Supuhstar
 */
public class Numbers
{
  /** The <tt>char</tt> representation of the default radix point (<tt>.</tt>) */
  public final char RAD_POI = '.';
  
  
  public static boolean isInRangeOf(byte num, byte[] nums)
  {
    double[] l = new double[nums.length];
    System.arraycopy(nums, 0, l, 0, nums.length);
    return isInRangeOf((double)num, l);
  }

  public static boolean isInRangeOf(short num, short[] nums)
  {
    double[] l = new double[nums.length];
    System.arraycopy(nums, 0, l, 0, nums.length);
    return isInRangeOf((double)num, l);
  }

  public static boolean isInRangeOf(int num, int[] nums)
  {
    double[] l = new double[nums.length];
    System.arraycopy(nums, 0, l, 0, nums.length);
    return isInRangeOf((double)num, l);
  }

  public static boolean isInRangeOf(long num, long[] nums)
  {
    double[] l = new double[nums.length];
    System.arraycopy(nums, 0, l, 0, nums.length);
    return isInRangeOf((double)num, l);
  }

  public static boolean isInRangeOf(float num, float[] nums)
  {
    double[] l = new double[nums.length];
    System.arraycopy(nums, 0, l, 0, nums.length);
    return isInRangeOf((double)num, l);
  }

  public static boolean isInRangeOf(double num, double[] nums)
  {
    double low = Double.MAX_VALUE;
    double high = Double.MIN_VALUE;
    for (int i=0; i < nums.length; i++)
    {
      if (nums[i] < low)
        low = nums[i];
      else if (nums[i] > high)
        high = nums[i];
    }
    return num > low && num < high;
  }

  public static boolean contains(byte num, byte[] nums)
  {
    double[] l = new double[nums.length];
    System.arraycopy(nums, 0, l, 0, nums.length);
    return contains((double)num, l);
  }

  public static boolean contains(short num, short[] nums)
  {
    double[] l = new double[nums.length];
    System.arraycopy(nums, 0, l, 0, nums.length);
    return contains((double)num, l);
  }

  public static boolean contains(int num, int[] nums)
  {
    double[] l = new double[nums.length];
    System.arraycopy(nums, 0, l, 0, nums.length);
    return contains((double)num, l);
  }

  public static boolean contains(long num, long[] nums)
  {
    double[] l = new double[nums.length];
    System.arraycopy(nums, 0, l, 0, nums.length);
    return contains((double)num, l);
  }

  public static boolean contains(float num, float[] nums)
  {
    double[] l = new double[nums.length];
    System.arraycopy(nums, 0, l, 0, nums.length);
    return contains((double)num, l);
  }

  public static boolean contains(double num, double[] nums)
  {
    for (int i=0; i < nums.length; i++)
      if (nums[i] == num)
        return true;
    return false;
  }
  
  /**
   * Works like Double.parseDouble, but ignores any extraneous characters. The first radix point (<tt>.</tt>) is the only one treated as such.<br/>
   * <h4>Examples:</h4>
   * <li><tt>extractDouble("123456.789")</tt> returns the double value of <tt>123456.789</tt>
   * <li><tt>extractDouble("1qw2e3rty4uiop[5a'6.p7u8&9")</tt> returns the double value of <tt>123456.789</tt>
   * <li><tt>extractDouble("123,456.7.8.9")</tt> returns the double value of <tt>123456.789</tt>
   * <li><tt>extractDouble("I have $9,862.39 in the bank.")</tt> returns the double value of <tt>9862.39</tt>
   * @param str The <tt>String</tt> from which to extract a <tt>double</tt>.
   * @return the <tt>double</tt> that has been found within the string, if any.
   * @throws NumberFormatException if <tt>str</tt> does not contain a digit between 0 and 9, inclusive. 
   */
  public double extractDouble(String str) throws NumberFormatException
  {
    try
    {
      return Double.parseDouble(str);
    }
    finally
    {
      boolean r = true;
      String d = "";
      for (int i=0; i < str.length(); i++)
        if (Character.isDigit(str.charAt(i)) || (str.charAt(i) == RAD_POI && r))
        {
          if (str.charAt(i) == RAD_POI && r)
            r = false;
          d += str.charAt(i);
        }
      try
      {
        return Double.parseDouble(d);
      } catch (NumberFormatException ex)
      {
        
      }
    }
  }

  public static String groupDigits(long l)
  {
    return groupDigits(l, (byte)3, ",");
  }

  public static String groupDigits(long l, byte size)
  {
    return groupDigits(l, size, ",");
  }

  public static String groupDigits(long l, String grouper)
  {
    return groupDigits(l, (byte)3, grouper);
  }

  public static String groupDigits(long l, byte size, String grouper)
  {
    if (l < Math.pow(10, size) && l > 0)
      return Long.toString(l);

    String ret = "", lS = Long.toString(l);
    for (int i=0, j=lS.length() - 1; i < lS.length(); i++, j--)
    {
      ret = lS.charAt(j) + (i % size == 0 && i != 0 ? grouper : "") + ret;
    }
    return ret;
  }

  public static String lenFmt(String num, int fmt)
  {
    if (num.length() >= fmt)
      return num.substring(0, fmt);

    String ret = num;
    for (int i=0; i < fmt - num.length(); i++)
      ret = '0' + ret;
    return ret;
  }

  public static String radFmt(String num, int fmt)
  {
    throw new UnsupportedOperationException("CBB");
  }
}
