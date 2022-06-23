package dev.bozho.core.states;

import dev.bozho.core.StateManager;

public abstract class BaseState {

    public abstract void UpdateState(StateManager stateManager);

    public abstract void EnterState(StateManager stateManager);

}
