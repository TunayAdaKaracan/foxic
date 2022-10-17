package dev.kutuptilkisi.foxic.builder;

import dev.kutuptilkisi.foxic.handler.ViewHandler;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;


public class ButtonBuilder {
    private final ViewHandler handler;

    private ButtonStyle style;
    private String id;
    private String label;
    private Emoji emoji;

    private String URL;

    private ViewHandler.ButtonCallback callback;

    public ButtonBuilder(ViewHandler handler){
        this.handler = handler;
    }

    public ButtonBuilder setStyle(ButtonStyle style){
        this.style = style;
        return this;
    }

    public ButtonBuilder setID(String id){
        this.id = id;
        return this;
    }

    public ButtonBuilder setLabel(String label){
        this.label = label;
        return this;
    }

    public ButtonBuilder setEmoji(Emoji emoji){
        this.emoji = emoji;
        return this;
    }

    public ButtonBuilder setURL(String URL){
        this.URL = URL;
        return this;
    }

    public ButtonBuilder setCallback(ViewHandler.ButtonCallback callback){
        this.callback = callback;
        return this;
    }

    public Button build(){
        handler.listen(id, callback);
        return Button.of(style, id, label).withEmoji(emoji).withUrl(URL);
    }
}
