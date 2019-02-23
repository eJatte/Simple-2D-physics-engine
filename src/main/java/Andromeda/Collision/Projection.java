package Andromeda.Collision;

/**
 * Classname: Projection
 * Author: Elias Jätte
 * Date: 18/08/02
 *
 * Class for simple projection checks.
 */
public class Projection {
    private double min, max;

    public Projection (double min, double max){
        this.min = min;
        this.max = max;
    }

    public boolean overlap(Projection p){
        return !(min > p.getMax() || p.getMin() > max);
    }

    public boolean contains(Projection p){
        return p.getMin() > min && max > p.getMax();
    }

    public double getContainOverlap(Projection p){
        double o1 = max-p.getMin();
        double o2 = min-p.getMax();
        if(Math.abs(o1) < Math.abs(o2)) {
            return o1;
        }
        else {
            return o2;
        }
    }

    public double getOverlap(Projection p){
        double o1 = max-p.getMin();
        double o2 = min-p.getMax();
        if(Math.abs(o1) < Math.abs(o2))
            return o1;
        else
            return o2;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public String toString(){
        return "min: "+min+" max: "+max;
    }
}
