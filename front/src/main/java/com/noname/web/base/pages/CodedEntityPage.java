package com.noname.web.base.pages;

import com.noname.domain.CodedEntity;
import com.noname.services.CodedEntityService;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
public abstract class CodedEntityPage<E extends CodedEntity> extends EntityPage<E> {

	protected abstract CodedEntityService<E> getEntityService();

	@Override
	protected E get(final Long entityId) {
		return entityId != null ? getEntityService().get(entityId, getLocale()) : null;
	}
}
