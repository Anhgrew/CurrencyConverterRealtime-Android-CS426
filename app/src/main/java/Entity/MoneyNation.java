package Entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class MoneyNation implements Parcelable, Serializable {
    public double costRate;
    public String name;
    public int ensignImage;
    public String information;
    public double result;

    public MoneyNation(double costRate, String name, int ensignImage, String information, double result) {
        this.costRate = costRate;
        this.name = name;
        this.ensignImage = ensignImage;
        this.information = information;
        this.result = result;
    }

    @Override
    public String toString() {
        return "MoneyNation{" +
                "costRate=" + costRate +
                ", name='" + name + '\'' +
                ", ensignImage=" + ensignImage +
                ", information='" + information + '\'' +
                ", result=" + result +
                '}';
    }

    public MoneyNation(double costRate, String name, int ensignImage, String information) {
        this.costRate = costRate;
        this.name = name;
        this.ensignImage = ensignImage;
        this.information = information;
    }

    public MoneyNation() {

    }


    protected MoneyNation(Parcel in) {
        costRate = in.readDouble();
        name = in.readString();
        ensignImage = in.readInt();
        information = in.readString();
        result = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(costRate);
        dest.writeString(name);
        dest.writeInt(ensignImage);
        dest.writeString(information);
        dest.writeDouble(result);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MoneyNation> CREATOR = new Creator<MoneyNation>() {
        @Override
        public MoneyNation createFromParcel(Parcel in) {
            return new MoneyNation(in);
        }

        @Override
        public MoneyNation[] newArray(int size) {
            return new MoneyNation[size];
        }
    };
}
