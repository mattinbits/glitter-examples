package com.mjlivesey.gl.examples.example_3;

import com.mjlivesey.gl.geometry.*;
import com.mjlivesey.gl.render.Program;
import com.mjlivesey.gl.render.ProgramHolder;
import com.mjlivesey.gl.util.Log;
import org.apache.logging.log4j.Level;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by Matthew on 21/08/2014.
 */
public class TexturedCube  implements ProgramHolder {

    private String texLocation = "/textures/example_3/Dice.png";

    float[] vertices = {
            /*position*/           /*tex coords*/
            /*forward face(1)*/
            -1,1,1,/*0*/            0,1,
            -1,-1,1,/*1*/           0,0.5f,
            1,-1,1,/*2*/            0.3333f,0.5f,
            1,1,1,/*3*/             0.3333f,1.0f,
            /*right face(4)*/
            1,1,1,/*4*/             0,0.5f,
            1,-1,1,/*5*/            0,0,
            1,-1,-1,/*6*/           0.3333f,0,
            1,1,-1,/*7*/            0.3333f,0.5f,
            /*left face (3)*/
            -1,1,-1,/*8*/           0.6666f,1,
            -1,-1,-1,/*9*/          0.6666f,0.5f,
            -1,-1,1,/*10*/          1,0.5f,
            -1,1,1,/*11*/           1,1,
            /*back face(6)*/
            1,1,-1,/*12*/           0.6666f,0.5f,
            1,-1,-1,/*13*/          0.6666f,0,
            -1,-1,-1,/*14*/         1,0,
            -1,1,-1,/*15*/          1,0.5f,
            /*top face(2)*/
            -1,1,-1,/*16*/          0.3333f,1,
            -1,1,1,/*17*/           0.3333f,0.5f,
            1,1,1,/*18*/            0.6666f,0.5f,
            1,1,-1,/*19*/           0.6666f,1,
            /*bottom face(5)*/
            -1,-1,1,/*20*/          0.3333f,0.5f,
            -1,-1,-1,/*21*/         0.3333f,0,
            1,-1,-1,/*22*/          0.6666f,0,
            1,-1,1,/*23*/           0.6666f,0.5f


    };
    int[] vertex_indices = {
            /*Forward face*/
            /*t1*/0,1,2,/*t2*/0,2,3,
            /*Right face*/
            /*t1*/4,5,6,/*t2*/4,6,7,
            /*Left face*/
            /*t1*/8,9,10,/*t2*/8,10,11,
            /*Back face*/
            /*t1*/12,13,14,/*t2*/12,14,15,
            /*Top face*/
            /*t1*/16,17,18,/*t2*/16,18,19,
            /*Left face*/
            /*t1*/20,21,22,/*t2*/20,22,23
    };

    Geometry texGeometry = new DirectTextureGeometry(vertices,vertex_indices,texLocation);

    long completeRotationTime;
    float axis_x,axis_y,axis_z;

    Translation t = new Translation(0,0,-10);
    Matrix4f modelMatrix = new Matrix4f();
    Matrix4f mvpMatrix = new Matrix4f();

    public TexturedCube(long completeRotationTime, float [] axis)
    {
        this.completeRotationTime = completeRotationTime;
        this.axis_x=axis[0];
        this.axis_y=axis[1];
        this.axis_z=axis[2];
    }

    @Override
    public Program getProgram() {
        return texGeometry.getProgram();
    }

    @Override
    public void renderWithProgram(Matrix4f vpMatrix, long elapsedTime) {
        mvpMatrix.setIdentity();
        modelMatrix.setIdentity();
        Matrix4f rotationMatrix = getRotationMatrix(elapsedTime);
        Matrix4f.mul(t.getMatrix(),rotationMatrix,modelMatrix);
        if(Log.logger.getLevel()== Level.TRACE) {
            Log.logger.trace("renderWithProgram() Translation Matrix is\r\n{}", t.getMatrix());
            Log.logger.trace("renderWithProgram() Rotation Matrix is\r\n{}", rotationMatrix);
            Log.logger.trace("renderWithProgram() Model Matrix is\r\n{}", modelMatrix);
            Log.logger.trace("renderWithProgram() View Projection Matrix is\r\n{}",vpMatrix);
        }
        if(Log.logger.getLevel()== Level.TRACE)
            Log.logger.trace("View Projection Matrix is\r\n{}",vpMatrix);
        Matrix4f.mul(vpMatrix, modelMatrix, mvpMatrix);
        if(Log.logger.getLevel()== Level.TRACE)
            Log.logger.trace("Model View Projection Matrix is\r\n{}", Log.matrixToString(mvpMatrix));
        texGeometry.renderWithProgram(mvpMatrix,0);
    }

    private Matrix4f getRotationMatrix(long elapsedTime)
    {
        long step = elapsedTime % completeRotationTime;
        float angle = 360 * (((float)step)/completeRotationTime);
        return QRotation.fromAxisAndAngleDeg(axis_x,axis_y,axis_z,angle).getMatrix();
    }


    @Override
    public boolean init() {
        return texGeometry.init();
    }

}
