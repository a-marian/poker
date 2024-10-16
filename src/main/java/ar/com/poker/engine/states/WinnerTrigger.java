package ar.com.poker.engine.states;

import ar.com.poker.engine.model.ModelContext;
import ar.com.util.statemachine.IStateTrigger;

public class WinnerTrigger implements IStateTrigger<ModelContext> {
    @Override
    public boolean execute(ModelContext context) {
        return false;
    }
}
