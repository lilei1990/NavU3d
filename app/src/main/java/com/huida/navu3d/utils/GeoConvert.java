package com.huida.navu3d.utils;

import android.content.Context;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.huida.navu3d.bean.PointXYData;

public enum GeoConvert {
    INSTANCE;

    private static final double sm_a = 6378137.0;
    private static final double sm_b = 6356752.314;
    private static final double UTMScaleFactor = 0.9996;
    private static final double UTMScaleFactor2 = 1.0004001600640256102440976390556;

    private boolean isFirstFixPositionSet;
    private double zone;

    public double[] convertLatLonToUTM(double latitude, double longitude) {
        //only calculate the zone once!
        if (!isFirstFixPositionSet) {
            zone = Math.floor((longitude + 180.0) * 0.16666666666666666666666666666667) + 1;
            isFirstFixPositionSet = true;
        }

        double[] xy = MapLatLonToXY(latitude * 0.01745329251994329576923690766743,
                longitude * 0.01745329251994329576923690766743,
                (-183.0 + (zone * 6.0)) * 0.01745329251994329576923690766743);

        xy[0] = (xy[0] * UTMScaleFactor) + 500000.0;
        xy[1] *= UTMScaleFactor;
        if (xy[1] < 0.0)
            xy[1] += 10000000.0;

        return xy;
    }

    public double[] convertUTMToLatLon(double X, double Y) {
        double cmeridian;

        X -= 500000.0;
        X *= UTMScaleFactor2;

        /* If in southern hemisphere, adjust y accordingly. */
//        if (pn.hemisphere == 'S')
//            Y -= 10000000.0;
        Y *= UTMScaleFactor2;

        cmeridian = (-183.0 + (zone * 6.0)) * 0.01745329251994329576923690766743;
        double[] latlon = new double[2]; //= MapXYToLatLon(X, Y, cmeridian);

        double phif, Nf, Nfpow, nuf2, ep2, tf, tf2, tf4, cf;
        double x1frac, x2frac, x3frac, x4frac, x5frac, x6frac, x7frac, x8frac;
        double x2poly, x3poly, x4poly, x5poly, x6poly, x7poly, x8poly;

        /* Get the value of phif, the footpoint latitude. */
        phif = FootpointLatitude(Y);

        /* Precalculate ep2 */
        ep2 = (Math.pow(sm_a, 2.0) - Math.pow(sm_b, 2.0))
                / Math.pow(sm_b, 2.0);

        /* Precalculate cos (phif) */
        cf = Math.cos(phif);

        /* Precalculate nuf2 */
        nuf2 = ep2 * Math.pow(cf, 2.0);

        /* Precalculate Nf and initialize Nfpow */
        Nf = Math.pow(sm_a, 2.0) / (sm_b * Math.sqrt(1 + nuf2));
        Nfpow = Nf;

        /* Precalculate tf */
        tf = Math.tan(phif);
        tf2 = tf * tf;
        tf4 = tf2 * tf2;

            /* Precalculate fractional coefficients for x**n in the equations
               below to simplify the expressions for latitude and longitude. */
        x1frac = 1.0 / (Nfpow * cf);

        Nfpow *= Nf;   /* now equals Nf**2) */
        x2frac = tf / (2.0 * Nfpow);

        Nfpow *= Nf;   /* now equals Nf**3) */
        x3frac = 1.0 / (6.0 * Nfpow * cf);

        Nfpow *= Nf;   /* now equals Nf**4) */
        x4frac = tf / (24.0 * Nfpow);

        Nfpow *= Nf;   /* now equals Nf**5) */
        x5frac = 1.0 / (120.0 * Nfpow * cf);

        Nfpow *= Nf;   /* now equals Nf**6) */
        x6frac = tf / (720.0 * Nfpow);

        Nfpow *= Nf;   /* now equals Nf**7) */
        x7frac = 1.0 / (5040.0 * Nfpow * cf);

        Nfpow *= Nf;   /* now equals Nf**8) */
        x8frac = tf / (40320.0 * Nfpow);

            /* Precalculate polynomial coefficients for x**n.
               -- x**1 does not have a polynomial coefficient. */
        x2poly = -1.0 - nuf2;

        x3poly = -1.0 - 2 * tf2 - nuf2;

        x4poly = 5.0 + 3.0 * tf2 + 6.0 * nuf2 - 6.0 * tf2 * nuf2
                - 3.0 * (nuf2 * nuf2) - 9.0 * tf2 * (nuf2 * nuf2);

        x5poly = 5.0 + 28.0 * tf2 + 24.0 * tf4 + 6.0 * nuf2 + 8.0 * tf2 * nuf2;

        x6poly = -61.0 - 90.0 * tf2 - 45.0 * tf4 - 107.0 * nuf2
                + 162.0 * tf2 * nuf2;

        x7poly = -61.0 - 662.0 * tf2 - 1320.0 * tf4 - 720.0 * (tf4 * tf2);

        x8poly = 1385.0 + 3633.0 * tf2 + 4095.0 * tf4 + 1575 * (tf4 * tf2);

        /* Calculate latitude */
        latlon[0] = phif + x2frac * x2poly * (X * X)
                + x4frac * x4poly * Math.pow(X, 4.0)
                + x6frac * x6poly * Math.pow(X, 6.0)
                + x8frac * x8poly * Math.pow(X, 8.0);

        /* Calculate longitude */
        latlon[1] = cmeridian + x1frac * X
                + x3frac * x3poly * Math.pow(X, 3.0)
                + x5frac * x5poly * Math.pow(X, 5.0)
                + x7frac * x7poly * Math.pow(X, 7.0);
        latlon[0] = latlon[0] * 57.295779513082325225835265587528;
        latlon[1] = latlon[1] * 57.295779513082325225835265587528;

        return latlon;
    }


    private double ArcLengthOfMeridian(double phi) {
        double n = (sm_a - sm_b) / (sm_a + sm_b);
        double alpha = ((sm_a + sm_b) / 2.0) * (1.0 + (Math.pow(n, 2.0) / 4.0) + (Math.pow(n, 4.0) / 64.0));
        double beta = (-3.0 * n / 2.0) + (9.0 * Math.pow(n, 3.0) * 0.0625) + (-3.0 * Math.pow(n, 5.0) / 32.0);
        double gamma = (15.0 * Math.pow(n, 2.0) * 0.0625) + (-15.0 * Math.pow(n, 4.0) / 32.0);
        double delta = (-35.0 * Math.pow(n, 3.0) / 48.0) + (105.0 * Math.pow(n, 5.0) / 256.0);
        double epsilon = (315.0 * Math.pow(n, 4.0) / 512.0);
        return alpha * (phi + (beta * Math.sin(2.0 * phi))
                + (gamma * Math.sin(4.0 * phi))
                + (delta * Math.sin(6.0 * phi))
                + (epsilon * Math.sin(8.0 * phi)));
    }

    private double[] MapLatLonToXY(double phi, double lambda, double lambda0) {
        double[] xy = new double[2];
        double ep2 = (Math.pow(sm_a, 2.0) - Math.pow(sm_b, 2.0)) / Math.pow(sm_b, 2.0);
        double nu2 = ep2 * Math.pow(Math.cos(phi), 2.0);
        double n = Math.pow(sm_a, 2.0) / (sm_b * Math.sqrt(1 + nu2));
        double t = Math.tan(phi);
        double t2 = t * t;
        double l = lambda - lambda0;
        double l3Coef = 1.0 - t2 + nu2;
        double l4Coef = 5.0 - t2 + (9 * nu2) + (4.0 * (nu2 * nu2));
        double l5Coef = 5.0 - (18.0 * t2) + (t2 * t2) + (14.0 * nu2) - (58.0 * t2 * nu2);
        double l6Coef = 61.0 - (58.0 * t2) + (t2 * t2) + (270.0 * nu2) - (330.0 * t2 * nu2);
        double l7Coef = 61.0 - (479.0 * t2) + (179.0 * (t2 * t2)) - (t2 * t2 * t2);
        double l8Coef = 1385.0 - (3111.0 * t2) + (543.0 * (t2 * t2)) - (t2 * t2 * t2);

        /* Calculate easting (x) */
        xy[0] = (n * Math.cos(phi) * l)
                + (n / 6.0 * Math.pow(Math.cos(phi), 3.0) * l3Coef * Math.pow(l, 3.0))
                + (n / 120.0 * Math.pow(Math.cos(phi), 5.0) * l5Coef * Math.pow(l, 5.0))
                + (n / 5040.0 * Math.pow(Math.cos(phi), 7.0) * l7Coef * Math.pow(l, 7.0));

        /* Calculate northing (y) */
        xy[1] = ArcLengthOfMeridian(phi)
                + (t / 2.0 * n * Math.pow(Math.cos(phi), 2.0) * Math.pow(l, 2.0))
                + (t / 24.0 * n * Math.pow(Math.cos(phi), 4.0) * l4Coef * Math.pow(l, 4.0))
                + (t / 720.0 * n * Math.pow(Math.cos(phi), 6.0) * l6Coef * Math.pow(l, 6.0))
                + (t / 40320.0 * n * Math.pow(Math.cos(phi), 8.0) * l8Coef * Math.pow(l, 8.0));

        return xy;
    }

    private double FootpointLatitude(double y) {
        double y_, alpha_, beta_, gamma_, delta_, epsilon_, n;
        double result;

        /* Precalculate n (Eq. 10.18) */
        n = (sm_a - sm_b) / (sm_a + sm_b);

        /* Precalculate alpha_ (Eq. 10.22) */
        /* (Same as alpha in Eq. 10.17) */
        alpha_ = ((sm_a + sm_b) / 2.0)
                * (1 + (Math.pow(n, 2.0) / 4) + (Math.pow(n, 4.0) / 64));

        /* Precalculate y_ (Eq. 10.23) */
        y_ = y / alpha_;

        /* Precalculate beta_ (Eq. 10.22) */
        beta_ = (3.0 * n / 2.0) + (-27.0 * Math.pow(n, 3.0) / 32.0)
                + (269.0 * Math.pow(n, 5.0) / 512.0);

        /* Precalculate gamma_ (Eq. 10.22) */
        gamma_ = (21.0 * Math.pow(n, 2.0) * 0.0625)
                + (-55.0 * Math.pow(n, 4.0) / 32.0);

        /* Precalculate delta_ (Eq. 10.22) */
        delta_ = (151.0 * Math.pow(n, 3.0) / 96.0)
                + (-417.0 * Math.pow(n, 5.0) / 128.0);

        /* Precalculate epsilon_ (Eq. 10.22) */
        epsilon_ = (1097.0 * Math.pow(n, 4.0) / 512.0);

        /* Now calculate the sum of the series (Eq. 10.21) */
        result = y_ + (beta_ * Math.sin(2.0 * y_))
                + (gamma_ * Math.sin(4.0 * y_))
                + (delta_ * Math.sin(6.0 * y_))
                + (epsilon_ * Math.sin(8.0 * y_));

        return result;
    }



}
