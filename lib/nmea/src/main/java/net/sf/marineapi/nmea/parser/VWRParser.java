/* 
 * VWRParser.java
 * Copyright (C) 2016 Henri Laurent
 * 
 * This file is part of Java Marine API.
 * <http://sourceforge.net/projects/marineapi/>
 * 
 * Java Marine API is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Java Marine API is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Java Marine API. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.marineapi.nmea.parser;

import net.sf.marineapi.nmea.sentence.SentenceId;
import net.sf.marineapi.nmea.sentence.TalkerId;
import net.sf.marineapi.nmea.sentence.VWRSentence;
import net.sf.marineapi.nmea.util.Direction;

/**
 * VWR sentence parser.
 * 
 * @author Henri Laurent
 */
class VWRParser extends SentenceParser implements VWRSentence {

	private static final int WIND_ANGLE_DEGREES = 0;
	private static final int WIND_DIRECTION_LEFT_RIGHT_OF_BOW = 1;
	private static final int SPEED_KNOTS = 2;
	private static final int KNOTS_INDICATOR = 3;
	private static final int SPEED_MPS = 4;
	private static final int MPS_INDICATOR = 5;
	private static final int SPEED_KMPH = 6;
	private static final int KMPH_INDICATOR = 7;

	/**
	 * Creates a new instance of VWRParser.
	 *
	 * @param nmea VWR sentence String
	 * @throws IllegalArgumentException If specified sentence is invalid
	 */
	public VWRParser(String nmea) {
		super(nmea, SentenceId.VWR);
	}

	/**
	 * Creates VWR parser with empty sentence.
	 *
	 * @param talker TalkerId to set
	 */
	public VWRParser(TalkerId talker) {
		super(talker, SentenceId.VWR, 9);
		setCharValue(KNOTS_INDICATOR, VWRSentence.KNOT);
		setCharValue(MPS_INDICATOR, VWRSentence.MPS);
		setCharValue(KMPH_INDICATOR, VWRSentence.KMPH);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.marineapi.nmea.sentence.VWRSentence#getWindAngle()
	 */
	public double getWindAngle() {
		return getDoubleValue(WIND_ANGLE_DEGREES);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.marineapi.nmea.sentence.VWRSentence#getDirectionLeftRight()
	 */
	public Direction getDirectionLeftRight() {
		return Direction.valueOf(getCharValue(WIND_DIRECTION_LEFT_RIGHT_OF_BOW));
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.marineapi.nmea.sentence.VWRSentence#getSpeedKmh()
	 */
	public double getSpeedKmh() {
		return getDoubleValue(SPEED_KMPH);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.marineapi.nmea.sentence.VWRSentence#getSpeedKnots()
	 */
	public double getSpeedKnots() {
		return getDoubleValue(SPEED_KNOTS);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.marineapi.nmea.sentence.VWRSentence#getSpeedMps()
	 */
	public double getTrueCourse() {
		return getDoubleValue(SPEED_MPS);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.marineapi.nmea.sentence.VWRSentence#setSpeedKmh(double)
	 */
	public void setSpeedKmh(double kmh) {
		if (kmh < 0) {
			throw new IllegalArgumentException("Speed cannot be negative");
		}
		setDoubleValue(SPEED_KMPH, kmh, 1, 2);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.marineapi.nmea.sentence.VWRSentence#setSpeedKnots(double)
	 */
	public void setSpeedKnots(double knots) {
		if (knots < 0) {
			throw new IllegalArgumentException("Speed cannot be negative");
		}
		setDoubleValue(SPEED_KNOTS, knots, 1, 2);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.marineapi.nmea.sentence.VWRSentence#setWindAngle(double)
	 */
	public void setWindAngle(double mWindAngle) {
		setDegreesValue(WIND_ANGLE_DEGREES, mWindAngle);
	}

    /*
	 * (non-Javadoc)
	 * @see
	 * net.sf.marineapi.nmea.sentence.VWRSentence#setMode(net.sf.marineapi.nmea
	 * .com.sailgrib.nmea.util.Direction)
	 */
    public void setDirectionLeftRight(Direction directionLeftRight) {
        setCharValue(WIND_DIRECTION_LEFT_RIGHT_OF_BOW, directionLeftRight.toChar());
    }
}
