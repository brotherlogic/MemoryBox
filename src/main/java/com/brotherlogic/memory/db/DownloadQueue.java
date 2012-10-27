package com.brotherlogic.memory.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public abstract class DownloadQueue implements Runnable
{
   boolean running = true;

   private static final int WAIT_TIME = 5000;

   @Override
   public void run()
   {
      while (running)
      {
         // Wait a bit
         try
         {
            Thread.sleep(WAIT_TIME);
         }
         catch (InterruptedException e)
         {
            e.printStackTrace();
         }

         // Process the next download
         Downloadable next = getFromQueue();
         if (next != null)
            try
            {
               doDownload(next);
            }
            catch (IOException e)
            {
               e.printStackTrace();
            }
      }
   }

   private static final int BUFFER_SIZE = 1024;

   protected DownloadQueue()
   {
      // Blocking constructor
   }

   protected abstract void removeFromQueue(Downloadable dl);

   protected abstract Downloadable getFromQueue();

   protected abstract void addToQueue(Downloadable dl);

   protected abstract String newFile();

   public String download(URL url)
   {
      String location = newFile();
      Downloadable able = new Downloadable();
      able.downloadLocation = url;
      able.pathToStore = location;

      addToQueue(able);

      return location;
   }

   private void doDownload(Downloadable downloadable) throws IOException
   {
      // Create the file if necessary
      File f = new File(downloadable.pathToStore);
      if (!f.exists())
         if (!f.createNewFile())
            throw new IOException("Cannot create file: " + f.getAbsolutePath());

      BufferedWriter writer = new BufferedWriter(new FileWriter(f));
      BufferedReader reader = new BufferedReader(new InputStreamReader(
            downloadable.downloadLocation.openStream()));
      char[] byteBuffer = new char[BUFFER_SIZE];
      int read = reader.read(byteBuffer);
      while (read > 0)
      {
         writer.write(byteBuffer, 0, read);
         read = reader.read(byteBuffer);
      }

      reader.close();
      writer.close();

      removeFromQueue(downloadable);
   }
}

class Downloadable
{
   URL downloadLocation;
   String pathToStore;
}
