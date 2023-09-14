package me.limeglass.skacket.elements.expressions;

import java.util.Locale;

import org.bukkit.Sound;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ExpressionType;
import me.limeglass.skacket.events.NamedSoundEvent;

@Name("Event Sound")
@Description({
	"Get the string version of the sound that was played. Can be set to a string of a Sound.",
	"Can be not present. Mojang made it that way."
})
@Since("1.0.13")
public class ExprSound extends EventValueExpression<String> {

	static {
		Skript.registerExpression(ExprSound.class, String.class, ExpressionType.SIMPLE, "[the] sound");
	}

	public ExprSound() {
		super(String.class);
	}

	@Nullable
	private EventValueExpression<String> sound;

	@Override
	@Nullable
	public Class<?>[] acceptChange(ChangeMode mode) {
		if (mode != ChangeMode.SET)
			return null;
		sound = new EventValueExpression<>(String.class);
		if (sound.init())
			return new Class[] {String.class};
		return null;
	}

	@Override
	public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
		String sound = delta == null ? null : (String) delta[0];
		if (sound == null)
			return;
		try {
			Sound enumSound = Sound.valueOf(sound.toUpperCase(Locale.ENGLISH).replaceAll(" ", "_"));
			NamedSoundEvent soundEvent = (NamedSoundEvent) event;
			soundEvent.setSound(enumSound);
		} catch (Exception ignored) {}
	}

}
