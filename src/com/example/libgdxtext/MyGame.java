package com.example.libgdxtext;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class MyGame extends Game {
	private OrthographicCamera camera;
	private World world;
	private Box2DDebugRenderer debugRenderer;
	float delta;
	
	Stage stage;
	TextureAtlas atlas;
	TextureRegion bgRegion;//背景图片
	Image bgImage;
	
	@Override
	public void create() {
		stage = new Stage(480, 800, false);
		atlas = new TextureAtlas(Gdx.files.internal("data/loading.atlas"));
		bgRegion = atlas.findRegion("bg");
		bgImage = new Image(bgRegion);
		
		// 镜头需要设定宽高
		camera = new OrthographicCamera(80, 48);
		camera.update();
		createDynamicBody();
		createStaticBody();
		createKinematicBody();
		
		stage.addActor(bgImage);
		Gdx.input.setInputProcessor(stage);
	}
	private void createDynamicBody() {
		world = new World(new Vector2(0f, -9.8f), true);
		debugRenderer = new Box2DDebugRenderer();
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		// We set our body to dynamic, for something like ground which doesn't
		// move we would set it to StaticBody
		bodyDef.type = BodyType.DynamicBody;
		// Set our body's starting position in the world
		bodyDef.position.set(0, 0);
		// Create our body in the world using our body definition
		Body body = world.createBody(bodyDef);
		// Create a circle shape and set its radius to 6
		CircleShape circle = new CircleShape();
		circle.setRadius(6f);
		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f; // Make it bounce a little bit
		// Create our fixture and attach it to the body
		Fixture fixture = body.createFixture(fixtureDef);
		// Remember to dispose of any shapes after you're done with them!
		// BodyDef and FixtureDef don't need disposing, but shapes do.
		circle.dispose();
	}
	private void createStaticBody() {
		// Create our body definition
		BodyDef groundBodyDef = new BodyDef();
		// Set its world position
		groundBodyDef.position.set(new Vector2(0, -20));
		// Create a body from the defintion and add it to the world
		Body groundBody = world.createBody(groundBodyDef);
		// Create a polygon shape
		PolygonShape groundBox = new PolygonShape();
		// Set the polygon shape as a box which is twice the size of our view
		// port and 4 high
		// (setAsBox takes half-width and half-height as arguments)
		groundBox.setAsBox(80 / 2f, 2.0f);
		// Create a fixture from our polygon shape and add it to our ground body
		groundBody.createFixture(groundBox, 0.0f);
		// Clean up after ourselves
		groundBox.dispose();
	}
	private void createKinematicBody() {
		// Create our body definition
		BodyDef kinematicBodyDef = new BodyDef();
		// set body to Kinematic type
		kinematicBodyDef.type = BodyType.KinematicBody;
		// Set its world position
		kinematicBodyDef.position.set(new Vector2(0, 10));
		// Create a body from the defintion and add it to the world
		Body kinematicBody = world.createBody(kinematicBodyDef);
		// Create a polygon shape
		PolygonShape kinematicBox = new PolygonShape();
		// Set the polygon shape as a box which is twice the size of our view
		// port and 4 high
		// (setAsBox takes half-width and half-height as arguments)
		kinematicBox.setAsBox(2.0f, 2.0f);
		// Create a fixture from our polygon shape and add it to our ground body
		kinematicBody.createFixture(kinematicBox, 1.0f);
		// set velocity
		kinematicBody.setLinearVelocity(-0.5f, -3f);
		// Clean up after ourselves
		kinematicBox.dispose();
	}
	@Override
	public void render() {
		// 防止一帧消耗时间过长， 导致卡顿走动现象
		delta = Math.min(0.06f, Gdx.graphics.getDeltaTime());
		world.step(1 / 60f, 6, 2);
		// 镜头的更新与设置矩阵到SpriteBatch
		camera.update();
		// 设置背景
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		// 清屏
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
		
		debugRenderer.render(world, camera.combined);
		
		

	}
}
