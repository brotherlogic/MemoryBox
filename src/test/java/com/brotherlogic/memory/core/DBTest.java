package com.brotherlogic.memory.core;

import java.io.IOException;

import org.junit.Before;

import com.brotherlogic.memory.db.DBFactory;
import com.brotherlogic.memory.db.DBFactory.Mode;

/**
 * Tests the DB functionality
 * 
 * @author simon
 * 
 */
public class DBTest
{
   /**
    * Prepares for the test to run
    * 
    * @throws IOException
    *            if we can't clear the database
    */
   @Before
   public final void setUp() throws IOException
   {
      DBFactory.setMode(Mode.TESTING);
      DBFactory.buildInterface().clear();
   }
}
