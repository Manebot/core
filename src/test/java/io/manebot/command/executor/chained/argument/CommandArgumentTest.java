package io.manebot.command.executor.chained.argument;

import io.manebot.command.executor.chained.ChainState;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class CommandArgumentTest {

    protected ChainState empty() {
        return new ChainState(null, new ArrayList<>(), new ArrayList<>());
    }

    protected ChainState parse(String arguments) {
        return new ChainState(null, new ArrayList<>(Arrays.asList(arguments.split(" "))), new ArrayList<>());
    }

}
