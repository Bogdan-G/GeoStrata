/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2014
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.GeoStrata.Registry;

import net.minecraftforge.common.Configuration;
import Reika.DragonAPI.Exception.RegistrationException;
import Reika.DragonAPI.Interfaces.ConfigList;
import Reika.GeoStrata.GeoStrata;

public enum GeoOptions implements ConfigList {

	RETROGEN("Retrogen Crystals", false),
	NOISE("Lamp Noises", true),
	EFFECTS("Lamp Effects", false),
	NETHER("Nether Crystals", true),
	PURPLE("Instant-XP on Purple Crystals Break", false),
	TFGEN("Generate Rock in the Twilight Forest", true),
	DIMGEN("Generate Rock in Other Dimensions", true),
	BOXRECIPES("Alternate Brick Recipes", false),
	DENSITY("Rock Density", 1F),
	NOPARTICLES("Disable Pendant Particles", true);

	private String label;
	private boolean defaultState;
	private int defaultValue;
	private float defaultFloat;
	private Class type;

	public static final GeoOptions[] optionList = GeoOptions.values();

	private GeoOptions(String l, boolean d) {
		label = l;
		defaultState = d;
		type = boolean.class;
	}

	private GeoOptions(String l, int d) {
		label = l;
		defaultValue = d;
		type = int.class;
	}

	private GeoOptions(String l, float d) {
		label = l;
		defaultFloat = d;
		type = float.class;
	}

	public boolean isBoolean() {
		return type == boolean.class;
	}

	public boolean isNumeric() {
		return type == int.class;
	}

	public boolean isDecimal() {
		return type == float.class;
	}

	public float setDecimal(Configuration config) {
		if (!this.isDecimal())
			throw new RegistrationException(GeoStrata.instance, "Config Property \""+this.getLabel()+"\" is not decimal!");
		return (float)config.get("Control Setup", this.getLabel(), defaultFloat).getDouble(defaultFloat);
	}

	public float getFloat() {
		return (Float)GeoStrata.config.getControl(this.ordinal());
	}

	public Class getPropertyType() {
		return type;
	}

	public int setValue(Configuration config) {
		if (!this.isNumeric())
			throw new RegistrationException(GeoStrata.instance, "Config Property \""+this.getLabel()+"\" is not numerical!");
		return config.get("Control Setup", this.getLabel(), defaultValue).getInt();
	}

	public String getLabel() {
		return label;
	}

	public boolean setState(Configuration config) {
		if (!this.isBoolean())
			throw new RegistrationException(GeoStrata.instance, "Config Property \""+this.getLabel()+"\" is not boolean!");
		return config.get("Control Setup", this.getLabel(), defaultState).getBoolean(defaultState);
	}

	public boolean getState() {
		return (Boolean)GeoStrata.config.getControl(this.ordinal());
	}

	public int getValue() {
		return (Integer)GeoStrata.config.getControl(this.ordinal());
	}

	public boolean isDummiedOut() {
		return type == null;
	}

	@Override
	public boolean getDefaultState() {
		return defaultState;
	}

	@Override
	public int getDefaultValue() {
		return defaultValue;
	}

	@Override
	public float getDefaultFloat() {
		return defaultFloat;
	}

	@Override
	public boolean isEnforcingDefaults() {
		return false;
	}

	@Override
	public boolean shouldLoad() {
		return true;
	}

	public static float getRockDensity() {
		float f = DENSITY.getFloat();
		f = Math.min(f, 2.5F);
		return Math.max(f, 0.5F);
	}

}
