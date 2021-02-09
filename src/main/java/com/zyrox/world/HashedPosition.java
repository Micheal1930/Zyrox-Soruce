package com.zyrox.world;

import com.zyrox.model.Position;

/**
 * An implementation of {@link Position} which is used for storing
 * the position values in a map, since the key can only be found
 * if the {@link #equals(Object object)} returns {@value true}.
 *
 * @author relex lawl
 */
public final class HashedPosition extends Position {

	public HashedPosition(int x, int y, int z) {
		super(x, y, z);
	}
	
	public HashedPosition(Position position) {
		this(position.getX(), position.getY(), position.getZ());
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (object.getClass() != getClass())
			return false;
		final HashedPosition other = (HashedPosition) object;
		return other.sameAs(this);
	}
}
