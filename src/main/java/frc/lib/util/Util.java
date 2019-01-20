package frc.lib.util;

public class Util {
    /** Prevent this class from being instantiated. */
    private Util() {
    }

    public static double kEpsilon = 1e-6;
    public static boolean epsilonEquals(double a, double b, double epsilon) {
        return (a - epsilon <= b) && (a + epsilon >= b);
    }
    public static boolean epsilonEquals(double a, double b){
        return epsilonEquals(a,b,kEpsilon);
    }

    public static int clamp(int x, int min, int max){
        return Math.max(Math.min(x,max),min);
    }
    public static double clamp(double x, double min, double max){
        return Math.max(Math.min(max,x),0);
    }

    public static double linearInterp(double a, double b, double t){
        t = clamp(t,0,1);
        return a + (b-a)*t;
    }
    public static Vec2 linearInterp(Vec2 a, Vec2 b, double t){
        return new Vec2(linearInterp(a.x,b.x,t), linearInterp(a.y,b.y,t));
    }

    /** calculate: integral of cos(t^2 * pi/2)dt from 0 to x */
    // if -2 < x < 2, use lookup table; else, use an approximation
    public static double fresnelC(double x){
        if(Math.abs(x) < 2) return Math.signum(x)*tableFresnelC.get(Math.abs(x));
        double x2 = x*x, x3 = x2*x, x4 = x3*x;
        return Math.signum(x)*0.5
                + (0.3183099-0.0968/x4)*Math.sin(x2*Math.PI/2)/x 
                - (0.10132-0.154/x4)*Math.cos(x2*Math.PI/2)/x3;
    }

    /** calculate: integral of sin(t^2 * pi/2)dt from 0 to x */
    // if -2 < x < 2, use lookup table; else, use an approximation
    public static double fresnelS(double x){
        if(Math.abs(x) < 2) return Math.signum(x)*tableFresnelS.get(Math.abs(x));
        double x2 = x*x, x3 = x2*x, x4 = x3*x;
        return Math.signum(x)*0.5
                - (0.3183099-0.0968/x4)*Math.cos(x2*Math.PI/2)/x 
                - (0.10132-0.154/x4)*Math.sin(x2*Math.PI/2)/x3;
    }

    
    private static LookupTable tableFresnelC = new LookupTable(new double[]{
        0.0000000,                                                             //x=0
        0.0200000,0.0400000,0.0599998,0.0799992,0.0999975,0.1199939,0.1399867,
        0.1599741,0.1799534,0.1999211,0.2198729,0.2398036,0.2597070,0.2795756,
        0.2994010,0.3191731,0.3388806,0.3585109,0.3780496,0.3974808,0.4167868,
        0.4359482,0.4549440,0.4737510,0.4923442,0.5106969,0.5287801,0.5465630,
        0.5640131,0.5810954,0.5977737,0.6140094,0.6297625,0.6449912,0.6596524,
        0.6737012,0.6870920,0.6997779,0.7117113,0.7228442,0.7331283,0.7425154,
        0.7509579,0.7584090,0.7648230,0.7701563,0.7743672,0.7774168,0.7792695,
        0.7798934,                                                             //x=1
        0.7792611,0.7773501,0.7741434,0.7696303,0.7638067,0.7566760,0.7482494,
        0.7385468,0.7275968,0.7154377,0.7021176,0.6876947,0.6722378,0.6558263,
        0.6385505,0.6205111,0.6018195,0.5825973,0.5629759,0.5430958,0.5231058,
        0.5031623,0.4834280,0.4640705,0.4452612,0.4271732,0.4099799,0.3938529,
        0.3789596,0.3654617,0.3535120,0.3432529,0.3348132,0.3283061,0.3238269,
        0.3214502,0.3212283,0.3231887,0.3273325,0.3336329,0.3420339,0.3524496,
        0.3647635,0.3788293,0.3944705,0.4114824,0.4296333,0.4486669,0.4683056,
        0.4882534                                                              //x=2  
    }, 0, 0.02);

    private static LookupTable tableFresnelS = new LookupTable(new double[]{
        0.0000000,                                                             //x=0
        0.0000042,0.0000335,0.0001131,0.0002681,0.0005236,0.0009047,0.0014367,
        0.0021444,0.0030531,0.0041876,0.0055730,0.0072340,0.0091954,0.0114816,
        0.0141170,0.0171256,0.0205311,0.0243568,0.0286255,0.0333594,0.0385802,
        0.0443085,0.0505642,0.0573663,0.0647324,0.0726789,0.0812206,0.0903708,
        0.1001409,0.1105402,0.1215759,0.1332528,0.1455729,0.1585354,0.1721365,
        0.1863689,0.2012221,0.2166816,0.2327288,0.2493414,0.2664922,0.2841498,
        0.3022780,0.3208355,0.3397763,0.3590493,0.3785981,0.3983612,0.4182721,
        0.4382591,                                                             //x=1
        0.4582458,0.4781508,0.4978884,0.5173686,0.5364979,0.5551792,0.5733128,
        0.5907966,0.6075274,0.6234009,0.6383134,0.6521619,0.6648456,0.6762672,
        0.6863333,0.6949562,0.7020550,0.7075567,0.7113977,0.7135251,0.7138977,
        0.7124878,0.7092816,0.7042812,0.6975050,0.6889888,0.6787867,0.6669713,
        0.6536346,0.6388877,0.6228607,0.6057026,0.5875804,0.5686783,0.5491960,
        0.5293473,0.5093584,0.4894649,0.4699094,0.4509388,0.4328006,0.4157397,
        0.3999944,0.3857925,0.3733473,0.3628537,0.3544837,0.3483830,0.3446665,
        0.3434157                                                              //x=2
    }, 0, 0.02);
}