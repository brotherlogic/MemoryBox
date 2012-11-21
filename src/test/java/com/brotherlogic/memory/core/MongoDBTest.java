package com.brotherlogic.memory.core;

import java.io.IOException;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.brotherlogic.memory.db.DBFactory;
import com.brotherlogic.memory.db.DBInterface;
import com.brotherlogic.memory.db.MongoInterface;
import com.brotherlogic.memory.feeds.FeedReader;
import com.brotherlogic.memory.feeds.GitEventFeedReader;

/**
 * Class to test our link with Mongo
 * 
 * @author simon
 * 
 */
public class MongoDBTest extends DBTest
{
   /**
    * Test to see if we can get the latest memory correctly
    * 
    * @throws IOException
    *            if DB link goes wrong
    */
   @Test
   public void testGetLatestMemory() throws IOException
   {
      // Add a memory
      UntappdMemory memory = new UntappdMemory();
      memory.setImagePath("/blah");
      memory.setTimestamp(10L);
      memory.setBeerName("IPA");
      memory.setBreweryName("Magic");
      memory.setUniqueID("uid1");
      DBFactory.buildInterface().storeMemory(memory);

      // Check that the latest memory is this one
      Memory mem = DBFactory.buildInterface().retrieveLatestMemory(UntappdMemory.class);
      Assert.assertNotNull("No object retrieved", mem);
      Assert.assertEquals("Stored object not retrieved", memory, mem);

      // Store a different memory
      DiscogsMemory discogs = new DiscogsMemory();
      discogs.setImagePath("/blah2");
      discogs.setTimestamp(5L);
      discogs.setArtist("David Bowie");
      discogs.setUniqueID("uid2");
      DBFactory.buildInterface().storeMemory(discogs);

      // Check that we can retrieve both the latest versions
      Assert.assertEquals("Cannot retrieve Discogs memory", discogs, DBFactory.buildInterface()
            .retrieveLatestMemory(DiscogsMemory.class));
      Assert.assertEquals("Cannot retrieve Untappd memory", memory, DBFactory.buildInterface()
            .retrieveLatestMemory(UntappdMemory.class));

      // Add in a newer Untappd
      UntappdMemory untappd2 = new UntappdMemory();
      untappd2.setImagePath("/blah3");
      untappd2.setTimestamp(14L);
      untappd2.setBeerName("IPA");
      untappd2.setUniqueID("uid3");
      DBFactory.buildInterface().storeMemory(untappd2);

      // We should now get the newer one
      Assert.assertEquals("Retrieved wrong Untappd memory", untappd2, DBFactory.buildInterface()
            .retrieveLatestMemory(UntappdMemory.class));
   }

   @Test
   public void testReaderStoreAndRetrieve() throws IOException
   {
      DBInterface inter = new MongoInterface();
      inter.followMemory(GitMemory.class, GitEventFeedReader.class, "brotherlogic");
      Collection<FeedReader> readers = inter.getMemoryReaders();

      Assert.assertEquals("Not enough readers returned", readers.size(), 1);
      Assert.assertEquals("Wrong reader class", readers.iterator().next().getClass(),
            GitEventFeedReader.class);
   }
}
