package com.ice1000.asgame;

import android.graphics.Color;
import android.view.MotionEvent;

import com.lfk.justweengine.Anim.VelocityAnimation;
import com.lfk.justweengine.Drawable.Button.ColorAnimation;
import com.lfk.justweengine.Drawable.Button.OnClickListener;
import com.lfk.justweengine.Drawable.Button.TextButton;
import com.lfk.justweengine.Drawable.Sprite.BaseSprite;
import com.lfk.justweengine.Drawable.Sprite.BaseSub;
import com.lfk.justweengine.Engine.Engine;
import com.lfk.justweengine.Engine.GameTextPrinter;
import com.lfk.justweengine.Engine.GameTexture;
import com.lfk.justweengine.Engine.GameTimer;
import com.lfk.justweengine.Engine.TouchMode;
import com.lfk.justweengine.Info.UIdefaultData;
import com.lfk.justweengine.Utils.tools.SpUtils;

import java.util.Random;

import util.ASSprite;
import util.SpriteSelector;

public class Game extends Engine {

    private static final int AS = 0x000;
    private static final int ASBullet = 0x001;
    private static final int EC = 0x002;
    private static final int ECBullet = 0x003;
    private static final int FIRE = 200;
    private static final int bulletSize = 20;
    private static final long ENEMY_FIRE = 600;
    private static final String START = "START";
    private static final String AS_SPRITE = "ASSprite";
    private static final String BEST = "BEST";
    private static final String RESTART = "RESTART";
    private static long ENEMY = 400;
    private int score, level, best, textSize, textFromLeft, life;
    private SpriteSelector selector;
    private ASSprite asSprite;
    private GameTimer fire, enemy, enemyFire;
    private Random random;
    private TextButton restartButton;
    private boolean isDied = false;
    /**
     * 这个拿来优化启动速度 不过好像没什么卵用
     */
    private boolean isButtonInitialized = false;

    public Game() {
        super(false);
        fire = new GameTimer();
        enemy = new GameTimer();
        enemyFire = new GameTimer();
        random = new Random();
    }

    @Override
    public void init() {
        super.setScreenOrientation(ScreenMode.PORTRAIT);
        UIdefaultData.init(this);
        setBackgroundColor(Color.BLACK);
        selector = new SpriteSelector(this);
        initData();
        textSize = 30;
        textFromLeft = 50;
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
        asSprite.setAlpha(0xee);
        addToSpriteGroup(asSprite);
    }

    @Override
    public void draw() {
        GameTextPrinter printer = new GameTextPrinter(getCanvas());
        printer.setTextSize(textSize);
        printer.setTextColor(Color.WHITE);
        int column = 0;
        printer.drawText("By 冰封 & JustWeEngine",
                textFromLeft, textSize * ++column);
        printer.drawText("score : " + score, textFromLeft, textSize * ++column);
        printer.drawText("best:  " + best, textFromLeft, textSize * ++column);
        printer.drawText("level : " + level, textFromLeft, textSize * ++column);
        printer.drawText(
                isDied ? "You die!" : ("life : " + life),
                textFromLeft, textSize * ++column);
        if (isDied) {
            addToButtonGroup(restartButton);
        }
    }

    @Override
    public void update() {
        if (asSprite.getFixedAnimation(START).animating)
            asSprite.fixedAnimation(START);
        if (enemy.stopWatch(ENEMY))
            addEnemy();
        if (enemyFire.stopWatch(ENEMY_FIRE))
            addEnemyFire(90);
        if (isDied) {
            if (!isButtonInitialized)
                initButton();
            // 虽然我觉得这么做没什么卵用。。。但是还是这样好点
            if (getTouchMode() != TouchMode.BUTTON)
                setTouchMode(TouchMode.BUTTON);
        } else {
            if (getTouchMode() != TouchMode.SINGLE)
                setTouchMode(TouchMode.SINGLE);
            if (fire.stopWatch(FIRE))
                fire();
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
            case ASBullet:
                if (baseSub.getOffender().getIdentifier() == EC) {
                    baseSub.getOffender().setAlive(false);
                    baseSub.setAlive(false);
                    removeFromSpriteGroup(baseSub.getOffender());
                    addToRecycleGroup(baseSub.getOffender());
                    score++;
                    if (ENEMY > 200) ENEMY--;
                    if (score % 80 == 0 && score > 0)
                        level++;
                    // 死后天女散花，此时求玩家心理阴影面积
                    if (level > 5) {
                        for (int i = 0; i < 360; i += 60 / (level - 5)) {
                            addToSpriteGroup(getEnemyDyingAttack(
                                    (int) (baseSub.s_position.x),
                                    (int) (baseSub.s_position.y),
                                    selector.EC(), i));
                        }
                    }
                }
                break;
            // 敌方子弹不会被你的子弹打掉
            case EC:
            case ECBullet:
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

    /**
     * init data. Used in init and restart
     */
    private void initData() {
        isDied = false;
        score = 0;
        level = 1;
        life = 5;
    }

    /**
     * actually an extension of getEnemy()
     *
     * @param texture the texture
     * @param dir     direction
     * @return the enemy bullet
     * @see #getEnemy(GameTexture)
     */
    private BaseSprite getEnemyBullet(GameTexture texture, double dir) {
        BaseSprite bullet = getEnemy(texture);
        bullet.setIdentifier(ECBullet);
        bullet.setDipScale(8, 8);
        bullet.clearAllAnimation();
        bullet.addAnimation(new VelocityAnimation(
                dir, 5, 10000));
        return bullet;
    }

    /**
     * another extension function.
     *
     * @param x       die pos.x
     * @param y       die pos.y
     * @param dir     direction
     * @param texture using texture
     * @return created dying attack
     * @see #getEnemyBullet(GameTexture, double)
     */
    private BaseSprite getEnemyDyingAttack(int x, int y, GameTexture dir, int texture) {
        BaseSprite sprite = getEnemyBullet(dir, texture);
        sprite.setDipPosition(x, y);
        return sprite;
    }

    private void fire() {
        float x = asSprite.getPosition().x +
                asSprite.getWidthWithScale() / 2 - bulletSize / 2;
        float y = asSprite.getPosition().y - 50;
        switch (level) {
            // no break!!!
            default:
            case 3:
                addToSpriteGroup(getBullet(
                        x + 50, y, selector.RM()));
            case 2:
                addToSpriteGroup(getBullet(
                        x - 50, y, selector.PC()));
            case 1:
                addToSpriteGroup(getBullet(
                        x, y, selector.IJ()));
        }
    }

    private void addEnemy() {
        switch (level) {
            default:
            case 4:
                addToSpriteGroup(getEnemy(selector.EC3()));
                break;
            case 3:
                addToSpriteGroup(getEnemy(selector.EC2()));
                break;
            case 2:
                addToSpriteGroup(getEnemy(selector.EC1()));
                break;
            case 1:
                addToSpriteGroup(getEnemy(selector.EC()));
                break;
        }
    }

    /**
     * 敌人发子弹的方法，我感觉没啥用，可能会删除
     *
     * @param dir direction
     */
    private void addEnemyFire(double dir) {
        switch (level) {
            // no break!!!
            default:
            case 6:
                addToSpriteGroup(getEnemyBullet(
                        selector.EC(), dir));
            case 5:
                addToSpriteGroup(getEnemyBullet(
                        selector.EC3(), dir));
            case 4:
            case 3:
            case 2:
            case 1:
        }
    }

    /**
     * get enemy by texture
     *
     * @param texture the texture
     * @return an enemy
     */
    private BaseSprite getEnemy(GameTexture texture) {
        BaseSprite enemy;
        enemy = new BaseSprite(this);
        enemy.setTexture(texture);
        enemy.setIdentifier(EC);
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
     * width: 200
     * height: 40
     */
    private void initButton() {
        isButtonInitialized = true;
        int w = 200, h = 40;
        restartButton = new TextButton(this, w, h, "restart");
        restartButton.setTextColor(Color.WHITE);
        restartButton.setZoomCenter(true);
        restartButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick() {
                initData();
                removeButtonFromGroup(RESTART);
            }
        });
        restartButton.setPosition(
                (UIdefaultData.screenWidth - w) / 2,
                // 中间再下移 1/8 个屏幕
                (UIdefaultData.screenHeight - h) / 2 +
                        UIdefaultData.screenHeight / 8
        );
        restartButton.setAnimation(new ColorAnimation(
                getResources().getColor(R.color.button_n),
                getResources().getColor(R.color.button_p)));
    }

    /**
     * size = bulletSize * bulletSize
     *
     * @param x position x
     * @param y position y
     * @return a bullet instance
     */
    private BaseSprite getBullet(float x, float y, GameTexture texture) {
        BaseSprite bullet = new BaseSprite(this);
        bullet.setTexture(texture);
        bullet.setIdentifier(ASBullet);
        bullet.setDipScale(bulletSize, bulletSize);
        bullet.addAnimation(new VelocityAnimation(
                270, 20, 2500));
        bullet.setPosition(x, y);
        bullet.setAlive(true);
        return bullet;
    }
}
