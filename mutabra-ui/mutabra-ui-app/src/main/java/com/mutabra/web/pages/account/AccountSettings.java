package com.mutabra.web.pages.account;

import com.mutabra.domain.game.Account;
import com.mutabra.services.BaseEntityService;
import com.mutabra.web.base.pages.AbstractPage;
import com.mutabra.web.services.AccountContext;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.InjectService;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
@RequiresAuthentication
public class AccountSettings extends AbstractPage {

	@Property
	private String email;

	@Property
	private String password;

	@Property
	private String confirmPassword;

	@Inject
	private AccountContext accountContext;

	@InjectService("accountService")
	private BaseEntityService<Account> accountService;

	public Account getValue() {
		return accountContext.getAccount();
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "accountForm")
	void save() {
		accountService.saveOrUpdate(getValue());
		//todo: add success notification
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "changeEmailForm")
	void changeEmail() {
		//todo: validate if if email exist, if new email differs from current and if new email are not exist
//		accountManager.changeEmail(getValue().getEmail(), email);
		//todo: add success notification
	}

	@OnEvent(value = EventConstants.SUCCESS, component = "changePasswordForm")
	void changePassword() {
		//todo: validate if email exist, if passwords match each other...
//		accountManager.changePassword(getValue().getEmail(), password);
		//todo: add success notification
	}
}
