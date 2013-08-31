package com.opam.deserializers;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.opam.request.types.Account;

public class AccountDeserializer implements JsonDeserializer<Account>{

	@Override
	public Account deserialize(JsonElement json, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		Gson g = new GsonBuilder().create();
		Account acc = g.fromJson(json, Account.class);
		return acc;
	}

}
