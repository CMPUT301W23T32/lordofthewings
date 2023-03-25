package com.project.lordofthewings.Models.Authors;

import java.util.List;
import java.util.Map;

public interface AuthorNamesCallback {

    void onAuthorNamesReceived();

    void checkQRCodeOwner();
}
