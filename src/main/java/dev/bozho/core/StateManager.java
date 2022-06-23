package dev.bozho.core;

import dev.bozho.core.states.BaseState;

public class StateManager {

    BaseState currentState;

    public void Start() {}

    public void Update() {
        currentState.UpdateState(this);
    }
}
