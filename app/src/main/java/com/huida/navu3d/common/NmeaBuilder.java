package com.huida.navu3d.common;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * nmea数据生成类
 */
public enum NmeaBuilder {
    INSTANCE;

    //Our two new nmea strings
    private StringBuilder sbOGI = new StringBuilder();

    private StringBuilder sbHDT = new StringBuilder();

    private StringBuilder sbGGA = new StringBuilder();
    private StringBuilder sbVTG = new StringBuilder();

    //The entire string to send out
    private StringBuilder sbSendText = new StringBuilder();

    //GPS related properties
    private int fixQuality = 4, sats = 21;

    private double HDOP = 0.9;
    public double altitude = 79.3015;
    private char EW = 'E';
    private char NS = 'N';

    public double latitude, longitude;

    private double latDeg, latMinu, longDeg, longMinu, latNMEA, longNMEA;
    public double speed = 0, headingTrue, stepDistance = 0, steerAngle;
    public double steerAngleScrollBar = 0;
    private double degrees;

    public boolean isButtonAngle;

    private final SimpleDateFormat mSimpleDateFormat;
    //The checksum of an NMEA line
    private String sumStr = "";
    double nudHz = 20.0;
    NmeaBuilder() {//116.407387,39.904179
        latitude = 39.904179;
        longitude = 116.407387;
        degrees = 340.66;
        mSimpleDateFormat = new SimpleDateFormat("HHmmss.00");
    }

    public void btnChangeAngle(double angle) {
        isButtonAngle = true;
        steerAngle = angle;
    }

    public String doTick() {

        double temp = (stepDistance * Math.tan(steerAngle * 0.0165329252) / 3.3);
        headingTrue += temp;
        if (headingTrue > (2.0 * Math.PI)) headingTrue -= (2.0 * Math.PI);
        if (headingTrue < 0) headingTrue += (2.0 * Math.PI);

        degrees = new BigDecimal(headingTrue * 57.2958).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        //lblHeading.Text = degrees.ToString();

        //Calculate the next Lat Long based on heading and distance
        CalculateNewPostionFromBearingDistance(latitude, longitude, degrees, stepDistance / 1000.0);

        //calc the speed
        //speed = Math.Round(1.944 * stepDistance * (double)nudHz.Value, 1);
        speed = new BigDecimal(1.944 * stepDistance * nudHz).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        //lblSpeed.Text = (Math.Round(1.852 * speed, 1)).ToString();

        sbSendText.append(buildGGA());
        sbSendText.append(buildVTG());
        sbSendText.append(buildHDT());
        String result = sbSendText.toString();
        sbSendText.delete(0, sbSendText.length());
        return result;
    }

    private void CalculateNewPostionFromBearingDistance(double lat, double lng, double bearing, double distance) {
        final double R = 6371; // Earth Radius in Km

        double lat2 = Math.asin((Math.sin(Math.PI / 180 * lat) * Math.cos(distance / R))
                + (Math.cos(Math.PI / 180 * lat) * Math.sin(distance / R) * Math.cos(Math.PI / 180 * bearing)));

        double lon2 = (Math.PI / 180 * lng) + Math.atan2(Math.sin(Math.PI / 180 * bearing) * Math.sin(distance / R)
                * Math.cos(Math.PI / 180 * lat), Math.cos(distance / R) - (Math.sin(Math.PI / 180 * lat) * Math.sin(lat2)));

        latitude = 180 / Math.PI * lat2;
        longitude = 180 / Math.PI * lon2;

        //convert to DMS from Degrees
        latMinu = latitude;
        longMinu = longitude;

        latDeg = (int) latitude;
        longDeg = (int) longitude;

        latMinu -= latDeg;
        longMinu -= longDeg;

        latMinu = new BigDecimal(latMinu * 60.0).setScale(7, BigDecimal.ROUND_HALF_UP).doubleValue();
        longMinu = new BigDecimal(longMinu * 60.0).setScale(7, BigDecimal.ROUND_HALF_UP).doubleValue();

        latDeg *= 100.0;
        longDeg *= 100.0;

        latNMEA = latMinu + latDeg;
        longNMEA = longMinu + longDeg;

        if (latitude >= 0) NS = 'N';
        else NS = 'S';
        if (longitude >= 0) EW = 'E';
        else EW = 'W';
    }

    //calculate the NMEA checksum to stuff at the end
    private void CalculateChecksum(String Sentence) {
        int sum = 0, inx;
        char[] sentence_chars = Sentence.toCharArray();
        char tmp;
        // All character xor:ed results in the trailing hex checksum
        // The checksum calc starts after '$' and ends before '*'
        for (inx = 1; ; inx++) {
            tmp = sentence_chars[inx];
            // Indicates end of data and start of checksum
            if (tmp == '*')
                break;
            sum ^= tmp;    // Build checksum
        }
        // Calculated checksum converted to a 2 digit hex string
        sumStr = String.format("%02x", sum);
    }

    private String buildGGA() {
        sbGGA.delete(0, sbGGA.length());
        sbGGA.append("$GPGGA,");
        sbGGA.append(mSimpleDateFormat.format(System.currentTimeMillis())).append(",");
        sbGGA.append(String.format(Locale.CHINA, "%.7f", Math.abs(latNMEA))).append(',').append(NS).append(',');
        sbGGA.append(String.format(Locale.CHINA, "%.7f", Math.abs(longNMEA))).append(',').append(EW).append(',');
        sbGGA.append(fixQuality).append(",").append(sats).append(",").append(1.2).append(",").append(altitude);
        sbGGA.append(",M,20.082,M,00,0974*");

        CalculateChecksum(sbGGA.toString());
        sbGGA.append(sumStr);
        sbGGA.append("\r\n");
        return sbGGA.toString();

            /*
        $GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M ,  ,*47
           0     1      2      3    4      5 6  7  8   9    10 11  12 13  14
                Time      Lat       Lon     FixSatsOP Alt */
    }

    private String buildVTG() {
        sbVTG.delete(0, sbVTG.length());
        sbVTG.append("$GPVTG,");
        sbVTG.append(degrees);
        sbVTG.append(",T,034.4,M,");
        sbVTG.append(speed);
        sbVTG.append(",N,");
        sbVTG.append((speed * 1.852));
        sbVTG.append(",K*");

        CalculateChecksum(sbVTG.toString());
        sbVTG.append(sumStr);
        sbVTG.append("\n");
        return sbVTG.toString();

            /*         $GPVTG,054.7,T,034.4,M,005.5,N,010.2,K*48
               VTG          Track made good and ground speed
               054.7,T      True track made good (degrees)
               034.4,M      Magnetic track made good
               005.5,N      Ground speed, knots
               010.2,K      Ground speed, Kilometers per hour
               *48          Checksum
            */
    }

    private String buildHDT() {
        sbHDT.delete(0, sbHDT.length());
        sbHDT.append("$GNHDT,");
        sbHDT.append((degrees));
        sbHDT.append(",T*");

        CalculateChecksum(sbHDT.toString());
        sbHDT.append(sumStr);
        sbHDT.append("\r\n");
        return sbHDT.toString();

            /*        Heading from True North
                An example of the HDT string is:

                $GNHDT,123.456,T*00

                Heading from true north message fields
                Field	Meaning
                0	Message ID $GNHDT
                1	Heading in degrees
                2	T: Indicates heading relative to True North
                3	The checksum data, always begins with *

            */
    }


}
