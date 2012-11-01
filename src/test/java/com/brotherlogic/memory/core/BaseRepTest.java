package com.brotherlogic.memory.core;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.brotherlogic.memory.db.BaseRepStore;
import com.brotherlogic.memory.db.DBFactory;

public class BaseRepTest extends DBTest
{
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
