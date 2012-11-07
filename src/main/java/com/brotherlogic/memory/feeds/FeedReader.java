package com.brotherlogic.memory.feeds;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.brotherlogic.memory.core.Memory;
import com.brotherlogic.memory.db.DBFactory;

/**
 * An abstraction of a feed reader
 * 
 * @author simon
 * 
 */
public abstract class FeedReader
{
   /** The logger to be used */
   private static Logger logger = Logger.getLogger("com.brotherlogic.memory.feeds.FeedReader");

   /**
    * Gets the underlying representation for this object
    * 
    * @param mem
    *           The memory to get the rep for
    * @return The text of the read representation
    * @throws IOException
    *            if we can't read the representation
    */
   public abstract String getUnderlyingRepresentation(Memory mem) throws IOException;

   /**
    * Probes the feed - returns the latest memory
    * 
    * @return The latest Memory present in the feed
    * @throws IOException
    *            if something goes wrong with reading
    */
   public abstract Memory probeFeed() throws IOException;

   /**
    * Updates all the feed
    * 
    * @throws IOException
    *            if we can't process the feed
    */
   public void update() throws IOException
   {
      Memory mem = probeFeed();
      Memory topDBMem = null;
      if (mem != null)
         topDBMem = DBFactory.buildInterface().retrieveLatestMemory(mem.getClass());

      logger.log(Level.INFO, "Got " + mem + " and " + topDBMem);

      if (topDBMem != null)
         logger.log(Level.INFO, "Also: " + topDBMem.isFilled());

      // Check on the versions - we may not have got any DB Mems
      if (topDBMem == null)
         updateAllMemories(true);
      else if (!topDBMem.isFilled())
         updateAllMemories(false);
      else
      {
         logger.log(Level.INFO, "Top " + topDBMem.getTimestamp() + " and " + mem.getTimestamp());
         logger.log(Level.INFO, "Top = " + mem);
         logger.log(Level.INFO, "DBTOP = " + topDBMem);

         // Check on the underlying representations
         String retBase = getUnderlyingRepresentation(mem);
         String givenBase = DBFactory.buildBaseRepStore().getBaseRep(mem);
         if (!retBase.equals(givenBase))
            updateAllMemories(true);
         else if (mem.getTimestamp() > topDBMem.getTimestamp())
            updateMemories(topDBMem.getTimestamp());
      }
   }

   /**
    * Update all the memories - refresh the memories
    * 
    * @param alsoUpdateUnderlying
    *           if we also know that we need to update the underlying
    *           representation
    * @throws IOException
    *            if we can't update the memories
    */
   public abstract void updateAllMemories(boolean alsoUpdateUnderlying) throws IOException;

   /**
    * Update the memories up to the latest version
    * 
    * @param timestamp
    *           The latest time to pull memories up to
    * @throws IOException
    *            if we can't update the memories
    */
   public abstract void updateMemories(long timestamp) throws IOException;
}
