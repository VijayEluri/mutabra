package com.noname.web.pages.common.card;

import com.noname.domain.common.Card;
import com.noname.services.common.CardService;
import com.noname.web.base.pages.EntityListPage;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.greatage.security.annotations.Secured;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
@Secured
public class CardList extends EntityListPage<Card> {

	@Inject
	private CardService cardService;

	@InjectPage
	private CardDetails cardDetails;

	@Override
	protected Object getDetails(String state, Long recordId) {
		cardDetails.setState(state);
		cardDetails.setRecordId(recordId);
		return cardDetails;
	}

	@Override
	protected CardService getEntityService() {
		return cardService;
	}
}