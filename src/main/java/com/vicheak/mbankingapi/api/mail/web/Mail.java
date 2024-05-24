package com.vicheak.mbankingapi.api.mail.web;

import lombok.Data;

@Data
public class Mail<T> {
   private String subject;
   private String sender;
   private String receiver;
   private String text;
   private T metaData;
}
