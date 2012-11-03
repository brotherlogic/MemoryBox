package com.brotherlogic.memory.core;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.brotherlogic.memory.db.DBFactory;

public class BugFixing extends DBTest
{
   /**
    * Tests that memory additions don't overlap
    * 
    * @throws IOException
    */
   @Test
   public void testMemoryOverlap() throws IOException
   {
      // Add two memories
      UntappdMemory mem1 = new UntappdMemory();
      mem1.setTimestamp(10L);
      mem1.setBeerName("Donkey");

      UntappdMemory mem2 = new UntappdMemory();
      mem2.setTimestamp(10L);
      mem2.setBeerName("Donkey");
      mem2.setAbv(5.2);

      // Add the first memory to the DB
      DBFactory.buildInterface().storeMemory(mem1);
      Assert.assertTrue("Extra memory crept in somewhere: "
            + DBFactory.buildInterface().retrieveMemories(UntappdMemory.class).size(), DBFactory
            .buildInterface().retrieveMemories(UntappdMemory.class).size() == 1);

      // Should have null abv
      Assert.assertNull("ABV has been set somehow", ((UntappdMemory) DBFactory.buildInterface()
            .retrieveMemories(UntappdMemory.class).iterator().next()).getAbv());

      // Add the second memory to the DB
      DBFactory.buildInterface().storeMemory(mem2);
      Assert.assertTrue("New memory has been added in addition to old", DBFactory.buildInterface()
            .retrieveMemories(UntappdMemory.class).size() == 1);

      // Should have added the ABV data
      Assert.assertNotNull("ABV has not been set", ((UntappdMemory) DBFactory.buildInterface()
            .retrieveMemories(UntappdMemory.class).iterator().next()).getAbv());
   }
}
