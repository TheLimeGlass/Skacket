package me.limeglass.skacket.sections;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.event.Event;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.config.Node;
import ch.njol.skript.config.SectionNode;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.skript.lang.TriggerSection;
import ch.njol.skript.log.HandlerList;
import ch.njol.skript.log.LogHandler;
import ch.njol.skript.log.ParseLogHandler;
import ch.njol.skript.log.RetainingLogHandler;
import ch.njol.skript.log.SkriptLogger;
import ch.njol.util.Kleenean;
import me.limeglass.skacket.Skacket;

public abstract class Section extends Condition {

	protected static Map<Class<? extends Section>, Section> map = new HashMap<>();
	private boolean executeNext = true;
	protected boolean hasIfOrElseIf;
	private TriggerSection trigger;
	protected SectionNode section;

	public Section() {
		Node node = SkriptLogger.getNode();
		if (node == null || !(node instanceof SectionNode))
			return;
		hasIfOrElseIf = StringUtils.startsWithIgnoreCase(node.getKey(), "if ") || StringUtils.startsWithIgnoreCase(node.getKey(), "else if ");
		String comment = getField(Node.class, node, "comment");
		if (comment == null)
			comment = "";
		section = new SectionNode(node.getKey(), comment, node.getParent(), node.getLine());
		setField(SectionNode.class, section, "nodes", getField(SectionNode.class, node, "nodes"));
		setField(SectionNode.class, node, "nodes", new ArrayList<>());
	}

	protected abstract boolean initalize(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult);

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		if (hasIfOrElseIf) {
			Skript.error("You can't use the effect in if/else if sections.");
			return false;
		}
		return initalize(expressions, matchedPattern, isDelayed, parseResult);
	}

	protected abstract void execute(Event event);

	@Override
	public boolean check(Event event) {
		execute(event);
		if (executeNext && trigger != null)
			setNext(trigger.getNext());
		return !hasSection();
	}

	public void loadSection(boolean setNext) {
		if (section != null) {
			RetainingLogHandler errors = SkriptLogger.startRetainingLog();
			Section previous = map.put(getClass(), this);
			try {
				trigger = new TriggerSection(section) {

					@Override
					public String toString(Event event, boolean debug) {
						return Section.this.toString(event, debug);
					}

					@Override
					public TriggerItem walk(Event event) {
						return walk(event, true);
					}
				};
				if (setNext) {
					trigger.setNext(getNext());
					setNext(null);
				}
			} finally {
				stopLog(errors);
			}
			map.put(getClass(), previous);
			section = null;
		}
	}

	@SuppressWarnings("unchecked")
	public void loadSection(String name, boolean setNext, Class<? extends Event>... events) {
		if (section != null && name != null && events != null && events.length > 0) {
			String previousName = ScriptLoader.getCurrentEventName();
			Class<? extends Event>[] previousEvents = ScriptLoader.getCurrentEvents();
			Kleenean previousDelay = ScriptLoader.hasDelayBefore;
			ScriptLoader.setCurrentEvent(name, events);
			loadSection(setNext);
			ScriptLoader.setCurrentEvent(previousName, previousEvents);
			ScriptLoader.hasDelayBefore = previousDelay;
		}
	}

	public boolean hasSection() {
		return section != null || trigger != null;
	}

	protected void runSection(Event event) {
		executeNext = false;
		TriggerItem.walk(trigger, event);
	}

	public SectionNode getSectionNode() {
		return section;
	}

	private void stopLog(RetainingLogHandler logger) {
		logger.stop();
		HandlerList handler = getField(SkriptLogger.class, null, "handlers");
		if (handler == null)
			return;
		Iterator<LogHandler> it = handler.iterator();
		List<LogHandler> toStop = new ArrayList<>();
		while (it.hasNext()) {
			LogHandler l = it.next();
			if (!(l instanceof ParseLogHandler))
				break;
			toStop.add(l);
		}
		toStop.forEach(LogHandler::stop); //Stopping them
		SkriptLogger.logAll(logger.getLog()); //Sending the errors to Skript logger.
	}

	@SuppressWarnings("unchecked")
	public static boolean isCurrentSection(Class<? extends Section>... classes) {
		return getCurrentSection(classes) != null;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Section> T getCurrentSection(Class<? extends Section>... classes) {
		for (Class<? extends Section> clz : classes) {
			T result = (T) map.get(clz);
			if (result != null)
				return result;
		}
		return null;
	}

	private <T> boolean setField(Class<T> from, Object obj, String field, Object newValue) {
		try {
			Field f = from.getDeclaredField(field);
			f.setAccessible(true);
			f.set(obj, newValue);
			return true;
		} catch (Exception e){
			if (Skacket.getInstance().getConfig().getBoolean("debug", false))
				e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private <T> T getField(Class<?> from, Object obj, String field) {
		try {
			Field f = from.getDeclaredField(field);
			f.setAccessible(true);
			return (T) f.get(obj);
		} catch (Exception e){
			if (Skacket.getInstance().getConfig().getBoolean("debug", false))
				e.printStackTrace();
		}
		return null;
	}

}
