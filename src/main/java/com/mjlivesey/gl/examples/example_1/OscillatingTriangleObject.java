package com.mjlivesey.gl.examples.example_1;

import com.mjlivesey.gl.geometry.DirectColourGeometry;
import com.mjlivesey.gl.geometry.Geometry;
import com.mjlivesey.gl.geometry.Scale;
import com.mjlivesey.gl.geometry.Translation;
import com.mjlivesey.gl.render.Program;
import com.mjlivesey.gl.render.ProgramHolder;
import com.mjlivesey.gl.util.Log;
import org.apache.logging.log4j.Level;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by Matthew on 10/08/2014.
 */
public class OscillatingTriangleObject implements ProgramHolder {

    float[] vertices = {
            /*position*/           /*colour*/
            -3f, 0,  0,    1.0f, 0.0f, 0.0f,
            3f,  0,  0,    0.0f, 1.0f, 0.0f,
            0, 3f,0,     0.0f, 0.0f, 1.0f
    };
    int[] vertex_indices = {
            0, 1, 2
    };

    Geometry triangleGeometry = new DirectColourGeometry(vertices,vertex_indices);

    long oscillationPeriod;
    float maxFactor;

    Scale sc = new Scale(3.0f);
    Translation t = new Translation(0,0,-200);
    Matrix4f modelMatrix = new Matrix4f();
    Matrix4f mvpMatrix = new Matrix4f();

    public OscillatingTriangleObject(long oscillationPeriod, float maxFactor)
    {
        this.oscillationPeriod = oscillationPeriod;
        this.maxFactor = maxFactor;
    }

    @Override
    public Program getProgram() {
        return triangleGeometry.getProgram();
    }

    @Override
    public void renderWithProgram(Matrix4f vpMatrix, long elapsedTime) {
        sc.setScaleFactor(getScaling(elapsedTime));
        mvpMatrix.setIdentity();
        modelMatrix.setIdentity();
        Matrix4f.mul(t.getMatrix(),sc.getMatrix(),modelMatrix);
        if(Log.logger.getLevel()== Level.TRACE)
            Log.logger.trace("View Projection Matrix is\r\n{}",vpMatrix);
        Matrix4f.mul(vpMatrix, modelMatrix, mvpMatrix);
        if(Log.logger.getLevel()== Level.TRACE)
            Log.logger.trace("Model Matrix is \r\n{}", modelMatrix);
            Log.logger.trace("Model View Projection Matrix is\r\n{}", Log.matrixToString(mvpMatrix));
        triangleGeometry.renderWithProgram(mvpMatrix,0);
    }

    private float getScaling(long elapsedTime)
    {
        long step = elapsedTime % oscillationPeriod;
        long halfPeriod = (oscillationPeriod/2);
        float ratio = (step < halfPeriod) ?
                ((float)step)/((float)halfPeriod) : 1.0f -((float)(step-halfPeriod))/((float)halfPeriod);
        return 1 + (maxFactor-1)*ratio;
    }

    @Override
    public boolean init() {
        return triangleGeometry.init();
    }
}
