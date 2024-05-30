package ru.clients.consumer.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KafkaMessage {

    private Status status;

    private String message;

    public KafkaMessage(String message, long time) {
        this.message = message;
        this.status = getStatus(time);
    }

    private Status getStatus(long time) {
        if (time < 2000) {
            return Status.FAST;
        } else if (time < 5000) {
            return Status.MEDIUM;
        } else {
            return Status.LONG;
        }
    }

    private enum Status {
        FAST,
        MEDIUM,
        LONG
    }

    @Override
    public String toString() {
        return "status=" + status +
                ", message='" + message + '\'';
    }
}
