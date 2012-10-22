package com.brotherlogic.memory.core;

import java.io.IOException;

import junit.framework.TestCase;

import com.brotherlogic.memory.db.DBFactory;
import com.brotherlogic.memory.db.DBInterface;

public class UntappdTest extends TestCase
{
   @Override
   public void setUp() throws IOException
   {
      // Clear the db
      DBFactory.buildInterface().clear();
   }

   public void testStoreUntappd() throws IOException
   {
      // Create an untapped object
      UntappdMemory untapd = new UntappdMemory();
      untapd.setTimestamp(12345L);
      untapd.setImagePath("/usr/blah/void/");

      // Store it in the given DB
      DBInterface db = DBFactory.buildInterface();
      db.storeMemory(untapd);

      // Retrieve it
      UntappdMemory mem = (UntappdMemory) db.retrieveMemory(12345L, untapd.getClass().getName());
      assertTrue("Stored object does not match the object we stored!", untapd.equals(mem));
   }
}
