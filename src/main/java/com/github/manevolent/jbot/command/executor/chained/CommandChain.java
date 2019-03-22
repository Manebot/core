package com.github.manevolent.jbot.command.executor.chained;

import com.github.manevolent.jbot.command.executor.chained.argument.CommandArgument;

import java.util.LinkedList;
import java.util.List;

public class CommandChain {
    private final CommandArgument argument;
    private final List<CommandChain> children = new LinkedList<>();

    private String description = null;
    private CommandChain parent = null;
    private ChainExecutor executor;

    public CommandChain(CommandArgument argument) {
        this.argument = argument;
    }

    public CommandArgument getArgument() {
        return argument;
    }

    public void addChild(CommandChain chain) {
        if (argument != null && !argument.canExtend(chain.getArgument()))
            throw new IllegalArgumentException(
                    "cannot extend " + argument.getClass().getName() +
                    " with " + chain.getClass().getName()
            );

        for (CommandChain child : children) {
            if (!child.getArgument().canCoexist(chain.getArgument()) ||
                    !chain.getArgument().canCoexist(child.getArgument()))
                throw new IllegalArgumentException("argument " + child.getArgument().getClass().getName() +
                        " cannot co-exist with " + chain.getArgument().getClass().getName());
        }

        chain.parent = this;
        children.add(chain);
    }

    public boolean removeChild(CommandChain chain) {
        return children.remove(chain);
    }

    public List<CommandChain> getChildren() {
        return children;
    }


    public ChainExecutor getExecutor() {
        return executor;
    }

    public CommandChain setExecutor(ChainExecutor executor) {
        this.executor = executor;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CommandChain withDescription(String description) {
        this.description = description;
        return this;
    }

    public CommandChain getParent() {
        return parent;
    }
}
