package com.mutabra.domain.common;

import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
@Embeddable
public class EffectImpl implements Effect {

    private EffectType type;
    private TargetType targetType;
    private int power;
    private int duration;
    private int health;
    private List<Ability> abilities = new ArrayList<Ability>();

    public EffectType getType() {
        return type;
    }

    public void setType(final EffectType type) {
        this.type = type;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(final TargetType targetType) {
        this.targetType = targetType;
    }

    public int getPower() {
        return power;
    }

    public void setPower(final int power) {
        this.power = power;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(final int health) {
        this.health = health;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }
}
