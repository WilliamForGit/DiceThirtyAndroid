package dice.thirty;

import android.os.Parcel;
import android.os.Parcelable;

//object for result
//Haiou Xiao
public class GameResult implements Parcelable {
    private String roundNumber;
    private String combinationNumber;
    private String roundPoint;

    public GameResult(String round, String combinationNumber, String roundPoint) {
        this.roundNumber = round;
        this.combinationNumber = combinationNumber;
        this.roundPoint = roundPoint;
    }

    public int describeContents()
    {
        return 0;
    }

    private GameResult(Parcel in)
    {
        roundNumber = in.readString();
        combinationNumber = in.readString();
        roundPoint = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeString(roundNumber);
        out.writeString(combinationNumber);
        out.writeString(roundPoint);
    }

    public static final Parcelable.Creator<GameResult> CREATOR = new Parcelable.Creator<GameResult>() {
        public GameResult createFromParcel(Parcel in) {
            return new GameResult(in);
        }

        public GameResult[] newArray(int size) {
            return new GameResult[size];
        }
    };

    public String getRoundNumber() {
        return this.roundNumber;
    }

    public String getCombinationNumber() {
        return this.combinationNumber;
    }

    public String getRoundPoint() {
        return this.roundPoint;
    }
}




