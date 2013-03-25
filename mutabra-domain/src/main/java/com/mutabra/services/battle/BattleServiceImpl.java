package com.mutabra.services.battle;

import com.google.code.morphia.Datastore;
import com.mutabra.domain.battle.*;
import com.mutabra.domain.common.Card;
import com.mutabra.domain.common.Effect;
import com.mutabra.domain.game.Account;
import com.mutabra.domain.game.AccountHero;
import com.mutabra.domain.game.Hero;
import com.mutabra.services.BaseEntityServiceImpl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
public class BattleServiceImpl
        extends BaseEntityServiceImpl<Battle>
        implements BattleService {

    private final ScriptEngine scriptEngine;

    public BattleServiceImpl(final Datastore datastore, final ScriptEngine scriptEngine) {
        super(datastore, Battle.class);

        this.scriptEngine = scriptEngine;
    }

    public Battle get(final Account account) {
        final AccountHero accountHero = account.getHero();
        return accountHero != null ?
                query().filter("heroes.id =", accountHero.getId()).get() :
                null;
    }

    public void create(final Hero hero) {
        final Battle battle = new Battle();
        battle.setStartedAt(new Date());
        battle.setActive(false);

        // init hero
        final BattleHero battleHero = new BattleHero(hero.getId());
        fillHero(battleHero, hero);
        battleHero.setReady(true);
        battle.getHeroes().add(battleHero);

        save(battle);
    }

    public void apply(final Battle battle, final Hero hero) {
        // init hero
        final BattleHero battleHero = new BattleHero(hero.getId());
        fillHero(battleHero, hero);
        battleHero.setReady(true);
        battle.getHeroes().add(battleHero);

        start(battle);
    }

    public List<Battle> findBattles() {
        return query()
                .filter("active =", false)
                .filter("startedAt <", new Date(System.currentTimeMillis() + 60000))
                .order("-startedAt")
                .limit(20)
                .asList();
    }

    private void start(final Battle battle) {
        battle.setActive(true);
        battle.setStartedAt(new Date());
        battle.setRound(1);

        for (BattleHero battleHero : battle.getHeroes()) {
            final Hero hero = dao().getDatastore().get(Hero.class, battleHero.getId());
            fillHero(battleHero, hero);

            // the initial hero position is center
            battleHero.setPosition(new BattlePosition(1, 0));
            battleHero.setReady(false);

            // init hero cards
            final List<Card> cards = dao().getDatastore().get(Card.class, hero.getCards()).asList();
            // shuffle deck
            Collections.shuffle(cards);
            // init card copies for battle
            for (Card card : cards) {
                final BattleCard battleCard = new BattleCard();
                fillCard(battleCard, card);

                battleCard.setType(BattleCardType.DECK);
                battleHero.getCards().add(battleCard);
            }
            // get 3 first cards from deck to hand
            for (int i = 0; i < 3; i++) {
                battleHero.getCards().get(i).setType(BattleCardType.HAND);
            }
        }

        save(battle);
    }

    private void end(final Battle battle) {
        for (BattleHero battleHero : battle.getHeroes()) {
            final Hero hero = dao().getDatastore().get(Hero.class, battleHero.getId());
            // update hero rating
            hero.getLevel().setRating(battleHero.getLevel().getRating());
            // TODO: add level-up logic
            dao().getDatastore().save(hero);
        }

        delete(battle);
    }

    private void endRound(final Battle battle) {
        // process effects
        scriptEngine.executeScripts(battle);

        for (BattleHero battleHero : battle.getHeroes()) {
            if (battleHero.getHealth() <= 0 || battleHero.getMentalPower() <= 0) {
                end(battle);
                return;
            }
        }

        //start new round
        battle.setRound(battle.getRound() + 1);
        for (BattleHero battleHero : battle.getHeroes()) {
            // increase mental power each round
            battleHero.setMentalPower(battleHero.getMentalPower() + 1);
            // move one card from deck to hand
            final List<BattleCard> deck = battleHero.getDeck();
            if (deck.size() > 0) {
                deck.get(0).setType(BattleCardType.HAND);
            }
            // each hero should be able to cast cards in the beginning of the round
            // except the situation when there are no cards in the hand
            battleHero.setReady(battleHero.getHand().isEmpty());

            for (BattleCreature battleCreature : battleHero.getCreatures()) {
                // each creature should be able to use abilities in the beginning of the round
                // except the situation when there are no abilities
                battleCreature.setReady(battleCreature.getAbilities().isEmpty());
            }
        }

        save(battle);
    }

    public void cast(final Battle battle,
                     final BattleCard card,
                     final BattleTarget target) {
        final BattleHero hero = card.getHero();
        for (Effect effect : card.getEffects()) {
            final BattleEffect battleEffect = new BattleEffect();
            fillEffect(battleEffect, effect);

            battleEffect.setCode(card.getCode());
            battleEffect.getTarget().setPosition(target.getPosition());
            battleEffect.getTarget().setSide(target.getSide());
            battleEffect.getCaster().setHero(hero);

            battle.getEffects().add(battleEffect);
        }

        hero.setReady(true);
        //TODO: replace with some battle effect that takes place after round ends
        hero.setHealth(hero.getHealth() - card.getBloodCost());
        card.setType(BattleCardType.GRAVEYARD);

        if (battle.isAllReady()) {
            endRound(battle);
        } else {
            save(battle);
        }
    }

    public void cast(final Battle battle,
                     final BattleAbility ability,
                     final BattleTarget target) {
        final BattleCreature creature = ability.getCreature();
        for (Effect effect : ability.getEffects()) {
            final BattleEffect battleEffect = new BattleEffect();
            fillEffect(battleEffect, effect);

            battleEffect.setCode(ability.getCode());
            battleEffect.getTarget().setPosition(target.getPosition());
            battleEffect.getTarget().setSide(target.getSide());
            battleEffect.getCaster().setCreature(creature);

            battle.getEffects().add(battleEffect);
        }

        creature.setReady(true);
        //TODO: replace with some battle effect that takes place after round ends
        creature.setHealth(creature.getHealth() - ability.getBloodCost());

        if (battle.isAllReady()) {
            endRound(battle);
        } else {
            save(battle);
        }
    }

    public void skip(final Battle battle, final BattleHero hero) {
        hero.setReady(true);

        for (BattleCreature battleCreature : hero.getCreatures()) {
            battleCreature.setReady(true);
        }

        if (battle.isAllReady()) {
            endRound(battle);
        } else {
            save(battle);
        }
    }

    private void fillHero(final BattleHero battleHero, final Hero hero) {
        battleHero.setHealth(hero.getHealth());
        battleHero.setMentalPower(hero.getMentalPower());

        battleHero.getLevel().setCode(hero.getLevel().getCode());
        battleHero.getLevel().setRating(hero.getLevel().getRating());
        battleHero.getLevel().setNextLevelRating(hero.getLevel().getNextLevelRating());

        battleHero.getAppearance().setName(hero.getAppearance().getName());
        battleHero.getAppearance().setRace(hero.getAppearance().getRace());
        battleHero.getAppearance().setFace(hero.getAppearance().getFace());
    }

    private void fillCard(final BattleCard battleCard, final Card card) {
        battleCard.setCode(card.getCode());
        battleCard.setTargetType(card.getTargetType());
        battleCard.setBloodCost(card.getBloodCost());
        battleCard.getEffects().addAll(card.getEffects());
    }

    private void fillEffect(final BattleEffect battleEffect, final Effect effect) {
        battleEffect.setDuration(effect.getDuration());
        battleEffect.setHealth(effect.getHealth());
        battleEffect.setPower(effect.getPower());
        battleEffect.setType(effect.getType());
        battleEffect.setTargetType(effect.getTargetType());
        battleEffect.getAbilities().addAll(effect.getAbilities());
    }
}
