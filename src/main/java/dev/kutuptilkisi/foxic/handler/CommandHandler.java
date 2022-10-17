package dev.kutuptilkisi.foxic.handler;

import dev.kutuptilkisi.foxic.annotations.Choice;
import dev.kutuptilkisi.foxic.annotations.Executor;
import dev.kutuptilkisi.foxic.annotations.Option;
import dev.kutuptilkisi.foxic.annotations.SlashCommand;
import dev.kutuptilkisi.foxic.data.CommandInfo;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CommandHandler extends ListenerAdapter implements ComponentHandler {
    private final HashMap<String, CommandInfo> commandMap = new HashMap<>();

    private final JDA jda;

    private final List<String> developerGuilds;
    public CommandHandler(JDA jda){
        this.jda = jda;
        developerGuilds = new ArrayList<>();
        jda.addEventListener(this);
    }

    /*
        Testing
     */

    public void addDeveloperGuilds(List<String> developerGuilds){
        for(String developerGuild : developerGuilds){
            this.addDeveloperGuild(developerGuild);
        }
    }

    public void addDeveloperGuild(String developerGuild){
        if(!developerGuilds.contains(developerGuild)){
            developerGuilds.add(developerGuild);
        }
    }

    public void addDeveloperGuildsLong(List<Long> developerGuilds){
        this.addDeveloperGuilds(developerGuilds.stream().map(String::valueOf).collect(Collectors.toList()));
    }

    public void addDeveloperGuildLong(long developerGuild){
        this.addDeveloperGuild(String.valueOf(developerGuild));
    }

    /*
        Registering
     */

    public void registerCommands(Object... objects){
        for(Object o : objects){
            registerCommand(o);
        }
    }

    public void registerCommand(Object object){
        if(!object.getClass().isAnnotationPresent(SlashCommand.class)){
            return;
        }
        CommandInfo data = getCommandInfo(object);
        commandMap.put(data.getAnnotation().label(), data);
        parseCommand(data);
    }

    /*
        Parsing
    */

    private void parseCommand(CommandInfo info){
        SlashCommandData data = Commands.slash(info.getAnnotation().label(), info.getAnnotation().description());
        data.addOptions(info.getOptions());
        data.setGuildOnly(info.getAnnotation().guildOnly());
        for(CommandInfo group : info.getSub()){

            if(group.getCallback() != null){
                data.addSubcommands(new SubcommandData(group.getAnnotation().label(), group.getAnnotation().description()).addOptions(group.getOptions()));
                continue;
            }

            SubcommandGroupData subcommandGroupData = new SubcommandGroupData(group.getAnnotation().label(), group.getAnnotation().description());

            for(CommandInfo subcommand : group.getSub()){
                subcommandGroupData.addSubcommands(new SubcommandData(subcommand.getAnnotation().label(), subcommand.getAnnotation().description()).addOptions(subcommand.getOptions()));
            }
            data.addSubcommandGroups(subcommandGroupData);
        }

        if(info.getAnnotation().developerMode()){
            for(String devGuild : developerGuilds){
                Guild target = jda.getGuildById(devGuild);
                if(target == null){
                    continue;
                }
                target.updateCommands().addCommands(data).queue();
            }
        } else {
            jda.updateCommands().addCommands(data).queue();
        }
    }

    private ArrayList<OptionData> parseOption(Method method){
        ArrayList<OptionData> data = new ArrayList<>();
        if(method == null) return data;
        for(Annotation[] annotations : method.getParameterAnnotations()){
            if(annotations.length == 0) continue;

            Option option = (Option) getAnnotationFromMethod(annotations, Option.class);
            if(option == null) continue;

            OptionData optionData = new OptionData(option.optionType(), option.name(), option.description(), option.required());
            for(Choice choice : option.choices()){
                optionData.addChoice(choice.name(), choice.value().equals("") ? choice.name() : choice.value());
            }
            data.add(optionData);
        }

        return data;
    }

    private CommandInfo getCommandInfo(Object object){
        SlashCommand slashCommandData = object.getClass().getAnnotation(SlashCommand.class);
        Method callback = getCallbackFromObject(object.getClass());
        CommandInfo data = new CommandInfo(object, slashCommandData, callback, new ArrayList<>(), parseOption(callback));

        for(Class<?> subGroup : object.getClass().getClasses()){
            if(!subGroup.isAnnotationPresent(SlashCommand.class)) continue;

            CommandInfo subgroupInfo = getSubcommandInfo(subGroup, object);

            for(Class<?> subCommand : subGroup.getClasses()){
                if(!subCommand.isAnnotationPresent(SlashCommand.class)) continue;

                CommandInfo subcommandInfo = getSubcommandInfo(subCommand, subgroupInfo.getObject());
                if(subcommandInfo.getCallback() != null) {
                    subgroupInfo.addSub(subcommandInfo);
                }

            }
            data.addSub(subgroupInfo);
        }

        return data;
    }

    private CommandInfo getSubcommandInfo(Class c, Object o){
        Object instance;
        try {
            instance = c.getConstructor(o.getClass()).newInstance(o);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        Method callback = getCallbackFromObject(c);

        return new CommandInfo(instance, instance.getClass().getAnnotation(SlashCommand.class), callback, new ArrayList<>(), parseOption(callback));
    }

    private Method getCallbackFromObject(Class c){
        for(Method m : c.getMethods()){
            if(m.isAnnotationPresent(Executor.class)){
                return m;
            }
        }
        return null;
    }

    private Annotation getAnnotationFromMethod(Annotation[] annotations, Class<?> annotationClass){
        for(Annotation annotation : annotations){
            if(annotation.annotationType().equals(annotationClass)){
                return annotation;
            }
        }
        return null;
    }

    /*
        Events & Event Fire
    */

    private Object parseOptionMapping(OptionMapping mapping){
        OptionType type = mapping.getType();

        return switch(type){
            case ROLE -> mapping.getAsRole();
            case ATTACHMENT -> mapping.getAsAttachment();
            case BOOLEAN -> mapping.getAsBoolean();
            case CHANNEL -> mapping.getAsChannel();
            case INTEGER -> mapping.getAsInt();
            case NUMBER -> mapping.getAsDouble();
            case MENTIONABLE -> mapping.getAsMentionable();
            case USER -> mapping.getAsMember();
            case STRING, UNKNOWN -> mapping.getAsString();
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e){
        String[] path = e.getCommandPath().split("/");
        CommandInfo info = commandMap.get(path[0]);
        if(path.length == 2){
            info = info.getSub(path[1]);
        }
        if(path.length == 3){
            info = info.getSub(path[2]);
        }

        List<OptionData> methodOptions = info.getOptions();

        Object[] args = new Object[methodOptions.size()+1];
        args[0] = e;
        for(int i=0; i<methodOptions.size(); i++){
            args[i+1] = parseOptionMapping(e.getOption(methodOptions.get(i).getName()));
        }

        try {
            info.getCallback().invoke(info.getObject(), args);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            System.out.println("Cant invoke callback. Command: "+e.getCommandPath() + "  |  Cause:"+ex.getCause());
        }
    }
}
