package com.mutabra.web.base.components;

import com.mutabra.web.internal.MessageUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.Locale;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
public class AbstractComponent {

    @Inject
    private ComponentResources resources;

    @Inject
    private Messages messages;

    @Inject
    private Locale locale;

    public ComponentResources getResources() {
        return resources;
    }

    public Messages getMessages() {
        return messages;
    }

    public Locale getLocale() {
        return locale;
    }

    public Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    protected String format(final String key, final Object... parameters) {
        return messages.format(key, parameters);
    }

    protected String message(final String key) {
        return messages.get(key);
    }

    protected String label(final String key) {
        return messages.get(MessageUtils.label(key));
    }
}
