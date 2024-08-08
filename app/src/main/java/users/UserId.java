package users;

import common.Id;

public class UserId extends Id {
    private static int ID_LENGTH = 7;

    public UserId() {
        super(ID_LENGTH);
    }

    public static UserId getUniqueUserId(String filePath){
        return getUniqueId(filePath, UserId.class);
    }


    public UserId(String id) {
        super(id);
    }

}