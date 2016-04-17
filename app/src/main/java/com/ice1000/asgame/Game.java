package com.ice1000.asgame;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.MotionEvent;

import com.lfk.justweengine.Anim.VelocityAnimation;
import com.lfk.justweengine.Engine.Engine;
import com.lfk.justweengine.Engine.GameTextPrinter;
import com.lfk.justweengine.Engine.GameTexture;
import com.lfk.justweengine.Engine.GameTimer;
import com.lfk.justweengine.Info.UIdefaultData;
import com.lfk.justweengine.Drawable.Sprite.BaseSprite;
import com.lfk.justweengine.Drawable.Sprite.BaseSub;
import com.lfk.justweengine.Utils.tools.SpUtils;

import java.util.Random;

import util.ASSprite;
import util.SpriteSelector;

public class Game extends Engine {

    private static final int AS = 0x000;
    private static final int IJ = 0x001;
    private static final int ECLIPSE = 0x002;
    private static final int FIRE = 80;
    private static final int bulletSize = 20;
    private static final String START = "START";
    private static final String AS_SPRITE = "ASSprite";
    private static int ENEMY = 400;
    private static final String BEST = "BEST";
    private int score, level, best, textSize, textFromLeft, life;
    private SpriteSelector selector;
    private ASSprite asSprite;
    private GameTimer fire;
    private GameTimer enemy;
    private Random random;
    private boolean isDied = false;

    public Game() {
        super(false);
//        shouldPrintInfo = false;
        selector = new SpriteSelector(this);
        fire = new GameTimer();
        enemy = new GameTimer();
        random = new Random();
    }

    @Override
    public void init() {
        super.setScreenOrientation(ScreenMode.PORTRAIT);
        UIdefaultData.init(this);
        setBackgroundColor(Color.BLACK);
        isDied = false;
        score = 0;
        best = 0;
        level = 1;
        textSize = 30;
        textFromLeft = 50;
        life = 5;
        best = (int) SpUtils.get(this, BEST, 0);
    }

    @Override
    public void load() {
        selector.load();
        // 100 * 110
        asSprite = new ASSprite(this, 100, 110, 4);
        asSprite.addfixedAnimation(
                START,
                asSprite.getStartAnimation()
        );
        asSprite.setName(AS_SPRITE);
        asSprite.setScale(1.2f);
        asSprite.setIdentifier(AS);
//        BaseSprite background = new BaseSprite(
//                this,
//                UIdefaultData.screenWidth,
//                UIdefaultData.screenHeight,
//                FrameType.SIMPLE
//        );
//        background.setTexture(bg);
//        background.setPosition(0, 0);
//        background.setDipScale(
//                UIdefaultData.screenWidth,
//                UIdefaultData.screenHeight
//        );
//        addToSpriteGroup(background);
        addToSpriteGroup(asSprite);
    }

    @Override
    public void draw() {
        GameTextPrinter printer = new GameTextPrinter(getCanvas());
        printer.setTextSize(textSize);
        printer.setTextColor(Color.WHITE);
        int column = 1;
        printer.drawText("By 千里冰封 & JustWeEngine",
                textFromLeft, textSize * column++);
        printer.drawText("score : " + score, textFromLeft, textSize * column++);
        printer.drawText("best:  " + best, textFromLeft, textSize * column++);
        printer.drawText("level : " + level, textFromLeft, textSize * column++);
        printer.drawText(
                isDied ? "You die!" : ("life : " + life),
                textFromLeft, textSize * column);
    }

    @Override
    public void update() {
        if (asSprite.getFixedAnimation(START).animating) {
            asSprite.fixedAnimation(START);
        } else if (isDied) {
            addEnemy();
        } else {
            fire();
            addEnemy();
        }
    }

    @Override
    public void touch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                asSprite.setOffset(event);
                asSprite.move();
                // no break, because always should setStart.
            case MotionEvent.ACTION_DOWN:
                asSprite.setStart(event);
                break;
        }
    }

    @Override
    public void collision(BaseSub baseSub) {
        switch (baseSub.getIdentifier()) {
            case IJ:
                if (baseSub.getOffender().getIdentifier() == ECLIPSE) {
                    baseSub.getOffender().setAlive(false);
                    baseSub.setAlive(false);
                    removeFromSpriteGroup(baseSub.getOffender());
                    addToRecycleGroup(baseSub.getOffender());
                    score++;
                    if (ENEMY > 100) ENEMY--;
                    switch (score) {
                        case 60:
                            level++;
                            break;
                        case 150:
                            level++;
                            break;
                        case 250:
                            level++;
                            break;
                    }
                }
                break;
            case ECLIPSE:
                if (baseSub.getOffender().getIdentifier() == AS) {
                    if (score > best) {
                        SpUtils.put(this, BEST, score);
                        best = score;
                    }
                    life--;
                    baseSub.setAlive(false);
                    if (life == 0)
                        isDied = true;
                }
                break;
        }
    }

    private void fire() {
        if (!fire.stopWatch(FIRE))
            return;
        float x = asSprite.getPosition().x +
                asSprite.getWidthWithScale() / 2 - bulletSize / 2;
        float y = asSprite.getPosition().y - 50;
        switch (level) {
            // no break!!!
            case 3:
                addToSpriteGroup(getBullet(
                        x + 50, y, selector.RM()));
            case 2:
                addToSpriteGroup(getBullet(
                        x - 50, y, selector.PC()));
            default:
                addToSpriteGroup(getBullet(
                        x, y, selector.IJ()));
        }
    }

    private void addEnemy() {
        if (!enemy.stopWatch(ENEMY))
            return;
        switch (level) {
            default:
            case 4:
                addToSpriteGroup(getEnemy(selector.EC3()));
            case 3:
                addToSpriteGroup(getEnemy(selector.EC2()));
            case 2:
                addToSpriteGroup(getEnemy(selector.EC1()));
            case 1:
                addToSpriteGroup(getEnemy(selector.EC()));
        }
    }

    private BaseSprite getEnemy(GameTexture texture) {
        BaseSprite enemy;
        enemy = new BaseSprite(this);
        enemy.setTexture(texture);
        enemy.setIdentifier(ECLIPSE);
//        enemy.setDipScale(80, 80);
        enemy.addAnimation(new VelocityAnimation(
                90, 30, 5000
        ));
        enemy.setPosition(
                random.nextInt(UIdefaultData.screenWidth),
                -enemy.getHeightWidthScale()
        );
        enemy.setAlive(true);
        return enemy;
    }

    /**
     * size = bulletSize * bulletSize
     *
     * @param x position x
     * @param y position y
     * @return a bullet instance
     */
    private BaseSprite getBullet(float x, float y, GameTexture texture) {
        BaseSprite bullet;
        bullet = new BaseSprite(this);
        bullet.setTexture(texture);
        bullet.setIdentifier(IJ);
        bullet.setDipScale(bulletSize, bulletSize);
        bullet.addAnimation(new VelocityAnimation(
                270, 20, 2500));
        bullet.setPosition(x, y);
        bullet.setAlive(true);
        return bullet;
    }
}
