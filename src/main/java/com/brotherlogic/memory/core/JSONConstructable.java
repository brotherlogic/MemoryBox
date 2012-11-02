package com.brotherlogic.memory.core;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Object can be constructed from JSON
 * 
 * @author simon
 * 
 */
public interface JSONConstructable
{
   /**
    * Build the object from its JSON representation
    * 
    * @param obj
    *           The object to build from
    * @return the version number of the resultant object
    * @throws JSONException
    *            If the JSON can't be parsed
    */
   int buildFromJSON(final JSONObject obj) throws JSONException;
}
