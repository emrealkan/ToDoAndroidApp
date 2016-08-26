package todo.iyzico.com.todoapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by emrealkan on 26/08/16.
 */
public class UserToDos {

    private static UserToDos userToDos;

    private String status;

    @SerializedName("data")
    private ArrayList<ToDo> toDoList = new ArrayList<ToDo>();

    public static UserToDos getInstance()
    {
        return userToDos;
    }

    public static void setUserToDos(UserToDos userToDos)
    {
        userToDos = userToDos;
    }

    public ArrayList<ToDo> getToDoList()
    {
        return toDoList;
    }

    public void setToDoList(ArrayList<ToDo> userToDos)
    {
        this.toDoList = toDoList;
    }

    public void deleteToDo(Long todoID)
    {
        for(ToDo toDo: toDoList)
        {
            if(toDo.getTodoID() == todoID)
            {
                toDoList.remove(todoID);
            }
        }
    }
}
