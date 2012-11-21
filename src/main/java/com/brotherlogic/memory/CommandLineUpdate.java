package com.brotherlogic.memory;

import com.brotherlogic.memory.core.UntappdMemory;
import com.brotherlogic.memory.db.DBFactory;
import com.brotherlogic.memory.db.DownloadQueue;
import com.brotherlogic.memory.feeds.FeedReader;
import com.brotherlogic.memory.feeds.UntappdFeedReader;

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
    * @throws Exception
    *            if something goes wrong
    */
   public static void main(final String[] args) throws Exception
   {
      CommandLineUpdate clu = new CommandLineUpdate();
      clu.run();
   }

   /**
    * Runs the stuff
    * 
    * @throws Exception
    *            if something goes wrong
    */
   public void run() throws Exception
   {
      // Start up the download queue
      DownloadQueue queue = DBFactory.buildInterface().getDownloadQueue();
      Thread downloadThread = new Thread(queue);
      downloadThread.start();

      // Add the untapped reader
      DBFactory.buildInterface().followMemory(UntappdMemory.class, UntappdFeedReader.class,
            "brotherlogic");

      for (FeedReader reader : DBFactory.buildInterface().getMemoryReaders())
         reader.update();

      queue.slowStop();
   }
}
