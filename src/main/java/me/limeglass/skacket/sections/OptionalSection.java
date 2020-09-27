package me.limeglass.skacket.sections;

import org.bukkit.event.Event;

import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.config.SimpleNode;
import ch.njol.skript.log.SkriptLogger;

public abstract class OptionalSection extends Section {

	public static OptionalSection lastInstance;

	public OptionalSection() {
		Node current = SkriptLogger.getNode();
		if (current != null && current instanceof SectionNode) {
			section = (SectionNode) current;
			map.put(getClass(), this);
			//An internal effect, to detect the end of a section.
			((SectionNode) current).add(new SimpleNode("$ end skacket section", "", 1, (SectionNode)current));
		}
		lastInstance = this;
	}

	@Override
	public boolean check(Event event) {
		execute(event);
		return true;
	}

	public static void removeCurrentSection() {
		if (lastInstance != null)
			map.remove(lastInstance.getClass());
		lastInstance = null;
	}

}
