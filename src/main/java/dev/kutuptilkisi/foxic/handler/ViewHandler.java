package dev.kutuptilkisi.foxic.handler;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ViewHandler extends ListenerAdapter implements ComponentHandler{

    public interface ButtonCallback {
        void call(ButtonInteractionEvent event);
    }

    public interface ModalCallback {
        void call(ModalInteractionEvent event);
    }

    private final JDA jda;

    private final HashMap<String, ButtonCallback> buttonCallbackHashMap;
    private final HashMap<String, ModalCallback> modalCallbackHashMap;

    public ViewHandler(JDA jda){
        this.jda = jda;
        buttonCallbackHashMap = new HashMap<>();
        modalCallbackHashMap = new HashMap<>();

        this.jda.addEventListener(this);
    }

    public void listen(String id, ButtonCallback callback){
        buttonCallbackHashMap.put(id, callback);
    }

    public void listen(String id, ModalCallback callback){
        this.modalCallbackHashMap.put(id, callback);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if(buttonCallbackHashMap.containsKey(event.getComponentId())){
            buttonCallbackHashMap.get(event.getComponentId()).call(event);
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if(modalCallbackHashMap.containsKey(event.getModalId())){
            modalCallbackHashMap.get(event.getModalId()).call(event);
        }
    }
}
