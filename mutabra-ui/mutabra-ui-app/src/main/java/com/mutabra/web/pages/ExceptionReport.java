package com.mutabra.web.pages;

import com.mutabra.web.internal.NotFoundException;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Response;
import org.greatage.security.AccessDeniedException;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
public class ExceptionReport extends org.apache.tapestry5.corelib.pages.ExceptionReport {

	@Inject
	private Messages messages;

	@Inject
	private Response response;

	private int status;

	public String getTitle() {
		return messages.get("page.error" + (status > 0 ? "-" + status : "") + ".title");
	}

	public String getMessage() {
		return messages.get("message.error" + (status > 0 ? "-" + status : ""));
	}

	@Override
	public void reportException(final Throwable exception) {
		final Throwable rootCause = getRootCause(exception);
		if (rootCause instanceof NotFoundException) {
			setStatus(HttpServletResponse.SC_NOT_FOUND);
		} else if (rootCause instanceof AccessDeniedException) {
			setStatus(HttpServletResponse.SC_FORBIDDEN);
		} else {
			setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		super.reportException(exception);
	}

	private void setStatus(final int status) {
		response.setStatus(status);
		this.status = status;
	}

	private static Throwable getRootCause(Throwable throwable) {
		Throwable cause;
		while ((cause = throwable.getCause()) != null) {
			throwable = cause;
		}
		return throwable;
	}
}
