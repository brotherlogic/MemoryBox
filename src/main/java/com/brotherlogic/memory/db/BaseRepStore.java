package com.brotherlogic.memory.db;

import java.io.IOException;
import java.util.Collection;

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
    * Gets the base reps for a class of memories
    * 
    * @param className
    *           The name of the class to get memories for
    * @return A {@link Collection} of String base reps
    * @throws IOException
    *            if we can't access the database
    */
   public abstract Collection<String> getBaseRep(String className) throws IOException;

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
