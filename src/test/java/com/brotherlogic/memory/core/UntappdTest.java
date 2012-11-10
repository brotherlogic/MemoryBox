package com.brotherlogic.memory.core;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.brotherlogic.memory.db.DBFactory;
import com.brotherlogic.memory.db.DBInterface;

/**
 * Testing that we can handle Untappd stuff correctly
 * 
 * @author simon
 * 
 */
public class UntappdTest
{

   /**
    * Tests that we can store and retrieve Untappd objects
    * 
    * @throws IOException
    *            if we can't read from the database
    */
   @Test
   public final void testStoreUntappd() throws IOException
   {
      // Create an untapped object
      UntappdMemory untapd = new UntappdMemory();
      untapd.setTimestamp(12345L);
      untapd.setImagePath("/usr/blah/void/");

      // Store it in the given DB
      DBInterface db = DBFactory.buildInterface();
      db.storeMemory(untapd);

      // Retrieve it
      UntappdMemory mem = (UntappdMemory) db.retrieveMemory(untapd.getUniqueID(), untapd.getClass()
            .getName());
      Assert.assertTrue("Stored object does not match the object we stored!", untapd.equals(mem));
   }
}
