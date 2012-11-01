package com.brotherlogic.memory.db;

/**
 * A factory for producing databases
 * 
 * @author simon
 * 
 */
public final class DBFactory
{
   /**
    * The mode in which we are running test/prod
    * 
    * @author simon
    * 
    */
   public enum Mode
   {
      /** Production Mode */
      PRODUCTION,

      /** Testing Mode */
      TESTING;
   }

   private static BaseRepStore baseRepStore = null;

   /** The singleton interface currently in use */
   private static DBInterface currInterface = null;

   /** The current mode we're operating in */
   private static Mode currMode = Mode.PRODUCTION;

   public static BaseRepStore buildBaseRepStore()
   {
      if (baseRepStore == null)
         baseRepStore = new MongoBaseRepStore(currMode);
      return baseRepStore;
   }

   /**
    * Build the database interface
    * 
    * @return a valid functional DBInterface
    */
   public static DBInterface buildInterface()
   {
      if (currInterface == null)
         currInterface = getMongoInterface();
      return currInterface;
   }

   protected static Mode getMode()
   {
      return currMode;
   }

   /**
    * Gets a Mongo interface as a DB Interface
    * 
    * @return The DBInterface for a mongo representation
    */
   private static DBInterface getMongoInterface()
   {
      return new MongoInterface();
   }

   /**
    * Set the mode of operation
    * 
    * @param newMode
    *           The new mode to operate in - will cause DB disconnect
    */
   public static void setMode(final Mode newMode)
   {
      currMode = newMode;

      // Reset the current interfaces
      currInterface = null;
   }

   /**
    * Blocking constructor
    */
   private DBFactory()
   {

   }
}
