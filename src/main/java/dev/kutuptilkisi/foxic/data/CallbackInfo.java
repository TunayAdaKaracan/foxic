package dev.kutuptilkisi.foxic.data;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.lang.reflect.Method;
import java.util.List;

public class CallbackInfo {
    private Method callback;

    private List<OptionData> options;

    public CallbackInfo(Method callback, List<OptionData> options){
        this.callback = callback;
        this.options = options;
    }

    public Method getCallback() {
        return callback;
    }

    public List<OptionData> getOptions() {
        return options;
    }
}
