package me.limeglass.skacket.elements;

import org.bukkit.World;
import org.eclipse.jdt.annotation.Nullable;
import org.skriptlang.skript.lang.converter.Converter;
import org.skriptlang.skript.lang.converter.Converters;

import me.limeglass.skacket.objects.ClientWorldBorder;

public class DefaultConverters {

	static {
		Converters.registerConverter(ClientWorldBorder.class, World.class, new Converter<ClientWorldBorder, World>() {
			@Override
			@Nullable
			public World convert(ClientWorldBorder border) {
				return border.getWorld();
			}
		});
	}

}
