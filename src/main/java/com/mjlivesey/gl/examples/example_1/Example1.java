package com.mjlivesey.gl.examples.example_1;

import com.mjlivesey.gl.input.ExitProgramAction;
import com.mjlivesey.gl.input.InputAction;
import com.mjlivesey.gl.input.KeyboardInputHandler;
import com.mjlivesey.gl.render.ProgramHolder;
import com.mjlivesey.gl.util.Application;
import com.mjlivesey.gl.util.FPSMeasure;
import com.mjlivesey.gl.util.Log;
import com.mjlivesey.gl.view.Camera;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;

import java.util.ArrayList;

public class Example1 extends Application {

    ArrayList<ProgramHolder> objects = new ArrayList<ProgramHolder>();

    boolean exitProgram;

    KeyboardInputHandler kHandler;

    Camera camera;

    public Example1(String name) {
        super(name);
        exitProgram = false;
        kHandler = new KeyboardInputHandler();
    }

    public void initialize() throws Exception{
        Log.logger.info("Initializing {}", this.getClass().getCanonicalName());
        init(800, 600, false, false);
        Log.logger.info("Aspect Ratio is {}", ((float)Display.getWidth())/Display.getHeight());
        camera = Camera.getLookAtCameraWithFrustum(90.0f,((float)Display.getWidth())/Display.getHeight(),1000.0f);
        ArrayList<InputAction> actions = new ArrayList<InputAction>();
        ExitProgramAction exitAction = new ExitProgramAction(this);
        actions.add(exitAction);
        kHandler = new KeyboardInputHandler();
        kHandler.initialize(actions);
        OscillatingTriangleObject oto = new OscillatingTriangleObject(3000,2.0f);
        oto.init();
        objects.add(oto);
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
            Log.logger.debug("run() View Projection Matrix is\r\n{}",Log.matrixToString(vpMatrix));
        while(!exitProgram) {
            kHandler.checkForInput();
            GL11.glClearColor(0.5f, 0.5f, 1.0f, 1.0f);
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
        Example1 prog = new Example1("Test App");
        prog.initialize();
        prog.run();
        System.exit(0);
    }

    @Override
    public void exit() {
        exitProgram = true;
    }
}