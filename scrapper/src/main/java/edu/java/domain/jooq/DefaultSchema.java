/*
 * This file is generated by jOOQ.
 */
package edu.java.domain.jooq;


import edu.java.domain.jooq.tables.Chat;
import edu.java.domain.jooq.tables.GithubCommit;
import edu.java.domain.jooq.tables.Link;
import edu.java.domain.jooq.tables.LinkChat;
import edu.java.domain.jooq.tables.StackoverflowAnswer;

import java.util.Arrays;
import java.util.List;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class DefaultSchema extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DEFAULT_SCHEMA</code>
     */
    public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();

    /**
     * The table <code>CHAT</code>.
     */
    public final Chat CHAT = Chat.CHAT;

    /**
     * The table <code>GITHUB_COMMIT</code>.
     */
    public final GithubCommit GITHUB_COMMIT = GithubCommit.GITHUB_COMMIT;

    /**
     * The table <code>LINK</code>.
     */
    public final Link LINK = Link.LINK;

    /**
     * The table <code>LINK_CHAT</code>.
     */
    public final LinkChat LINK_CHAT = LinkChat.LINK_CHAT;

    /**
     * The table <code>STACKOVERFLOW_ANSWER</code>.
     */
    public final StackoverflowAnswer STACKOVERFLOW_ANSWER = StackoverflowAnswer.STACKOVERFLOW_ANSWER;

    /**
     * No further instances allowed
     */
    private DefaultSchema() {
        super("", null);
    }


    @Override
    @NotNull
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    @NotNull
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Chat.CHAT,
            GithubCommit.GITHUB_COMMIT,
            Link.LINK,
            LinkChat.LINK_CHAT,
            StackoverflowAnswer.STACKOVERFLOW_ANSWER
        );
    }
}
