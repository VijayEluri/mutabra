package com.mutabra.domain.common;

import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
@Embeddable
public class AbilityImpl implements Ability {

    private String code;
    private TargetType targetType;
    private int bloodCost;
    private List<Effect> effects = new ArrayList<Effect>();

    public String getCode() {
        return code;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(final TargetType targetType) {
        this.targetType = targetType;
    }

    public int getBloodCost() {
        return bloodCost;
    }

    public void setBloodCost(final int bloodCost) {
        this.bloodCost = bloodCost;
    }

    public List<Effect> getEffects() {
        return effects;
    }
}
