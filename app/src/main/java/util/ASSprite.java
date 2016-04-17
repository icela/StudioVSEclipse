package util;

import android.renderscript.Float2;
import android.view.MotionEvent;

import com.lfk.justweengine.Anim.FrameAnimation;
import com.lfk.justweengine.Anim.MoveAnimation;
import com.lfk.justweengine.Engine.Engine;
import com.lfk.justweengine.Engine.GameTexture;
import com.lfk.justweengine.Info.UIdefaultData;
import com.lfk.justweengine.Drawable.Sprite.BaseSprite;

/**
 * Created by ice1000 on 2016/4/15.
 * Packed ASSprite class
 */
public class ASSprite extends BaseSprite {
    private int startX, startY;
    private int offsetX, offsetY;
    private GameTexture as;

    public ASSprite(Engine engine, int w, int h, int columns) {
        super(engine, w, h, columns);
        as = new GameTexture(engine);
        as.loadFromAsset("pic/as.png");
        setTexture(as);
        setPosition(
                // 从最下面出来
                UIdefaultData.centerInHorizontalX - getWidthWithScale() / 2,
                UIdefaultData.screenHeight + getHeightWidthScale()
        );
        setDipScale(100, 110);
        addAnimation(new FrameAnimation(0, 3, 1));
    }

    public void setStart(MotionEvent event) {
        this.startX = (int) event.getX();
        this.startY = (int) event.getY();
    }

    public void setOffset(MotionEvent event) {
        offsetX = (int) (event.getX() - startX);
        offsetY = (int) (event.getY() - startY);
    }

    public void move() {
        setPosition(
                getPosition().x + offsetX,
                getPosition().y + offsetY
        );
    }

    public MoveAnimation getStartAnimation() {
        return new MoveAnimation(
                UIdefaultData.centerInHorizontalX - getWidthWithScale() / 2,
                UIdefaultData.screenHeight - 2 * getHeightWidthScale(),
                new Float2(10, 10)
        );
    }
}
