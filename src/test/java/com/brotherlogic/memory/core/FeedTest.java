package com.brotherlogic.memory.core;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.brotherlogic.memory.feeds.GitEventFeedReader;
import com.brotherlogic.memory.feeds.UntappdFeedReader;
import com.brotherlogic.memory.feeds.discogs.DiscogsFeedReader;

public class FeedTest extends DBTest
{
   @Test
   public void testDiscogsFeed() throws IOException
   {
      DiscogsMemory mem = (DiscogsMemory) new DiscogsFeedReader().probeFeed();

      // Memory should be correctly filled
      Assert.assertTrue("Discogs Memory is not correctly filled", mem.isFilled());
      System.out.println(mem.getUniqueID());
   }

   @Test
   public void testGitFeed() throws IOException
   {
      GitMemory mem = (GitMemory) new GitEventFeedReader("brotherlogic").probeFeed();

      // Memory should be correctly filled
      Assert.assertTrue("Git Memory is not correctly filled", mem.isFilled());
      System.out.println(mem.getUniqueID());
   }

   @Test
   public void testUntappdFeed() throws IOException
   {
      UntappdMemory mem = (UntappdMemory) new UntappdFeedReader("brotherlogic").probeFeed();

      // Memory should be correctly filled
      Assert.assertTrue("Untappd Memory is not correctly filled", mem.isFilled());
      System.out.println(mem.getUniqueID());
   }
}
