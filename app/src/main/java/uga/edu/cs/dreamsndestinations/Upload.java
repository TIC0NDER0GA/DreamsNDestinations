package uga.edu.cs.dreamsndestinations;

public class Upload {
    private String mName;
    private String Url;


    public Upload() {

    }

    public Upload(String name, String title,String body) {
        if (name.trim().equals("")) {
            name = "None";
        }
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

}
