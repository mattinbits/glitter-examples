package com.mjlivesey.gl.examples.example_3;

import com.mjlivesey.gl.input.ExitProgramAction;
import com.mjlivesey.gl.input.InputAction;
import com.mjlivesey.gl.input.KeyboardInputHandler;
import com.mjlivesey.gl.render.ProgramHolder;
import com.mjlivesey.gl.util.Application;
import com.mjlivesey.gl.util.FPSMeasure;
import com.mjlivesey.gl.util.Log;
import com.mjlivesey.gl.view.Camera;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import java.util.ArrayList;

/**
 * Created by Matthew on 21/08/2014.
 */
public class Example3 extends Application {
    ArrayList<ProgramHolder> objects = new ArrayList<ProgramHolder>();

    boolean exitProgram;

    KeyboardInputHandler kHandler;

    static int period;
    static float x,y,z;

    Camera camera;

    public Example3(String name) {
        super(name);
        exitProgram = false;
        kHandler = new KeyboardInputHandler();
    }

    public void initialize() throws Exception{
        Log.logger.info("Initializing {}", this.getClass().getCanonicalName());
        init(1200, 800, false, false);
        Log.logger.info("Aspect Ratio is {}", ((float) Display.getWidth())/Display.getHeight());
        camera = Camera.getLookAtCameraWithFrustum(90.0f,((float)Display.getWidth())/Display.getHeight(),1000.0f);
        ArrayList<InputAction> actions = new ArrayList<InputAction>();
        ExitProgramAction exitAction = new ExitProgramAction(this);
        actions.add(exitAction);
        kHandler = new KeyboardInputHandler();
        kHandler.initialize(actions);
        TexturedCube cube = new TexturedCube(period,new float[]{x,y,z});
        cube.init();
        objects.add(cube);
    }

    public void run()
    {
        //QRotation rotation = QRotation.fromPreset(QRotation.IDENTITY,QRotation.POSITIVE);
        FPSMeasure fps = new FPSMeasure();
        this.resetTime();
        Matrix4f vpMatrix =new Matrix4f();
        Matrix4f.mul(camera.getProjectionMatrix(),camera.getViewMatrix(),vpMatrix);
        //vpMatrix.setIdentity();
        if(Log.logger.getLevel()== Level.DEBUG)
            Log.logger.debug("run() View Matrix is\r\n{}",Log.matrixToString(camera.getViewMatrix()));
        if(Log.logger.getLevel()== Level.DEBUG)
            Log.logger.debug("run() View Projection Matrix is\r\n{}",Log.matrixToString(vpMatrix));
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glCullFace(GL11.GL_BACK);
        while(!exitProgram) {
            kHandler.checkForInput();
            GL11.glClearColor(1,1,1, 1.0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            for(ProgramHolder obj: objects)
            {
                obj.renderWithProgram(vpMatrix,this.getElapsedTime());
            }
            update();
            fps.frame(getElapsedTime());
        }

    }

    public static void main(String[] args) throws Exception {
        period = Integer.parseInt(args[1]);
        x = Float.parseFloat(args[2]);
        y = Float.parseFloat(args[3]);
        z = Float.parseFloat(args[4]);
        Example3 prog = new Example3("Example 3");
        prog.initialize();
        prog.run();
        System.exit(0);
    }

    @Override
    public void exit() {
        exitProgram = true;
    }
}
