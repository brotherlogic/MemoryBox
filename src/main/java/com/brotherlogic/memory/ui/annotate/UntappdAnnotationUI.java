package com.brotherlogic.memory.ui.annotate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.brotherlogic.memory.core.Memory;
import com.brotherlogic.memory.core.UntappdMemory;
import com.brotherlogic.memory.db.DBFactory;

public class UntappdAnnotationUI extends JPanel
{
   public static void main(String[] args) throws Exception
   {
      UntappdAnnotationUI anno = new UntappdAnnotationUI();

      JFrame framer = new JFrame();
      framer.add(anno);
      framer.setSize(500, 500);
      framer.setVisible(true);
      framer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      framer.pack();

      anno.run();
   }

   JPanel imgPanel;

   JLabel topLabel;

   UntappdMemory unannotatedMemory;

   public UntappdAnnotationUI()
   {
      initGUI();
   }

   private void initGUI()
   {
      this.setLayout(new BorderLayout());

      topLabel = new JLabel();
      add(topLabel, BorderLayout.NORTH);

      imgPanel = new JPanel()
      {
         @Override
         public Dimension getPreferredSize()
         {
            return new Dimension(500, 500);
         }

         @Override
         public void paint(Graphics g)
         {
            super.paint(g);

            if (unannotatedMemory != null && unannotatedMemory.getImagePath() != null
                  && unannotatedMemory.getImagePath().length() > 0)
               try
               {
                  System.out.println("HERE: " + this.getHeight() + " => "
                        + unannotatedMemory.getImagePath());
                  Image img = ImageIO.read(new File(unannotatedMemory.getImagePath()));
                  g.drawImage(img, 0, 0, 500, 500, null);
               }
               catch (IOException e)
               {
                  e.printStackTrace();
               }
         }
      };
      this.add(imgPanel, BorderLayout.CENTER);

      final JTextField field = new JTextField();
      this.add(field, BorderLayout.SOUTH);
      field.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent arg0)
         {
            if (field.getText().equals("HP"))
               unannotatedMemory.setAmount(284);
            else if (field.getText().equals("TP"))
               unannotatedMemory.setAmount(189);
            else if (field.getText().equals("USP"))
               unannotatedMemory.setAmount(473);
            else if (field.getText().equals("P"))
               unannotatedMemory.setAmount(568);
            else
               unannotatedMemory.setAmount(Integer.parseInt(field.getText()));

            try
            {
               DBFactory.buildInterface().storeMemory(unannotatedMemory);
               run();
            }
            catch (IOException e)
            {
               e.printStackTrace();
            }
         }
      });
   }

   public void run() throws IOException
   {
      Collection<Memory> memories = DBFactory.buildInterface()
            .retrieveMemories(UntappdMemory.class);
      for (Memory memory : memories)
         if (((UntappdMemory) memory).getAmount() == null)
         {
            set((UntappdMemory) memory);
            return;
         }
   }

   public void set(UntappdMemory mem)
   {
      unannotatedMemory = mem;
      topLabel.setText(mem.getBreweryName() + " - " + mem.getBeerName());
      this.revalidate();
      imgPanel.repaint();
   }
}
