package Entity;


import java.util.HashMap;
import java.util.List;

public class GlobalStorage {
    private HashMap<String, Double> setData;
    private String baseCurrent;
    private String date;
    private List<MoneyNation> moneyList;

    public List<MoneyNation> getMoneyList() {
        return moneyList;
    }

    public void setMoneyList(List<MoneyNation> moneyList) {
        this.moneyList = moneyList;
    }

    public HashMap<String, Double> getSetData() {
        return setData;
    }

    public void setSetData(HashMap<String, Double> setData) {
        this.setData = setData;
    }

    public String getBaseCurrent() {
        return baseCurrent;
    }

    public void setBaseCurrent(String baseCurrent) {
        this.baseCurrent = baseCurrent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public GlobalStorage(HashMap<String, Double> setData, String baseCurrent, String date) {
        this.setData = setData;
        this.baseCurrent = baseCurrent;
        this.date = date;
    }

    public GlobalStorage() {
    }
}
