# Foxic
## A Simple Handler For Both Interactions In JDA

[![](https://jitpack.io/v/TunayAdaKaracan/foxic.svg)](https://jitpack.io/#TunayAdaKaracan/foxic)

Foxic is a simple library with purpose of simplifying process of handling commands and components.

- Not overkill
- User friendly
- Non-limiting

## Features

- Command Handler
- Automatic subgroups and subcommands
- Easy Implemantation
- Developer/Test Commands (Commands only for provided servers)
- Modal and button handler.
- Modal and button builder.

### Command Examples

##### Creating Command Handler And Registering Commands
```java
JDA jda = JDABuilder.createDefault("TOKEN").build();
jda.awaitReady(); // This is neccessary

CommandHandler commandHandler = new CommandHandler(jda);
commandHandler.addDeveloperGuildLong(844842869285847060L);
commandHandler.registerCommand(new EchoCommand());
```
&nbsp;
##### Creating Commands Using Foxic Lib
```java
@SlashCommand(label = "echo", developerMode = true, description = "Repeats your message", guildOnly = false)
public class EchoCommand {

    @Executor
    public void execute(SlashCommandInteractionEvent e, @Option(name="message", description = "Your message", required = true) String message){
        e.reply(message).queue();
    }
}
```

##### Nested Commands (Subcommands and Subgroups)
```java
@SlashCommand(label = "point", developerMode = true, description = "Point commands", guildOnly = true)
public class PointCommand {

    @SlashCommand(label = "add", description = "Add points")
    public class AddPoint {
        @Executor
        public void callback(SlashCommandInteractionEvent e, @Option(name="points", description = "Points you want to add", optionType = OptionType.INTEGER, required = true) int point){
            e.reply(point+" point added").queue();
        }
    }

    @SlashCommand(label = "remove", description = "Remove points")
    public class RemovePoint {
        @Executor
        public void callback(SlashCommandInteractionEvent e, @Option(name="points", description = "Points you want to remove", optionType = OptionType.INTEGER, required = true) int point){
            e.reply(point+" point removed").queue();
        }
    }

}
```

### Button And Modal Handler
```java
@SlashCommand(label = "testbutton", developerMode = true)
public class TestCommand {

    ViewHandler handler;

    public TestCommand(ViewHandler handler){
        this.handler = handler;
    }

    @Executor
    public void execute(SlashCommandInteractionEvent e){
        Button button = ButtonBuilder.create(handler)
                .setLabel("Click Me!")
                .setStyle(ButtonStyle.SUCCESS)
                .setID("special-id")
                .setCallback(event -> {
                    event.reply("Yeeeet! You clicked me!").setEphemeral(true).queue();
                }).build();
        e.reply("A Message with button").addActionRow(button).queue();
    }

}
```
Proccess with modal is same too!

**Contact Me**: Kutup Tilkisi#3506
