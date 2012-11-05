package com.brotherlogic.memory.ui.calendar;

import java.awt.Graphics;
import java.util.Calendar;

import javax.swing.JPanel;

public abstract class CalendarPane extends JPanel
{
   int month = Calendar.getInstance().get(Calendar.MONTH) - 1;

   int SIDE_MARGIN = 20;
   int TOP_MARGIN = 50;
   int year = Calendar.getInstance().get(Calendar.YEAR);

   protected abstract String getRowSummary(int row);

   @Override
   public void paint(Graphics g)
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

   protected abstract void paintBox(Graphics g, Calendar date, int x, int y, int width, int height,
         int row, int day);
}
