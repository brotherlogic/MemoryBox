package com.brotherlogic.memory.db;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import com.brotherlogic.memory.core.Memory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * Mongo table for storing the base reps of the objects
 * 
 * @author simon
 * 
 */
public class MongoBaseRepStore extends BaseRepStore
{
   /** The collection where the base rep is stored */
   private DBCollection baseCollection;

   /**
    * Connect to the database
    * 
    * @throws IOException
    *            If we can't reach the database
    */
   private void connect() throws IOException
   {
      if (baseCollection == null)
      {
         MongoInterface inter = new MongoInterface();
         baseCollection = inter.getCollection("BaseRep");
      }
   }

   @Override
   public String getBaseRep(final Memory mem) throws IOException
   {
      connect();

      BasicDBObject query = new BasicDBObject();
      query.put("memtype", mem.getClass().getName());
      query.put("time", mem.getTimestamp());

      DBObject cursor = baseCollection.findOne(query);

      if (cursor != null)
         return (String) cursor.get("baserep");
      else
         return null;
   }

   @Override
   public Collection<String> getBaseRep(final String className) throws IOException
   {
      connect();

      BasicDBObject query = new BasicDBObject();
      query.put("memtype", className);

      Collection<String> bases = new LinkedList<String>();
      DBCursor cursor = baseCollection.find(query);
      while (cursor.hasNext())
         bases.add((String) cursor.next().get("baserep"));

      return bases;
   }

   @Override
   public void storeBaseRep(final Memory mem, final String baseRep) throws IOException
   {
      connect();

      BasicDBObject store = new BasicDBObject();
      store.put("memtype", mem.getClass().getName());
      store.put("time", mem.getTimestamp());
      store.put("baserep", baseRep);

      baseCollection.insert(store);
   }
}
