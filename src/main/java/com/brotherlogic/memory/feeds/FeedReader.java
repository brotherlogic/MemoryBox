package com.brotherlogic.memory.feeds;

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
   /**
    * Probes the feed - returns the latest memory
    * 
    * @return The latest Memory present in the feed
    */
   public abstract Memory probeFeed();

   /**
    * Update all the memories - refresh the memories
    */
   public abstract void updateAllMemories();

   /**
    * Update the memories up to the latest version
    * 
    * @param timestamp
    *           The latest time to pull memories up to
    */
   public abstract void updateMemories(long timestamp);

   /**
    * Updates all the feed
    */
   public void update()
   {
      Memory mem = probeFeed();
      Memory topDBMem = DBFactory.buildInterface().retrieveLatestMemory(mem.getClass());

      // Check on the versions
      if (mem.getVersion() != topDBMem.getVersion())
         updateAllMemories();
      else if (mem.getTimestamp() > topDBMem.getTimestamp())
         updateMemories(topDBMem.getTimestamp());
   }
}
