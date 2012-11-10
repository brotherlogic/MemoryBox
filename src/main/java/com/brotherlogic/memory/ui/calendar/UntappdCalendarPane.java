package com.brotherlogic.memory.ui.calendar;

import java.awt.Graphics;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;

import com.brotherlogic.memory.core.Memory;
import com.brotherlogic.memory.core.UntappdMemory;
import com.brotherlogic.memory.db.DBFactory;

/**
 * Beer calendar
 * 
 * @author simon
 * 
 */
public class UntappdCalendarPane extends ImageCalendarPane
{
   /**
    * Main method
    * 
    * @param args
    *           No command line arguments
    */
   public static void main(final String[] args)
   {
      JFrame framer = new JFrame();
      UntappdCalendarPane pane = new UntappdCalendarPane();

      framer.setSize(500, 500);
      framer.setLocationRelativeTo(null);
      framer.getContentPane().add(pane);
      framer.setVisible(true);

      framer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }

   /** Units per row */
   private final Map<Integer, Double> rowCount = new TreeMap<Integer, Double>();

   /**
    * Constructor
    */
   public UntappdCalendarPane()
   {
      super("com.brotherlogic.memory.core.UntappdMemory");
   }

   @Override
   protected String getRowSummary(final int row)
   {
      return "" + rowCount.get(row);
   }

   @Override
   public void paint(final Graphics g)
   {
      rowCount.clear();
      super.paint(g);
   }

   @Override
   protected void paintBox(final Graphics g, final Calendar date, final int x, final int y,
         final int width, final int height, final int rowG, final int day)
   {

      // TODO Auto-generated method stub
      super.paintBox(g, date, x, y, width, height, rowG, day);

      try
      {
         Collection<Memory> memories = DBFactory.buildInterface().retrieveMemories(date,
               UntappdMemory.class.getName());
         for (Memory mem : memories)
         {
            if (!rowCount.containsKey(rowG))
               rowCount.put(rowG, 0.0);
            UntappdMemory uMem = (UntappdMemory) mem;

            if (uMem.getAmount() != null)
               rowCount.put(rowG, rowCount.get(rowG) + (uMem.getAbv() * uMem.getAmount() / 1000));
         }
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}
