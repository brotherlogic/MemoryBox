package com.brotherlogic.memory.db;

import java.io.IOException;

import com.brotherlogic.memory.core.Memory;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class MongoBaseRepStore extends BaseRepStore
{
   private DBCollection baseCollection;
   private DBFactory.Mode mode;

   public MongoBaseRepStore(DBFactory.Mode mode)
   {

   }

   private void connect() throws IOException
   {
      if (baseCollection == null)
      {
         MongoInterface inter = new MongoInterface();
         baseCollection = inter.getCollection("BaseRep");
      }
   }

   @Override
   public String getBaseRep(Memory mem) throws IOException
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
   public void storeBaseRep(Memory mem, String baseRep) throws IOException
   {
      connect();

      BasicDBObject store = new BasicDBObject();
      store.put("memtype", mem.getClass().getName());
      store.put("time", mem.getTimestamp());
      store.put("baserep", baseRep);

      baseCollection.insert(store);
   }
}
