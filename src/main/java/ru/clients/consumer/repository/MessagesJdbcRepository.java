package ru.clients.consumer.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.clients.consumer.models.Message;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MessagesJdbcRepository implements MessagesRepository {

    private static final String INSERT_MESSAGE_QUERY = "INSERT INTO messages (received_at, delay, payload, in_progressed)" +
            " VALUES (?, ?, ?, ?)";

    private static final String SELECT_BEFORE_DATA = "SELECT payload FROM messages" +
            " WHERE in_progressed = false AND received_at + INTERVAL '1 millisecond' * delay < ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(Message message) {
        log.info("save + " + message.getReceivedAt());
        jdbcTemplate.update(INSERT_MESSAGE_QUERY,message.getReceivedAt(), message.getDelay(), message.getPayload().toString(), false);
    }

    @Override
    public List<String> findByIsProcessedFalseAndTimeBefore(LocalDateTime time) {
        List<String> result =  jdbcTemplate.queryForList(SELECT_BEFORE_DATA, new Object[]{time}, String.class );
        updateIsProcessed(time);
        return result;
    }

    private void updateIsProcessed(LocalDateTime time) {
        jdbcTemplate.execute((Connection con) -> {
            try (CallableStatement callableStatement = con.prepareCall("call update_is_processed(?)")) {
                callableStatement.setTimestamp(1, Timestamp.valueOf(time));
                callableStatement.execute();
            }
            return null;
        });
    }

    public void deleteSentMessages() {
        jdbcTemplate.execute((Connection con) -> {
            try (CallableStatement callableStatement = con.prepareCall("call delete_sent_messages()")) {
                callableStatement.execute();
            }
            return null;
        });
    }
}
