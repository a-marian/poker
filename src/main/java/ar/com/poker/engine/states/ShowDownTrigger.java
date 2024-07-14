package ar.com.poker.engine.states;

import ar.com.poker.api.core.Card;
import ar.com.poker.api.core.HandEvaluator;
import ar.com.poker.api.game.Hands7Evaluator;
import ar.com.poker.api.game.TexasHoldEmUtil;
import ar.com.poker.engine.model.ModelContext;
import ar.com.poker.engine.model.PlayerEntity;
import ar.com.util.statemachine.IStateTrigger;
import net.jcip.annotations.ThreadSafe;

import java.util.*;
import java.util.stream.Collectors;

@ThreadSafe
public class ShowDownTrigger  implements IStateTrigger<ModelContext> {

    private List<PlayerEntity> calculateHandValue(List<Card> communityCards, List<PlayerEntity> players){
        Hands7Evaluator evaluator = new Hands7Evaluator(new HandEvaluator());
        evaluator.setCommunityCards(communityCards);
        players.stream().forEach(p -> p.setHandValue(evaluator.eval(p.getCard(0), p.getCard(1))));
        return players;
    }

    @Override
    public boolean execute(ModelContext context) {
        List<PlayerEntity> players = calculateHandValue(context.getCommunityCards(), context.getPlayers());
        Map<Long, List<PlayerEntity>> indexByBet = players.stream().filter(p -> p.getBet() >0)
                .collect(Collectors.groupingBy(p -> p.getBet()));

        List<Long> inverseSortBets = new ArrayList<>(indexByBet.keySet());
        Collections.sort(inverseSortBets, (l0, l1) -> Long.compare(l1, l0));

        Iterator<Long> iterator = inverseSortBets.iterator();
        List<PlayerEntity> lastPlayers = indexByBet.get(iterator.next());
        while(iterator.hasNext()){
            List<PlayerEntity> currentPlayers = indexByBet.get(iterator.next());
            currentPlayers.addAll(lastPlayers);
            lastPlayers = currentPlayers;
        }
        Set<Long> bet4Analysis = players.stream().filter(p -> p.getState() == TexasHoldEmUtil.PlayerState.ALL_IN)
                .map(p -> p.getBet()).collect(Collectors.toSet());
        bet4Analysis.add(inverseSortBets.get(0));
        long accumulateChips = 0L;
        long lastBet = 0L;
        while(!inverseSortBets.isEmpty()){
            Long bet=inverseSortBets.remove(inverseSortBets.size()-1);
            List<PlayerEntity> currentPlayers = indexByBet.get(bet);
            accumulateChips += (bet -lastBet) * currentPlayers.size();
            if(bet4Analysis.contains(bet)){
                Collections.sort(currentPlayers, (p0, p1) -> p1.getHandValue() -p0.getHandValue());
                List<PlayerEntity> winners = new ArrayList<>(currentPlayers.size());
                currentPlayers.stream().filter(p -> p.getState() != TexasHoldEmUtil.PlayerState.OUT)
                        .peek(p -> p.showCards(true))
                        .filter(p -> winners.isEmpty() || p.getHandValue() == winners.get(0).getHandValue())
                        .forEach(winners::add);
                long chips4Player = accumulateChips/winners.size();
                winners.stream().forEach(p -> p.addChips(chips4Player));
                int remain = (int) accumulateChips % winners.size();
                if(remain > 0){
                    Collections.shuffle(winners);
                    winners.stream().limit(remain).forEach(p -> p.addChips(1));
                }
                accumulateChips = 0L;
            }
            lastBet = bet;
        }
        return true;
    }
}
