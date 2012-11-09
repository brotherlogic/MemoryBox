package com.brotherlogic.memory.core;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.brotherlogic.memory.db.DBFactory;

/**
 * Test case for fixing bugs
 * 
 * @author simon
 * 
 */
public class BugFixingTest extends DBTest
{
   @Test
   public void testGitRetrieve() throws IOException
   {
      long time = 1325456352000L;
      String branch = "magic";

      GitMemory mem1 = new GitMemory();
      mem1.setTimestamp(time);
      mem1.setBranch(branch);
      DBFactory.buildInterface().storeMemory(mem1);

      UntappdMemory mem2 = new UntappdMemory();
      mem2.setAbv(1.2);
      mem2.setAmount(100);
      mem2.setBeerName("Blah");
      mem2.setBreweryName("Donkey");
      mem2.setTimestamp(time);
      mem2.setImagePath("blah");
      DBFactory.buildInterface().storeMemory(mem2);

      GitMemory mem = (GitMemory) DBFactory.buildInterface().retrieveMemory(time,
            GitMemory.class.getName());
      Assert.assertNotNull("Branch is null", mem.getBranch());

      // Test the calendar retrieve
      Calendar cal = Calendar.getInstance();
      cal.setTimeInMillis(time);
      Collection<Memory> memories = DBFactory.buildInterface().retrieveMemories(cal,
            GitMemory.class.getName());

      Assert.assertTrue("Mismatch in memory count: " + memories.size(), memories.size() == 1);
      Assert.assertNotNull("Initial branch is null",
            ((GitMemory) memories.iterator().next()).getBranch());
   }

   /**
    * Tests that memory additions don't overlap
    * 
    * @throws IOException
    */
   @Test
   public void testMemoryOverlap() throws IOException
   {
      // Add two memories - this one has an annotated field (amount)
      UntappdMemory mem1 = new UntappdMemory();
      mem1.setTimestamp(10L);
      mem1.setBeerName("Donkey");
      mem1.setAmount(500);

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

      // Should have retained the amount information
      Assert.assertNotNull("Amount has been reset?", ((UntappdMemory) DBFactory.buildInterface()
            .retrieveMemories(UntappdMemory.class).iterator().next()).getAmount());
   }
}
