package bean;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class Stat {
    
    private double value;
    private String description;
    
    public Stat(){}

    public double getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Stat{" + "value=" + value + ", description=" + description + '}';
    }
    
}
