package Entity;


public class RecordDto {
    public String base;
    public String target;
    public String datetime;
    public String baseResult;
    public String targetResult;

    public RecordDto(String base, String target, String datetime, String baseResult, String targetResult) {
        this.base = base;
        this.target = target;
        this.datetime = datetime;
        this.baseResult = baseResult;
        this.targetResult = targetResult;
    }

    @Override
    public String toString() {
        return "RecordDto{" +
                "base='" + base + '\'' +
                ", target='" + target + '\'' +
                ", datetime='" + datetime + '\'' +
                ", baseResult='" + baseResult + '\'' +
                ", targetResult='" + targetResult + '\'' +
                '}';
    }

    public RecordDto() {

    }
}
