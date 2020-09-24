package Connect;

import java.io.IOException;

import Entity.GlobalStorage;

public interface OnTaskCompleted{
    void onTaskCompleted(GlobalStorage storage) throws IOException;
}
