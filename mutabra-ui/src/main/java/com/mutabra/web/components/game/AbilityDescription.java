package com.mutabra.web.components.game;

import com.mutabra.domain.battle.BattleAbility;
import com.mutabra.domain.common.Effect;
import com.mutabra.web.base.components.AbstractComponent;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
public class AbilityDescription extends AbstractComponent implements ClientElement {

    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String id;

    @Property
    @Parameter(required = true, allowNull = false)
    private BattleAbility ability;

    @Property
    private Effect effect;

    public String getClientId() {
        return id;
    }

    public String getName() {
        return property("name");
    }

    public String getDescription() {
        return property("description");
    }

    @SetupRender
    void setup() {
        effect = ability.getEffects().get(0);
    }

    private String property(final String property) {
        return message(i18n("ability", ability.getCode(), property));
    }
}