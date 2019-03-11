package com.github.manevolent.jbot.command.executor.chained;

import com.github.manevolent.jbot.command.CommandSender;
import com.github.manevolent.jbot.command.exception.CommandExecutionException;
import com.github.manevolent.jbot.command.executor.chained.argument.ChainedCommandArgument;
import com.github.manevolent.jbot.command.executor.chained.argument.ChainedCommandArgumentNone;

import java.lang.annotation.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.LinkedList;

public abstract class ReflectiveCommandExecutor extends ChainedCommandExecutor {
    public ReflectiveCommandExecutor() {
        try {
            register();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Problem registering command methods on " + getClass().getName(), e);
        }
    }

    private void register() throws ReflectiveOperationException {
        for (Method method : getClass().getMethods()) {
            // Get Command annotation
            Command commandDefinition = method.getAnnotation(Command.class);
            if (commandDefinition == null) continue;

            // Argument parsing
            Collection<ChainedCommandArgument> arguments = new LinkedList<>();
            Parameter[] parameters = method.getParameters();
            Parameter commandSenderParameter = null;

            for (Parameter parameter : parameters) {
                if (commandSenderParameter == null) {
                    if (!parameter.getType().isAssignableFrom(CommandSender.class))
                        throw new ReflectiveOperationException(new IllegalArgumentException(
                                "first parameter of type " +
                                        parameter.getType().getName() + " is not assignable from " +
                                    CommandSender.class.getName()
                    ));

                    commandSenderParameter = parameter;

                    continue;
                }

                Argument argument = null;
                Annotation argumentAnnotation = null;
                for (Annotation parameterAnnotation : parameter.getAnnotations()) {
                    Argument parameterArgument = parameterAnnotation.annotationType().getAnnotation(Argument.class);

                    if (parameterArgument != null) {
                        if (argument != null)
                            throw new ReflectiveOperationException(
                                    "argument parameter annotation extending " + Argument.class.getName()
                                            + " already declared"
                            );

                        argument = parameterArgument;
                        argumentAnnotation = parameterAnnotation;
                    }
                }

                if (argument == null)
                    throw new ReflectiveOperationException(
                            "argument parameter annotation not found extending " + Argument.class.getName()
                    );

                Class<? extends ChainedCommandArgument> argumentClass = argument.type();

                Constructor<? extends ChainedCommandArgument> argumentConstructor = argumentClass.getConstructor(
                        argumentAnnotation.annotationType()
                );

                ChainedCommandArgument instance = argumentConstructor.newInstance(argumentAnnotation);

                arguments.add(instance);
            }

            if (arguments.size() <= 0) arguments.add(new ChainedCommandArgumentNone());

            CommandChain chain = withArguments(arguments);

            if (commandDefinition.description().length() > 0)
                chain.withDescription(commandDefinition.description());

            chain.setExecutor((sender, label, args) -> {
                try {
                    Object[] invocationArgs = new Object[args.length + 1];
                    System.arraycopy(args, 0, invocationArgs, 1, args.length);
                    invocationArgs[0] = sender;

                    method.setAccessible(true);
                    method.invoke(this, invocationArgs);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new CommandExecutionException(e);
                }
            });
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Command {
        String description() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    public @interface Argument {
        Class<? extends ChainedCommandArgument> type();
    }
}
