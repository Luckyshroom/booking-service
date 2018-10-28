/*
 * Copyright (c) 2014 Red Hat, Inc. and others
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package ru.doublegis.model;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link ru.doublegis.model.Order}.
 *
 * NOTE: This class has been automatically generated from the {@link ru.doublegis.model.Order} original class using Vert.x codegen.
 */
public class OrderConverter {

  public static void fromJson(JsonObject json, Order obj) {
    if (json.getValue("hallNum") instanceof Number) {
      obj.setHallNum(((Number)json.getValue("hallNum")).intValue());
    }
    if (json.getValue("holderEmail") instanceof String) {
      obj.setHolderEmail((String)json.getValue("holderEmail"));
    }
    if (json.getValue("holderName") instanceof String) {
      obj.setHolderName((String)json.getValue("holderName"));
    }
    if (json.getValue("seatNums") instanceof JsonArray) {
      obj.setSeatNums(((JsonArray)json.getValue("seatNums")).copy());
    }
  }

  public static void toJson(Order obj, JsonObject json) {
    json.put("hallNum", obj.getHallNum());
    if (obj.getHolderEmail() != null) {
      json.put("holderEmail", obj.getHolderEmail());
    }
    if (obj.getHolderName() != null) {
      json.put("holderName", obj.getHolderName());
    }
    if (obj.getSeatNums() != null) {
      json.put("seatNums", obj.getSeatNums());
    }
  }
}