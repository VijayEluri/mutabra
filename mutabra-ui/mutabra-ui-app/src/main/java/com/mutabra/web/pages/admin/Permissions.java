package com.mutabra.web.pages.admin;

import com.mutabra.domain.security.Permission;
import com.mutabra.services.BaseEntityService;
import com.mutabra.services.security.PermissionQuery;
import com.mutabra.web.base.pages.AbstractPage;
import com.mutabra.web.components.admin.PermissionDialog;
import com.mutabra.web.internal.BaseEntityDataSource;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.InjectService;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
public class Permissions extends AbstractPage {

	@InjectService("permissionService")
	private BaseEntityService<Permission, PermissionQuery> permissionService;

	@InjectComponent
	private PermissionDialog entityDialog;

	@Property
	private Permission row;

	public GridDataSource getSource() {
		return new BaseEntityDataSource<Permission>(permissionService.query(), Permission.class);
	}

	Object onEdit(final Permission permission) {
		return entityDialog.show(permission);
	}

	Object onSuccess() {
		permissionService.saveOrUpdate(entityDialog.getValue());
		return this;
	}
}