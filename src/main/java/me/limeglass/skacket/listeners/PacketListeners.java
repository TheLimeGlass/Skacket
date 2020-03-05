package me.limeglass.skacket.listeners;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;

import me.limeglass.skacket.Skacket;
import me.limeglass.skacket.events.SteerVehicleEvent;
import me.limeglass.skacket.events.SteerVehicleEvent.Movement;

public class PacketListeners {

	static {
		Skacket instance = Skacket.getInstance();
		instance.getProtocolManager().addPacketListener(new PacketAdapter(instance, ListenerPriority.NORMAL, PacketType.Play.Client.STEER_VEHICLE) {
			@Override
			public void onPacketReceiving(PacketEvent event) {
				PacketContainer packet = event.getPacket();
				Set<Movement> movements = new HashSet<>();
				StructureModifier<Float> floats = packet.getFloat();
				float sideways = floats.read(0);
				if (sideways != 0)
					movements.add(sideways > 0 ? Movement.LEFT : Movement.RIGHT);
				float forwards = floats.read(1);
				if (forwards != 0)
					movements.add(forwards > 0 ? Movement.FORWARDS : Movement.BACKWARDS);
				StructureModifier<Boolean> booleans = packet.getBooleans();
				if (booleans.read(0))
					movements.add(Movement.JUMP);
				if (booleans.read(1))
					movements.add(Movement.UNMOUNT);
				if (movements.isEmpty())
					return;
				SteerVehicleEvent steer = new SteerVehicleEvent(event.getPlayer(), movements.toArray(new Movement[movements.size()]));
				Bukkit.getPluginManager().callEvent(steer);
				if (steer.isCancelled())
					event.setCancelled(true);
			}
		});
	}

}
