package com.brotherlogic.memory.core;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.brotherlogic.memory.db.BaseRepStore;
import com.brotherlogic.memory.db.DBFactory;

/**
 * Test class for the base rep
 * 
 * @author simon
 * 
 */
public class BaseRepTest extends DBTest
{
   /**
    * Tests that we can store and retrieve the base rep correctly
    * 
    * @throws IOException
    *            if something goes wrong with storage
    */
   @Test
   public void testBaseRep() throws IOException
   {
      UntappdMemory mem = new UntappdMemory();
      mem.setBeerName("Magic");
      mem.setImagePath("blah");
      mem.setTimestamp(10L);

      String baseRep = "blah blah blah";

      BaseRepStore store = DBFactory.buildBaseRepStore();
      store.storeBaseRep(mem, baseRep);
      String newRep = store.getBaseRep(mem);

      Assert.assertEquals("Base Rep Mismatch", newRep, baseRep);
   }
}
