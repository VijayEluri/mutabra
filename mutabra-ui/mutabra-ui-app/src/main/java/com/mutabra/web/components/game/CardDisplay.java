package com.mutabra.web.components.game;

import com.mutabra.domain.battle.BattleHero;
import com.mutabra.domain.common.Card;
import com.mutabra.domain.common.TargetType;
import com.mutabra.web.base.components.AbstractComponent;
import com.mutabra.web.internal.IdUtils;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
public class CardDisplay extends AbstractComponent implements ClientElement {

	@Property
	@Parameter
	private BattleHero hero;

	@Property
	@Parameter
	private Card value;

	@Inject
	private JavaScriptSupport support;

	public String getClientId() {
		return IdUtils.generateId(value);
	}

	public String getDescriptionSelector() {
		return "#" + IdUtils.generateDescriptionId(value);
	}

	public String getFieldSelector() {
		final TargetType targetType = value.getTargetType();

		final StringBuilder sideSelector = new StringBuilder("path");
		if (targetType.supportsEnemy() && !targetType.supportsFriend()) {
			sideSelector.append(".enemy");
		} else if(targetType.supportsFriend() && !targetType.supportsEnemy()) {
			sideSelector.append(".friend");
		}

		final StringBuilder selector = new StringBuilder();
		if (targetType.supportsEmpty()) {
			selector.append(sideSelector).append(".empty");
		}
		if (targetType.supportsHero()) {
			if (selector.length() > 0) {
				selector.append(",");
			}
			selector.append(sideSelector).append(".hero");
		}
		if (targetType.supportsCreature()) {
			if (selector.length() > 0) {
				selector.append(",");
			}
			selector.append(sideSelector).append(".creature");
		}
		if (selector.length() == 0) {
			selector.append(sideSelector);
		}

		return selector.toString();
	}

	@AfterRender
	void renderScript() {
		final TargetType targetType = value.getTargetType();
		support.addInitializerCall("card", new JSONObject()
				.put("id", getClientId())
				.put("url", getResources().createEventLink("registerHeroAction", hero, value).toAbsoluteURI())
				.put("massive", targetType.isMassive())
				.put("supports_enemy_side", targetType.supportsEnemy())
				.put("supports_friend_side", targetType.supportsFriend())
				.put("supports_empty_point", targetType.supportsEmpty())
				.put("supports_hero_point", targetType.supportsHero())
				.put("supports_creature_point", targetType.supportsCreature()));
	}
}
