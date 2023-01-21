package org.moskito.control.plugins.escalation;

import com.google.gson.annotations.SerializedName;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.configureme.annotations.AfterConfiguration;
import org.configureme.annotations.ConfigureMe;
import org.configureme.annotations.Configure;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ConfigureMe(allfields = false)
@SuppressFBWarnings(value = {"EI_EXPOSE_REP2", "EI_EXPOSE_REP"},
        justification = "This is the way configureMe works, it provides beans for access")
public class EscalationPluginConfig {

    @Configure
    @SerializedName("@escalations")
    private EscalationConfigEntry[] escalations = new EscalationConfigEntry[0];

    private Map<Integer, String> escalationMap = new HashMap<>();

    public EscalationConfigEntry[] getEscalations() {
        return escalations;
    }

    public void setEscalations(EscalationConfigEntry[] escalations) {
        this.escalations = escalations;
    }

    @Override
    public String toString() {
        return "EscalationPluginConfig{" +
                "escalations=" + Arrays.toString(escalations) +
                '}';
    }

    @AfterConfiguration public void setupTickMap(){
        escalationMap.clear();
        for (EscalationConfigEntry escalation : escalations){
            escalationMap.put(escalation.getTicks(), escalation.getMessage());
        }
    }

    public String getMessageForTickCount(int tickCount){
        String message = escalationMap.get(tickCount);
        //if message is null, it means no escalation message and no escalation.
        return message;
    }
}
