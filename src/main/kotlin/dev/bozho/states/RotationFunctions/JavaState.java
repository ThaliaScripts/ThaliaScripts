package dev.bozho.states.RotationFunctions;

import net.minikloon.fsmgasm.State;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class JavaState extends State {

    @NotNull
    @Override
    public Duration getDuration() {
        return Duration.ZERO;
    }

    @Override
    protected void onEnd() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    protected void onStart() {

    }
}
