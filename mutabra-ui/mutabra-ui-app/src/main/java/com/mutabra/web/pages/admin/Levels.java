package com.mutabra.web.pages.admin;

import com.mutabra.domain.common.Level;
import com.mutabra.services.BaseEntityService;
import com.mutabra.services.common.LevelQuery;
import com.mutabra.web.base.pages.AbstractPage;
import com.mutabra.web.components.admin.LevelDialog;
import com.mutabra.web.internal.BaseEntityDataSource;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.InjectService;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
public class Levels extends AbstractPage {

	@InjectService("levelService")
	private BaseEntityService<Level, LevelQuery> levelService;

	@InjectComponent
	private LevelDialog entityDialog;

	@Property
	private Level row;

	public GridDataSource getSource() {
		return new BaseEntityDataSource<Level>(levelService.query(), Level.class);
	}

	Object onEdit(final Level level) {
		return entityDialog.show(level);
	}

	Object onSuccess() {
		levelService.saveOrUpdate(entityDialog.getValue());
		return this;
	}
}
