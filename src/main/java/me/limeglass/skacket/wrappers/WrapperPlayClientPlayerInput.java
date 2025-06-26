package me.limeglass.skacket.wrappers;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;

public class WrapperPlayClientPlayerInput extends WrapperPlayClientSteerVehicle {

	public WrapperPlayClientPlayerInput() {
		super();
	}

	public WrapperPlayClientPlayerInput(final PacketContainer packet) {
		super(packet);
	}

	@Override
	public float getSideways() {
		StructureModifier<Boolean> input = this.getHandle().getStructures().read(0).getBooleans();
		float sideways = 0;
		if (input.read(2)) { // Left (0x04)
			sideways += 1;
		}
		if (input.read(3)) { // Right (0x08)
			sideways -= 1;
		}
		return sideways;
	}

	@Override
	public void setSideways(final float value) {
		final StructureModifier<Boolean> input = this.getHandle().getStructures().read(0).getBooleans();
		if (value > 0) {
			input.write(2, true);  // Left (0x04)
			input.write(3, false); // Right (0x08)
		} else if (value < 0) {
			input.write(2, false);
			input.write(3, true);
		} else {
			input.write(2, false);
			input.write(3, false);
		}
	}

	@Override
	public float getForward() {
		final StructureModifier<Boolean> input = this.getHandle().getStructures().read(0).getBooleans();
		float forward = 0;
		if (input.read(0)) { // Forward (0x01)
			forward += 1;
		}
		if (input.read(1)) { // Backward (0x02)
			forward -= 1;
		}
		return forward;
	}

	@Override
	public void setForward(final float value) {
		final StructureModifier<Boolean> input = this.getHandle().getStructures().read(0).getBooleans();
		if (value > 0) {
			input.write(0, true);  // Forward (0x01)
			input.write(1, false); // Backward (0x02)
		} else if (value < 0) {
			input.write(0, false);
			input.write(1, true);
		} else {
			input.write(0, false);
			input.write(1, false);
		}
	}

	@Override
	public boolean isJump() {
		return this.handle.getStructures().read(0).getBooleans().read(4); // Jump (0x10)
	}

	@Override
	public void setJump(final boolean value) {
		this.handle.getStructures().read(0).getBooleans().write(4, value); // Jump (0x10)
	}

	/**
	 * @deprecated Use {@link #isSneak()} and {@link #setSneak(boolean)} instead.
	 */
	@Deprecated
	@Override
	public boolean isUnmount() {
		return isSneak();
	}

	/**
	 * @deprecated Use {@link #setSneak(boolean)} instead.
	 */
	@Deprecated
	@Override
	public void setUnmount(final boolean value) {
		setSneak(value);
	}

	/**
	 * Returns whether the sneak (unmount) key is pressed.
	 */
	public boolean isSneak() {
		return this.handle.getStructures().read(0).getBooleans().read(5); // Sneak (0x20)
	}

	/**
	 * Sets the sneak (unmount) key state.
	 */
	public void setSneak(final boolean value) {
		this.handle.getStructures().read(0).getBooleans().write(5, value); // Sneak (0x20)
	}

	public boolean isSprint() {
		return this.handle.getStructures().read(0).getBooleans().read(6);
	}

	public void setSprint(final boolean value) {
		this.handle.getStructures().read(0).getBooleans().write(6, value);
	}
}
