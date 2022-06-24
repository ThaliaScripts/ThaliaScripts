package dev.bozho.states.rotationfunctions;

import dev.bozho.states.statelibrary.State;
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
