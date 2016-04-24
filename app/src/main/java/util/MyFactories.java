package util;

import com.ice1000.asgame.Game;
import com.lfk.justweengine.Anim.VelocityAnimation;
import com.lfk.justweengine.Drawable.Sprite.BaseSprite;
import com.lfk.justweengine.Engine.Engine;
import com.lfk.justweengine.Engine.ObjectPool;
import com.lfk.justweengine.Info.UIdefaultData;

import java.util.Random;

/**
 * @author ice1000
 *         Created by asus1 on 2016/4/24.
 */
public class MyFactories {
    private ObjectPool[] pools;
    private SpriteSelector spriteSelector;
    public Random random;

    public MyFactories(final Engine engine, SpriteSelector spriteSelector, Random random) {
        this.spriteSelector = spriteSelector;
        this.random = random;
        pools = new ObjectPool[]{
                new ObjectPool(new GetEnemy(engine, 0), 10),
                new ObjectPool(new GetEnemy(engine, 1), 10),
                new ObjectPool(new GetEnemy(engine, 2), 10),
                new ObjectPool(new GetEnemy(engine, 3), 10)
        };
    }

    public ObjectPool[] getPools() {
        return pools;
    }

    class GetEnemy implements ObjectPool.publicObjectFactory {

        private com.lfk.justweengine.Engine.Engine engine;
        private int ecc;

        public GetEnemy(Engine engine, int ecc) {
            this.engine = engine;
            this.ecc = ecc;
        }

        /**
         * get enemy by texture
         *
         * @return an enemy
         */

        @Override
        public Object createObject() {
            BaseSprite enemy;
            enemy = new BaseSprite(engine);
            enemy.setTexture(spriteSelector.getByCount(ecc));
            enemy.setIdentifier(Game.EC);
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
    }
}
