package com.mutabra.services;

import com.mutabra.domain.Translation;
import org.greatage.domain.EntityCriteria;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
public class TranslationFilterProcessor extends BaseEntityFilterProcessor<Translation, TranslationQuery> {

	public TranslationFilterProcessor() {
		super(Translation.class);
	}

	@Override
	protected void processFilter(final EntityCriteria criteria, final TranslationQuery filter) {
		if (filter.getType() != null) {
			criteria.add(criteria.getProperty(Translation.TYPE_PROPERTY).eq(filter.getType()));
		}
		if (filter.getLocales() != null) {
			criteria.add(criteria.getProperty(Translation.LOCALE_PROPERTY).in(filter.getLocales()));
		}
		if (filter.getCodes() != null) {
			criteria.add(criteria.getProperty(Translation.CODE_PROPERTY).in(filter.getCodes()));
		}
		if (filter.getVariants() != null) {
			criteria.add(criteria.getProperty(Translation.VARIANT_PROPERTY).in(filter.getVariants()));
		}
	}
}