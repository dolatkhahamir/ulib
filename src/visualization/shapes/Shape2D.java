package visualization.shapes;

import jmath.datatypes.functions.Arc2D;
import jmath.functions.utils.Sampling;
import visualization.canvas.CoordinatedScreen;
import visualization.canvas.Graph2DCanvas;
import visualization.canvas.Render;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("unused")
public class Shape2D implements Render {
    private boolean isFill;
    private Runnable tick;
    private final ArrayList<Arc2D> bounds;
    private final CoordinatedScreen canvas;
    private float thicknessOfBounds;
    private int numOfThreadsForSampling;
    private Color color;
    private double deltaOfSampling;
    private double tL;
    private double tU;
    private boolean doTick;

    public Shape2D(CoordinatedScreen canvas, Color color, double tL, double tU, Arc2D... arcs) {
        this.canvas = canvas;
        this.color = color;
        this.tL = tL;
        this.tU = tU;
        deltaOfSampling = 0.01;
        numOfThreadsForSampling = 10;
        thicknessOfBounds = 1.5f;
        bounds = new ArrayList<>();
        bounds.addAll(Arrays.asList(arcs));
        isFill = true;
        doTick = true;
        tick = () -> {};
    }

    public double getTL() {
        return tL;
    }

    public void setTL(double tL) {
        this.tL = tL;
    }

    public double getTU() {
        return tU;
    }

    public void setTU(double tU) {
        this.tU = tU;
    }

    public double getDeltaOfSampling() {
        return deltaOfSampling;
    }

    public void setDeltaOfSampling(double deltaOfSampling) {
        this.deltaOfSampling = deltaOfSampling;
    }

    public boolean isFill() {
        return isFill;
    }

    public void setFill(boolean fill) {
        isFill = fill;
    }

    public float getThicknessOfBounds() {
        return thicknessOfBounds;
    }

    public void setThicknessOfBounds(float thicknessOfBounds) {
        this.thicknessOfBounds = thicknessOfBounds;
    }

    public Runnable getTick() {
        return tick;
    }

    public boolean isDoTick() {
        return doTick;
    }

    public void setDoTick(boolean doTick) {
        this.doTick = doTick;
    }

    public void setTick(Runnable tick) {
        this.tick = tick;
    }

    public ArrayList<Arc2D> getBounds() {
        return bounds;
    }

    public void addBounds(Arc2D... arcs) {
        bounds.addAll(Arrays.asList(arcs));
    }

    public int getNumOfThreadsForSampling() {
        return numOfThreadsForSampling;
    }

    public CoordinatedScreen getCanvas() {
        return canvas;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setNumOfThreadsForSampling(int numOfThreadsForSampling) {
        this.numOfThreadsForSampling = numOfThreadsForSampling;
    }

    @Override
    public void render(Graphics2D g2d) {
        for (var b : bounds) {
            var sample = Sampling.multiThreadSampling(b, tL, tU, deltaOfSampling, numOfThreadsForSampling);
            if (isFill) {
                Graph2DCanvas.flat2DSurfacePlotter(sample, color.darker().darker(), color, canvas::screenX, canvas::screenY, g2d);
            } else {
                Graph2DCanvas.typicalPlotter(sample, color, canvas::screenX, canvas::screenY, g2d);
            }
        }
    }

    @Override
    public void tick() {
        tick.run();
    }
}
