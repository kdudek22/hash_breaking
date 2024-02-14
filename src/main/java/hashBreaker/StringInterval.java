package hashBreaker;

import org.jetbrains.annotations.NotNull;

public class StringInterval implements Comparable{
    public String startString;
    public String endString;

    public StringInterval(String startString, String endString){
        this.startString = startString;
        this.endString = endString;
    }

    @Override
    public String toString() {
        return this.startString + "-" + this.endString;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        StringInterval s = (StringInterval) o;
        if(s.endString.equals(this.endString)){
            return 0;
        }
        return (int) (StringProvider.convertStringToNumber(this.endString)-StringProvider.convertStringToNumber(s.endString));
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }
        StringInterval stringInterval = (StringInterval) o;
        if (this.startString.equals(stringInterval.startString) && this.endString.equals(stringInterval.endString)) {
            return true;
        }
        return false;
    }
}
