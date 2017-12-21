package com.olszar.platformer.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.olszar.platformer.PlatformerGame;
import com.olszar.platformer.Screens.PlayScreen;
import com.olszar.platformer.Sprites.Brick;
import com.olszar.platformer.Sprites.CoinBrick;
import com.olszar.platformer.Sprites.Slime;

/**
 * Created by lubos on 17.11.2016.
 */
public class B2WorldCreator {
    private Array<Slime> slimes;

    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //Create ground bodies/fixtures
        for(MapObject object : map.getLayers().get(2).getObjects()){
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / PlatformerGame.PPM, (rect.getY() + rect.getHeight() / 2) / PlatformerGame.PPM);

                body = world.createBody(bdef);

                shape.setAsBox(rect.getWidth() / 2 / PlatformerGame.PPM, rect.getHeight() / 2 / PlatformerGame.PPM);
                fdef.shape = shape;
                body.createFixture(fdef);
            }

            else if(object instanceof PolylineMapObject){

                float[] vertices = ((PolylineMapObject) object).getPolyline().getTransformedVertices();
                Vector2[] worldVertices = new Vector2[vertices.length / 2];


                for (int i = 0; i < vertices.length / 2; ++i) {
                    worldVertices[i] = new Vector2();
                    worldVertices[i].x = vertices[i * 2] / PlatformerGame.PPM;
                    worldVertices[i].y = vertices[i * 2 + 1] / PlatformerGame.PPM;
                }

                ChainShape chain = new ChainShape();
                chain.createChain(worldVertices);

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set(0, 0);

                body = world.createBody(bdef);

                fdef.shape = chain;
                body.createFixture(fdef);
            }
        }

        //Create brick bodies/fixtures
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Brick(screen, rect);
        }

        //Create coinBrick bodies/fixtures
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

           new CoinBrick(screen, rect);
        }

        //Create stone bodies/fixtures
        for(MapObject object : map.getLayers().get(5).getObjects()){
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / PlatformerGame.PPM, (rect.getY() + rect.getHeight() / 2) / PlatformerGame.PPM);

                body = world.createBody(bdef);

                shape.setAsBox(rect.getWidth() / 2 / PlatformerGame.PPM, rect.getHeight() / 2 / PlatformerGame.PPM);
                fdef.shape = shape;
                fdef.filter.categoryBits = PlatformerGame.OBJECT_BIT;
                body.createFixture(fdef);
        }

        //Create all slimes
        slimes = new Array<Slime>();
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            slimes.add(new Slime(screen, rect.getX() / PlatformerGame.PPM, rect.getY() / PlatformerGame.PPM));
        }

    }

    public Array<Slime> getSlimes() {
        return slimes;
    }
}
