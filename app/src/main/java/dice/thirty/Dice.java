package dice.thirty;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

//objects for dice
//Haiou Xiao
public class Dice implements Parcelable
{
    public ImageView img;
    public boolean locked;
    public int randomValue;
    public boolean counted;

    public Dice() {
        //2019-08-29
        locked=false;
    }
    public int describeContents()
    {
        return 0;
    }

    private Dice(Parcel in)
    {
        randomValue = in.readInt();
        locked = in.readByte()!=0;  //if byte!=0, locked == true
        counted = in.readByte()!=0;  //if byte!=0, counted == true
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeInt(randomValue);
        out.writeByte((byte) (locked ? 1:0));  //if locked==rue, byte==1
        out.writeByte((byte) (counted ? 1:0));  //if counted==rue, byte==1
    }

    public static final Parcelable.Creator<Dice> CREATOR = new Parcelable.Creator<Dice>() {
        public Dice createFromParcel(Parcel in) {
            return new Dice(in);
        }

        public Dice[] newArray(int size) {
            return new Dice[size];
        }
    };

    public void setRandomValue(int value)
    {
        this.randomValue = value;
    }

    public int getRandomValue()
    {
        return this.randomValue;
    }

    //set image to dice with white picture,that means these dices have not been choose or used
    public void setRandomImages(int aRandomNumber) {

        switch (aRandomNumber) {
            case 1:
                this.img.setImageResource(R.drawable.white1);
                break;
            case 2:
                this.img.setImageResource(R.drawable.white2);
                break;
            case 3:
                this.img.setImageResource(R.drawable.white3);
                break;
            case 4:
                this.img.setImageResource(R.drawable.white4);
                break;
            case 5:
                this.img.setImageResource(R.drawable.white5);
                break;
            case 6:
                this.img.setImageResource(R.drawable.white6);
                break;
        }
    }

    // set image to dice with red picture,that means these dices have been choosed and can not run
    public void setChoosedImages(int aRandomNumber) {

        switch (aRandomNumber) {
            case 1:
                this.img.setImageResource(R.drawable.red1);
                break;
            case 2:
                this.img.setImageResource(R.drawable.red2);
                break;
            case 3:
                this.img.setImageResource(R.drawable.red3);
                break;
            case 4:
                this.img.setImageResource(R.drawable.red4);
                break;
            case 5:
                this.img.setImageResource(R.drawable.red5);
                break;
            case 6:
                this.img.setImageResource(R.drawable.red6);
                break;
        }

    }

    //set image to dice with grey picture,that means these dices have been used for count point
    public void setUsedImages(int aRandomNumber) {

        switch (aRandomNumber) {
            case 1:
                this.img.setImageResource(R.drawable.grey1);
                break;
            case 2:
                this.img.setImageResource(R.drawable.grey2);
                break;
            case 3:
                this.img.setImageResource(R.drawable.grey3);
                break;
            case 4:
                this.img.setImageResource(R.drawable.grey4);
                break;
            case 5:
                this.img.setImageResource(R.drawable.grey5);
                break;
            case 6:
                this.img.setImageResource(R.drawable.grey6);
                break;
        }

    }
}

