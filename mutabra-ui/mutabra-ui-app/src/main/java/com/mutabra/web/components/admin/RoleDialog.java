package com.mutabra.web.components.admin;

import com.mutabra.domain.security.Role;
import com.mutabra.web.base.components.EntityDialog;
import org.apache.tapestry5.annotations.OnEvent;

import static org.apache.tapestry5.EventConstants.PREPARE_FOR_SUBMIT;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
public class RoleDialog extends EntityDialog<Role> {

	@OnEvent(PREPARE_FOR_SUBMIT)
	void prepare(final Role entity) {
		init(entity);
	}
}
