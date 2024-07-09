package ar.com.poker.api.game;

import ar.com.poker.api.game.TexasHoldEmUtil.BetCommandType;
import ar.com.util.exceptions.ExceptionUtil;
import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class BetCommand {

	private final BetCommandType type;
	private long chips;
	
	public BetCommand(BetCommandType type){
		this(type, 0);
	}
	
	
	public BetCommand(BetCommandType type, int chips){
		ExceptionUtil.checkNullArgument(type, "type");
		ExceptionUtil.checkMinValueArgument(chips, 0L, "chips");
		this.type = type;
		this.chips = chips;
	}
	
	public BetCommandType getType() {
        return type;
    }

    public long getChips() {
        return chips;
    }

    public void setChips(long chips) {
        this.chips = chips;
    }

    @Override
    public String toString() {
        return String.join("{class:'BetCommand', type:'", type.toString(), "', chips:", Long.toString(chips), "}");
    }
	
}
