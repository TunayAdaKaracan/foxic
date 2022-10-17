package dev.kutuptilkisi.foxic.data;

import dev.kutuptilkisi.foxic.annotations.Option;
import dev.kutuptilkisi.foxic.annotations.SlashCommand;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.lang.reflect.Method;
import java.util.List;

public class CommandInfo {

    private final Object object;

    private final SlashCommand annotation;

    private CallbackInfo callback;

    private List<CommandInfo> sub;

    public CommandInfo(Object instance, SlashCommand annotation, Method callbackFromObject, List<CommandInfo> es, List<OptionData> methodOptions) {
        this.object = instance;
        this.annotation = annotation;
        this.callback = new CallbackInfo(callbackFromObject, methodOptions);
        this.sub = es;
    }

    public Object getObject() {
        return object;
    }

    public SlashCommand getAnnotation() {
        return annotation;
    }

    public Method getCallback() {
        return callback.getCallback();
    }

    public List<OptionData> getOptions() {
        return callback.getOptions();
    }

    public List<CommandInfo> getSub() {
        return sub;
    }

    public CommandInfo getSub(String label){
        for(CommandInfo sub : this.sub){
            if(sub.getAnnotation().label().equalsIgnoreCase(label)){
                return sub;
            }
        }
        return null;
    }

    public void addSub(CommandInfo info){
        this.sub.add(info);
    }
}
