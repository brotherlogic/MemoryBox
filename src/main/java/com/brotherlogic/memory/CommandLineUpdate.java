package com.brotherlogic.memory;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.brotherlogic.memory.db.DBFactory;
import com.brotherlogic.memory.db.DownloadQueue;
import com.brotherlogic.memory.feeds.FeedReader;
import com.brotherlogic.memory.feeds.instagram.InstagramFeedReader;
import com.brotherlogic.memory.feeds.instagram.InstagramMemory;

/**
 * Test bed for updating the database from the command line
 * 
 * @author simon
 * 
 */
public class CommandLineUpdate
{
   /**
    * Main method
    * 
    * @param args
    *           CL Params are not used
    */
   public static void main(final String[] args)
   {
      CommandLineUpdate clu = new CommandLineUpdate();
      clu.run();
   }

   /** The logger to be used */
   private final Logger logger = Logger.getLogger(this.getClass().getName());

   /**
    * Runs the stuff
    * 
    * @throws Exception
    *            if something goes wrong
    */
   public void run()
   {
      // Start up the download queue
      DownloadQueue queue = DBFactory.buildInterface().getDownloadQueue();
      Thread downloadThread = new Thread(queue);
      downloadThread.start();

      try
      {
         // Add the things we want
         logger.log(Level.INFO, "Adding memory classes");
         // DBFactory.buildInterface().followMemory(DiscogsMemory.class,
         // DiscogsFeedReader.class, "");
         // DBFactory.buildInterface().followMemory(UntappdMemory.class,
         // UntappdFeedReader.class,
         // "brotherlogic");
         DBFactory.buildInterface().followMemory(InstagramMemory.class, InstagramFeedReader.class,
               "");

         logger.log(Level.INFO, "Updating readers");
         for (FeedReader reader : DBFactory.buildInterface().getMemoryReaders())
         {
            logger.log(Level.INFO, "Starting Update " + reader.getClass());
            reader.updateAllMemories(true);
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }

      queue.slowStop();
   }
}
