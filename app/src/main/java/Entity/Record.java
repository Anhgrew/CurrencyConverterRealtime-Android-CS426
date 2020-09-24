package Entity;

import java.io.Serializable;
import java.util.List;

public class Record implements Serializable {
    public MoneyNation base;
    public List<MoneyNation> target;
    public String datetime;

    public Record() {

    }

    public Record(MoneyNation base, List<MoneyNation> target, String datetime) {
        this.base = base;
        this.target = target;
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "Record{" +
                "base=" + base +
                ", target=" + target +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
