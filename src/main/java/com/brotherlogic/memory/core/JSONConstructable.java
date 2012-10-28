package com.brotherlogic.memory.core;

import org.json.JSONException;
import org.json.JSONObject;

public interface JSONConstructable
{
   public int buildFromJSON(JSONObject obj) throws JSONException;
}
