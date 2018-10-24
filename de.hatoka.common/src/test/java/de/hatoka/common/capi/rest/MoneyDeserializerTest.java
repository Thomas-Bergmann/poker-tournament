package de.hatoka.common.capi.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hatoka.common.capi.math.Money;

class MoneyDeserializerTest
{
    public static class MoneyContainer
    {
        private String name;
        private Money amount;
        public String getName()
        {
            return name;
        }
        public void setName(String name)
        {
            this.name = name;
        }
        public Money getAmount()
        {
            return amount;
        }
        public void setAmount(Money amount)
        {
            this.amount = amount;
        }
    }

    @Test
    void test() throws JsonMappingException, JsonProcessingException
    {
        String json = "{ \"name\":\"myname\", \"amount\":\"10 EUR\"}";
        MoneyContainer resolved = new ObjectMapper().readValue(json, MoneyContainer.class);
        assertEquals(Money.valueOf("10 EUR"), resolved.amount);
        assertEquals("myname", resolved.name);
    }

}
