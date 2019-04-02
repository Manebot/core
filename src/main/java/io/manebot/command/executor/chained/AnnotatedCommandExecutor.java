package io.manebot.command.executor.chained;

import io.manebot.command.CommandSender;
import io.manebot.command.exception.CommandExecutionException;
import io.manebot.command.executor.chained.argument.CommandArgument;
import io.manebot.command.executor.chained.argument.CommandArgumentNone;
import io.manebot.security.Grant;
import io.manebot.security.Permission;
import io.manebot.virtual.Virtual;

import java.lang.annotation.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.LinkedList;

public abstract class AnnotatedCommandExecutor extends ChainedCommandExecutor {
    public AnnotatedCommandExecutor() {
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
            Collection<CommandArgument> arguments = new LinkedList<>();
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

                Class<? extends CommandArgument> argumentClass = argument.type();

                Constructor<? extends CommandArgument> argumentConstructor = argumentClass.getConstructor(
                        argumentAnnotation.annotationType()
                );

                CommandArgument instance = argumentConstructor.newInstance(argumentAnnotation);

                arguments.add(instance);
            }

            if (arguments.size() <= 0) arguments.add(new CommandArgumentNone());

            CommandChain chain = withArguments(arguments);

            if (commandDefinition.description().length() > 0)
                chain.withDescription(commandDefinition.description());

            chain.setExecutor((sender, label, args) -> {
                try {
                    if (commandDefinition.permission().length() > 0) {
                        Permission.checkPermission(commandDefinition.permission(), commandDefinition.defaultGrant());
                    }

                    Object[] invocationArgs = new Object[args.length + 1];
                    System.arraycopy(args, 0, invocationArgs, 1, args.length);
                    invocationArgs[0] = sender;

                    method.setAccessible(true);
                    method.invoke(this, invocationArgs);
                } catch (IllegalAccessException e) {
                    throw new CommandExecutionException(e);
                } catch (InvocationTargetException ite) {
                    throw new CommandExecutionException(ite.getTargetException());
                }
            });
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Command {
        /**
         * Description of this command, used in command help.
         * @return description.
         */
        String description() default "";

        /**
         * Permission to check for when this command is execute.
         * @return permission node, blank for no permission checking.
         */
        String permission() default "";

        /**
         * Default grant behavior of <b>permission</b> property.  When set to Grant.DENY, permission is assumed
         * denied when no permission is set on the user.  When set to Grant.ALLOW, command execution is allowed unless
         * the user explicitly has a defined Grant.DENY permission grant matching the command's required node.
         * @return default grant behavior.
         */
        Grant defaultGrant() default Grant.DENY;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    public @interface Argument {
        Class<? extends CommandArgument> type();
    }
}
