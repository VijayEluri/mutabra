package com.mutabra.web.pages.game.hero;

import com.mutabra.domain.player.Hero;
import com.mutabra.domain.security.Account;
import com.mutabra.services.BaseEntityService;
import com.mutabra.web.base.pages.AbstractPage;
import com.mutabra.web.internal.Authorities;
import com.mutabra.web.pages.game.GameHome;
import com.mutabra.web.services.AccountContext;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.greatage.security.annotations.Allow;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
@Allow(Authorities.ROLE_USER)
public class CreateHero extends AbstractPage {

	@InjectService("accountService")
	private BaseEntityService<Account> accountService;

	@InjectService("heroService")
	private BaseEntityService<Hero> heroService;

	@Property
	private Hero value;

	@Inject
	private AccountContext accountContext;

	@OnEvent(value = EventConstants.SUCCESS)
	Object createHero() {
		value = heroService.create();
		value.setAccount(accountContext.getAccount());
		heroService.saveOrUpdate(value);
		return back();
	}

	@OnEvent
	Object cancel(final String source) {
		return back();
	}

	private Object back() {
		return GameHome.class;
	}
}