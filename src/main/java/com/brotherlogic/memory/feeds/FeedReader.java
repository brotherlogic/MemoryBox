package com.brotherlogic.memory.feeds;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.brotherlogic.memory.core.Memory;

/**
 * An abstraction of a feed reader
 * 
 * @author simon
 * 
 */
public abstract class FeedReader
{
   /** The logger to be used */
   private final Logger logger = Logger.getLogger("com.brotherlogic.memory.feeds.FeedReader");

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
    * Login to the system if necessary
    * 
    * @throws IOException
    *            If something goes wrong
    */
   protected abstract void login() throws IOException;

   /**
    * Updates all the feed
    * 
    * @throws IOException
    *            if we can't process the feed
    */
   public void update() throws IOException
   {
      logger.log(Level.INFO, "Logging in");
      login();
      updateAllMemories(true);
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
}
