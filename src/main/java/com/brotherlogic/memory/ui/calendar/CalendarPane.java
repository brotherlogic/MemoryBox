package com.brotherlogic.memory.ui.calendar;

import java.awt.Graphics;
import java.util.Calendar;

import javax.swing.JPanel;

/**
 * A pane which builds a calendar display
 * 
 * @author simon
 * 
 */
public abstract class CalendarPane extends JPanel
{
   /** The margin for the calendar */
   private static final int SIDE_MARGIN = 20;

   /** The margin at the top */
   private static final int TOP_MARGIN = 50;

   /** The month we're building for */
   private final int month = Calendar.getInstance().get(Calendar.MONTH);

   /** The year we're building for */
   private final int year = Calendar.getInstance().get(Calendar.YEAR);

   /**
    * Gets a summary value for a row of the calendar
    * 
    * @param row
    *           The number row to get a summary value for
    * @return The string summary for this row
    */
   protected abstract String getRowSummary(int row);

   @Override
   public void paint(final Graphics g)
   {
      // Draw the bounding box
      g.drawRect(SIDE_MARGIN, TOP_MARGIN, this.getWidth() - 2 * SIDE_MARGIN, this.getHeight()
            - SIDE_MARGIN - TOP_MARGIN);

      double boxWidth = (this.getWidth() - 2 * SIDE_MARGIN + 0.0) / 7;
      double boxHeight = (this.getHeight() - SIDE_MARGIN - TOP_MARGIN + 0.0) / 5;

      Calendar start = Calendar.getInstance();
      start.set(Calendar.MONTH, month);
      start.set(Calendar.YEAR, year);
      start.set(Calendar.DAY_OF_MONTH, 1);

      // Reset the calendar to point to the previous monday
      start.add(Calendar.DAY_OF_YEAR, -start.get(Calendar.DAY_OF_WEEK) + 2);

      // Draw each other box
      for (int row = 0; row < 5; row++)
         for (int day = 0; day < 7; day++)
         {
            g.drawRect((int) (SIDE_MARGIN + day * boxWidth), (int) (TOP_MARGIN + row * boxHeight),
                  (int) (boxWidth), (int) (boxHeight));
            g.drawString("" + start.get(Calendar.DAY_OF_MONTH),
                  (int) (SIDE_MARGIN + day * boxWidth), (int) (TOP_MARGIN + row * boxHeight) + 10);
            paintBox(g, start, (int) (SIDE_MARGIN + day * boxWidth), (int) (TOP_MARGIN + row
                  * boxHeight) + 10, (int) (boxWidth), (int) (boxHeight - 10), row, day);
            start.add(Calendar.DAY_OF_YEAR, 1);
         }

      // Draw in the row summaries
      for (int row = 0; row < 5; row++)
      {
         String text = getRowSummary(row);
         if (text != null && text.length() > 0)
            g.drawString(text, (int) (SIDE_MARGIN + 7 * boxWidth), (int) (TOP_MARGIN + row
                  * boxHeight) + 10);

      }
   }

   /**
    * Paint a specific box for a specific day
    * 
    * @param g
    *           The graphics object
    * @param date
    *           The date which we're painting for
    * @param x
    *           The top left corner of the box
    * @param y
    *           The top left corner of the box
    * @param width
    *           Width of the box
    * @param height
    *           Height of the box
    * @param row
    *           The row which we're painting for
    * @param day
    *           The day (DOW) that we're painting for
    */
   protected abstract void paintBox(final Graphics g, final Calendar date, final int x,
         final int y, final int width, final int height, final int row, final int day);
}
