package com.brotherlogic.memory.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * Class to do some basic testing
 * 
 * @author simon
 * 
 */
public class BasicTest extends DBTest
{
   /**
    * Tests that the isFilled property works correctly
    */
   @Test
   public void testUnfilled()
   {
      Memory mem = new Memory()
      {

      };

      // This should fail since our blank memory has no timestamp
      Assert.assertFalse("Memory is filled when it isn't", mem.isFilled());

      // Fill the memory
      mem.setTimestamp(10L);

      // This should now pass
      Assert.assertTrue("Memory is indicating it isn't filled", mem.isFilled());

   }
}
