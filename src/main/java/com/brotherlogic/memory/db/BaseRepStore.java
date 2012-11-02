package com.brotherlogic.memory.db;

import java.io.IOException;

import com.brotherlogic.memory.core.Memory;

/**
 * The base rep store stores the underlying representation of each object in a
 * neat store
 * 
 * @author simon
 * 
 */
public abstract class BaseRepStore
{
   /**
    * Gets a base rep for a given memory
    * 
    * @param me
    *           The memory to retrieve the base rep for
    * @return The String representation of the base rep
    * @throws IOException
    *            if we can't retrieve for this given memory
    */
   public abstract String getBaseRep(Memory me) throws IOException;

   /**
    * Stores the base rep somewhere
    * 
    * @param mem
    *           The memory to store for
    * @param baseRep
    *           the underlying representation
    * @throws IOException
    *            if we can't store this data
    */
   public abstract void storeBaseRep(Memory mem, String baseRep) throws IOException;
}
