package com.noname.web.components.game;

import com.noname.domain.common.Race;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.AssetSource;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
public class RaceDisplay {

	@Property
	@Parameter(required = true)
	private Race race;

	@Inject
	private AssetSource assetSource;

	public Asset getRaceImage() {
		try {
			return assetSource.getContextAsset("img/races/" + race.getCode().toLowerCase() + ".png", null);
		} catch (Exception e) {
			return null;
		}
	}
}
