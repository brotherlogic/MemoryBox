package com.brotherlogic.memory.core;

import java.io.IOException;

import org.junit.Assert;

import com.brotherlogic.memory.feeds.GitEventFeedReader;
import com.brotherlogic.memory.feeds.UntappdFeedReader;
import com.brotherlogic.memory.feeds.discogs.DiscogsFeedReader;

/**
 * Tests that we can access the relevant feeds
 * 
 * @author simon
 * 
 */
public class FeedTest extends DBTest
{
   /**
    * Tests that we can reach the discogs feed
    * 
    * @throws IOException
    *            if something goes wrong
    */
   public void testDiscogsFeed() throws IOException
   {
      DiscogsMemory mem = (DiscogsMemory) new DiscogsFeedReader().probeFeed();

      // Memory should be correctly filled
      Assert.assertTrue("Discogs Memory is not correctly filled", mem.isFilled());
      System.out.println(mem.getUniqueID());
   }

   /**
    * Tests that we can read the git feed
    * 
    * @throws IOException
    *            if something goes wrong
    */
   public void testGitFeed() throws IOException
   {
      GitMemory mem = (GitMemory) new GitEventFeedReader("brotherlogic").probeFeed();

      // Memory should be correctly filled
      Assert.assertTrue("Git Memory is not correctly filled", mem.isFilled());
      System.out.println(mem.getUniqueID());
   }

   /**
    * Tests that we can reach the untappd feed
    * 
    * @throws IOException
    *            If something goes wrong
    */
   public void testUntappdFeed() throws IOException
   {
      UntappdMemory mem = (UntappdMemory) new UntappdFeedReader("brotherlogic").probeFeed();

      // Memory should be correctly filled
      Assert.assertTrue("Untappd Memory is not correctly filled", mem.isFilled());
      System.out.println(mem.getUniqueID());
   }
}
