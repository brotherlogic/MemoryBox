package com.brotherlogic.memory.ui.calendar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.brotherlogic.memory.core.ImageMemory;
import com.brotherlogic.memory.core.Memory;
import com.brotherlogic.memory.db.DBFactory;

public class ImageCalendarPane extends CalendarPane
{
   public static void main(String[] args)
   {
      JFrame framer = new JFrame();
      ImageCalendarPane pane = new ImageCalendarPane("com.brotherlogic.memory.core.UntappdMemory");

      framer.setSize(500, 500);
      framer.setLocationRelativeTo(null);
      framer.getContentPane().add(pane);
      framer.setVisible(true);

      framer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

   }

   String imageType;

   public ImageCalendarPane(String type)
   {
      imageType = type;
   }

   @Override
   protected void paintBox(Graphics g, Calendar date, int x, int y, int width, int height)
   {
      // TODO Auto-generated method stub
      try
      {
         Collection<Memory> memories = DBFactory.buildInterface().retrieveMemories(date, imageType);
         Collection<ImageMemory> images = new LinkedList<ImageMemory>();
         for (Memory mem : memories)
            if (mem instanceof ImageMemory)
            {
               // Only add image memorys that have images attached to them
               ImageMemory imgMem = (ImageMemory) mem;
               if (!imgMem.getImagePath().endsWith("NONE"))
                  images.add(imgMem);
            }

         int multiplier = 1;
         if (images.size() > 0 && images.size() < 10)
            multiplier = (int) Math.round(Math.sqrt(images.size()));

         if (images.size() > 0)
         {
            int count = 0;
            for (ImageMemory imgMem : images)
               if (count < multiplier * multiplier)
               {
                  int row = count % multiplier;
                  int col = count / multiplier;

                  String path = imgMem.getImagePath();
                  Image img = ImageIO.read(new File(path));

                  int imgWidth = img.getWidth(null);
                  int imgHeight = img.getHeight(null);

                  double scaleFactor = (imgWidth + 0.0) / (width / multiplier);
                  if (imgHeight > imgWidth || width > height)
                     scaleFactor = imgHeight / (height / multiplier);
                  int rwidth = (int) (imgWidth / scaleFactor);
                  int rheight = (int) (imgHeight / scaleFactor);

                  // Resize the image to fit
                  g.drawImage(img, x + row * (width / multiplier), y + col * (height / multiplier),
                        rwidth, rheight, Color.white, null);
                  count++;
               }
         }

      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}
