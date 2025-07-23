package com.flashcards.repository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;

import com.flashcards.CardHelper;
import com.flashcards.model.Card;

class CardRepositoryCustomImpl implements CardRepositoryCustom {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Card> getAllPossibleCards() {

        String outerQueryStr = "{$or: [%s,%s,%s,%s,%s,%s]}";
        String innerQueryStr = "{mastery_level: %d}";
        String innerQueryStrWithDate = "{mastery_level: %d, last_correct: {$lte: %s}}";

        String m1_date =
                CardHelper.getIsoDateFormat(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)));
        String m2_date =
                CardHelper.getIsoDateFormat(Date.from(Instant.now().minus(2, ChronoUnit.DAYS)));
        String m3_date =
                CardHelper.getIsoDateFormat(Date.from(Instant.now().minus(5, ChronoUnit.DAYS)));
        String m4_date =
                CardHelper.getIsoDateFormat(Date.from(Instant.now().minus(10, ChronoUnit.DAYS)));

        String q0 = String.format(innerQueryStr, 0);
        String q1 = String.format(innerQueryStrWithDate, 1, m1_date);
        String q2 = String.format(innerQueryStrWithDate, 2, m2_date);
        String q3 = String.format(innerQueryStrWithDate, 3, m3_date);
        String q4 = String.format(innerQueryStrWithDate, 4, m4_date);
        String q5 = "{last_correct: null}";

        String combinedQuery = String.format(outerQueryStr, q0, q1, q2, q3, q4, q5);
        BasicQuery query = new BasicQuery(combinedQuery);
        return mongoTemplate.find(query, Card.class);
    }
}
