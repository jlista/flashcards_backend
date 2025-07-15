package com.flashcards.repository;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.flashcards.model.Card;
import com.mongodb.BasicDBObject;

class CardRepositoryCustomImpl implements CardRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Card getOneCardSR(){

    

        //String queryStr = "{mastery_level: %d, last_correct: {$lt: %s}}";
        String queryStr = "{mastery_level: %d}";

        // Date startDate = Date.from(Instant.now().minus(2, ChronoUnit.DAYS));
        // Query query = new Query();
        // query.addCriteria(Criteria.where("last_correct").lt(startDate));

        //String s = "{\"$or\": [ {\"name\": \"buzz\"}, {\"age\": {\"$lt\": 20 }} ] }";
        //Bson bson =  BasicDBObject.parse(s);
        String m0_date = MongoHelper.isoDateFormat(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)));
        String f = String.format(queryStr, 1);
        BasicQuery query = new BasicQuery(f);
        List<Card> cardChoices_m0 =  mongoTemplate.find(query, Card.class);

        String m1_date = MongoHelper.isoDateFormat(Date.from(Instant.now()));
        String f1 = String.format(queryStr, 0, m1_date);
        query = new BasicQuery(f1);
        List<Card> cardChoices_m1 =  mongoTemplate.find(query, Card.class);

        String m2_date = MongoHelper.isoDateFormat(Date.from(Instant.now().minus(2, ChronoUnit.DAYS)));
        query = new BasicQuery(String.format(queryStr, 2, m2_date));
        List<Card> cardChoices_m2 =  mongoTemplate.find(query, Card.class);

        String m3_date = MongoHelper.isoDateFormat(Date.from(Instant.now().minus(5, ChronoUnit.DAYS)));
        query = new BasicQuery(String.format(queryStr, 5, m3_date));
        List<Card> cardChoices_m3 =  mongoTemplate.find(query, Card.class);
        
        String m4_date = MongoHelper.isoDateFormat(Date.from(Instant.now().minus(10, ChronoUnit.DAYS)));
        query = new BasicQuery(String.format(queryStr, 10, m4_date));
        List<Card> cardChoices_m4 =  mongoTemplate.find(query, Card.class);

        List<Card> allCardChoices = new ArrayList<>();
        Stream.of(cardChoices_m0, cardChoices_m1, cardChoices_m2, cardChoices_m3, cardChoices_m4).flatMap(Collection::stream).toList();
        Random rand = new Random();
        return allCardChoices.get(rand.nextInt(allCardChoices.size()));
    }
  
}