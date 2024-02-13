package edu.java.bot.command;

public abstract class AbstractCommand implements Command {
    public final String command;

    protected AbstractCommand(String command) {
        this.command = command;
    }

}
