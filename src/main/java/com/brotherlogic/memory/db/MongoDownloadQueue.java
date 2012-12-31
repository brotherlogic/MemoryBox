package com.brotherlogic.memory.db;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * Download queue which uses mogno
 * 
 * @author simon
 * 
 */
public class MongoDownloadQueue extends DownloadQueue
{
   private static MongoDownloadQueue singleton = null;

   public static MongoDownloadQueue build()
   {
      if (singleton == null)
         singleton = new MongoDownloadQueue();
      return singleton;
   }

   /** used to log events */
   private final Logger logger = Logger.getLogger("com.brotherlogic.memory.db.MongoDownloadQueue");

   /** The queue we use */
   private DBCollection queue;

   @Override
   protected void addToQueue(final Downloadable dl)
   {
      logger.log(Level.INFO, "Adding " + dl.getDownloadLocation() + " to download queue " + queue
            + " from " + this);

      connect();

      DBObject add = new BasicDBObject();
      add.put("url", dl.getDownloadLocation().toString());
      add.put("file", dl.getPathToStore());

      logger.log(Level.INFO, "Inserting object to queue " + queue);
      queue.insert(add);
      logger.log(Level.INFO, "Inserted");

      logger.log(Level.INFO, "Added " + dl.getDownloadLocation() + " to download queue");
   }

   /**
    * Connects to the database
    */
   private void connect()
   {
      if (queue == null)
         try
         {
            queue = new MongoInterface().getCollection("DownloadQueue");
         }
         catch (IOException e)
         {
            e.printStackTrace();
         }
   }

   @Override
   protected Downloadable getFromQueue()
   {
      connect();

      // Get one at random
      DBObject next = queue.findOne();
      if (next != null)
      {
         Downloadable dl = new Downloadable();
         try
         {
            dl.setDownloadLocation(new URL((String) next.get("url")));
         }
         catch (MalformedURLException e)
         {
            e.printStackTrace();
         }
         dl.setPathToStore((String) next.get("file"));

         return dl;
      }

      return null;
   }

   @Override
   protected String newFile(final String url)
   {
      if (!new File(System.getProperty("user.home") + "/.memorybox/images").exists())
         new File(System.getProperty("user.home") + "/.memorybox/images").mkdirs();

      String base = System.getProperty("user.home") + "/.memorybox/images/";

      return new File(base + url.hashCode() + ".image").getAbsolutePath();
   }

   @Override
   protected String newStore(final String key)
   {
      if (!new File(System.getProperty("user.home") + "/.memorybox/objects").exists())
         new File(System.getProperty("user.home") + "/.memorybox/objects").mkdirs();

      String base = System.getProperty("user.home") + "/.memorybox/objects/";

      return new File(base + key).getAbsolutePath();
   }

   @Override
   protected void removeFromQueue(final Downloadable dl)
   {
      connect();

      DBObject del = new BasicDBObject();
      del.put("url", dl.getDownloadLocation().toString());
      del.put("file", dl.getPathToStore());

      queue.remove(del);
   }

}
