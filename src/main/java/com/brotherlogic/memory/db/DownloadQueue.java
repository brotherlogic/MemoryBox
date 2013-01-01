package com.brotherlogic.memory.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A downloadable object
 * 
 * @author simon
 * 
 */
class Downloadable
{
   /** The location of the download */
   private URL downloadLocation;
   /** The path where the download is stored */
   private String pathToStore;

   /**
    * Get method for download location
    * 
    * @return the URL we're downloading from
    */
   public URL getDownloadLocation()
   {
      return downloadLocation;
   }

   /**
    * Get method for store path
    * 
    * @return The file location we've stored this at
    */
   public String getPathToStore()
   {
      return pathToStore;
   }

   /**
    * Set method for download location
    * 
    * @param location
    *           The URL to download from
    */
   public void setDownloadLocation(final URL location)
   {
      this.downloadLocation = location;
   }

   /**
    * Set method for path to store
    * 
    * @param path
    *           The place where the file goes
    */
   public void setPathToStore(final String path)
   {
      this.pathToStore = path;
   }

}

/**
 * A download queue
 * 
 * @author simon
 * 
 */
public abstract class DownloadQueue implements Runnable
{
   /** Download buffer size */
   private static final int BUFFER_SIZE = 1024;

   /** The time to wait before checking the queue */
   private static final int WAIT_TIME = 5000;

   /** Used to log stuff */
   private final Logger logger = Logger.getLogger("com.brotherlogic.memory.db.DownloadQueue");

   /** Flag to indicate that we're running */
   private boolean running = true;

   /** Flag indicating we're in the slow stop */
   private boolean slowStop = false;

   /**
    * Blocking constructor
    */
   protected DownloadQueue()
   {

   }

   /**
    * Add an element to the queue
    * 
    * @param dl
    *           The object to add to the queue
    */
   protected abstract void addToQueue(Downloadable dl);

   /**
    * Run a download
    * 
    * @param downloadable
    *           The object to download
    * @throws IOException
    *            if something goes wrong
    * @return flag indicating we've downloaded something
    */
   private boolean doDownload(final Downloadable downloadable) throws IOException
   {
      boolean doneDownload = false;
      logger.log(Level.INFO, "Downloading: " + downloadable.getDownloadLocation());

      // Create the file if necessary - we don't download if the file already
      // exists
      File f = new File(downloadable.getPathToStore());
      if (!f.exists() || f.length() == 0)
      {
         if (!f.exists() && !f.createNewFile())
            throw new IOException("Cannot create file: " + f.getAbsolutePath());

         logger.log(Level.INFO, "Downloading: " + downloadable.getDownloadLocation());

         doneDownload = true;

         BufferedWriter writer = new BufferedWriter(new FileWriter(f));
         BufferedReader reader = new BufferedReader(new InputStreamReader(downloadable
               .getDownloadLocation().openStream()));
         char[] byteBuffer = new char[BUFFER_SIZE];
         int read = reader.read(byteBuffer);
         while (read > 0)
         {
            writer.write(byteBuffer, 0, read);
            read = reader.read(byteBuffer);
         }

         reader.close();
         writer.close();
      }

      return doneDownload;
   }

   /**
    * Run a download
    * 
    * @param url
    *           The URL to download
    * @return The location of the resultant download
    */
   public String download(final URL url)
   {
      String location = newFile(url.toString());
      Downloadable able = new Downloadable();
      able.setDownloadLocation(url);
      able.setPathToStore(location);

      addToQueue(able);

      return location;
   }

   /**
    * Get the next downloadable item from the queue
    * 
    * @return the next valid downloadable item, or null
    */
   protected abstract Downloadable getFromQueue();

   /**
    * Build a new file
    * 
    * @param url
    *           The URL to build for
    * 
    * @return The place where the file is stored
    */
   protected abstract String newFile(String url);

   /**
    * Store an arbritary file
    * 
    * @param key
    *           The key to store under
    * @return The String path to a file where this object can be stored
    */
   protected abstract String newStore(String key);

   /**
    * Remove an item from the download queue
    * 
    * @param dl
    *           The item to remove
    */
   protected abstract void removeFromQueue(Downloadable dl);

   @Override
   public void run()
   {
      boolean wait = true;
      while (running)
      {
         // Wait a bit
         if (wait)
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
               wait = doDownload(next);
            }
            catch (IOException e)
            {
               e.printStackTrace();
            }
            finally
            {
               removeFromQueue(next);
            }
         else if (slowStop)
            running = false;
      }
   }

   /**
    * Slow stops the download queue - i.e. waits until all downloads have been
    * processed and then stops
    */
   public void slowStop()
   {
      logger.log(Level.INFO, "Running a slow stop");
      slowStop = true;
   }
}
