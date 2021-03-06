package jcchen.goodsmanager.model;

public interface LocalModel {
    String savePrivateObject(String objectName, Object object, boolean hasTimeStamp);
    Object getPrivateObject(String objectName, boolean hasTimeStamp);
    void updatePrivateObject(String objectName, Object object);
    void removePrivateObject(String objectName);
}