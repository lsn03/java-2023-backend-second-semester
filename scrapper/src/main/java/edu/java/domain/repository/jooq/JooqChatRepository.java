package edu.java.domain.repository.jooq;

import edu.java.domain.jooq.tables.Chat;
import edu.java.domain.model.ChatDto;
import edu.java.domain.repository.ChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

@RequiredArgsConstructor
public class JooqChatRepository implements ChatRepository {
    private final DSLContext dslContext;

    @Override

    public void add(Long tgChatId) {

        if (findInActiveUserById(tgChatId)) {
            dslContext.update(Chat.CHAT)
                .set(Chat.CHAT.ACTIVE, true)
                .where(Chat.CHAT.CHAT_ID.eq(tgChatId))
                .execute();
        } else {
            dslContext.insertInto(Chat.CHAT, Chat.CHAT.CHAT_ID)
                .values(tgChatId)
                .execute();
        }

    }

    @Override

    public void remove(Long tgChatId) {
        dslContext.update(Chat.CHAT)
            .set(Chat.CHAT.ACTIVE, false)
            .where(Chat.CHAT.CHAT_ID.eq(tgChatId))
            .execute();
    }

    @Override
    public List<ChatDto> findAll() {
        return dslContext.selectFrom(Chat.CHAT)
            .fetchInto(ChatDto.class);
    }

    protected boolean findInActiveUserById(Long tgChatId) {
        return dslContext.selectFrom(Chat.CHAT)
            .where(Chat.CHAT.CHAT_ID.eq(tgChatId))
            .and(Chat.CHAT.ACTIVE.isFalse())
            .fetchOptional()
            .isPresent();
    }
}
