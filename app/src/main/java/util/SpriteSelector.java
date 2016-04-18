package util;

import android.content.Context;

import com.lfk.justweengine.Engine.GameTexture;

/**
 * Created by ice1000 on 2016/4/15.
 * Single instance class
 */
public class SpriteSelector {

    private static final String FILENAME = "pic/new.jpg";
    private GameTexture IntelliJ_IDEA;
    private GameTexture Android_Studio;
    private GameTexture PyCharm;
    private GameTexture RubyMine;
    private GameTexture Eclipse;
    private GameTexture Eclipse1;
    private GameTexture Eclipse2;
    private GameTexture Eclipse3;
//    private GameTexture ReSharper;
//    private GameTexture CLion;

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
    public void load(){
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

    public GameTexture IJ (){return IntelliJ_IDEA;}
    public GameTexture PC (){return PyCharm;}
    public GameTexture RM (){return RubyMine;}
    public GameTexture AS (){return Android_Studio;}
    public GameTexture EC (){return Eclipse;}
    public GameTexture EC1(){return Eclipse1;}
    public GameTexture EC2(){return Eclipse2;}
    public GameTexture EC3(){return Eclipse3;}

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
