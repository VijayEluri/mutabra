package com.mutabra.game;

import org.greatage.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
public class BattleFieldImpl implements BattleField {
	private final BattleCommand command;
	private final Map<FieldLine, List<Locatable>> locatables = CollectionUtils.newConcurrentMap();

	public BattleFieldImpl(final BattleCommand command) {
		this.command = command;

		for (FieldLine line : FieldLine.values()) {
			final List<Locatable> locatables = CollectionUtils.newList();
			for (int i = 0; i < line.getSize(); i++) {
				locatables.add(i, null);
			}
			this.locatables.put(line, locatables);
		}
	}

	public BattleCommand getCommand() {
		return command;
	}

	public List<Locatable> getLine(final FieldLine line) {
		return locatables.get(line);
	}

	public Locatable getLocatable(final Location location) {
		return locatables.get(location.getLine()).get(location.getPosition());
	}

	public void setLocatable(final Location location, final Locatable locatable) {
		if (getLocatable(location) != null) {
			throw new IllegalStateException(String.format("Location is busy: %s:%s", location.getLine(), location.getPosition()));
		}
		locatables.get(location.getLine()).add(location.getPosition(), locatable);
	}
}