package com.mutabra.domain.common;

import java.util.List;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
public interface Ability {

    String getCode();

    TargetType getTargetType();

    void setTargetType(TargetType targetType);

    int getBloodCost();

    void setBloodCost(int bloodCost);

    List<Effect> getEffects();
}
