package dev.kutuptilkisi.foxic.builder;

import dev.kutuptilkisi.foxic.handler.ViewHandler;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;

import java.util.ArrayList;
import java.util.List;

public class ModalBuilder {
    private final ViewHandler handler;

    private String id;

    private String title;

    private List<TextInput> fields;

    private ViewHandler.ModalCallback callback;

    public ModalBuilder(ViewHandler handler){
        this.handler = handler;
        fields = new ArrayList<>();
    }

    public ModalBuilder(ViewHandler handler, Modal modal){
        this.handler = handler;
        fields = new ArrayList<>();

        this.id = modal.getId();
        this.title = modal.getTitle();
        for(ActionRow row : modal.getActionRows()){
            this.fields.add((TextInput) row.getComponents().get(0));
        }
    }

    public ModalBuilder setID(String id){
        this.id = id;
        return this;
    }

    public ModalBuilder setTitle(String title){
        this.title = title;
        return this;
    }

    public ModalBuilder addTextField(TextInput field){
        fields.add(field);
        return this;
    }

    public ModalBuilder setCallback(ViewHandler.ModalCallback callback){
        this.callback = callback;
        return this;
    }

    public Modal build(){
        if(id == null || title == null){
            throw new RuntimeException("ID and title must be set");
        }
        handler.listen(id, callback);
        return Modal.create(id, title).addActionRow(fields).build();
    }
}
