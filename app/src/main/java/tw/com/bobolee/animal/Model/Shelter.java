package tw.com.bobolee.animal.Model;

public class Shelter {
    private String ShelterId;
    private String ShelterAreaId;
    private String ShelterPhysical;
    private String ShelterName;
    private String ShelterPhoneNumber;
    private String ShelterAddress;
    private String ShelterOpenTime;
    private String AreaPic;
    private String ShelterPic;

    Shelter()
    {
    }

    public String getShelterAreaId(){ return ShelterAreaId;}
    public String getShelterId(){ return ShelterId;}
    public String getShelterPhysical(){ return ShelterPhysical;}
    public String getShelterName(){ return ShelterName;}
    public String getShelterPhoneNumber(){ return ShelterPhoneNumber;}
    public String getShelterAddress(){ return ShelterAddress;}
    public String getShelterOpenTime(){ return ShelterOpenTime;}
    public String getAreaPic() {return AreaPic;}
    public String getShelterPic() {return ShelterPic;}
}
