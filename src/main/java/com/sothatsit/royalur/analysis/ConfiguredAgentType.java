package com.sothatsit.royalur.analysis;

import com.sothatsit.royalur.simulation.Agent;

/**
 * An agent type that has been given configured.
 */
public class ConfiguredAgentType {

    private final String[] args;
    private final AgentType type;

    public ConfiguredAgentType(String[] args, AgentType type) {
        this.args = args;
        this.type = type;
    }

    public String getName() {
        return type.name;
    }

    public AgentType getType() {
        return type;
    }

    public Agent newAgent() {
        return type.newAgent(args);
    }
}
