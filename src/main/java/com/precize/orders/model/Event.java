package com.precize.orders.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public abstract class Event {
    private final String eventId;
    @JsonProperty("timestamp")
    private final Instant timeStamp;
    private final String eventType;

    public Event(String eventId, Instant timeStamp, String eventType) {
        this.eventId = eventId;
        this.timeStamp = timeStamp;
        this.eventType = eventType;
    }

    public String getEventId() {
        return eventId;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public String getEventType() {
        return eventType;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId='" + eventId + '\'' +
                ", timeStamp=" + timeStamp +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
