package dev.kutuptilkisi.foxic.handler;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ViewHandler extends ListenerAdapter implements ComponentHandler{

    public interface ButtonCallback {
        void call(ButtonInteractionEvent event);
    }
    private final JDA jda;

    private final HashMap<String, ButtonCallback> callbackHashMap;

    public ViewHandler(JDA jda){
        this.jda = jda;
        callbackHashMap = new HashMap<>();

        this.jda.addEventListener(this);
    }

    public void listen(Button button, ButtonCallback callback){
        this.listen(button.getId(), callback);
    }

    public void stopListen(Button button){
        this.stopListen(button.getId());
    }

    public void listen(String id, ButtonCallback callback){
        callbackHashMap.put(id, callback);
    }

    public void stopListen(String id){
        callbackHashMap.remove(id);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if(callbackHashMap.containsKey(event.getComponentId())){
            callbackHashMap.get(event.getComponentId()).call(event);
        }
    }

    @Override
    public void onUserContextInteraction(@NotNull UserContextInteractionEvent event) {
        super.onUserContextInteraction(event);
    }
}
