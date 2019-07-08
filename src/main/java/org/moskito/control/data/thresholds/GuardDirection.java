package org.moskito.control.data.thresholds;

/**
 * Possible threshold directions.
 *
 */
public enum GuardDirection {
	/**
	 * The new value is higher than the guarded value.
	 */
	UP("up"),
	ABOVE("above"),
	HIGHER("higher"),
	UPPER("upper"),
	OVER("over"),
	MORE("more"),

	/**
	 * The new value is lower than the guarded value.
	 */
	DOWN("down"),
	BELOW("below"),
	LOWER("lower"),
	UNDER("under"),
	LESS("less");

	private String name;

	GuardDirection(String name) {
		this.name = name;
	}

	public String getDirection() {
		return name;
	}

	public void setDirection(String name) {
		this.name = name;
	}

	public static GuardDirection forName(String directionName) {
		for (GuardDirection direction : values()) {
			if (direction.name.equalsIgnoreCase(directionName)) {

				switch (direction) {
					case UP:
					case ABOVE:
					case HIGHER:
					case UPPER:
					case OVER:
					case MORE:
						return UP;

					case DOWN:
					case BELOW:
					case LOWER:
					case UNDER:
					case LESS:
						return DOWN;
				}
			}
		}

		throw new IllegalArgumentException("Unknown direction " + directionName);
	}
}
