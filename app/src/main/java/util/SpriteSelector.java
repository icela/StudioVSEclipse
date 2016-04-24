package util;

import android.content.Context;

import com.lfk.justweengine.Engine.GameTexture;

/**
 * Created by ice1000 on 2016/4/15.
 * Single instance class
 */
public class SpriteSelector {

    //    private static final String FILENAME = "pic/new.jpg";
    private GameTexture IntelliJ_IDEA,
            Android_Studio, PyCharm, RubyMine,
            Eclipse, Eclipse1, Eclipse2, Eclipse3;
//    private GameTexture ReSharper;
//    private GameTexture CLion;

    public static final int EC = 0;
    public static final int EC1 = 1;
    public static final int EC2 = 2;
    public static final int EC3 = 3;
    public static final int IJ = 4;
    public static final int RM = 5;
    public static final int PC = 6;
    public static final int AS = -1;

    public SpriteSelector(Context engine) {
        PyCharm = new GameTexture(engine);
        IntelliJ_IDEA = new GameTexture(engine);
        RubyMine = new GameTexture(engine);
        Android_Studio = new GameTexture(engine);
        Eclipse = new GameTexture(engine);
        Eclipse1 = new GameTexture(engine);
        Eclipse2 = new GameTexture(engine);
        Eclipse3 = new GameTexture(engine);
//        ReSharper = new GameTexture(engine);
//        CLion = new GameTexture(engine);
    }

    // IJ : x: 0 - 160, y: 0 - 150
    // R# : x: 0 - 160, y: 450 - 600
    // CL : x: 0 - 160, y: 300 - 450
    public void load() {
//        IntelliJ_IDEA.loadFromAssetStripFrame(FILENAME, 0, 0, 160, 150);
//        ReSharper.loadFromAssetStripFrame(FILENAME, 0, 450, 160, 600);
//        CLion.loadFromAssetStripFrame(FILENAME, 0, 300, 160, 450);
        PyCharm.loadFromAsset("pic/pc.png");
        IntelliJ_IDEA.loadFromAsset("pic/ij.png");
        RubyMine.loadFromAsset("pic/rm.png");
        Android_Studio.loadFromAsset("pic/life.png");
        Eclipse.loadFromAsset("pic/ec.png");
        Eclipse1.loadFromAsset("pic/ec1.png");
        Eclipse2.loadFromAsset("pic/ec2.png");
        Eclipse3.loadFromAsset("pic/ec3.png");
    }

    public GameTexture AS() {
        return Android_Studio;
    }

    public GameTexture IJ() {
        return IntelliJ_IDEA;
    }

    public GameTexture PC() {
        return PyCharm;
    }

    public GameTexture RM() {
        return RubyMine;
    }

    public GameTexture EC() {
        return Eclipse;
    }

    public GameTexture EC1() {
        return Eclipse1;
    }

    public GameTexture EC2() {
        return Eclipse2;
    }

    public GameTexture EC3() {
        return Eclipse3;
    }

    public GameTexture getByCount(int c) {
        switch (c) {
            case EC:
                return EC();
            case EC1:
                return EC1();
            case EC2:
                return EC2();
            case EC3:
                return EC3();
            case IJ:
                return IJ();
            case RM:
                return RM();
            case PC:
                return PC();
            case AS:
            default:
                return AS();
        }
    }
}
//    Random random = new Random();
//        switch (random.nextInt(3)){
//            case 0:
//                return ReSharper;
//            case 1:
//                return CLion;
//            default:
//                return IntelliJ_IDEA;
//        }
