package com.dragon.utils;

public class UserUtil {

    /**
     * 生成随机头像
     */
    private static final String[] AVATARURLARRAY = new String[] {
            "http://oimagec6.ydstatic.com/image?id=-4541055657611236390&product=bisheng",
            "http://oimageb2.ydstatic.com/image?id=8981431901149412470&product=bisheng",
            "http://oimagea2.ydstatic.com/image?id=-6268572656325873060&product=bisheng",
            "http://oimagea2.ydstatic.com/image?id=-38385107928742692&product=bisheng",
            "http://oimageb4.ydstatic.com/image?id=3484504410139022595&product=bisheng"
    };

    public static String getRandomAvatar(String userName) {
        int h = userName.hashCode();
        h = h < 0 ? -h : h;
        return AVATARURLARRAY[h % AVATARURLARRAY.length];
    }

}
