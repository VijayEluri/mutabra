package com.mutabra.web.pages.player.hero;

import com.mutabra.domain.common.Race;
import com.mutabra.domain.player.Hero;
import com.mutabra.services.common.RaceService;
import com.mutabra.services.player.HeroService;
import com.mutabra.web.base.pages.AbstractPage;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.greatage.tapestry.internal.SelectModelBuilder;

import java.util.List;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
//@Allow(AuthorityConstants.ROLE_USER)
public class HeroCreate extends AbstractPage {

	@Inject
	private HeroService heroService;

	@Inject
	private RaceService raceService;

	@Inject
	private SelectModelBuilder selectModelBuilder;

	@InjectPage
	private HeroSelect heroSelectPage;

	@Component
	private Form createForm;

	private SelectModel raceModel;

	private Hero record;

	public SelectModel getRaceModel() {
		if (raceModel == null) {
			final List<Race> races = raceService.getEntities();
			raceModel = selectModelBuilder.buildFormatted(Race.class, races, "%s", "this:description");
		}
		return raceModel;
	}

	public Hero getRecord() {
		return record;
	}

	public void setRecord(Hero record) {
		this.record = record;
	}

	void onActivate() {
		record = heroService.create();
	}

	Object onCreate() {
		if (createForm.isValid()) {
			final Hero hero = getRecord();
			hero.setAccount(getApplicationContext().getAccount());
			heroService.save(hero);
			return onCancel();
		}
		return null;
	}

	Object onCancel() {
		return heroSelectPage;
	}
}